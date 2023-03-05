package com.jun.chatgpt.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
@Entity
data class MessageDTO(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
){
}
