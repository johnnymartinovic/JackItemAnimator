package com.johnnym.jackitemanimator.sample.greentuesday

import com.johnnym.jackitemanimator.sample.common.di.ActivityScoped
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayActivity
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(
        modules = [(GreenTuesdayModule::class)])
interface GreenTuesdayComponent {

    fun inject(greenTuesdayActivity: GreenTuesdayActivity)
}
