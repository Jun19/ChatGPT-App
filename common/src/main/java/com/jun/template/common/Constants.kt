package com.jun.template.common

/**
 * 常量
 *
 * @author Jun
 * @time 2022/2/18
 */
object Constants {
    const val LOGGER_TAG = "opengpt"
    const val DATABASE_NAME = "opengpt"
    const val PREFERENCE_NAME = "opengpt"
    const val BASE_API_URL = "https://api.openai.com/"

    //BASE64 加密
    const val OPEN_AI_KEY = "c2stVmhJdGhXMk9kM3RlZ0w3VnU4RXhUM0JsYmtGSmt6czJOc2xjMUlvbDQ5QUF4NTNt"

    const val CHAT_MODEL = "gpt-3.5-turbo-0301"

    const val NETWORK_TIME_OUT = 30L


    object Config {
        const val TEMP = "temp"
        const val API_KEY = "api_key"
    }
}