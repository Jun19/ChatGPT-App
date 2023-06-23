package com.jun.chatgpt.net

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


/**
 * 对gpt服务错误代码做处理 以可以让实体获取到报错的信息
 *
 * @author Jun
 * @time 2023/3/7
 */
class GptErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response = chain.proceed(request)
        if (response.code == 401 || response.code == 429 || response.code == 500 || response.code == 404) {
            val json = response.body!!.string()
            response = Response.Builder().code(200).message("OK").request(request)
                .protocol(response.protocol)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull())).build()
        }
        return response
    }
}