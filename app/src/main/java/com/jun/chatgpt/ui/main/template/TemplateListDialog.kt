package com.jun.chatgpt.ui.main.template

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jun.chatgpt.R
import com.jun.chatgpt.model.Template

/**
 *
 *
 * @author Jun
 * @time 2023/3/13
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateListDialog(
    templateList: List<Template>,
    onCancel: () -> Unit,
    onLoad: (Template) -> Unit,
    onDelete: (Template) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancel.invoke() },
        title = { Text(text = stringResource(id = R.string.more_template_list)) },
        text = {
            val scrollState = rememberLazyListState()
            LazyColumn(state = scrollState) {
                items(templateList.size) {
                    Spacer(modifier = Modifier.height(10.dp))
                    val template = templateList[it]
                    Card(modifier = Modifier.clickable {
                        onLoad.invoke(template)
                    }) {
                        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                            val (textR, cleaR) = createRefs()
                            val title = template.name
                            Text(
                                text = title,
                                maxLines = 1,
                                modifier = Modifier
                                    .constrainAs(textR) {
                                        start.linkTo(parent.start)
                                        end.linkTo(cleaR.start)
                                        top.linkTo(parent.top)
                                    }
                                    .padding(
                                        start = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp,
                                        end = 10.dp
                                    )
                                    .fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            IconButton(
                                onClick = {
                                    onDelete.invoke(template)
                                },
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .constrainAs(cleaR) {
                                        end.linkTo(parent.end)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    }
                            ) {
                                Icon(Icons.Filled.Clear, contentDescription = null)
                            }
                        }

                    }

                }
            }

        },
        confirmButton = {

        },
        dismissButton = {

        },
    )
}
