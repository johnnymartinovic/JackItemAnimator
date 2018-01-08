package com.johnnym.recyclerviewdemo.common.di

import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class ActivityScoped