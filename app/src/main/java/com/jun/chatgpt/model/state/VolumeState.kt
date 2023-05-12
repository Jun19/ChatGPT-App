package com.jun.chatgpt.model.state

/**
 *
 *
 * @author Jun
 * @time 2023/5/12
 */
data class VolumeState(
    val touchDown: Boolean = false, val touchUp: Boolean = false
)