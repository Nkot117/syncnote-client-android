package com.nkot117.data.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nkot117.data.model.auth.RefreshTokenData

class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
    context,
    "secret_shared_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    );

    fun saveAccessToken(accessToken: String) {
        with(sharedPreferences.edit()) {
            putString("jwt_access_token", accessToken)
            apply()
        }
    }

    fun saveRefreshToken(refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("jwt_refresh_token", refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("jwt_access_token", null)
    }

    fun getRefreshToken(): RefreshTokenData? {
        val token = sharedPreferences.getString("jwt_refresh_token", null) ?: return null
        return RefreshTokenData(
            refreshToken = token
        )
    }

    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("jwt_access_token")
            remove("jwt_refresh_token")
            apply()
        }
    }
}