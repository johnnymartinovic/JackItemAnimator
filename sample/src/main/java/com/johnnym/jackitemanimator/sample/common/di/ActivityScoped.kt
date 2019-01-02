package com.johnnym.jackitemanimator.sample.common.di

import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class ActivityScoped