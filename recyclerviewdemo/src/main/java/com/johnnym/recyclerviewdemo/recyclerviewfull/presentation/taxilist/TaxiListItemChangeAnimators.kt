package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.os.Build
import android.support.annotation.ColorInt
import android.view.ViewAnimationUtils
import android.animation.AnimatorSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus

fun setupItemViewHolderAndCreateChangeAnimator(
        holder: TaxiListAdapter.ItemViewHolder,
        preInfo: TaxiListItemHolderInfo,
        postInfo: TaxiListItemHolderInfo
): Animator {
    val animatorNullableList: MutableList<Animator?> = mutableListOf()

    val itemPayload = createCombinedTaxiListItemPayload(preInfo.payloads)

    val taxiStatusChange = itemPayload.taxiStatusChange
    if (taxiStatusChange != null) {
        animatorNullableList.add(setupHolderAndCreateTaxiStatusChangeAnimator(holder, taxiStatusChange))
    }

    val distanceChange = itemPayload.distanceChange
    if (distanceChange != null) {
        animatorNullableList.add(setupHolderAndCreateDistanceChangeAnimator(holder, itemPayload.distanceChange))
    }

    if (preInfo.left != postInfo.left || preInfo.top != postInfo.top) {
        val deltaX = postInfo.left - preInfo.left - holder.itemView.translationX
        val deltaY = postInfo.top - preInfo.top - holder.itemView.translationY

        animatorNullableList.add(setupHolderAndCreateMoveAnimator(holder, deltaX, deltaY))
    }

    val animatorList = animatorNullableList.filterNotNull()
    return AnimatorSet().apply {
        playTogether(animatorList)
    }
}

private fun setupHolderAndCreateTaxiStatusChangeAnimator(
        holder: TaxiListAdapter.ItemViewHolder,
        taxiStatusChange: Change<TaxiStatus>
): Animator {
    @ColorInt val startColor: Int
    @ColorInt val endColor: Int
    if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
        startColor = holder.statusAvailableColor
        endColor = holder.statusUnavailableColor
    } else {
        startColor = holder.statusUnavailableColor
        endColor = holder.statusAvailableColor
    }

    holder.statusBar.setBackgroundColor(startColor)

    val spinningFirstHalf = ValueAnimator.ofFloat(0f, 810f)
            .apply {
                addUpdateListener {
                    holder.statusBar.rotationY = it.animatedValue as Float
                }
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator) {
                        holder.statusBar.setBackgroundColor(startColor)
                    }
                })
                interpolator = AccelerateInterpolator()
            }

    val spinningSecondHalf = ValueAnimator.ofFloat(-810f, 0f)
            .apply {
                addUpdateListener {
                    holder.statusBar.rotationY = it.animatedValue as Float
                }
                interpolator = DecelerateInterpolator()
            }

    val colorChange = ValueAnimator.ofInt(startColor, endColor)
            .apply {
                setEvaluator(ArgbEvaluator())
                addUpdateListener {
                    holder.statusBar.setBackgroundColor(it.animatedValue as Int)
                }
            }

    val spinningFull = AnimatorSet().apply {
        playSequentially(spinningFirstHalf, spinningSecondHalf)
    }

    return AnimatorSet().apply {
        playTogether(spinningFull, colorChange)
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.statusBar.rotationY = 0f
                holder.statusBar.setBackgroundColor(endColor)
            }
        })
    }
}

private fun setupHolderAndCreateDistanceChangeAnimator(
        holder: TaxiListAdapter.ItemViewHolder,
        distanceChange: Change<Float>
): Animator {
    val x = (holder.distance.x + holder.distance.width / 2).toInt()
    val y = (holder.distance.y + holder.distance.height / 2).toInt()

    val startRadius = 0
    val endRadius = Math.hypot(
            holder.distance.width.toDouble(),
            holder.distance.height.toDouble()
    ).toInt()

    val distanceChangeCircularRevealAnimator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        ViewAnimationUtils.createCircularReveal(holder.revealHelperView, x, y, startRadius.toFloat(), endRadius.toFloat())
    } else {
        null
    }

    @ColorInt val normalColor = holder.transparentColor
    @ColorInt val effectColor =
            if (distanceChange.old > distanceChange.new) holder.distanceDecreasedSignalColor
            else holder.distanceIncreasedSignalColor

    val distanceChangeCircuralRevealColorAnimator = ValueAnimator.ofInt(normalColor, effectColor, normalColor)
            .apply {
                setEvaluator(ArgbEvaluator())
                addUpdateListener { holder.revealHelperView.setBackgroundColor(it.animatedValue as Int) }
            }

    val distanceChangeAnimator = AnimatorSet()

    if (distanceChangeCircularRevealAnimator != null) {
        distanceChangeAnimator.playTogether(
                distanceChangeCircularRevealAnimator,
                distanceChangeCircuralRevealColorAnimator)
    } else {
        distanceChangeAnimator.playTogether(
                distanceChangeCircuralRevealColorAnimator
        )
    }

    return distanceChangeAnimator.apply {
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.revealHelperView.setBackgroundColor(normalColor)
            }
        })
    }
}
