package com.jun.template.common.utils.persistence

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.util.*

class MMKVPersistence : BasePersistence {
    val mmkv: MMKV? by lazy { MMKV.defaultMMKV() }

    fun put(key: String, value: Any?): Boolean {
        return when (value) {
            is String -> mmkv?.encode(key, value)!!
            is Float -> mmkv?.encode(key, value)!!
            is Boolean -> mmkv?.encode(key, value)!!
            is Int -> mmkv?.encode(key, value)!!
            is Long -> mmkv?.encode(key, value)!!
            is Double -> mmkv?.encode(key, value)!!
            is ByteArray -> mmkv?.encode(key, value)!!
            else -> false
        }
    }

    /**
     * 查找数据 返回给调用方法一个具体的对象
     * 如果查找不到类型就采用反序列化方法来返回类型
     * default是默认对象 以防止会返回空对象的异常
     * 即如果name没有查找到value 就返回默认的序列化对象，然后经过反序列化返回
     */
    override fun <A> findPreference(name: String, default: A): A = with(mmkv!!) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> getString(name, serialize(default))?.let(this@MMKVPersistence::deSerialization)
        }!!
        res as A
    }

    override fun <A> putPreference(name: String, value: A) = with(mmkv!!) {
        var a = when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.commit()
    }


    override fun clearPreference() {
        mmkv?.edit()?.clear()?.commit()

    }

    override fun clearPreference(key: String) {
        mmkv?.edit()?.remove(key)?.commit()
    }

    /**
     * 这里使用安卓自带的Parcelable序列化，它比java支持的Serializer序列化性能好些
     */
    fun <T : Parcelable> put(key: String, t: T?): Boolean {
        if (t == null) {
            return false
        }
        return mmkv?.encode(key, t)!!
    }

    fun put(key: String, sets: Set<String>?): Boolean {
        if (sets == null) {
            return false
        }
        return mmkv?.encode(key, sets)!!
    }


    fun getInt(key: String): Int? {
        return mmkv?.decodeInt(key, 0)
    }

    fun getDouble(key: String): Double? {
        return mmkv?.decodeDouble(key, 0.00)
    }

    fun getLong(key: String): Long? {
        return mmkv?.decodeLong(key, 0L)
    }

    fun getBoolean(key: String): Boolean? {
        return mmkv?.decodeBool(key, false)
    }

    fun getFloat(key: String): Float? {
        return mmkv?.decodeFloat(key, 0F)
    }

    fun getByteArray(key: String): ByteArray? {
        return mmkv?.decodeBytes(key)
    }

    fun getString(key: String): String? {
        return mmkv?.decodeString(key, "")
    }

    inline fun <reified T : Parcelable> getParcelable(key: String): T? {
        return mmkv?.decodeParcelable(key, T::class.java)
    }

    fun getStringSet(key: String): Set<String>? {
        return mmkv?.decodeStringSet(key, Collections.emptySet())
    }

    fun removeKey(key: String) {
        mmkv?.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv?.clearAll()
    }

}