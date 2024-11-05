package com.dongsu.timely.data.di

import com.dongsu.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

//    @Singleton
//    @DefaultClient
//    @Provides
//    fun providesDefaultOkHttpClient(): OkHttpClient =
//        OkHttpClient.Builder().build()

//    @LoggingClient
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
        .baseUrl(BuildConfig.TMAP_BASE_URL)
        .client(okHttpClient)
        .build()

//    @Singleton
//    @Provides
//    fun provideTmapService(retrofit: Retrofit): TmapService =
//        retrofit.create(TmapService::class.java)
}
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class DefaultClient
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class LoggingClient