package com.johnnym.jackitemanimator.sample.common.domain

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

abstract class UseCase<ReturnType, ParamsType>(
        private val subscribeOnScheduler: Scheduler,
        private val observeOnScheduler: Scheduler) {

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCase(params: ParamsType): Single<ReturnType>

    fun execute(observer: DisposableSingleObserver<ReturnType>, params: ParamsType) {
        val disposable: Disposable = buildUseCase(params)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribeWith(observer)

        disposables.add(disposable)
    }

    fun disposePendingExecutions() {
        disposables.clear()
    }
}