package com.jun.chatgpt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    ) {
}