package com.jun.template.common.utils.persistence

import com.blankj.utilcode.util.SPUtils

/**
 *
 *
 * @author Jun
 * @time 2022/11/10
 */
class SpPersistence : BasePersistence {
    override fun <A> findPreference(name: String, default: A): A = with(SPUtils.getInstance()) {
        val res = when (default) {
            is Long -> this?.getLong(name, default)
            is String -> this?.getString(name, default)
            is Int -> this?.getInt(name, default)
            is Boolean -> this?.getBoolean(name, default)
            is Float -> this?.getFloat(name, default)
            else -> this?.getString(name, serialize(default))?.let { deSerialization(it) }
        } ?: ""
        res as A
    }

    override fun <A> putPreference(name: String, value: A) = with(SPUtils.getInstance()) {
        var a = when (value) {
            is Long -> this?.put(name, value)
            is String -> this?.put(name, value)
            is Int -> this?.put(name, value)
            is Boolean -> this?.put(name, value)
            is Float -> this?.put(name, value)
            else -> this?.put(name, serialize(value))
        }
    }

    override fun clearPreference() {
        SPUtils.getInstance().clear()
    }

    override fun clearPreference(key: String) {
        SPUtils.getInstance().remove(key)
    }
}