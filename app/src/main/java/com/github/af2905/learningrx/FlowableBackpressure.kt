package com.github.af2905.learningrx

import io.reactivex.BackpressureOverflowStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
    val disposeBag = CompositeDisposable()
    bufferingExample2(disposeBag)
}

fun flowableExample(disposeBag: CompositeDisposable) {
    disposeBag.add(Flowable.range(0, 5000000).doOnNext {
        println("emission number: $it is coming")
    }
        .subscribeOn(Schedulers.computation())
        .subscribe({
            Thread.sleep(200)
            println(it)
        }, {

        }, {

        }, {
            it.request(Long.MAX_VALUE)
        }))
    Thread.sleep(10000)
}

fun flowableExample2() {
    val source = Flowable.interval(1, TimeUnit.SECONDS)
    source.onBackpressureBuffer(
        10,
        { println("overflow") },
        BackpressureOverflowStrategy.DROP_LATEST
    ).subscribe()
    source.onBackpressureLatest().subscribe()
    source.onBackpressureDrop().subscribe()
}

fun bufferingExample(disposeBag: CompositeDisposable) {
    val source = Observable.interval(200, TimeUnit.MILLISECONDS).buffer(1, TimeUnit.SECONDS)
    disposeBag.add(source.subscribe { println(it) })
    Thread.sleep(10000)
}

fun bufferingExample2(disposeBag: CompositeDisposable) {
    val source = Observable.interval(200, TimeUnit.MILLISECONDS).buffer(5, 3)
    disposeBag.add(source.subscribe { println(it) })
    Thread.sleep(10000)
}

