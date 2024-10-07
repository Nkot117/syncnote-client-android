package com.nkot117.syncnoteclientapp.di

import com.nkot117.syncnoteclientapp.data.AuthRepository
import com.nkot117.syncnoteclientapp.data.AuthRepositoryImpl
import com.nkot117.syncnoteclientapp.network.SyncnoteServerApi
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
object AppModule {
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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://syncnote-server.onrender.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        return retrofit.create(SyncnoteServerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(syncnoteServerApi: SyncnoteServerApi, moshi: Moshi): AuthRepository {
        return AuthRepositoryImpl(syncnoteServerApi, moshi)
    }
}