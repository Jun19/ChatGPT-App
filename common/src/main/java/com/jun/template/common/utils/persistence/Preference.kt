package com.jun.template.common.utils.persistence

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(val name: String, val default: T) : ReadWriteProperty<Any?, T> {

    companion object {
        private lateinit var context: Context
        private lateinit var persistence: BasePersistence

        private var fileName = "default"

        fun init(context: Context, fileName: String) {
            persistence = PersistenceUtils.initialize(context)
            Companion.context = context.applicationContext
            Companion.fileName = fileName
        }
    }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default!!)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    /**
     * 查找数据 返回给调用方法一个具体的对象
     * 如果查找不到类型就采用反序列化方法来返回类型
     * default是默认对象 以防止会返回空对象的异常
     * 即如果name没有查找到value 就返回默认的序列化对象，然后经过反序列化返回
     */
    private fun <A> findPreference(name: String, default: A): A {
        return persistence.findPreference(name, default)
    }

    private fun <A> putPreference(name: String, value: A) {
        return persistence.putPreference(name, value)
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        persistence.clearPreference()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        persistence.clearPreference(key)
    }

}
