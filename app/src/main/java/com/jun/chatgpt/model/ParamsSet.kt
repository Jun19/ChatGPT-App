package com.jun.chatgpt.model

import com.jun.chatgpt.utils.ChatParamsHelper


data class ParamsSet(
    //温度
    var temperature: String = ChatParamsHelper.temperature,
    //当前模型index
    var selectPosition: Int = ChatParamsHelper.selectPosition,
    //跟随文本
    var followContent: String = ChatParamsHelper.followContent,
)