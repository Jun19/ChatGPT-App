package com.jun.chatgpt.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jun.chatgpt.model.enums.MessageStatus

/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Expose @SerializedName("role") val role: String,
    @Expose @SerializedName("content") val content: String,
    val sessionId: Int = 0,
    //响应时间
    val responseTime: Long = 1,
    //插入时间
    val insertTime: Long = System.currentTimeMillis(),
    var status: Int = MessageStatus.UNFINISHED.status
) {

    fun toDTO(): MessageDTO {
        return MessageDTO(role = role, content = content)
    }
}
