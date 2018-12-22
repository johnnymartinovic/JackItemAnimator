package com.johnnym.recyclerviewanimator

import androidx.recyclerview.widget.RecyclerView

internal class JackItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var payloads: MutableList<Any> = mutableListOf()
}

val RecyclerView.ItemAnimator.ItemHolderInfo.isChanged
    get() = changeFlags and RecyclerView.ItemAnimator.FLAG_CHANGED != 0

val RecyclerView.ItemAnimator.ItemHolderInfo.isRemoved
    get() = changeFlags and RecyclerView.ItemAnimator.FLAG_REMOVED != 0

val RecyclerView.ItemAnimator.ItemHolderInfo.isInvalidated
    get() = changeFlags and RecyclerView.ItemAnimator.FLAG_INVALIDATED != 0

val RecyclerView.ItemAnimator.ItemHolderInfo.isMoved
    get() = changeFlags and RecyclerView.ItemAnimator.FLAG_MOVED != 0

val RecyclerView.ItemAnimator.ItemHolderInfo.isAppearedInPreLayout
    get() = changeFlags and RecyclerView.ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT != 0
