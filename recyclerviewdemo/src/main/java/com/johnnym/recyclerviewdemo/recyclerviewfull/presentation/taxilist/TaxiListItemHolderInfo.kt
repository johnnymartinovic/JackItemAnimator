package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import androidx.recyclerview.widget.RecyclerView

class TaxiListItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

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

class FlagsValues(itemHolderInfo: RecyclerView.ItemAnimator.ItemHolderInfo) {

    val isChanged = itemHolderInfo.changeFlags and RecyclerView.ItemAnimator.FLAG_CHANGED != 0

    val isRemoved = itemHolderInfo.changeFlags and RecyclerView.ItemAnimator.FLAG_REMOVED != 0

    val isInvalidated = itemHolderInfo.changeFlags and RecyclerView.ItemAnimator.FLAG_INVALIDATED != 0

    val isMoved = itemHolderInfo.changeFlags and RecyclerView.ItemAnimator.FLAG_MOVED != 0

    val isAppearedInPreLayout = itemHolderInfo.changeFlags and RecyclerView.ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT != 0
}