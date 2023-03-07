package com.jun.chatgpt.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 侧边栏
 *
 * @author Jun
 * @time 2023/3/7
 */
@Composable
fun MainSideBar(
    onButtonClick: () -> Unit,
    onOutSideClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0f))
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { onOutSideClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
                .background(Color.White)
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Menu",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = { onButtonClick.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Confirm")

            }
        }
    }

}
