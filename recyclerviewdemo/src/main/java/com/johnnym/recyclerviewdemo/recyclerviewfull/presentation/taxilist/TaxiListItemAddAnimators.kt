package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet

fun setupHolderAndCreateAddAnimator(
        holder: RecyclerView.ViewHolder
): Animator {
    holder.itemView.translationX = 0f
    holder.itemView.alpha = 0f

    val deltaX = (-holder.itemView.width / 5).toFloat()
    val translationXAnimator = ValueAnimator.ofFloat(deltaX, 0f)
            .apply {
                addUpdateListener {
                    holder.itemView.translationX = it.animatedValue as Float
                }
            }

    val appearAnimation = ValueAnimator.ofFloat(0f, 1f)
            .apply {
                addUpdateListener {
                    val alpha = it.animatedValue as Float
                    holder.itemView.alpha = alpha
                }
            }

    return AnimatorSet().apply {
        playTogether(translationXAnimator, appearAnimation)
        addListener(object : AnimatorListenerAdapter() {

            // reset view holder state so it can be reused in other animations without improperly
            // set properties
            override fun onAnimationEnd(animation: Animator) {
                holder.itemView.translationX = 0f
                holder.itemView.alpha = 1f
            }
        })
    }
}
