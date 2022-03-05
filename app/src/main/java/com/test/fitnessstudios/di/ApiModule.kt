package com.test.fitnessstudios.di

import com.test.fitnessstudios.yelpAPIKey
import com.test.fitnessstudios.BuildConfig
import com.test.fitnessstudios.network.ApiHelper
import com.test.fitnessstudios.network.ApiHelperImpl
import com.test.fitnessstudios.network.ApiService
import com.test.fitnessstudios.network.WebConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $yelpAPIKey")
                    .build()
                chain.proceed(request)
            })
    } else {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $yelpAPIKey")
                    .build()
                chain.proceed(request)
            })
    }

    @Provides
    fun provideApi(clientBuilder: OkHttpClient.Builder): Retrofit =
        Retrofit.Builder().baseUrl(WebConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(clientBuilder.build()).build()


    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}