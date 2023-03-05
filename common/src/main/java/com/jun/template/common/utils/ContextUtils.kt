package com.jun.template.common.utils

import android.app.Activity
import android.content.Context

object ContextUtils {

    fun isValidContext(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        if (context is Activity) {
            if (context.isDestroyed || context.isFinishing) {
                return false
            }
        }
        return true
    }

}