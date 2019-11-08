package com.veganafro.controller

import com.veganafro.controller.generic.GenericView
import com.veganafro.controller.implementation.MainActivityPresenter
import com.veganafro.model.NytTopic
import com.veganafro.networking.nyt.NytService
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.just
import io.mockk.every
import io.mockk.verify
import io.mockk.coEvery
import io.mockk.Ordering
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.lang.Exception

class MainActivityPresenterTest {

    @MockK
    lateinit var nytService: NytService

    @MockK
    lateinit var genericView: GenericView

    @MockK
    lateinit var compositeDisposable: CompositeDisposable

    @SpyK(recordPrivateCalls = true)
    var presenter = MainActivityPresenter()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { genericView.onFetchDataStarted() } just Runs
        every { genericView.onFetchDataCompleted() } just Runs
        every { genericView.onFetchDataError(any()) } just Runs
        every { genericView.onFetchDataSuccess(any()) } just Runs

        every { compositeDisposable.clear() } just Runs
        every { compositeDisposable.add(any()) } returns true

        presenter.nytMostShared = nytService
        presenter.subscriptions = compositeDisposable
        presenter.mainScheduler = Schedulers.trampoline()
        presenter.backgroundScheduler = Schedulers.trampoline()
        presenter.view = genericView
    }

    @Test
    @Ignore("Currently researching coroutine testing")
    fun `coroutine presenter subscribe should load data into view`() {
        runBlocking {
            val results: MutableList<NytTopic.Article> = listOf(
                NytTopic.Article("testUrl", "testTitle", "testSection")
            ).toMutableList()
            val nytTopic = NytTopic(results = results)

            coEvery { nytService.coMostShared(any(), any()) } coAnswers { nytTopic }
        }
    }

    @Test
    fun `presenter subscribe should load data into view`() {
        val results: MutableList<NytTopic.Article> = listOf(
            NytTopic.Article("testUrl", "testTitle", "testSection")
        ).toMutableList()
        val nytTopic = NytTopic(results = results)

        every { nytService.mostShared(any(), any()) } returns Observable.just(nytTopic)

        presenter.subscribe()

        verify(ordering = Ordering.SEQUENCE) {
            genericView.onFetchDataStarted()
            genericView.onFetchDataSuccess(nytTopic.results)
            genericView.onFetchDataCompleted()
        }
    }

    @Test
    fun `presenter subscribe should send error to view`() {
        val result: Throwable = Exception("test error")

        every { nytService.mostShared(any(), any()) } returns Observable.error(result)

        presenter.subscribe()

        verify(ordering = Ordering.SEQUENCE) {
            genericView.onFetchDataStarted()
            genericView.onFetchDataError(result)
        }

        verify(exactly = 0) {
            genericView.onFetchDataCompleted()
        }
    }
}
