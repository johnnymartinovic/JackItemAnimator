package com.johnnym.jackitemanimator.sample.common.presentation

import androidx.annotation.CallSuper
import com.johnnym.jackitemanimator.sample.common.mvp.DisposablePresenter
import io.reactivex.observers.DisposableSingleObserver

abstract class GeneralSingleObserver<T>(
        private val disposablePresenter: DisposablePresenter
) : DisposableSingleObserver<T>() {

    @CallSuper
    override fun onStart() {
        disposablePresenter.addDisposable(this)
    }
}