package com.dongsu.timely.data.di

import com.dongsu.timely.data.remote.service.TmapService
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
object RetrofitRESTModule {
    private const val BASE_URL = "https://apis.openapi.sk.com/"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Singleton
    @DefaultClient
    @Provides
    fun providesDefaultOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @LoggingClient
    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideTmapService(retrofit: Retrofit): TmapService =
        retrofit.create(TmapService::class.java)
}
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingClient