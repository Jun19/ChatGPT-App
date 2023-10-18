package com.jun.chatgpt.model

import com.jun.template.common.GlobalConfig


data class RequestSet(
    //api key
    var apiKey: String = GlobalConfig.apiKey,
    //domain
    var baseUrl: String = GlobalConfig.baseUrl,
)