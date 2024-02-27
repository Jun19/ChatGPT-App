package com.jun.chatgpt.model

import com.jun.chatgpt.utils.ChatParamsHelper


data class ParamsSet(
    //温度
    var temperature: String = ChatParamsHelper.temperature,
    //当前模型index
    var selectPosition: Int = ChatParamsHelper.selectPosition,
    //是否跟随
    var isFollow: Boolean = ChatParamsHelper.isFollow,
    //跟随文本
    var followContent: String = ChatParamsHelper.followContent,
    //字体大小
    var fontSize: Int = ChatParamsHelper.fontSize,
    //上下文条数限制
    var limitSize: Long = ChatParamsHelper.limitSize,
    var isFirst0301: Boolean = ChatParamsHelper.isFirst0301,
)