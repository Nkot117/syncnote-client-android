package com.nkot117.network.di

import com.nkot117.network.BuildConfig
import com.nkot117.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    @Singleton
    fun provideSyncnoteServiceApi(okHttpClient: OkHttpClient, moshi: Moshi): SyncnoteServerApi {
        val baseUrl = BuildConfig.BASE_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        return retrofit.create(SyncnoteServerApi::class.java)
    }
}
