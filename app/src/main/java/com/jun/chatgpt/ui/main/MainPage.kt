package com.jun.chatgpt.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.blankj.utilcode.util.ScreenUtils
import com.jun.chatgpt.R
import com.jun.chatgpt.model.Message
import com.jun.chatgpt.model.Session
import com.jun.chatgpt.model.enums.Role
import com.jun.chatgpt.model.state.VolumeState
import com.jun.chatgpt.ui.main.common.CommonTipsDialog
import com.jun.chatgpt.ui.theme.longClick
import com.jun.chatgpt.utils.ChatParamsHelper
import com.jun.chatgpt.viewmodel.MainPageViewModel
import com.jun.chatgpt.widget.TextCursorBlinking
import com.jun.template.common.utils.L
import kotlinx.coroutines.delay

/**
 * 主页页面
 *
 * @author Jun
 * @time 2023/3/6
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(viewModel: MainPageViewModel) {
    val messageList by viewModel.messageList.observeAsState(emptyList())
    val sessionList by viewModel.sessionList.observeAsState(emptyList())
    val templateList by viewModel.templateList.observeAsState(emptyList())
    val volumeState by viewModel.volumeState.observeAsState(VolumeState())
    val fontSize by viewModel.fontSize.observeAsState(ChatParamsHelper.fontSize)

    val currentSession by viewModel.currentSession.observeAsState(
        Session(
            title = "", lastSessionTime = System.currentTimeMillis()
        )
    )
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    var isShowRetryDialog by remember { mutableStateOf(false) }

    var isOpenPop by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }



    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topR, listR, bottomR) = createRefs()

            //顶部栏
            SmallTopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    val scrollState = rememberScrollState(0)
                    Box(modifier = Modifier.horizontalScroll(scrollState)) {
                        val title =
                            currentSession.title.ifEmpty { stringResource(id = R.string.app_title) }
                        Text(
                            title, maxLines = 1, style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(16) // 每隔16毫秒滚动一次
                            if (scrollState.value == scrollState.maxValue) {
                                delay(1000)
                                scrollState.scrollTo(0)
                                delay(1000)
                            } else {
                                scrollState.scrollBy(1f)

                            }
                        }
                    }
                }
            }, navigationIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(painter = painterResource(id = R.drawable.ic_more), "More")
                }
            }, actions = {
                IconButton(onClick = {
                    isOpenPop = !isOpenPop
                }) {
                    Icon(Icons.Filled.PlayArrow, "Info")
                }
            }, modifier = Modifier.constrainAs(topR) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            val scrollListState = rememberLazyListState()
            val scHeight = ScreenUtils.getScreenHeight() / 3 * 2
            //对音量键的监听
            if (volumeState.touchUp) {
                LaunchedEffect(key1 = System.currentTimeMillis()) {
                    L.d("volumeUp ${scrollListState.firstVisibleItemScrollOffset.toFloat()}")
                    scrollListState.animateScrollBy((-scHeight).toFloat())
                }
            }
            if (volumeState.touchDown) {
                LaunchedEffect(key1 = System.currentTimeMillis()) {
                    L.d("volumeDown ${scrollListState.firstVisibleItemScrollOffset.toFloat()}")
                    scrollListState.animateScrollBy(scHeight.toFloat())
                }
            }

            //列表 list
            Box(
                modifier = Modifier
                    .constrainAs(listR) {
                        top.linkTo(topR.bottom)
                        bottom.linkTo(bottomR.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .background(Color.White)) {

                LazyColumn(
                    state = scrollListState,
                ) {
                    items(messageList.size) { position ->
                        Spacer(modifier = Modifier.height(10.dp))
                        val message = messageList[position]
                        if (message.role == Role.ASSISTANT.roleName) {
                            LeftView(message, fontSize) {
                                viewModel.alreadyDeleteMessage = message
                                isShowDeleteDialog = true
                            }
                        } else if (message.role == Role.USER.roleName) {
                            RightView(message, fontSize, {
                                viewModel.deleteSubPosition = position
                                isShowDeleteDialog = true
                            }, {
                                viewModel.retryPosition = position
                                isShowRetryDialog = true
                            })
                        } else if (message.role == Role.SYSTEM.roleName) {
                            TipsView(message) {
                                viewModel.deleteMessage(message)
                            }
                        }
                        if (position == messageList.size - 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                }
                //列表自动滑动底部
                if (messageList.isNotEmpty() && viewModel.isBottom) {
                    LaunchedEffect(key1 = messageList) {
                        // 当 list 更新时，自动滚动到底部
                        scrollListState.animateScrollToItem(messageList.size - 1)
                        viewModel.isBottom = false
                    }
                }
            }
            val keyboardController = LocalSoftwareKeyboardController.current

            //底部栏 bottom
            ConstraintLayout(modifier = Modifier.constrainAs(bottomR) {
                bottom.linkTo(parent.bottom, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                width = Dimension.fillToConstraints
                end.linkTo(parent.end)
            }) {
                val (textR, sendR) = createRefs()
                TextField(value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .constrainAs(textR) {
                            start.linkTo(parent.start)
                            end.linkTo(sendR.start)
                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 5.dp, end = 5.dp),
                    maxLines = 3)
                Button(onClick = {
                    keyboardController?.hide()
                    viewModel.sendMessage(text)
                    text = ""
                }, modifier = Modifier
                    .constrainAs(sendR) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = 5.dp)
                    .width(90.dp)) {
                    Text(text = stringResource(id = R.string.btn_send))
                }
            }
        }

        // 侧边栏内容
        AnimatedVisibility(
            visible = isVisible, enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ), exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        ) {
            MainSideBar(sessionList, currentSession, {
                viewModel.startNewSession()
                isVisible = !isVisible
            }, { isVisible = !isVisible }, { session ->
                viewModel.switchSession(session)
            }, {
                viewModel.deleteCurrentSession()
            })
        }


        //popwindow
        if (isOpenPop) {
            MainPop(templateList,
                messageList.size,
                onCloseCallback = { isOpenPop = false },
                onSaveTemplate = {
                    viewModel.saveTemplate(it, messageList)
                },
                onTemplateDelete = {
                    viewModel.deleteTemplate(it.id)
                },
                onTemplateLoad = {
                    viewModel.loadTemplate(it)
                }, onChangeFont = {
                    viewModel.setFontSize(it)
                })
        }

        //deleteView
        if (isShowDeleteDialog) {
            CommonTipsDialog(text = stringResource(id = R.string.delete_tips),
                onCancel = { isShowDeleteDialog = false },
                onFirm = {
                    isShowDeleteDialog = false
                    viewModel.alreadyDeleteMessage?.let { it1 -> viewModel.deleteMessage(it1) }
                    if (viewModel.deleteSubPosition != -1) {
                        viewModel.deleteSubMessage(viewModel.deleteSubPosition)
                    }
                })
        }

        //retryDialog
        if (isShowRetryDialog) {
            CommonTipsDialog(text = stringResource(id = R.string.retry),
                onCancel = { isShowRetryDialog = false },
                onFirm = {
                    if (viewModel.retryPosition != -1) {
                        viewModel.retryMessage(viewModel.retryPosition)
                    }
                    isShowRetryDialog = false
                })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftView(message: Message, fontSize: Int, OnDelete: () -> Unit) {
    val content = message.content
    var isDeleteVisible by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .padding(start = 10.dp, end = 108.dp)
            .fillMaxWidth()
    ) {
        val (head, text, delete) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.ic_gpt),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(head) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop)
        SelectionContainer(
            modifier = Modifier
                .padding(start = 7.dp)
                .constrainAs(text) {
                    start.linkTo(head.end)
                    top.linkTo(head.top)
                }) {
            Card(modifier = Modifier.clickable {
                isDeleteVisible = !isDeleteVisible
            }) {
                if (content.isEmpty()) {
                    TextCursorBlinking(
                        text = content, modifier = Modifier.padding(15.dp)
                    )
                } else {
                    Text(
                        text = content,
                        color = Color.Black,
                        modifier = Modifier.padding(15.dp),
                        fontSize = fontSize.sp
                    )
                }
            }
        }
        IconButton(modifier = Modifier.constrainAs(delete) {
            start.linkTo(text.end)
            top.linkTo(text.top)
        }, onClick = {
            OnDelete.invoke()
        }) {
            if (isDeleteVisible) {
                Icon(Icons.Filled.Clear, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightView(message: Message, fontSize: Int, onDelete: () -> Unit, onRetry: () -> Unit) {
    val content = message.content
    var isOperateVisible by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .padding(end = 10.dp)
            .fillMaxWidth()
            .padding(start = 108.dp)
    ) {

        val (head, text, delete, retry) = createRefs()
        SelectionContainer(modifier = Modifier.constrainAs(text) {
            top.linkTo(head.top)
            end.linkTo(head.start)
        }) {
            Box(modifier = Modifier.clickable {
                isOperateVisible = !isOperateVisible
            }) {
                Card(modifier = Modifier.padding(end = 7.dp)) {
                    Text(
                        text = content, color = Color.Black, modifier = Modifier.padding(15.dp),
                        fontSize = fontSize.sp
                    )
                }
            }
        }
        if (isOperateVisible) {

            IconButton(modifier = Modifier
                .constrainAs(delete) {
                    top.linkTo(text.top)
                    end.linkTo(text.start)
                }
                .width(25.dp), onClick = {
                onDelete.invoke()
            }) {
                Icon(Icons.Filled.Clear, contentDescription = null)
            }
            IconButton(modifier = Modifier
                .constrainAs(retry) {
                    top.linkTo(delete.top)
                    end.linkTo(delete.start)
                }
                .width(25.dp), onClick = {
                isOperateVisible = false
                onRetry.invoke()
            }) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_me),
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .constrainAs(head) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun TipsView(message: Message, onDelete: () -> Unit) {
    val content = message.content
    val tips = stringResource(id = R.string.error_tips)
    Box(
        modifier = Modifier.padding(start = 20.dp, end = 20.dp), contentAlignment = Alignment.Center
    ) {
        SelectionContainer() {
            Text(
                text = "$tips $content",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .longClick {
                        onDelete.invoke()
                    },
                color = Color.Red,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    RightView("55AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA5")
}