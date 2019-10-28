# Summary

The intended objective of this project is to learn how to build a simple, modularized, and networked Android
app that displays a collection of items. The following documentation is a walkthrough of how to accomplish that,
starting from the leaves of the dependency graph and moving upwards. The resulting app will fetch trending
articles from the New York Times API and display their titles and sections using `CardView`s.

## Model

Begin by defining on the Android client the shape of the data that the New York Times API sends back.
In this case, a valid JSON response from the server looks something like this:

```json
{
    "status": "...",
    "results": [
      {
        "url": "...",
        "adx_keywords": "..."
      }
    ]
}
```

Therefore, the data model containing the needed information will be defined on the client side as follows:

```kotlin
@JsonClass(generateAdapter = true)
data class NytTopic(
    @Json(name = "results")
    val results: MutableList<Article>
) {
    @JsonClass(generateAdapter = true)
    data class Article(
        @Json(name = "url")
        val url: String,
        @Json(name = "title")
        val title: String,
        @Json(name = "section")
        val section: String
    )
}
```

There are a few things to note. Moshi is a JSON library that will be used to parse JSON into objects.
The classes are annotated with `@JsonClass(generateAdapter = true)`, which tells Moshi to generate a JSON
adapter to handle serializing and deserializing to and from JSON of the specified type. The
`@Json(name = "[value]")` annotation defines the JSON key name for serialization and the property to
set the value on with deserialization.

## Networking

Now that a response has been built using a data model, the next step is to define how to access the
New York Times API and fetch some data. Retrofit and RxKotlin are the relevant libraries used for this task.
Retrofit is a type-safe HTTP client for Android, and RxKotlin is a lightweight library that adds
convenient extension functions to RxJava (a library for composing asynchronous and event-based programs
using observable sequences for the Java VM).

Declare a service that contains methods for accessing API endpoints through HTTP requests:

```kotlin
interface NytService {

    @Headers("Accept: application/json")
    @GET("svc/mostpopular/v2/shared/{period}.json")
    fun mostShared(@Path("period") period: Int?, @Query("api-key") key: String): Observable<NytTopic>
}
```

The `@Headers("Accept: application/json")` annotation does what it sounds like it does. It adds HTTP
headers to the request. The relative URL of the resource is specified in the
`@GET("svc/mostpopular/v2/shared/{period}.json")` annotation. Finally, the `@Path("period")` annotation
adds the following parameter to the relative URL, and the `@Query("api-key")` adds the annotated argument
as a query parameter.

## Controller

The controller module contains the presenter, which is responsible for all presentation logic. All
presenters inherit from the `GenericPresenter` interface. The `MainActivityPresenter` is responsible for
displaying the main view, the list of trending articles.

