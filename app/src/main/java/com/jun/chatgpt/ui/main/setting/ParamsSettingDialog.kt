package com.jun.chatgpt.ui.main.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jun.chatgpt.R
import com.jun.chatgpt.model.ParamsSet
import com.jun.chatgpt.utils.ChatParamsHelper
import com.jun.template.common.extension.toast

/**
 * 参数设置dialog
 *
 * @author Jun
 * @time 2023/3/12
 */
@Composable
fun ParamsSettingDialog(
    paramsSet: ParamsSet,
    onCancel: () -> Unit,
    //确认返回的温度和模型index
    onFirm: (ParamsSet) -> Unit
) {
    val ctx = LocalContext.current
    var newParams = paramsSet
    AlertDialog(
        onDismissRequest = { onCancel.invoke() },
        title = { Text(text = stringResource(id = R.string.set_params)) },
        text = {
            DialogContent(paramsSet) { newParams = it }
        },
        confirmButton = {
            Button(onClick = {
                if (newParams.temperature.isEmpty()) {
                    ctx.toast(R.string.dialog_api_empty_tips)
                    return@Button
                }
                onFirm.invoke(newParams)
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = {
                onCancel.invoke()
            }) {
                Text(text = stringResource(id = R.string.dialog_cancel))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogContent(
    paramsSet: ParamsSet,
    onChange: (ParamsSet) -> Unit,
) {
    var myPosition by remember { mutableStateOf(paramsSet.selectPosition) }
    var myTemplate by remember { mutableStateOf(paramsSet.temperature) }
    var myFollowContent by remember { mutableStateOf(paramsSet.followContent) }
    var myFontSize by remember { mutableStateOf(paramsSet.fontSize) }

    Column() {
        Text(
            text = stringResource(id = R.string.font_size),
            fontSize = 20.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = myFontSize.toString(),
            onValueChange = {
                kotlin.runCatching {
                    it.toInt()
                }.onSuccess {
                    myFontSize = it
                    paramsSet.fontSize = myFontSize
                    onChange.invoke(paramsSet)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.follow_content),
            fontSize = 20.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(
            value = myFollowContent,
            onValueChange = {
                paramsSet.followContent = myFollowContent
                onChange.invoke(paramsSet)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(text = stringResource(id = R.string.temp_title), fontSize = 20.sp, color = Color.Black)
        Spacer(modifier = Modifier.padding(5.dp))
        Text(text = stringResource(id = R.string.temp_content), fontSize = 10.sp)
        TextField(
            value = myTemplate,
            onValueChange = {
                paramsSet.temperature = myTemplate
                onChange.invoke(paramsSet)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.model_title),
            fontSize = 20.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(5.dp))

        LazyColumn(modifier = Modifier.heightIn(0.dp, 200.dp)) {
            items(ChatParamsHelper.chatModes.size) { position ->
                val isMe = position == myPosition
                Card(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        myPosition = position
                        paramsSet.selectPosition = myPosition
                        onChange.invoke(paramsSet)
                    }) {

                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (textR, cleaR) = createRefs()
                        Text(
                            text = ChatParamsHelper.chatModes[position],
                            maxLines = 1,
                            modifier = Modifier
                                .constrainAs(textR) {
                                    start.linkTo(parent.start)
                                    end.linkTo(cleaR.start)
                                    top.linkTo(parent.top)
                                }
                                .padding(
                                    start = 30.dp,
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    end = 10.dp
                                )
                                .fillMaxWidth(),
                            color = if (isMe) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Black
                            },
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Column(modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .constrainAs(cleaR) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }) {
                            AnimatedVisibility(visible = isMe) {
                                IconButton(
                                    onClick = {
                                    }) {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                }
                            }

                        }

                    }


                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun ParamsSettingDialogPreview() {
    ParamsSettingDialog(ParamsSet("", 0, ""), {}, {})
}