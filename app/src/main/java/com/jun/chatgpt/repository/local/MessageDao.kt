package com.jun.chatgpt.repository.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jun.chatgpt.model.Message

@Dao
interface MessageDao : BaseDao<Message> {
    @Query("SELECT * FROM message")
    suspend fun queryAll(): List<Message>

    @Transaction
    @Query("select * from message where sessionId=:sessionId")
    fun selectMessageBySessionID(sessionId: Int): MutableList<Message>

    @Query("Delete FROM message ")
    suspend fun deleteAll()

    @Query("Delete FROM message where sessionId=:sessionId")
    suspend fun deleteAll(sessionId: Int)
}