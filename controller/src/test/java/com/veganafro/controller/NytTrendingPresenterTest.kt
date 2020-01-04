package com.veganafro.controller

import com.veganafro.contract.GenericFragment
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.mockk.MockKAnnotations
import io.mockk.spyk
import io.mockk.every
import io.mockk.verify
import io.mockk.coEvery
import io.mockk.Ordering
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.lang.Exception

class NytTrendingPresenterTest {

    @MockK
    lateinit var nytService: NytService

    @MockK(relaxed = true)
    lateinit var genericFragment: GenericFragment

    @MockK(relaxed = true)
    lateinit var compositeDisposable: CompositeDisposable

    lateinit var presenter: NytTrendingPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        presenter = spyk(
            NytTrendingPresenter(nytService),
            recordPrivateCalls = true
        )

        presenter.subscriptions = compositeDisposable
        presenter.mainScheduler = Schedulers.trampoline()
        presenter.backgroundScheduler = Schedulers.trampoline()
        presenter.fragment = genericFragment
    }

    @Test
    @Ignore("Currently researching coroutine testing")
    fun `coroutine presenter subscribe should load data into view`() {
        runBlockingTest {
            val results: MutableList<NytTopic.Article> = listOf(
                NytTopic.Article(
                    "testUrl",
                    "testTitle",
                    "testSection",
                    "testByline",
                    mutableListOf(NytTopic.Article.Photos(
                        mutableListOf(
                            NytTopic.Article.Photos.Photo("testUrl")
                        )
                    ))
                )
            ).toMutableList()
            val nytTopic = NytTopic(results = results)

            coEvery { nytService.coMostShared(any(), any()) } returns nytTopic
        }
    }

    @Test
    fun `presenter subscribe should load data into view`() {
        val results: MutableList<NytTopic.Article> = listOf(
            NytTopic.Article(
                "testUrl",
                "testTitle",
                "testSection",
                "testByline",
                mutableListOf(NytTopic.Article.Photos(
                    mutableListOf(
                        NytTopic.Article.Photos.Photo("testUrl")
                    )
                ))
            )
        ).toMutableList()
        val nytTopic = NytTopic(results = results)

        every { nytService.mostShared(any(), any()) } returns Observable.just(nytTopic)

        presenter.subscribe()

        verify(ordering = Ordering.SEQUENCE) {
            genericFragment.onFetchDataStarted()
            genericFragment.onFetchDataSuccess(nytTopic.results)
            genericFragment.onFetchDataCompleted()
        }
    }

    @Test
    fun `presenter subscribe should send error to view`() {
        val result: Throwable = Exception("test error")

        every { nytService.mostShared(any(), any()) } returns Observable.error(result)

        presenter.subscribe()

        verify(ordering = Ordering.SEQUENCE) {
            genericFragment.onFetchDataStarted()
            genericFragment.onFetchDataError(result)
        }

        verify(exactly = 0) {
            genericFragment.onFetchDataCompleted()
        }
    }
}
