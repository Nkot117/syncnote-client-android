package com.nkot117.data

import com.nkot117.data.model.auth.LoginData
import com.nkot117.data.preferences.TokenManager
import com.nkot117.data.model.Result
import com.nkot117.data.model.auth.UserData
import com.nkot117.data.repository.AuthRepository
import com.nkot117.data.repository.AuthRepositoryImpl
import com.nkot117.network.SyncnoteServerApi
import com.nkot117.network.model.login.LoginRequest
import com.nkot117.network.model.login.LoginResponse
import com.nkot117.network.model.login.UserInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
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
        // Common Arrange
        val email = "test@test.com"
        val password = "password"

        fun createLoginParams() : LoginRequest {
           return LoginData(email, password).toNetworkRequest()
        }

        context("正常系") {
            test("ログインが成功した場合、ユーザーデータが返却されること") {
                val requestParams = createLoginParams()
                val response = LoginResponse(
                    userInfo = UserInfo(name = "test user", email = "test@test.com"),
                    accessToken = "accessToken",
                    refreshToken = "refreshToken"
                )

                coEvery { mockApi.login(requestParams) } returns Response.success(response)

                // Act
                val result = repository.login(email, password)

                // Assert
                result.shouldBeInstanceOf<Result.Success<UserData>>()
                result.data.name shouldBe "test user"
                result.data.email shouldBe "test@test.com"
            }
        }

        context("異常系") {
            context("サーバーからエラーが返却") {
                test("レスポンスボディにエラーメッセージが含まれている場合、含まれているエラーメッセージが返却されること") {
                    // Arrange
                    val requestParams = createLoginParams()
                    val errorJson = """
                    {
                    "message": "error message",
                    "reason" : "error reason"
                    }
                """.trimIndent()
                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockApi.login(requestParams) } returns Response.error(500, responseBody)

                    // Act
                    val result = repository.login(email, password)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "error message"
                    result.errorMessage.reason shouldBe "error reason"
                }

                test("レスポンスボディがない場合、既定のエラーメッセージが返却されること") {
                    // Arrange
                    val requestParams = createLoginParams()

                    coEvery { mockApi.login(requestParams) } returns Response.error(
                        500,
                        mockk(relaxed = true)
                    )

                    // Act
                    val result = repository.login(email, password)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"
                }
            }

            context("処理中に異常終了した場合、既定のエラーメッセージが返却されること") {
                // Arrange
                val requestParams = createLoginParams()

                coEvery { mockApi.login(requestParams) } throws RuntimeException("Network error")

                // Act
                val result = repository.login(email, password)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message shouldBe "ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"
            }

            context("APIが成功を返してもレスポンスボディがnullの場合、既定のエラーメッセージが返却されること") {
                // Arrange
                val requestParams = createLoginParams()

                coEvery { mockApi.login(requestParams) } returns Response.success(null)

                // Act
                val result = repository.login(email, password)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message shouldBe "ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"
            }
        }
    }
})
