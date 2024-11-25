package com.nkot117.data.di


import android.content.Context
import com.nkot117.data.preferences.TokenManager
import com.nkot117.data.repository.AuthRepository
import com.nkot117.data.repository.MemoRepository
import com.nkot117.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(syncnoteServerApi: SyncnoteServerApi, moshi: Moshi, tokenManager: TokenManager): AuthRepository {
        return com.nkot117.data.repository.AuthRepositoryImpl(
            syncnoteServerApi,
            moshi,
            tokenManager
        )
    }

    @Provides
    @Singleton
    fun provideMemoRepository(syncnoteServerApi: SyncnoteServerApi, moshi: Moshi, tokenManager: TokenManager): MemoRepository {
        return com.nkot117.data.repository.MemoRepositoryImpl(
            syncnoteServerApi,
            moshi,
            tokenManager
        )
    }
}
