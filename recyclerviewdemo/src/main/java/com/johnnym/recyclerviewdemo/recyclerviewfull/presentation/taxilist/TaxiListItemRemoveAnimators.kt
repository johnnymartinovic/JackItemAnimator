package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.animation.AnimatorSet
import android.view.animation.AccelerateInterpolator

fun setupItemViewHolderAndCreateRemoveAnimator(
        holder: TaxiListAdapter.ItemViewHolder
): Animator {
    val subviewsDisappearAnimation = ValueAnimator.ofFloat(1f, 0f)
            .apply {
                addUpdateListener {
                    val alpha = it.animatedValue as Float

                    holder.backgroundHelperView.alpha = alpha
                    holder.statusBar.alpha = alpha
                    holder.driverName.alpha = alpha
                    holder.starIcon.alpha = alpha
                    holder.stars.alpha = alpha
                    holder.distanceIcon.alpha = alpha
                    holder.distance.alpha = alpha
                }
            }

    val itemMoveToRight = ValueAnimator.ofFloat(0f, holder.itemView.width.toFloat())
            .apply {
                addUpdateListener {
                    holder.itemView.translationX = it.animatedValue as Float
                }
                interpolator = AccelerateInterpolator()
            }

    return AnimatorSet().apply {
        playSequentially(subviewsDisappearAnimation, itemMoveToRight)
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.backgroundHelperView.alpha = 1f
                holder.statusBar.alpha = 1f
                holder.driverName.alpha = 1f
                holder.starIcon.alpha = 1f
                holder.stars.alpha = 1f
                holder.distanceIcon.alpha = 1f
                holder.distance.alpha = 1f

                holder.itemView.translationX = 0f
            }
        })
    }
}

fun setupSquareItemViewHolderAndCreateRemoveAnimator(
        holder: TaxiListAdapter.SquareItemViewHolder
): Animator {
    val subviewsDisappearAnimation = ValueAnimator.ofFloat(1f, 0f)
            .apply {
                addUpdateListener {
                    holder.statusBar.alpha = it.animatedValue as Float
                }
            }

    // TODO this way, views dissapears in the middle of the screen. Should upgrade animation
    val itemMoveToRight = ValueAnimator.ofFloat(0f, holder.itemView.width.toFloat())
            .apply {
                addUpdateListener {
                    holder.itemView.translationX = it.animatedValue as Float
                }
                interpolator = AccelerateInterpolator()
            }

    return AnimatorSet().apply {
        playSequentially(subviewsDisappearAnimation, itemMoveToRight)
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.statusBar.alpha = 1f
                holder.itemView.translationX = 0f
            }
        })
    }
}
