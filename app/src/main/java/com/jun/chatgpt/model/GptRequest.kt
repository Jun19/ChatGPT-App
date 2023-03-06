package com.jun.chatgpt.model

import com.google.gson.annotations.SerializedName


/**
 * 请求体
 *
 * @author Jun
 * @time 2023/3/5
 */
data class GptRequest(
    @SerializedName("messages") val messages: List<MessageDTO>,
    @SerializedName("model") val model: String,
)

