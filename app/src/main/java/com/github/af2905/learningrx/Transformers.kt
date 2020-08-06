package com.github.af2905.learningrx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable


fun main() {
    val disposeBag = CompositeDisposable()
    val source =
        Observable.just("cat", "dog", "python", "mouse")
            .map { it.length }
            .filter { it > 3 }
            .subscribe { println(it) }

    val source2 =
        Observable.just("white", "black", "red", "blue")
            .compose(mapToNumber())
            .subscribe { println(it) }

    disposeBag.addAll(source, source2)
}

fun mapToNumber(): ObservableTransformer<String, Int> = ObservableTransformer { it ->
    it.map { it.length }.filter { it > 3 }
}

