package com.johnnym.recyclerviewanimator

import android.animation.Animator

internal class JackItemAnimationHandler(
        val jackItemAnimation: JackItemAnimation
) {
    val animator: Animator = jackItemAnimation.createAnimator()
}
