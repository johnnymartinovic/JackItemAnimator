package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet

fun setupHolderAndCreateMoveAnimator(
        holder: RecyclerView.ViewHolder,
        deltaX: Float,
        deltaY: Float
): Animator {
    holder.itemView.translationX = -deltaX
    holder.itemView.translationY = -deltaY

    val translationXAnimator = ValueAnimator.ofFloat(-deltaX, 0f)
            .apply {
                addUpdateListener {
                    holder.itemView.translationX = it.animatedValue as Float
                }
            }
    val translationYAnimator = ValueAnimator.ofFloat(-deltaY, 0f)
            .apply {
                addUpdateListener {
                    holder.itemView.translationY = it.animatedValue as Float
                }
            }

    return AnimatorSet().apply {
        playTogether(translationXAnimator, translationYAnimator)
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.itemView.translationX = 0f
                holder.itemView.translationY = 0f
            }
        })
    }
}