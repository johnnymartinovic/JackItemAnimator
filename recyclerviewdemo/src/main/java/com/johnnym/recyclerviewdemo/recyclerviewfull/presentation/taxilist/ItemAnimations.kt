package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.recyclerviewanimator.JackItemAnimation
import com.johnnym.recyclerviewanimator.defaultanimations.createMoveAndFadeAnimator

class ItemTranslateToRightAndFadeOutAnimation(
        holder: RecyclerView.ViewHolder
) : JackItemAnimation() {

    private val itemView = holder.itemView

    override fun willAnimate(): Boolean = true

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
) : JackItemAnimation() {

    private val itemView = holder.itemView

    override fun willAnimate(): Boolean = true

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
) : JackItemAnimation() {

    private val itemView = holder.itemView

    private val startTranslationX = -itemView.width.toFloat() / 5
    private val startAlpha = 0f

    override fun willAnimate(): Boolean = true

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

class ItemMoveAndTaxiStatusChangeAnimation(
        private val holder: NormalItemViewHolder,
        private val startTranslationX: Int,
        private val endTranslationX: Int,
        private val startTranslationY: Int,
        private val endTranslationY: Int,
        private val payloads: List<Any>
) : JackItemAnimation() {

    private val itemView = holder.normalItemView
    private val combinedPayload = createCombinedPayload(payloads as List<Change<TaxiListItemState>>)

    override fun willAnimate(): Boolean {
        val isTaxiStatusChanged = combinedPayload.newData.taxiStatus != combinedPayload.oldData.taxiStatus

        return !(startTranslationX == endTranslationX
                && startTranslationY == endTranslationY
                && !isTaxiStatusChanged)
    }

    override fun setStartingState() {
        itemView.translationX = startTranslationX.toFloat()
        itemView.translationY = startTranslationY.toFloat()
        holder.setTaxiStatus(combinedPayload.oldData.taxiStatus)
    }

    override fun createAnimator(): Animator {
        val isTaxiStatusChanged = combinedPayload.newData.taxiStatus != combinedPayload.oldData.taxiStatus

        val moveAnimator = createMoveAndFadeAnimator(
                itemView,
                startTranslationX,
                endTranslationX,
                startTranslationY,
                endTranslationY,
                1f,
                1f)

        val taxiStatusChangeAnimator =
                if (isTaxiStatusChanged)
                    holder.createTaxiStatusChangeAnimator(
                            combinedPayload.oldData.taxiStatus,
                            combinedPayload.newData.taxiStatus)
                else
                    null

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
        holder.setTaxiStatus(combinedPayload.newData.taxiStatus)
    }
}
