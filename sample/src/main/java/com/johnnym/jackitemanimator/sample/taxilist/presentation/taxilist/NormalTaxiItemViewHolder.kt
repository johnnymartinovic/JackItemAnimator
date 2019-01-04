package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.rstanic12.resourceful.bindColor
import com.github.rstanic12.resourceful.bindString
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatus
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListItemViewModel

class NormalTaxiItemViewHolder(
        private val context: Context,
        val normalItemView: NormalTaxiItemView
) : RecyclerView.ViewHolder(normalItemView) {

    private val starsFormattedText: String by bindString(R.string.taxi_list_item_stars_format)
    private val distanceFormattedText: String by bindString(R.string.taxi_list_item_distance_format)

    private val statusAvailableColor: Int by bindColor(R.color.taxi_list_item_status_available)
    private val statusUnavailableColor: Int by bindColor(R.color.taxi_list_item_status_unavailable)

    fun bind(itemViewModel: TaxiListItemViewModel) {
        Glide.with(context)
                .load(itemViewModel.driverPhotoUrl)
                .apply(RequestOptions()
                        .placeholder(android.R.color.white)
                        .error(android.R.color.black)
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

    fun createTaxiStatusChangeAnimator(
            oldTaxiStatus: TaxiStatus,
            newTaxiStatus: TaxiStatus
    ): Animator {
        fun getColorForTaxiStatus(taxiStatus: TaxiStatus) = when (taxiStatus) {
            TaxiStatus.AVAILABLE -> statusAvailableColor
            TaxiStatus.OCCUPIED -> statusUnavailableColor
        }

        @ColorInt val startColor: Int = getColorForTaxiStatus(oldTaxiStatus)
        @ColorInt val endColor: Int = getColorForTaxiStatus(newTaxiStatus)

        val spinningFirstHalf = ValueAnimator.ofFloat(0f, 810f)
                .apply {
                    addUpdateListener {
                        normalItemView.statusBar.rotationY = it.animatedValue as Float
                    }
                    interpolator = AccelerateInterpolator()
                }

        val spinningSecondHalf = ValueAnimator.ofFloat(-810f, 0f)
                .apply {
                    addUpdateListener {
                        normalItemView.statusBar.rotationY = it.animatedValue as Float
                    }
                    interpolator = DecelerateInterpolator()
                }

        val colorChange = ValueAnimator.ofInt(startColor, endColor)
                .apply {
                    setEvaluator(ArgbEvaluator())
                    addUpdateListener {
                        normalItemView.statusBar.setBackgroundColor(it.animatedValue as Int)
                    }
                }

        val spinningFull = AnimatorSet().apply {
            playSequentially(spinningFirstHalf, spinningSecondHalf)
        }

        return AnimatorSet().apply {
            playTogether(spinningFull, colorChange)
        }
    }
}