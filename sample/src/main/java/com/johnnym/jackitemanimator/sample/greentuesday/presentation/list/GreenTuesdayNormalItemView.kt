package com.johnnym.jackitemanimator.sample.greentuesday.presentation.list

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import com.johnnym.jackitemanimator.sample.R

class GreenTuesdayNormalItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.green_tuesday_normal_item, this)

        outlineProvider = CapturedPhotoViewOutlineProvider()
        clipToOutline = true
    }

    class CapturedPhotoViewOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val radius = view.resources.getDimension(R.dimen.green_tuesday_item_radius)
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
}
