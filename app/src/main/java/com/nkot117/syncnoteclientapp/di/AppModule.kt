package com.nkot117.syncnoteclientapp.di

import android.content.Context
import com.nkot117.syncnoteclientapp.data.preferences.TokenManager
import com.nkot117.syncnoteclientapp.data.repository.AuthRepository
import com.nkot117.syncnoteclientapp.data.repository.AuthRepositoryImpl
import com.nkot117.syncnoteclientapp.data.repository.MemoRepository
import com.nkot117.syncnoteclientapp.data.repository.MemoRepositoryImpl
import com.nkot117.syncnoteclientapp.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(syncnoteServerApi: SyncnoteServerApi, moshi: Moshi, tokenManager: TokenManager): AuthRepository {
        return AuthRepositoryImpl(syncnoteServerApi, moshi, tokenManager)
    }

    @Provides
    @Singleton
    fun provideMemoRepository(syncnoteServerApi: SyncnoteServerApi, moshi: Moshi, tokenManager: TokenManager): MemoRepository {
        return MemoRepositoryImpl(syncnoteServerApi, moshi, tokenManager)
    }
}
