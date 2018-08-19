package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.rstanic12.resourceful.bindColor
import com.github.rstanic12.resourceful.bindString
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

class NormalItemViewHolder(
        private val context: Context,
        val normalItemView: NormalTaxiItemView
) : RecyclerView.ViewHolder(normalItemView) {

    private val starsFormattedText: String by bindString(R.string.taxi_list_item_stars_format)
    private val distanceFormattedText: String by bindString(R.string.taxi_list_item_distance_format)

    private val statusAvailableColor: Int by bindColor(R.color.taxi_list_item_status_available)
    private val statusUnavailableColor: Int by bindColor(R.color.taxi_list_item_status_unavailable)
    private val distanceDecreasedSignalColor: Int by bindColor(R.color.taxi_list_item_distance_decreased_signal)
    private val distanceIncreasedSignalColor: Int by bindColor(R.color.taxi_list_item_distance_increased_signal)
    private val transparentColor: Int by bindColor(android.R.color.transparent)

    fun bind(itemViewModel: TaxiListItemViewModel) {
        Glide.with(context)
                .load(itemViewModel.driverPhotoUrl)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_star)
                        .error(R.drawable.ic_distance)
                        .dontAnimate())
                .into(normalItemView.driverPhoto)

        normalItemView.driverName.text = itemViewModel.driverName
        normalItemView.stars.text = String.format(starsFormattedText, itemViewModel.stars)
        setTaxiStatus(itemViewModel.taxiStatus)
        setDistanceValue(itemViewModel.distance)
    }

    fun setTaxiStatus(taxiStatus: TaxiStatus) {
        when (taxiStatus) {
            TaxiStatus.AVAILABLE -> normalItemView.statusBar.setBackgroundColor(statusAvailableColor)
            TaxiStatus.OCCUPIED -> normalItemView.statusBar.setBackgroundColor(statusUnavailableColor)
        }
    }

    fun setDistanceValue(distance: Float) {
        normalItemView.distance.text = String.format(distanceFormattedText, distance)
    }

    // TODO
//    fun setupItemViewHolderAndCreateChangeAnimator(
//            preInfo: TaxiListItemHolderInfo,
//            postInfo: TaxiListItemHolderInfo
//    ): Animator {
//        val animatorNullableList: MutableList<Animator?> = mutableListOf()
//
//        val itemPayload = createCombinedTaxiListItemPayload(preInfo.payloads)
//
//        val taxiStatusChange = itemPayload.taxiStatusChange
//        if (taxiStatusChange != null) {
//            animatorNullableList.add(setupHolderAndCreateTaxiStatusChangeAnimator(taxiStatusChange))
//        }
//
//        val distanceChange = itemPayload.distanceChange
//        if (distanceChange != null) {
//            animatorNullableList.add(setupHolderAndCreateDistanceChangeAnimator(itemPayload.distanceChange))
//        }
//
//        if (preInfo.left != postInfo.left || preInfo.top != postInfo.top) {
//            animatorNullableList.add(createMoveAnimatorAndSetStartState(preInfo.left, preInfo.top, postInfo.left, postInfo.top))
//        }
//
//        val animatorList = animatorNullableList.filterNotNull()
//        return AnimatorSet().apply {
//            playTogether(animatorList)
//        }
//    }
//
//    private fun setupHolderAndCreateTaxiStatusChangeAnimator(
//            taxiStatusChange: Change<TaxiStatus>
//    ): Animator {
//        @ColorInt val startColor: Int
//        @ColorInt val endColor: Int
//        if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
//            startColor = statusAvailableColor
//            endColor = statusUnavailableColor
//        } else {
//            startColor = statusUnavailableColor
//            endColor = statusAvailableColor
//        }
//
//        normalItemView.statusBar.setBackgroundColor(startColor)
//
//        val spinningFirstHalf = ValueAnimator.ofFloat(0f, 810f)
//                .apply {
//                    addUpdateListener {
//                        normalItemView.statusBar.rotationY = it.animatedValue as Float
//                    }
//                    addListener(object : AnimatorListenerAdapter() {
//
//                        override fun onAnimationStart(animation: Animator) {
//                            normalItemView.statusBar.setBackgroundColor(startColor)
//                        }
//                    })
//                    interpolator = AccelerateInterpolator()
//                }
//
//        val spinningSecondHalf = ValueAnimator.ofFloat(-810f, 0f)
//                .apply {
//                    addUpdateListener {
//                        normalItemView.statusBar.rotationY = it.animatedValue as Float
//                    }
//                    interpolator = DecelerateInterpolator()
//                }
//
//        val colorChange = ValueAnimator.ofInt(startColor, endColor)
//                .apply {
//                    setEvaluator(ArgbEvaluator())
//                    addUpdateListener {
//                        normalItemView.statusBar.setBackgroundColor(it.animatedValue as Int)
//                    }
//                }
//
//        val spinningFull = AnimatorSet().apply {
//            playSequentially(spinningFirstHalf, spinningSecondHalf)
//        }
//
//        return AnimatorSet().apply {
//            playTogether(spinningFull, colorChange)
//            addListener(object : AnimatorListenerAdapter() {
//
//                // reset view holder state so it can be reused in other animations without improperly
//                // set properties
//                override fun onAnimationEnd(animation: Animator) {
//                    normalItemView.statusBar.rotationY = 0f
//                    normalItemView.statusBar.setBackgroundColor(endColor)
//                }
//            })
//        }
//    }
//
//    private fun setupHolderAndCreateDistanceChangeAnimator(
//            distanceChange: Change<Float>
//    ): Animator {
//        val x = (normalItemView.distance.x + normalItemView.distance.width / 2).toInt()
//        val y = (normalItemView.distance.y + normalItemView.distance.height / 2).toInt()
//
//        val startRadius = 0
//        val endRadius = Math.hypot(
//                normalItemView.distance.width.toDouble(),
//                normalItemView.distance.height.toDouble()
//        ).toInt()
//
//        val distanceChangeCircularRevealAnimator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ViewAnimationUtils.createCircularReveal(normalItemView.revealHelperView, x, y, startRadius.toFloat(), endRadius.toFloat())
//        } else {
//            null
//        }
//
//        @ColorInt val normalColor = transparentColor
//        @ColorInt val effectColor =
//                if (distanceChange.old > distanceChange.new) distanceDecreasedSignalColor
//                else distanceIncreasedSignalColor
//
//        val distanceChangeCircuralRevealColorAnimator = ValueAnimator.ofInt(normalColor, effectColor, normalColor)
//                .apply {
//                    setEvaluator(ArgbEvaluator())
//                    addUpdateListener { normalItemView.revealHelperView.setBackgroundColor(it.animatedValue as Int) }
//                }
//
//        val distanceChangeAnimator = AnimatorSet()
//
//        if (distanceChangeCircularRevealAnimator != null) {
//            distanceChangeAnimator.playTogether(
//                    distanceChangeCircularRevealAnimator,
//                    distanceChangeCircuralRevealColorAnimator)
//        } else {
//            distanceChangeAnimator.playTogether(
//                    distanceChangeCircuralRevealColorAnimator
//            )
//        }
//
//        return distanceChangeAnimator.apply {
//            addListener(object : AnimatorListenerAdapter() {
//
//                // reset view holder state so it can be reused in other animations without improperly
//                // set properties
//                override fun onAnimationEnd(animation: Animator) {
//                    normalItemView.revealHelperView.setBackgroundColor(normalColor)
//                }
//            })
//        }
//    }
}