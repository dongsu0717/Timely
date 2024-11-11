package com.dongsu.timely.data.di

import com.dongsu.timely.data.common.TIMELY_BASE_URL
import com.dongsu.timely.data.common.TMAP_BASE_URL
import com.dongsu.timely.data.datasource.UserLocalDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
    fun provideAuthInterceptor (
        userLocalDatasource: UserLocalDatasource
    ): Interceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${userLocalDatasource.getToken()}")
        chain.proceed(requestBuilder.build())
    }

    @Singleton
    @ExcludeHeader
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @IncludeHeader
    @Provides
    fun providesAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @TMap
    @Provides
    fun provideTMapRetrofit(
        @ExcludeHeader okHttpClient: OkHttpClient
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
        @ExcludeHeader okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TIMELY_BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @TimelyAuth
    @Provides
    fun provideTimelyAuthRetrofit(
        @IncludeHeader okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TIMELY_BASE_URL)
            .client(okHttpClient)
            .build()
}
//OkhttpClient
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExcludeHeader

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IncludeHeader

//Retrofit
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TMap

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Timely

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimelyAuth

