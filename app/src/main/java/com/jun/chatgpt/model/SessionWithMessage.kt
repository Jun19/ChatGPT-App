package com.jun.chatgpt.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 *
 *
 * @author Jun
 * @time 2023/3/8
 */
data class SessionWithMessage(
    @Embedded
    val session: Session,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val messages: List<Message>
) {

}