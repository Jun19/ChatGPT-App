package com.jun.chatgpt.repository.local

import androidx.room.Dao
import com.jun.chatgpt.model.Template

@Dao
interface TemplateDao : BaseDao<Template> {
}