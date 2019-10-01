## Learnings

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

### Recycler Views

#### Summary

The overall container for your user interface is a `RecyclerView` object that you add to your layout.
The `RecyclerView` fills itself with views provided by a layout manager that you provide. You can use
one of our standard layout managers (such as `LinearLayoutManager` or `GridLayoutManager`), or implement
your own.

The views in the list are represented by view holder objects. These objects are instances of a class
you define by extending `RecyclerView.ViewHolder`. Each view holder is in charge of displaying a single
item with a view.

The view holder objects are managed by an adapter, which you create by extending `RecyclerView.Adapter`.
The adapter creates view holders as needed. The adapter also binds the view holders to their data.
It does this by assigning the view holder to a position, and calling the adapter's `onBindViewHolder()` method.
That method uses the view holder's position to determine what the contents should be, based on its
list position.
