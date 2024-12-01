package com.nkot117.data

import com.nkot117.data.preferences.TokenManager
import com.nkot117.data.model.Result
import com.nkot117.data.model.auth.RefreshTokenData
import com.nkot117.data.model.memo.MemoData
import com.nkot117.data.repository.MemoRepository
import com.nkot117.data.repository.MemoRepositoryImpl
import com.nkot117.network.SyncnoteServerApi
import com.nkot117.network.model.login.RefreshTokenResponse
import com.nkot117.network.model.memo.MemoDetailResponse
import com.nkot117.network.model.memo.MemoInfo
import com.nkot117.network.model.memo.MemoListResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MemoRepositoryUnitTest : FunSpec({
    lateinit var mockTokenManager: TokenManager
    lateinit var mockApi: SyncnoteServerApi
    lateinit var moshi: Moshi
    lateinit var repository: MemoRepository

    beforeTest {
        mockApi = mockk()
        mockTokenManager = mockk(relaxed = true)
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        repository = MemoRepositoryImpl(mockApi, moshi, mockTokenManager)

    }

    // Common Arrange
    val memoInfo = MemoInfo(
        id = "1",
        title = "title",
        content = "content"
    )

    context("getMemoListのテスト") {
        // Common Arrange
        fun createMemoListResponse(): MemoListResponse {
            return MemoListResponse(
                listOf(
                    memoInfo
                )
            )
        }
        context("正常系") {
            test("メモが取得できた場合、メモのリストデータが返却されること") {
                // Arrange
                val response = createMemoListResponse()

                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.getMemoList(any()) } returns Response.success(response)

                // Act
                val result = repository.getMemoList()

                // Assert
                result.shouldBeInstanceOf<Result.Success<List<MemoData>>>()
                result.data[0].id.shouldBe(memoInfo.id)
                result.data[0].title.shouldBe(memoInfo.title)
                result.data[0].content.shouldBe(memoInfo.content)
            }
        }

        context("異常系") {
            test("アクセストークが保存されていない場合は既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns null

                // Act
                val result = repository.getMemoList()

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Token not found")
            }

            test("APIが成功を返してもレスポンスボディがnullの場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.getMemoList(any()) } returns Response.success(null)

                // Act
                val result = repository.getMemoList()

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Unknown error")
            }

            context("サーバーからエラーが返却") {
                test("トークン期限切れの場合、トークンリフレッシュ後に再試行されること") {
                    // Arrange
                    val expiredToken = "Expired Token"
                    val refreshToken = RefreshTokenData("Refresh Token")
                    val newToken = "New Token"

                    val getMemoListResponse = createMemoListResponse()
                    val refreshTokenResponse = RefreshTokenResponse(newToken)

                    val errorJson = """{
                        "message": "Token expired",
                        "reason": "expired"
                        }""".trimIndent()

                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockTokenManager.getAccessToken() } returns expiredToken andThen newToken

                    // 期限切れトークンが設定された場合、トークン期限切れエラーを返す
                    coEvery { mockApi.getMemoList("Bearer $expiredToken") } returns Response.error(
                        401,
                        responseBody
                    )
                    // 有効トークンが設定された場合、正常のレスポンスを返す
                    coEvery { mockApi.getMemoList("Bearer $newToken") } returns Response.success(
                        getMemoListResponse
                    )

                    coEvery { mockTokenManager.getRefreshToken() } returns refreshToken
                    coEvery { mockApi.refreshToken(refreshToken.toNetworkRequest()) } returns Response.success(
                        refreshTokenResponse
                    )

                    // Act
                    val result = repository.getMemoList()

                    // Assert
                    result.shouldBeInstanceOf<Result.Success<List<MemoData>>>()
                    coVerify(exactly = 1) { mockApi.getMemoList("Bearer $expiredToken") }
                    coVerify(exactly = 1) { mockApi.getMemoList("Bearer $newToken") }
                }


                test("レスポンスボディにエラーメッセージが含まれている場合、含まれているエラーメッセージが返却されること") {
                    // Arrange
                    val errorJson = """
                    {
                    "message": "error message",
                    "reason" : "error reason"
                    }
                """.trimIndent()
                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockApi.getMemoList(any()) } returns Response.error(
                        500,
                        responseBody
                    )

                    // Act
                    val result = repository.getMemoList()

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "error message"
                    result.errorMessage.reason shouldBe "error reason"
                }

                test("レスポンスボディがない場合、既定のエラーメッセージが返却されること") {
                    // Arrange
                    coEvery { mockApi.getMemoList(any()) } returns Response.error(
                        500,
                        mockk(relaxed = true)
                    )

                    // Act
                    val result = repository.getMemoList()

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "Unknown error"
                }
            }

            context("処理中に異常終了した場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery {  mockApi.getMemoList(any()) } throws RuntimeException("Network error")

                // Act
                val result = repository.getMemoList()

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message shouldBe "Unknown error"
            }
        }
    }
    context("getMemoDetailのテスト") {
        // Common Arrange
        fun createMemoDetailResponse(): MemoDetailResponse {
            return MemoDetailResponse(
                id = memoInfo.id,
                title = memoInfo.title,
                content = memoInfo.content
            )
        }
        context("正常系") {
            test("メモ詳細が取得できた場合、メモ詳細データが返却されること") {
                // Arrange
                val response = createMemoDetailResponse()

                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.getMemoDetail(any(), any()) } returns Response.success(response)

                // Act
                val result = repository.getMemoDetail(memoInfo.id)

                // Assert
                result.shouldBeInstanceOf<Result.Success<MemoData>>()
                result.data.id.shouldBe(memoInfo.id)
                result.data.title.shouldBe(memoInfo.title)
                result.data.content.shouldBe(memoInfo.content)
            }
        }

        context("異常系") {
            test("アクセストークが保存されていない場合は既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns null

                // Act
                val result = repository.getMemoDetail(memoInfo.id)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Token not found")
            }

            test("APIが成功を返してもレスポンスボディがnullの場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.getMemoList(any()) } returns Response.success(null)

                // Act
                val result = repository.getMemoDetail(memoInfo.id)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Unknown error")
            }

            context("サーバーからエラーが返却") {
                test("トークン期限切れの場合、トークンリフレッシュ後に再試行されること") {
                    // Arrange
                    val expiredToken = "Expired Token"
                    val refreshToken = RefreshTokenData("Refresh Token")
                    val newToken = "New Token"

                    val getMemoDetailResponse = createMemoDetailResponse()
                    val refreshTokenResponse = RefreshTokenResponse(newToken)

                    val errorJson = """{
                        "message": "Token expired",
                        "reason": "expired"
                        }""".trimIndent()

                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockTokenManager.getAccessToken() } returns expiredToken andThen newToken

                    // 期限切れトークンが設定された場合、トークン期限切れエラーを返す
                    coEvery { mockApi.getMemoDetail(memoInfo.id, "Bearer $expiredToken") } returns Response.error(
                        401,
                        responseBody
                    )
                    // 有効トークンが設定された場合、正常のレスポンスを返す
                    coEvery { mockApi.getMemoDetail(memoInfo.id, "Bearer $newToken") } returns Response.success(
                        getMemoDetailResponse
                    )

                    coEvery { mockTokenManager.getRefreshToken() } returns refreshToken
                    coEvery { mockApi.refreshToken(refreshToken.toNetworkRequest()) } returns Response.success(
                        refreshTokenResponse
                    )

                    // Act
                    val result = repository.getMemoDetail(memoInfo.id)

                    // Assert
                    result.shouldBeInstanceOf<Result.Success<MemoData>>()
                    coVerify(exactly = 1) { mockApi.getMemoDetail(memoInfo.id, "Bearer $expiredToken") }
                    coVerify(exactly = 1) { mockApi.getMemoDetail(memoInfo.id, "Bearer $newToken") }
                }


                test("レスポンスボディにエラーメッセージが含まれている場合、含まれているエラーメッセージが返却されること") {
                    // Arrange
                    val errorJson = """
                    {
                    "message": "error message",
                    "reason" : "error reason"
                    }
                """.trimIndent()
                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockApi.getMemoDetail(any(), any()) } returns Response.error(
                        500,
                        responseBody
                    )

                    // Act
                    val result = repository.getMemoDetail(memoInfo.id)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "error message"
                    result.errorMessage.reason shouldBe "error reason"
                }

                test("レスポンスボディがない場合、既定のエラーメッセージが返却されること") {
                    // Arrange
                    coEvery { mockApi.getMemoDetail(any(), any()) } returns Response.error(
                        500,
                        mockk(relaxed = true)
                    )

                    // Act
                    val result = repository.getMemoDetail(memoInfo.id)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "Unknown error"
                }
            }

            context("処理中に異常終了した場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery {  mockApi.getMemoDetail(any(), any()) } throws RuntimeException("Network error")

                // Act
                val result = repository.getMemoDetail(memoInfo.id)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message shouldBe "Unknown error"
            }
        }
    }
    context("updateMemoのテスト") {
        // Common Arrange
        fun createMemoDetailResponse(): MemoDetailResponse {
            return MemoDetailResponse(
                id = memoInfo.id,
                title = memoInfo.title,
                content = memoInfo.content
            )
        }
        context("正常系") {
            test("メモの更新が成功した場合、メモ詳細データが返却されること") {
                // Arrange
                val response = createMemoDetailResponse()

                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.updateMemo(any(), any(), any()) } returns Response.success(response)

                // Act
                val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                // Assert
                result.shouldBeInstanceOf<Result.Success<MemoData>>()
                result.data.id.shouldBe(memoInfo.id)
                result.data.title.shouldBe(memoInfo.title)
                result.data.content.shouldBe(memoInfo.content)
            }
        }

        context("異常系") {
            test("アクセストークが保存されていない場合は既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns null

                // Act
                val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Token not found")
            }

            test("APIが成功を返してもレスポンスボディがnullの場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery { mockTokenManager.getAccessToken() } returns "Access Token"
                coEvery { mockApi.updateMemo(any(), any(), any()) } returns Response.success(null)

                // Act
                val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message.shouldBe("Unknown error")
            }

            context("サーバーからエラーが返却") {
                test("トークン期限切れの場合、トークンリフレッシュ後に再試行されること") {
                    // Arrange
                    val expiredToken = "Expired Token"
                    val refreshToken = RefreshTokenData("Refresh Token")
                    val newToken = "New Token"

                    val getMemoDetailResponse = createMemoDetailResponse()
                    val refreshTokenResponse = RefreshTokenResponse(newToken)
                    val requestParams = MemoData(
                        memoInfo.id,
                        memoInfo.title,
                        memoInfo.content
                    )

                    val errorJson = """{
                        "message": "Token expired",
                        "reason": "expired"
                        }""".trimIndent()

                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockTokenManager.getAccessToken() } returns expiredToken andThen newToken

                    // 期限切れトークンが設定された場合、トークン期限切れエラーを返す
                    coEvery { mockApi.updateMemo(requestParams.id, "Bearer $expiredToken", requestParams.toNetworkRequest()) } returns Response.error(
                        401,
                        responseBody
                    )
                    // 有効トークンが設定された場合、正常のレスポンスを返す
                    coEvery { mockApi.updateMemo(requestParams.id, "Bearer $newToken", requestParams.toNetworkRequest()) } returns Response.success(
                        getMemoDetailResponse
                    )

                    coEvery { mockTokenManager.getRefreshToken() } returns refreshToken
                    coEvery { mockApi.refreshToken(refreshToken.toNetworkRequest()) } returns Response.success(
                        refreshTokenResponse
                    )

                    // Act
                    val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                    // Assert
                    result.shouldBeInstanceOf<Result.Success<MemoData>>()
                    coVerify(exactly = 1) { mockApi.updateMemo(requestParams.id, "Bearer $expiredToken", requestParams.toNetworkRequest()) }
                    coVerify(exactly = 1) { mockApi.updateMemo(requestParams.id, "Bearer $newToken", requestParams.toNetworkRequest()) }
                }


                test("レスポンスボディにエラーメッセージが含まれている場合、含まれているエラーメッセージが返却されること") {
                    // Arrange
                    val errorJson = """
                    {
                    "message": "error message",
                    "reason" : "error reason"
                    }
                """.trimIndent()
                    val responseBody = errorJson.toResponseBody("application/json".toMediaType())

                    coEvery { mockApi.updateMemo(any(), any(), any()) } returns Response.error(
                        500,
                        responseBody
                    )

                    // Act
                    val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "error message"
                    result.errorMessage.reason shouldBe "error reason"
                }

                test("レスポンスボディがない場合、既定のエラーメッセージが返却されること") {
                    // Arrange
                    coEvery { mockApi.updateMemo(any(), any(), any()) } returns Response.error(
                        500,
                        mockk(relaxed = true)
                    )

                    // Act
                    val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                    // Assert
                    result.shouldBeInstanceOf<Result.Failure>()
                    result.errorMessage.message shouldBe "Unknown error"
                }
            }

            context("処理中に異常終了した場合、既定のエラーメッセージが返却されること") {
                // Arrange
                coEvery {  mockApi.updateMemo(any(), any(), any()) } throws RuntimeException("Network error")

                // Act
                val result = repository.updateMemo(memoInfo.id, memoInfo.title, memoInfo.content)

                // Assert
                result.shouldBeInstanceOf<Result.Failure>()
                result.errorMessage.message shouldBe "Unknown error"
            }
        }
    }

})
