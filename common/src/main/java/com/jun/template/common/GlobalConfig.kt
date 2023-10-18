package com.jun.template.common

import android.annotation.SuppressLint
import com.jun.template.common.utils.persistence.Preference

/**
 * 全局配置
 *
 * @author Jun
 * @time 2022/2/18
 */
@SuppressLint("StaticFieldLeak")
object GlobalConfig {
    var temp: String by Preference(Constants.Config.TEMP, "")
    var apiKey: String by Preference(Constants.Config.API_KEY, "")
    var baseUrl: String by Preference(Constants.Config.BASE_URL, Constants.BASE_API_URL)

}