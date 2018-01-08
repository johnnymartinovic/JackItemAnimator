package com.johnnym.recyclerviewdemo.common.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AbsPresenter : DisposablePresenter(), BasePresenter {

    override fun viewDestroyed() {
        dispose()
    }
}

abstract class DisposablePresenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        disposables.dispose()
    }
}
