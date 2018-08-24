package com.johnnym.recyclerviewdemo.common.presentation

import androidx.annotation.CallSuper
import com.johnnym.recyclerviewdemo.common.mvp.DisposablePresenter
import io.reactivex.observers.DisposableSingleObserver

abstract class GeneralSingleObserver<T>(
        private val disposablePresenter: DisposablePresenter
) : DisposableSingleObserver<T>() {

    @CallSuper
    override fun onStart() {
        disposablePresenter.addDisposable(this)
    }
}