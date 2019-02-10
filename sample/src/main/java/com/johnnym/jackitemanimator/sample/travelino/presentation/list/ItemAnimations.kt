package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.JackItemAnimation
import com.johnnym.jackitemanimator.defaultanimations.createMoveAndFadeAnimator
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoItemViewModel

class FadeOutToLeftAnimation(
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

        val translationAnimation = ValueAnimator.ofFloat(0f, -itemView.width.toFloat() * 2 / 3)
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

class FadeOutAndScaleOutAnimation(
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

class FadeInFromLeftAnimation(
        holder: RecyclerView.ViewHolder
) : JackItemAnimation() {

    private val itemView = holder.itemView

    private val startTranslationX = -itemView.width.toFloat() * 2 / 3
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

class TravelinoItemMoveAndChangeAnimation(
        private val itemView: View,
        private val priceTextView: TextView,
        private val discountPercentageTextView: TextView,
        private val infoMessageTextView: TextView,
        private val startTranslationX: Int,
        private val endTranslationX: Int,
        private val startTranslationY: Int,
        private val endTranslationY: Int,
        private val change: Change<TravelinoItemViewModel>
) : JackItemAnimation() {

    private val oldData = change.oldData
    private val newData = change.newData

    private val isPriceChanged = newData.price != oldData.price
    private val isDiscountPercentageChanged = newData.discountPercentage != oldData.discountPercentage
    private val isInfoMessageChanged = newData.infoMessage != oldData.infoMessage

    override fun willAnimate(): Boolean {
        return !(startTranslationX == endTranslationX
                && startTranslationY == endTranslationY
                && !isPriceChanged
                && !isDiscountPercentageChanged
                && !isInfoMessageChanged)
    }

    override fun setStartingState() {
        itemView.translationX = startTranslationX.toFloat()
        itemView.translationY = startTranslationY.toFloat()
        priceTextView.text = oldData.price
        discountPercentageTextView.text = oldData.discountPercentage

        if (isInfoMessageChanged && oldData.infoMessage == "") {
            infoMessageTextView.text = newData.infoMessage
            infoMessageTextView.translationY = infoMessageTextView.height.toFloat() * 2
            infoMessageTextView.alpha = 0f
        } else {
            infoMessageTextView.text = oldData.infoMessage
        }
    }

    override fun createAnimator(): Animator {
        val moveAnimator = createMoveAndFadeAnimator(
                itemView,
                startTranslationX,
                endTranslationX,
                startTranslationY,
                endTranslationY,
                1f,
                1f)

        val priceAnimator =
                if (isPriceChanged) createTextViewSpinChangeAnimator(priceTextView, newData.price)
                else null

        val discountPercentageAnimator =
                if (isDiscountPercentageChanged) createTextViewSpinChangeAnimator(discountPercentageTextView, newData.discountPercentage)
                else null

        val infoMessagePercentageAnimator =
                if (isInfoMessageChanged) {
                    when {
                        oldData.infoMessage == "" -> createFadeInFromBottomAnimator(infoMessageTextView)
                        newData.infoMessage == "" -> createFadeOutToBottomAnimator(infoMessageTextView)
                        else -> createTextViewSpinChangeAnimator(infoMessageTextView, newData.infoMessage)
                    }
                } else null

        val nonNullAnimators: MutableList<Animator> = mutableListOf()
        nonNullAnimators.add(moveAnimator)
        if (priceAnimator != null) nonNullAnimators.add(priceAnimator)
        if (discountPercentageAnimator != null) nonNullAnimators.add(discountPercentageAnimator)
        if (infoMessagePercentageAnimator != null) nonNullAnimators.add(infoMessagePercentageAnimator)

        return AnimatorSet().apply {
            playTogether(nonNullAnimators)
        }
    }

    override fun resetState() {
        itemView.translationX = 0f
        itemView.translationY = 0f

        priceTextView.text = newData.price
        discountPercentageTextView.text = newData.discountPercentage
        infoMessageTextView.text = newData.infoMessage

        priceTextView.rotationX = 0f
        discountPercentageTextView.rotationX = 0f

        if (isInfoMessageChanged) {
            when {
                oldData.infoMessage == "" || newData.infoMessage == "" -> {
                    infoMessageTextView.translationX = 0f
                    infoMessageTextView.alpha = 1f
                }
                else -> infoMessageTextView.rotationX = 0f
            }
        }
    }
}

fun createFadeOutToBottomAnimator(
        view: View
): Animator {
    val translationYAnimator = ValueAnimator.ofFloat(0f, view.height.toFloat() * 2)
            .apply {
                addUpdateListener {
                    val translationY = it.animatedValue as Float
                    view.translationY = translationY
                }
            }

    val disappearAnimation = ValueAnimator.ofFloat(1f, 0f)
            .apply {
                addUpdateListener {
                    val alpha = it.animatedValue as Float
                    view.alpha = alpha
                }
            }

    return AnimatorSet().apply {
        playTogether(translationYAnimator, disappearAnimation)
    }
}

fun createFadeInFromBottomAnimator(
        view: View
): Animator {
    val translationYAnimator = ValueAnimator.ofFloat(view.height.toFloat() * 2, 0f)
            .apply {
                addUpdateListener {
                    val translationY = it.animatedValue as Float
                    view.translationY = translationY
                }
            }

    val appearAnimation = ValueAnimator.ofFloat(0f, 1f)
            .apply {
                addUpdateListener {
                    val alpha = it.animatedValue as Float
                    view.alpha = alpha
                }
            }

    return AnimatorSet().apply {
        playTogether(translationYAnimator, appearAnimation)
    }
}

fun createTextViewSpinChangeAnimator(
        textView: TextView,
        newText: String
): Animator {
    val spinningFirstHalf = ValueAnimator.ofFloat(0f, -90f)
            .apply {
                addUpdateListener {
                    textView.rotationX = it.animatedValue as Float
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        textView.text = newText
                    }
                })
                interpolator = AccelerateInterpolator()
            }

    val spinningSecondHalf = ValueAnimator.ofFloat(90f, 0f)
            .apply {
                addUpdateListener {
                    textView.rotationX = it.animatedValue as Float
                }
                interpolator = DecelerateInterpolator()
            }

    return AnimatorSet().apply {
        playSequentially(spinningFirstHalf, spinningSecondHalf)
    }
}
