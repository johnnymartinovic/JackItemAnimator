package com.johnnym.recyclerviewanimator

import android.animation.Animator

abstract class JackItemAnimation {

    abstract fun willAnimate(): Boolean

    abstract fun setStartingState()

    abstract fun createAnimator(): Animator

    abstract fun resetState()
}
