package com.jun.chatgpt.ui.main.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jun.chatgpt.R
import com.jun.template.common.GlobalConfig
import com.jun.template.common.extension.toast

/**
 * api编辑dialog
 *
 * @author Jun
 * @time 2023/3/12
 */
@Composable
fun ApiKeyEditDialog(onCancel: () -> Unit, onFirm: (String) -> Unit) {
    var apiKey = ""
    val ctx = LocalContext.current
    AlertDialog(
        onDismissRequest = { onCancel.invoke() },
        title = { Text(text = stringResource(id = R.string.change_key)) },
        text = {
            DialogContent() {
                apiKey = it
            }
        },
        confirmButton = {
            Button(onClick = {
                if (apiKey.isEmpty()) {
                    ctx.toast(R.string.dialog_api_empty_tips)
                    return@Button
                }
                onFirm.invoke(apiKey)
            }) {
                Text(text = stringResource(id = R.string.dialog_confirm))
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
private fun DialogContent(onChangeText: (String) -> Unit) {
    var text by remember { mutableStateOf(GlobalConfig.apiKey) }
    Column() {
        Text(text = stringResource(id = R.string.recommend_content))
        Spacer(modifier = Modifier.padding(10.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
                onChangeText.invoke(text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
        )
    }
}

@Preview
@Composable
fun ApiKeyEditDialogPreview() {
    ApiKeyEditDialog({}, {})
}