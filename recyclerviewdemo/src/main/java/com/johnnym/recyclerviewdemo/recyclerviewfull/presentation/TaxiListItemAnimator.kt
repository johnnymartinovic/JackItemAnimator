package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.animation.*
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.ViewAnimationUtils
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import android.animation.AnimatorSet

class TaxiListItemAnimator : DefaultItemAnimator() {

    private val viewHolderAnimatorMap: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

    override fun obtainHolderInfo(): ItemHolderInfo {
        return TaxiListItemHolderInfo()
    }

    override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int,
            payloads: MutableList<Any>
    ): ItemHolderInfo {
        val taxiListItemHolderInfo = super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads) as TaxiListItemHolderInfo

        return taxiListItemHolderInfo
                .apply {
                    this.payloads.addAll(payloads)
                }
    }

    override fun isRunning(): Boolean {
        return super.isRunning() || viewHolderAnimatorMap.isNotEmpty()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        viewHolderAnimatorMap[item]?.end()
        super.endAnimation(item)
    }

    override fun endAnimations() {
        viewHolderAnimatorMap.values.forEach { it.end() }
        super.endAnimations()
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preInfo: ItemHolderInfo,
            postInfo: ItemHolderInfo
    ): Boolean {
        val viewHolder = newHolder as TaxiListAdapter.ItemViewHolder
        val oldInfo = preInfo as TaxiListItemHolderInfo
        val newInfo = postInfo as TaxiListItemHolderInfo

        val animatorNullableList: MutableList<Animator?> = mutableListOf()

        val itemPayload = createCombinedTaxiListItemPayload(oldInfo.payloads)

        val taxiStatusChange = itemPayload.taxiStatusChange
        if (taxiStatusChange != null && taxiStatusChange.old != taxiStatusChange.new) {
            animatorNullableList.add(createTaxiStatusChangeAnimator(viewHolder, itemPayload.taxiStatusChange))
        }

        val distanceChange = itemPayload.distanceChange
        if (distanceChange != null && distanceChange.old != distanceChange.new) {
            animatorNullableList.add(createDistanceChangeAnimator(viewHolder, itemPayload.distanceChange))
        }

        if (oldInfo.left != newInfo.right || oldInfo.top != newInfo.top) {
            animatorNullableList.add(createMoveChangeAnimator(viewHolder, oldInfo.left, oldInfo.top, newInfo.left, newInfo.top))
        }

        val animatorList = animatorNullableList.filterNotNull()
        val changeAnimator = AnimatorSet()
        changeAnimator.playTogether(animatorList)

        changeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                viewHolderAnimatorMap.put(viewHolder, animation)
                dispatchAnimationStarted(viewHolder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAnimationFinished(viewHolder)
                viewHolderAnimatorMap.remove(viewHolder)
            }
        })

        changeAnimator.start()

        return true
    }

    private fun createTaxiStatusChangeAnimator(
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

    private fun createDistanceChangeAnimator(
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

    private fun createMoveChangeAnimator(
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
}

private class TaxiListItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var payloads: MutableList<Any> = mutableListOf()
}

const val CHANGE_ANIMATION_DURATION = 1000L
