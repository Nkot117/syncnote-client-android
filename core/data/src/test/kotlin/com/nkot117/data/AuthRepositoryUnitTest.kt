package com.nkot117.data

import com.nkot117.data.model.auth.LoginData
import com.nkot117.data.preferences.TokenManager
import com.nkot117.data.model.Result
import com.nkot117.data.model.auth.UserData
import com.nkot117.data.repository.AuthRepository
import com.nkot117.data.repository.AuthRepositoryImpl
import com.nkot117.network.SyncnoteServerApi
import com.nkot117.network.model.login.LoginResponse
import com.nkot117.network.model.login.UserInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class AuthRepositoryUnitTest : FunSpec({
    lateinit var mockTokenManager: TokenManager
    lateinit var mockApi: SyncnoteServerApi
    lateinit var moshi: Moshi
    lateinit var repository: AuthRepository

    beforeTest {
        mockApi = mockk()
        mockTokenManager = mockk(relaxed = true)
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        repository = AuthRepositoryImpl(mockApi, moshi, mockTokenManager)
    }

    context("loginのテスト") {
        context("正常系") {
            test("ログインが成功した場合、ユーザーデータが返却されること") {
                    // Arrange
                    val email = "test@test.com"
                    val password = "password"
                    val requestParams = LoginData(email, password).toNetworkRequest()
                    val response = LoginResponse(
                        userInfo = UserInfo(name = "test user", email = "test@test.com"),
                        accessToken = "accessToken",
                        refreshToken = "refreshToken"
                    )

                    coEvery { mockApi.login(requestParams) } returns Response.success(response)

                    // Action
                    val result = repository.login(email, password)

                    // Assert
                    result.shouldBeInstanceOf<Result.Success<UserData>>()
                    result.data.name shouldBe "test user"
                    result.data.email shouldBe "test@test.com"
                }
            }
        }

        context("異常系") {
    }

})