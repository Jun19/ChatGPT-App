package com.jun.chatgpt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 模板
 *
 * @author Jun
 * @time 2023/3/8
 */
@Entity(tableName = "template")
data class Template(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tempContent: String = "",
    val name: String = ""
) {
}