The `loadData` method fetches data from the New York Times API using the networking module like so
(comments explain what's going on):

```kotlin
override fun loadData() {
    // let the view know that a network call has been initiated
    view?.onFetchDataStarted()
    // stop any other network calls that might be in flight
    subscriptions.clear()

    // call the API endpoint
    val subscription: Disposable = nytMostShared
        .mostShared(1, BuildConfig.NYT_CONSUMER_KEY)
        // declare that the work of fetching data is done on the background thread
        .subscribeOn(backgroundScheduler)
        // declare that the results of the fetch should be sent to the main thread
        .observeOn(mainScheduler)
        // initiate the cold observable and supply callbacks that are executed
        // on success, on error, and on completion
        .subscribe(
            { nytTopic: NytTopic? ->
                // send the view the results of a successful network call
                view?.onFetchDataSuccess(nytTopic?.results)
            },
            { error: Throwable? ->
                // send the view the error if the network call failed
                view?.onFetchDataError(error!!)
            },
            {
                // inform the view that the data fetch is completed
                view?.onFetchDataCompleted()
            }
        )

    // add the data fetch to the list of in flight network calls 
    subscriptions.add(subscription)
}
```

The other methods in the presenter are to be used by the view. `subscribe` is used to subscribe the
view to new data (when the view is on the screen), `unsubscribe` is used to stop data fetches that are
in flight (maybe because the view has been added to the back stack), and `onDestroy` breaks the link
between the view and the presenter (when the view is destroyed).

## Injector

The injector module is where all the previously mentioned modules come together to be injected into the app
module using Dagger. Read more on the [Dagger site](https://dagger.dev/users-guide) or below about how
dependency injection with Dagger works.

## View

The most important details here are how to build a view to hold and display a collection of data, as
well as how that view is managed.

Start by creating a fragment that implements the `GenericView` interface, inherits from `Fragment`, and
inflates a `RecyclerView` layout.

```kotlin
class NytTrendingFragment
    : Fragment(R.layout.nyt_trending_view), GenericView
```

There are some lifecycle events to be aware of in this fragment and its managing activity, especially
with respect to configuration changes like when the device is rotated.

In the activity, preventing overlapping fragments is accomplished as follows:

```kotlin
savedInstanceState?.let {
    return
}
```

This makes sure that there aren't multiple instances of the fragment on the back stack. Read more about
the Android lifecycle [here](https://medium.com/androiddevelopers/the-android-lifecycle-cheat-sheet-part-iii-fragments-afc87d4f37fd).

Now, the inflated `RecyclerView` layout needs to know how to fill itself with views provided by a layout
manager. For this, use a `LinearLayoutManager`. These individual views are represented by a `RecyclerView.ViewHolder`
and are collectively managed by a `RecyclerView.Adapter`.

When creating a custom `RecyclerView.Adapter`, it might be worthwhile to inherit from `ListAdapter`.
This class, given a callback to diff collections of objects, provides some nice animations when adding
and removing `RecyclerView.ViewHolder` objects from the view. Proceed as follows:

```kotlin
class NytTrendingAdapter
    : ListAdapter<NytTopic.Article, NytTrendingAdapter.NytArticleViewHolder>(NytArticleDiffCallback()) {

    // use the built in `currentList` collection to store objects that are used to inflate views
    // this makes it possible to use built in methods to submit new data
    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NytArticleViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.nyt_trending_card, parent, false)
        return NytArticleViewHolder(view)
    }

    @Suppress("ReplaceGetOrSet")
    override fun onBindViewHolder(holder: NytArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // this defines how items in the `RecyclerView` are represented
    class NytArticleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(article: NytTopic.Article) {
            view.nyt_title_text.text = article.title
            view.nyt_section_text.text = article.section
        }
    }

    // when new data is given to the adapter, diff the collection using the callback below
    class NytArticleDiffCallback : DiffUtil.ItemCallback<NytTopic.Article>() {
        override fun areItemsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title
        }

    }
}
```

Now that the `RecyclerView.Adapter` is set up, set it as a property of the fragment's view like so:

```kotlin
adapter?.apply {} ?: run { this.adapter = viewAdapter }
```

Overall, that should be a good introduction to setting up a collection view on Android.

## Notes

### Dagger

#### Declaring and Satisfying Dependencies

Use `@Inject` to annotate the constructor that Dagger should use to create instances of a class. When a new instance is
requested, Dagger will obtain the required parameters values and invoke this constructor.

If your class has `@Inject`-annotated fields but no `@Inject`-annotated constructor, Dagger will inject those fields if requested,
but will not create new instances. Add a no-argument constructor with the `@Inject` annotation to indicate that Dagger may
create instances as well.

Classes that lack `@Inject` annotations cannot be constructed by Dagger.

`@Inject` doesn’t work everywhere:

* Interfaces can’t be constructed.
* Third-party classes can’t be annotated.
* Configurable objects must be configured!

For these cases where `@Inject` is insufficient or awkward, use an `@Provides`-annotated method to satisfy a dependency.
The method’s return type defines which dependency it satisfies.

All `@Provides` methods must belong to a module. These are just classes that have an `@Module` annotation.

#### Building the Graph

The `@Inject` and `@Provides`-annotated classes form a graph of objects, linked by their dependencies.
Calling code like an application’s `main` method or an Android `Application` accesses that graph via a
well-defined set of roots. In Dagger 2, that set is defined by an interface with methods that have no
arguments and return the desired type. By applying the `@Component` annotation to such an interface and
passing the module types to the `modules` parameter, Dagger 2 then fully generates an implementation of that contract.

The implementation has the same name as the interface prefixed with Dagger. Obtain an instance by invoking the
`builder()` method on that implementation and use the returned builder to set dependencies and `build()` a new
instance. The `create()` method can also be invoked on the implementation.

### Recycler Views

#### Summary

The overall container for your user interface is a `RecyclerView` object that you add to your layout.
The `RecyclerView` fills itself with views provided by a layout manager that you provide. You can use
one of our standard layout managers (such as `LinearLayoutManager` or `GridLayoutManager`), or implement
your own.

The views in the list are represented by view holder objects. These objects are instances of a class
you define by extending `RecyclerView.ViewHolder`. Each view holder is in charge of displaying a single
item with a view.

The view holder objects are managed by an adapter, which you create by extending `RecyclerView.Adapter` or
`ListAdapter` (the latter provides nice animations when updating the underlying data if it contains a diff).
The adapter creates view holders as needed. The adapter also binds the view holders to their data.
It does this by assigning the view holder to a position, and calling the adapter's `onBindViewHolder()` method.
That method uses the view holder's position to determine what the contents should be, based on its
list position.
