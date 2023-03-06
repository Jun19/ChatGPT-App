package com.jun.chatgpt.model

import com.google.gson.annotations.SerializedName


/**
 * 响应体
 *
 * @author Jun
 * @time 2023/3/5
 */
data class GptResponse (
    @SerializedName("choices") val choices: List<Choice>,
    @SerializedName("created") val created: Int,
    @SerializedName("id") val id: String,
    @SerializedName("model") val model: String,
    @SerializedName("object") val objectX: String,
    @SerializedName("usage") val usage: Usage
)

data class Choice(
    @SerializedName("finish_reason") val finishReason: String,
    @SerializedName("index") val index: Int,
    @SerializedName("message") val message: MessageDTO
)

data class Usage(
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)

