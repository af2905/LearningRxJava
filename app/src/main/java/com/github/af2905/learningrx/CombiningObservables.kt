package com.github.af2905.learningrx

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observables.GroupedObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


fun main() {
    val disposeBag = CompositeDisposable()
    mergeExample(disposeBag)
}

fun mergeExample(disposeBag: CompositeDisposable) {
    println("-----------------------------------------")
    val source1 = Observable.just("Alpha", "Beta").subscribeOn(Schedulers.io())
    val source2 = Observable.just("Gamma", "Delta").subscribeOn(Schedulers.io())
    val source3 = Observable.just("Epsilon", "Zeta").subscribeOn(Schedulers.io())
    val source4 = Observable.just("Eta", "Theta").subscribeOn(Schedulers.io())
    disposeBag.add(Observable.merge(source1, source2, source3, source4).subscribe { println(it) })
    println("--------------mergeWith-----------------")
    disposeBag.add(source1.mergeWith(source2).subscribe { println(it) })
}

fun flatMap(disposeBag: CompositeDisposable) {
    println("-----------------------------------------")
    val source1 = Observable.just("Alpha", "Beta", "Gamma").subscribeOn(Schedulers.io())
    disposeBag.add(source1.flatMap { it ->
        Observable.fromArray(it.split(""))
    }
        .subscribe {
            println(it)
        })
}

fun concatExample(disposeBag: CompositeDisposable) {
    println("-----------------------------------------")
    val source1 = Observable.just("Alpha", "Beta").subscribeOn(Schedulers.io())
    val source2 = Observable.just("Gamma", "Delta").subscribeOn(Schedulers.io())
    val source3 = Observable.just("Epsilon", "Zeta").subscribeOn(Schedulers.io())
    val source4 = Observable.just("Eta", "Theta").subscribeOn(Schedulers.io())
    disposeBag.add(Observable.concat(source1, source2, source3, source4).subscribe { println(it) })
    println("--------------concatWith-----------------")
    disposeBag.add(source1.concatWith(source2).subscribe { println(it) })
}

fun ambAndZipExample(disposeBag: CompositeDisposable) {
    println("--------------amb-----------------")
    val source1 = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
    val source2 = Observable.interval(300, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
    disposeBag.add(Observable.ambArray(source1, source2).subscribe { println(it) })
    Thread.sleep(5000)
    println("--------------zip-----------------")
    val source3 = Observable.just("Alpha", "Beta", "Gamma", "Delta").subscribeOn(Schedulers.io())
    val source4 = Observable.just("Epsilon", "Zeta").subscribeOn(Schedulers.io())
    disposeBag.add(Observable.zip(source3, source4,
        BiFunction<String, String, String> { t1, t2 -> "$t1-$t2" }).subscribe { println(it) })
}

fun combineLatestExample(disposeBag: CompositeDisposable) {
    println("--------------combineLatest-----------------")
    val source1 = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
    val source2 = Observable.interval(300, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
    disposeBag.add(
        Observable.combineLatest(source1, source2,
            BiFunction<Long, Long, String> { t1, t2 -> "$t1 - $t2" })
            .subscribe { println(it) }
    )
    Thread.sleep(5000)
}

fun withLatestFromExample(disposeBag: CompositeDisposable) {
    println("--------------withLatestFrom-----------------")
    val source3 = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
    val source4 = Observable.interval(300, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
    disposeBag.add(
        source3.withLatestFrom(source4, BiFunction<Long, Long, String> { t1, t2 -> "$t1 - $t2" })
            .subscribe { println(it) })
    Thread.sleep(5000)
}

fun groupByExample(disposeBag: CompositeDisposable) {
    println("--------------groupBy-----------------")
    val months =
        Observable.just("January", "February", "March", "April", "May", "June", "July", "August")
            .subscribeOn(Schedulers.io())
    val groups: Observable<GroupedObservable<Char, String>> = months.groupBy { it[0] }
    disposeBag.add(groups.flatMapSingle { it.toList() }.subscribe { println(it) })
}