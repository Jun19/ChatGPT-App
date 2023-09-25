package com.jun.chatgpt.utils

import com.jun.template.common.utils.persistence.Preference

/**
 * 模型
 *
 * @author Jun
 * @time 2023/6/24
 */
object ChatParamsHelper {
    var selectPosition: Int by Preference("model_position", 0)
    var temperature: String by Preference("model_params_temperature", "1")

    val chatModes = listOf(
        "gpt-3.5-turbo-16k-0613",
        "gpt-3.5-turbo-0301",
//        "gpt-3.5-turbo-0613",
//        "gpt-3.5-turbo-16k",
//        "gpt-3.5-turbo",
//        "gpt-4",
//        "gpt-4-0613",
//        "gpt-4-32k",
//        "gpt-4-32k-0613",
    )

}