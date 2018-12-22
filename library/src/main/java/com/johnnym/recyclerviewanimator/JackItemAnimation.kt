package com.johnnym.recyclerviewanimator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// TODO separate this file to single classes
class JackItemAnimationHandler(
        val jackItemAnimation: JackItemAnimation
) {

    val animator: Animator = jackItemAnimation.createAnimator()

    fun resetState() {
        jackItemAnimation.resetState()
    }
}

abstract class JackItemAnimation {

    abstract fun willAnimate(): Boolean

    abstract fun setStartingState()

    abstract fun createAnimator(): Animator

    abstract fun resetState()
}

class ItemMoveAndFadeAnimation(
        private val holder: RecyclerView.ViewHolder,
        private val startTranslationX: Int,
        private val endTranslationX: Int,
        private val startTranslationY: Int,
        private val endTranslationY: Int,
        private val fromAlpha: Float,
        private val toAlpha: Float
) : JackItemAnimation() {

    private val itemView = holder.itemView

    override fun willAnimate(): Boolean =
            !(startTranslationX == endTranslationX && startTranslationY == endTranslationY && fromAlpha == toAlpha)

    override fun setStartingState() {
        itemView.translationX = startTranslationX.toFloat()
        itemView.translationY = startTranslationY.toFloat()
        itemView.alpha = fromAlpha
    }

    override fun createAnimator(): Animator {
        return createMoveAndFadeAnimator(
                itemView,
                startTranslationX,
                endTranslationX,
                startTranslationY,
                endTranslationY,
                fromAlpha,
                toAlpha)
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f
        itemView.alpha = 1f
    }
}

fun createMoveAndFadeAnimator(
        view: View,
        startTranslationX: Int,
        endTranslationX: Int,
        startTranslationY: Int,
        endTranslationY: Int,
        fromAlpha: Float,
        toAlpha: Float
): Animator {
    val translationXAnimator = ValueAnimator.ofFloat(startTranslationX.toFloat(), endTranslationX.toFloat())
            .apply {
                addUpdateListener {
                    view.translationX = it.animatedValue as Float
                }
            }
    val translationYAnimator = ValueAnimator.ofFloat(startTranslationY.toFloat(), endTranslationY.toFloat())
            .apply {
                addUpdateListener {
                    view.translationY = it.animatedValue as Float
                }
            }

    val alphaAnimation = ValueAnimator.ofFloat(fromAlpha, toAlpha)
            .apply {
                addUpdateListener {
                    val alpha = it.animatedValue as Float
                    view.alpha = alpha
                }
            }

    return AnimatorSet().apply {
        playTogether(translationXAnimator, translationYAnimator, alphaAnimation)
    }
}
