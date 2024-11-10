package com.dongsu.timely.data.di

import com.dongsu.timely.data.common.TIMELY_BASE_URL
import com.dongsu.timely.data.common.TMAP_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @TMap
    @Provides
    fun provideTMapRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TMAP_BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Timely
    @Provides
    fun provideTimelyRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TIMELY_BASE_URL)
            .client(okHttpClient)
            .build()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TMap

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Timely
