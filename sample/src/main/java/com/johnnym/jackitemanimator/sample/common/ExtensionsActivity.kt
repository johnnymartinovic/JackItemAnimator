package com.johnnym.jackitemanimator.sample.common

import android.app.Activity
import com.johnnym.jackitemanimator.sample.RvdApplication

val Activity.rvdApplication: RvdApplication
    get() = application as RvdApplication