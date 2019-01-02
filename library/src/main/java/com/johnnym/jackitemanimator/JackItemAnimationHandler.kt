package com.johnnym.jackitemanimator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

internal class JackItemAnimationHandler(
        val jackItemAnimation: JackItemAnimation
) {
    var animationEndCallback: (() -> Unit)? = null

    val animator: Animator = jackItemAnimation.createAnimator()
            .apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        animationEndCallback?.invoke()
                    }
                })
            }
}
