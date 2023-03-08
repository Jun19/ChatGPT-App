package com.jun.chatgpt.repository.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * dao基类
 *
 * @author Jun
 * @time 2022/1/5
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: MutableList<T>)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T)

}