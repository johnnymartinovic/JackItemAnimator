package com.johnnym.jackitemanimator.sample.common

import android.app.Activity
import com.johnnym.jackitemanimator.sample.SampleApplication

val Activity.sampleApplication: SampleApplication
    get() = application as SampleApplication