package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus

abstract class ItemAnimation {

    lateinit var animator: Animator

    fun setupAnimator() {
        animator = createAnimator()
    }

    abstract fun shouldAnimate(): Boolean

    abstract fun setStartingState()

    abstract fun createAnimator(): Animator

    abstract fun resetState()
}

class ItemTranslateToRightAndFadeOutAnimation(
        holder: RecyclerView.ViewHolder
) : ItemAnimation() {

    private val itemView = holder.itemView

    override fun shouldAnimate(): Boolean = true

    override fun setStartingState() {}

    override fun createAnimator(): Animator {
        val alphaAnimation = ValueAnimator.ofFloat(1f, 0f)
                .apply {
                    addUpdateListener {
                        val alpha = it.animatedValue as Float
                        itemView.alpha = alpha
                    }
                }

        val translationAnimation = ValueAnimator.ofFloat(0f, itemView.width.toFloat() * 2 / 3)
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
) : ItemAnimation() {

    private val itemView = holder.itemView

    override fun shouldAnimate(): Boolean = true

    override fun setStartingState() {}

    override fun createAnimator(): Animator {
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
        itemView.alpha = 1f
        itemView.scaleX = 1f
        itemView.scaleY = 1f
    }
}

class ItemFadeInFromLeftAnimation(
        holder: RecyclerView.ViewHolder
) : ItemAnimation() {

    private val itemView = holder.itemView

    private val startTranslationX = -(itemView.width / 5).toFloat()
    private val startAlpha = 0f

    override fun shouldAnimate(): Boolean = true

    override fun setStartingState() {
        itemView.translationX = startTranslationX
        itemView.alpha = startAlpha
    }

    override fun createAnimator(): Animator {
        val translationXAnimator = ValueAnimator.ofFloat(startTranslationX, 0f)
                .apply {
                    addUpdateListener {
                        val translationX = it.animatedValue as Float
                        itemView.translationX = translationX
                    }
                }

        val appearAnimation = ValueAnimator.ofFloat(startAlpha, 1f)
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

class ItemMoveAndFadeAnimation(
        holder: RecyclerView.ViewHolder,
        private val startTranslationX: Int,
        private val endTranslationX: Int,
        private val startTranslationY: Int,
        private val endTranslationY: Int,
        private val fromAlpha: Float,
        private val toAlpha: Float
) : ItemAnimation() {

    private val itemView = holder.itemView

    override fun setStartingState() {
        itemView.translationX = startTranslationX.toFloat()
        itemView.translationY = startTranslationY.toFloat()
        itemView.alpha = fromAlpha
    }

    override fun shouldAnimate(): Boolean =
            !(startTranslationX == endTranslationX && startTranslationY == endTranslationY && fromAlpha == toAlpha)

    override fun createAnimator(): Animator = createMoveAndFadeAnimator(
            itemView,
            startTranslationX,
            endTranslationX,
            startTranslationY,
            endTranslationY,
            fromAlpha,
            toAlpha)

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f
        itemView.alpha = 1f
    }
}

class ItemMoveAndTaxiStatusChangeAnimation(
        private val holder: NormalItemViewHolder,
        private val startTranslationX: Int,
        private val endTranslationX: Int,
        private val startTranslationY: Int,
        private val endTranslationY: Int,
        private val taxiStatusChange: Change<TaxiStatus>?
) : ItemAnimation() {

    private val itemView = holder.normalItemView

    override fun setStartingState() {
        itemView.translationX = startTranslationX.toFloat()
        itemView.translationY = startTranslationY.toFloat()
        taxiStatusChange?.let { holder.setTaxiStatus(it.old) }
    }

    override fun shouldAnimate(): Boolean =
            !(startTranslationX == endTranslationX
                    && startTranslationY == endTranslationY
                    && taxiStatusChange == null)

    override fun createAnimator(): Animator {
        val moveAnimator = createMoveAndFadeAnimator(
                itemView,
                startTranslationX,
                endTranslationX,
                startTranslationY,
                endTranslationY,
                1f,
                1f)

        val taxiStatusChangeAnimator = taxiStatusChange?.let { holder.createTaxiStatusChangeAnimator(it) }

        return if (taxiStatusChangeAnimator != null)
            AnimatorSet().apply {
                playTogether(moveAnimator, taxiStatusChangeAnimator)
            }
        else moveAnimator
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f
        itemView.statusBar.rotationY = 0f
        taxiStatusChange?.let { holder.setTaxiStatus(it.new) }
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
