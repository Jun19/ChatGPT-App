package com.jun.chatgpt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
@Entity(tableName = "session")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val lastSessionTime: Long
) {

}