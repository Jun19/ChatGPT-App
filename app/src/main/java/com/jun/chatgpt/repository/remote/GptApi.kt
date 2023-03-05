package com.jun.chatgpt.repository.remote

import com.jun.chatgpt.model.GptRequest
import com.jun.chatgpt.model.GptResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers

import retrofit2.http.POST


/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
interface GptApi {
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun completions(
        @Header("Authorization") authKey: String, @Body requestBody: GptRequest
    ): GptResponse
}