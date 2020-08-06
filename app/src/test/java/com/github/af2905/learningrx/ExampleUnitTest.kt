package com.github.af2905.learningrx

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test1() {
        var count = 0
        val source = Observable.interval(300, TimeUnit.MILLISECONDS).take(10)
        source.blockingSubscribe { println(count++) }
        assert(count == 10)
    }

    @Test
    fun test2() {
        val source = Observable.interval(300, TimeUnit.MILLISECONDS)
            .take(10)
            .filter { it > 5 }
        source.blockingForEach { assert(it > 5) }
    }

    @Test
    fun test3() {
        val source = Observable.range(0, 10)
        val testObserver = TestObserver<Int>()
        testObserver.assertNotSubscribed()
        source.subscribe(testObserver)
        testObserver.assertSubscribed()
        //Block and wait for Observable to terminate
        testObserver.awaitTerminalEvent()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(10)
        testObserver.assertValues(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    }

    @Test
    fun test4() {
        val source = Single.just("123-Jack-456-Maria-789-John")
        val testObserver = TestObserver<String>()

        source.flatMapObservable { it -> Observable.fromIterable(it.split("-")) }
            .filter { it.matches("[A-Za-z]+".toRegex()) }
            .doOnNext { println(it) }
            .subscribe(testObserver)
        testObserver.assertValues("Jack", "Maria", "John")
    }
}