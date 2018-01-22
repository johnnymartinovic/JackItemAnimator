package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.ViewAnimationUtils
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import android.animation.AnimatorSet

const val CHANGE_ANIMATION_DURATION = 1000L

class TaxiListItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var payloads: MutableList<Any> = mutableListOf()
}

fun createTaxiStatusChangeAnimator(
        itemViewHolder: TaxiListAdapter.ItemViewHolder,
        taxiStatusChange: Change<TaxiStatus>
): Animator {
    @ColorInt val startColor: Int
    @ColorInt val endColor: Int
    if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
        startColor = itemViewHolder.statusAvailableColor
        endColor = itemViewHolder.statusUnavailableColor
    } else {
        startColor = itemViewHolder.statusUnavailableColor
        endColor = itemViewHolder.statusAvailableColor
    }
    return ValueAnimator.ofInt(startColor, endColor)
            .apply {
                setEvaluator(ArgbEvaluator())
                addUpdateListener { itemViewHolder.statusBar.setBackgroundColor(it.animatedValue as Int) }
                duration = CHANGE_ANIMATION_DURATION
            }
}

fun createDistanceChangeAnimator(
        itemViewHolder: TaxiListAdapter.ItemViewHolder,
        distanceChange: Change<Float>
): Animator {
    val distanceTextChangeAnimator = ValueAnimator.ofFloat(distanceChange.old, distanceChange.new)
            .apply {
                addUpdateListener {
                    itemViewHolder.distance.text =
                            String.format(itemViewHolder.distanceFormattedText, it.animatedValue as Float)
                }
                duration = CHANGE_ANIMATION_DURATION
            }

    val x = (itemViewHolder.distance.x + itemViewHolder.distance.width / 2).toInt()
    val y = (itemViewHolder.distance.y + itemViewHolder.distance.height / 2).toInt()

    val startRadius = 0
    val endRadius = Math.hypot(itemViewHolder.distance.width.toDouble(), itemViewHolder.distance.height.toDouble()).toInt()

    val distanceChangeCircuralRevealAnimator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        ViewAnimationUtils.createCircularReveal(itemViewHolder.revealHelperView, x, y, startRadius.toFloat(), endRadius.toFloat())
                .apply {
                    duration = CHANGE_ANIMATION_DURATION
                }
    } else {
        null
    }

    @ColorInt val normalColor = itemViewHolder.transparentColor
    @ColorInt val effectColor =
            if (distanceChange.old > distanceChange.new) itemViewHolder.distanceDecreasedSignalColor
            else itemViewHolder.distanceIncreasedSignalColor

    val distanceChangeCircuralRevealColorAnimator = ValueAnimator.ofInt(normalColor, effectColor, normalColor)
            .apply {
                setEvaluator(ArgbEvaluator())
                addUpdateListener { itemViewHolder.revealHelperView.setBackgroundColor(it.animatedValue as Int) }
                duration = CHANGE_ANIMATION_DURATION
            }

    val distanceChangeAnimator = AnimatorSet()

    if (distanceChangeCircuralRevealAnimator != null) {
        distanceChangeAnimator.playTogether(
                distanceTextChangeAnimator,
                distanceChangeCircuralRevealAnimator,
                distanceChangeCircuralRevealColorAnimator)
    } else {
        distanceChangeAnimator.playTogether(
                distanceChangeCircuralRevealAnimator,
                distanceChangeCircuralRevealColorAnimator
        )
    }

    return distanceChangeAnimator
}

fun createMoveChangeAnimator(
        itemViewHolder: TaxiListAdapter.ItemViewHolder,
        fromX: Int, fromY: Int,
        toX: Int, toY: Int
): Animator {
    val view = itemViewHolder.itemView
    val deltaX = toX - fromX - view.translationX
    val deltaY = toY - fromY - view.translationY

    val xAnimator = ValueAnimator.ofFloat(-deltaX, 0f)
            .apply {
                addUpdateListener {
                    view.translationX = it.animatedValue as Float
                }
                duration = CHANGE_ANIMATION_DURATION
            }
    val yAnimator = ValueAnimator.ofFloat(-deltaY, 0f)
            .apply {
                addUpdateListener {
                    view.translationY = it.animatedValue as Float
                }
                duration = CHANGE_ANIMATION_DURATION
            }

    val xyAnimator = AnimatorSet()
    xyAnimator.playTogether(xAnimator, yAnimator)

    return xyAnimator
}