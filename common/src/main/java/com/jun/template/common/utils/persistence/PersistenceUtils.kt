package com.jun.template.common.utils.persistence

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 *
 *
 * @author Jun
 * @time 2022/11/10
 */
object PersistenceUtils {

    fun initialize(context: Context): BasePersistence {
        //首先使用 mmkv 失败则用sp
        return try {
            MMKV.initialize(context, context.applicationInfo.dataDir + "/mmkv/")
            MMKVPersistence()
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
            SpPersistence()
        } catch (e: Exception) {
            e.printStackTrace()
            SpPersistence()
        }
    }


}