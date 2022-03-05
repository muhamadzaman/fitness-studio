package com.test.fitnessstudios.di

import com.test.fitnessstudios.repository.AppRepository
import com.test.fitnessstudios.repository.AppRepositoryImpl
import com.test.fitnessstudios.repository.BusinessRepository
import com.test.fitnessstudios.repository.BusinessRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideBusinessRepo(impl : BusinessRepositoryImpl): BusinessRepository = impl

    @Provides
    fun provideAppRepo(impl : AppRepositoryImpl): AppRepository = impl
}