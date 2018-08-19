package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.view.animation.AccelerateInterpolator

abstract class ItemAnimation(val holder: RecyclerView.ViewHolder) {

    fun setupAndGetAnimator(): Animator? {
        return createAnimator()?.apply {
            setStartingState()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    resetState()
                }
            })
        }
    }

    abstract fun setStartingState()

    abstract fun createAnimator(): Animator?

    abstract fun resetState()
}

class ItemTranslateToRightAndFadeOutAnimation(
        holder: RecyclerView.ViewHolder
) : ItemAnimation(holder) {

    private val itemView = holder.itemView
    private val initialAlpha = itemView.alpha
    private val initialTranslationX = itemView.translationX

    private val startAlpha = initialAlpha
    private val endAlpha = 0f
    private val startTranslationX = initialTranslationX
    private val endTranslationX = initialTranslationX + itemView.width.toFloat() * 2 / 3

    override fun setStartingState() {}

    override fun createAnimator(): Animator? {
        val alphaAnimation = ValueAnimator.ofFloat(startAlpha, endAlpha)
                .apply {
                    addUpdateListener {
                        val alpha = it.animatedValue as Float
                        itemView.alpha = alpha
                    }
                }
        val translationAnimation = ValueAnimator.ofFloat(startTranslationX, endTranslationX)
                .apply {
                    addUpdateListener {
                        itemView.translationX = it.animatedValue as Float
                    }
                    interpolator = AccelerateInterpolator()
                }

        return AnimatorSet().apply {
            playTogether(alphaAnimation, translationAnimation)
        }
    }

    override fun resetState() {
        itemView.alpha = 1f
        itemView.translationX = 0f
    }
}

class ItemFadeOutAndScaleOutAnimation(
        holder: RecyclerView.ViewHolder
) : ItemAnimation(holder) {

    private val itemView = holder.itemView

    override fun setStartingState() {}

    override fun createAnimator(): Animator? {
        val disappearAnimation = ValueAnimator.ofFloat(1f, 0f)
                .apply {
                    addUpdateListener {
                        val alpha = it.animatedValue as Float
                        itemView.alpha = alpha
                    }
                }

        val scaleAnimation = ValueAnimator.ofFloat(1f, 0.7f)
                .apply {
                    addUpdateListener {
                        val scale = it.animatedValue as Float
                        itemView.scaleX = scale
                        itemView.scaleY = scale
                    }
                }

        return AnimatorSet().apply {
            playTogether(disappearAnimation, scaleAnimation)
        }
    }

    override fun resetState() {
        itemView.scaleX = 1f
        itemView.scaleY = 1f
    }
}

class ItemFadeInFromLeftAnimation(
        holder: RecyclerView.ViewHolder
) : ItemAnimation(holder) {

    private val itemView = holder.itemView
    private val initialTranslationX = itemView.translationX

    private val startTranslationX = initialTranslationX - (itemView.width / 5).toFloat()
    private val endTranslationX = initialTranslationX
    private val startAlpha = 0f
    private val endAlpha = 1f

    override fun setStartingState() {
        itemView.translationX = startTranslationX
        itemView.alpha = startAlpha
    }

    override fun createAnimator(): Animator? {
        if (startTranslationX == endTranslationX && startAlpha == endAlpha) {
            return null
        }

        val translationXAnimator = ValueAnimator.ofFloat(startTranslationX, endTranslationX)
                .apply {
                    addUpdateListener {
                        val translationX = it.animatedValue as Float
                        itemView.translationX = translationX
                    }
                }

        val appearAnimation = ValueAnimator.ofFloat(startAlpha, endAlpha)
                .apply {
                    addUpdateListener {
                        val alpha = it.animatedValue as Float
                        itemView.alpha = alpha
                    }
                }

        return AnimatorSet().apply {
            playTogether(translationXAnimator, appearAnimation)
        }
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.alpha = 1f
    }
}

class ItemMoveAndRotateAnimationn(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
        fromRotation: Float,
        toRotation: Float
) : ItemAnimation(holder) {

    private val itemView = holder.itemView
    private val initialTranslationX = itemView.translationX
    private val initialTranslationY = itemView.translationY

    private val deltaX = toX - fromX
    private val deltaY = toY - fromY

    private val startTranslationX = initialTranslationX - (fromX - itemView.left)
    private val endTranslationX = startTranslationX + deltaX
    private val startTranslationY = initialTranslationY - (fromY - itemView.top)
    private val endTranslationY = startTranslationY + deltaY
    private val startRotation = fromRotation
    private val endRotation = toRotation

    override fun setStartingState() {
        itemView.translationX = startTranslationX
        itemView.translationY = startTranslationY
        itemView.rotation = startRotation
    }

    override fun createAnimator(): Animator? {
        if (startTranslationX == endTranslationX && startTranslationY == endTranslationY && startRotation == endRotation) {
            return null
        }

        val translationXAnimator = ValueAnimator.ofFloat(startTranslationX, endTranslationX)
                .apply {
                    addUpdateListener {
                        itemView.translationX = it.animatedValue as Float
                    }
                }
        val translationYAnimator = ValueAnimator.ofFloat(startTranslationY, endTranslationY)
                .apply {
                    addUpdateListener {
                        itemView.translationY = it.animatedValue as Float
                    }
                }
        val rotationAnimator = ValueAnimator.ofFloat(startRotation, endRotation)
                .apply {
                    addUpdateListener {
                        val rotation = it.animatedValue as Float
                        itemView.rotation = rotation
                    }
                }

        return AnimatorSet().apply {
            playTogether(translationXAnimator, translationYAnimator, rotationAnimator)
        }
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f
        itemView.rotation = 0f
    }
}

class ItemMoveAndFadeAnimation(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        fromAlpha: Float,
        toX: Int,
        toY: Int,
        toAlpha: Float
) : ItemAnimation(holder) {

    private val itemView = holder.itemView
    private val initialTranslationX = itemView.translationX
    private val initialTranslationY = itemView.translationY

    private val deltaX = toX - fromX
    private val deltaY = toY - fromY

    private val startTranslationX = initialTranslationX - (itemView.left - fromX)
    private val endTranslationX = startTranslationX + deltaX
    private val startTranslationY = initialTranslationY - (itemView.top - fromY)
    private val endTranslationY = startTranslationY + deltaY
    private val startAlpha = fromAlpha
    private val endAlpha = toAlpha

    override fun setStartingState() {
        itemView.translationX = startTranslationX
        itemView.translationY = startTranslationY
        itemView.alpha = startAlpha
    }

    override fun createAnimator(): Animator? {
        if (startTranslationX == endTranslationX && startTranslationY == endTranslationY && startAlpha == endAlpha) {
            return null
        }

        val translationXAnimator = ValueAnimator.ofFloat(startTranslationX, endTranslationX)
                .apply {
                    addUpdateListener {
                        itemView.translationX = it.animatedValue as Float
                    }
                }
        val translationYAnimator = ValueAnimator.ofFloat(startTranslationY, endTranslationY)
                .apply {
                    addUpdateListener {
                        itemView.translationY = it.animatedValue as Float
                    }
                }

        val alphaAnimation = ValueAnimator.ofFloat(startAlpha, endAlpha)
                .apply {
                    addUpdateListener {
                        val alpha = it.animatedValue as Float
                        itemView.alpha = alpha
                    }
                }

        return AnimatorSet().apply {
            playTogether(translationXAnimator, translationYAnimator, alphaAnimation)
        }
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f
        itemView.alpha = 1f
    }
}
