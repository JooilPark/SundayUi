package com.obelab.ui.ui

import com.obelab.ui.ui.healthmaxisb.HealthMaxControllers
import com.obelab.ui.ui.healthmaxisb.HealthMaxViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appmodule = module {
    viewModel { HealthMaxViewModel() }
    single { HealthMaxControllers(androidContext()) }
}