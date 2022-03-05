package com.test.fitnessstudios.di

import com.test.fitnessstudios.ui.activity.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ApiModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: HomeActivity)
}