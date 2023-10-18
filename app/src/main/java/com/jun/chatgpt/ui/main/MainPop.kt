package com.jun.chatgpt.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.jun.chatgpt.R
import com.jun.chatgpt.model.ParamsSet
import com.jun.chatgpt.model.Template
import com.jun.chatgpt.ui.main.api.ApiKeyEditDialog
import com.jun.chatgpt.ui.main.setting.ParamsSettingDialog
import com.jun.chatgpt.ui.main.template.TemplateAddDialog
import com.jun.chatgpt.ui.main.template.TemplateListDialog
import com.jun.chatgpt.utils.ChatParamsHelper
import com.jun.template.common.GlobalConfig
import com.jun.template.common.extension.toast

/**
 * 主页的popwindow
 *
 * @author Jun
 * @time 2023/3/12
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPop(
    templateList: List<Template>,
    count: Int,
    onCloseCallback: () -> Unit,
    onSaveTemplate: (String) -> Unit,
    onTemplateLoad: (Template) -> Unit,
    onTemplateDelete: (Template) -> Unit,
    onChangeFont: (Int) -> Unit,
) {
    var isShowAdd by remember { mutableStateOf(false) }
    var isShowList by remember { mutableStateOf(false) }
    var isShowEditKey by remember { mutableStateOf(false) }
    var isShowParamsSetting by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    Popup(
        offset = IntOffset(0, 150),
        alignment = Alignment.TopEnd,
        onDismissRequest = { onCloseCallback.invoke() },
        content = {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(modifier = Modifier.width(200.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (count > 0) {
                                    isShowAdd = true
                                } else {
                                    ctx.toast(R.string.dialog_template_add_tips)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = null,
                            modifier = buttonModifySettings()
                        )
                        Text(text = stringResource(id = R.string.more_templates_create))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isShowList = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Filled.Create,
                            contentDescription = null,
                            modifier = buttonModifySettings()
                        )
                        Text(text = stringResource(id = R.string.more_template_list))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isShowParamsSetting = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = null,
                            modifier = buttonModifySettings()
                        )

                        Text(text = stringResource(id = R.string.set_params))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isShowEditKey = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = buttonModifySettings()
                        )
                        Text(text = stringResource(id = R.string.change_key))
                    }
                }
            }

        }
    )

    if (isShowAdd) {
        TemplateAddDialog(onCancel = { isShowAdd = false }, onFirm = {
            onSaveTemplate.invoke(it)
            isShowAdd = false
            onCloseCallback.invoke()
            ctx.toast(R.string.dialog_save_success_tips)
        })
    }
    if (isShowList) {
        TemplateListDialog(templateList, onCancel = {
            isShowList = false
        }, onLoad = {
            isShowList = false
            onTemplateLoad.invoke(it)
        }, onDelete = { onTemplateDelete.invoke(it) })
    }
    if (isShowEditKey) {
        ApiKeyEditDialog(onCancel = {
            isShowEditKey = false
        }, onFirm = {
            GlobalConfig.apiKey = it.apiKey
            GlobalConfig.baseUrl = it.baseUrl
            ctx.toast(R.string.dialog_key_change_tips)
            isShowEditKey = false
        })
    }
    if (isShowParamsSetting) {
        ParamsSettingDialog(
            ParamsSet(),
            onCancel = {
                isShowParamsSetting = false
            },
            onFirm = { paramsSet ->
                ChatParamsHelper.temperature = paramsSet.temperature
                ChatParamsHelper.selectPosition = paramsSet.selectPosition
                ChatParamsHelper.followContent = paramsSet.followContent
                ChatParamsHelper.fontSize = paramsSet.fontSize
                ChatParamsHelper.isFollow = paramsSet.isFollow
                ChatParamsHelper.limitSize = paramsSet.limitSize
                onChangeFont.invoke(ChatParamsHelper.fontSize)
                ctx.toast(R.string.dialog_key_change_tips)
                isShowParamsSetting = false
            })
    }
}

private fun buttonModifySettings(): Modifier {
    return Modifier
        .size(45.0.dp)
        .padding(10.0.dp)
}

@Preview
@Composable
fun PopPreview() {
    MainPop(mutableListOf(), 1, {}, {}, {}, {}, {})
}
