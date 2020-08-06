package com.github.af2905.learningrx

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

fun main() {
    val disposeBag = CompositeDisposable()

    val names = listOf("Jack", "Tim", "Maria", "Peter")
    disposeBag.add(Single.just(names).subscribe({ println(it) }, {}))
    disposeBag.add(Observable.just("Jack", "Tim").first("!").subscribe({ println(it) }, {}))

}

fun coldObserveExample(disposeBag: CompositeDisposable) {
    val source: Observable<Int> = Observable.just(1, 2, 3, 4, 5)
    disposeBag.add(source.subscribe({ println("observer1: $it") }, {}, { println("Completed") }))
    disposeBag.add(source.subscribe({ println("observer2: $it") }, {}, { println("Completed") }))
}

fun hotObserveExample(disposeBag: CompositeDisposable) {
    println("----------------fromIterable Factory----------------")
    val names = listOf("Jack", "Tim", "Maria", "Peter")
    val source2 = Observable.fromIterable(names)
//    val source2 = Observable.fromArray(names)
    val hot = source2.publish()
    disposeBag.add(hot.subscribe({ println("observer1: $it") }, {}, { println("Completed") }))
    disposeBag.add(hot.subscribe({ println("observer2: $it") }, {}, { println("Completed") }))
    hot.connect()
}

fun intervalExample(disposeBag: CompositeDisposable) {
    disposeBag.add(Observable.interval(100, TimeUnit.MILLISECONDS).subscribe { println(it) })
    Thread.sleep(2000)
}

fun rangeExample(disposeBag: CompositeDisposable) {
    disposeBag.add(Observable.range(1, 10).subscribe { println(it) })
}

fun errorExample(disposeBag: CompositeDisposable) {
    val e: Exception = Exception("crush")
    disposeBag.add(
        Observable.error<String>(e).subscribe({
            println("same string $it")
        }, {
            println(it)
        }, {
            println("bla-bla-bla")
        })
    )
}

fun deferExample(disposeBag: CompositeDisposable) {
    val a = 1
    var b = 10
    val source = Observable.defer { Observable.range(a, b) }
    disposeBag.add(source.subscribe { println(it) })
    b = 15
    disposeBag.add(source.subscribe { println(it) })
}

fun retryExample(disposeBag: CompositeDisposable) {
    disposeBag.add(Observable.just(1, 3, 8, 12, 0, 4)
        .doOnNext { println("doOnNext") }
        .doOnComplete { println("doOnComplete") }
        .doOnError { println("doOnError") }
        .map { 5 / it }
        .retry(1)
        .subscribe { println(it) }
    )
}








