package com.jun.chatgpt.repository.local

import androidx.room.Dao
import androidx.room.Query
import com.jun.chatgpt.model.Message

@Dao
interface MessageDao : BaseDao<Message> {
    @Query("SELECT * FROM message")
    suspend fun fetchAll(): List<Message>

    @Query("Delete FROM message")
    suspend fun deleteAll()
}