package com.github.af2905.learningrx

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    val disposeBag = CompositeDisposable()
    connectableExample(disposeBag)
}

fun connectableExample(disposeBag: CompositeDisposable) {
    val source = Observable.range(1, 3).map { it + 5 }.subscribeOn(Schedulers.io()).publish()
    disposeBag.add(source.subscribeOn(Schedulers.io()).subscribe { println("Observer1: $it") })
    val service: ExecutorService = Executors.newFixedThreadPool(10)
    val scheduler = Schedulers.from(service)

    disposeBag.add(source.subscribeOn(scheduler).subscribe { println("Observer2: $it") })
    source.connect()
    disposeBag.add(source.subscribeOn(Schedulers.io()).subscribe { println("Observer3: $it") })
}

fun connectableExample2(disposeBag: CompositeDisposable) {
    val source =
        Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).publish().refCount()
    disposeBag.add(source.take(3).subscribe { println("Observer1: $it") })
    Thread.sleep(3000)
    disposeBag.add(source.subscribe { println("Observer2: $it") })
    Thread.sleep(3000)
    disposeBag.add(source.subscribe { println("Observer3: $it") })
    Thread.sleep(3000)
}