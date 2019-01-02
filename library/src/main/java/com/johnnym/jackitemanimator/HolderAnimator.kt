package com.johnnym.jackitemanimator

import android.animation.Animator
import androidx.recyclerview.widget.RecyclerView

data class HolderAnimator(
        val holder: RecyclerView.ViewHolder,
        val animator: Animator)
