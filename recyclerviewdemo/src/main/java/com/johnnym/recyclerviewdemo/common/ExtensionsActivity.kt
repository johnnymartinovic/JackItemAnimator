package com.johnnym.recyclerviewdemo.common

import android.app.Activity
import com.johnnym.recyclerviewdemo.RvdApplication

val Activity.rvdApplication: RvdApplication
    get() = application as RvdApplication