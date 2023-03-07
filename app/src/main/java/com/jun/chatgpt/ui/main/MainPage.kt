package com.jun.chatgpt.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.jun.chatgpt.R
import com.jun.chatgpt.model.enums.Role
import com.jun.chatgpt.viewmodel.MainPageViewModel
import com.jun.chatgpt.widget.TextCursorBlinking

/**
 * 主页页面
 *
 * @author Jun
 * @time 2023/3/6
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(viewModel: MainPageViewModel) {
    val list by viewModel.localList.observeAsState(emptyList())

    var text by remember { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topR, listR, bottomR) = createRefs()
        //头部 head
        Row(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(topR) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "",
                modifier = Modifier.padding(10.dp)
            )
            Text(text = stringResource(id = R.string.app_title), modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = "",
                modifier = Modifier
                    .clickable(interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            viewModel.clear()
                        })
                    .padding(10.dp)
            )
        }

        //列表 list
        Box(modifier = Modifier
            .constrainAs(listR) {
                top.linkTo(topR.bottom)
                bottom.linkTo(bottomR.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
            .background(Color.White)) {
            val scrollState = rememberLazyListState()
            LazyColumn(state = scrollState) {
                items(list.size) { position ->
                    Spacer(modifier = Modifier.height(4.dp))
                    val message = list[position]
                    if (message.role == Role.ASSISTANT.roleName) {
                        LeftView(message.content)
                    } else if (message.role == Role.USER.roleName) {
                        RightView(message.content)
                    } else if (message.role == Role.SYSTEAM.roleName) {
                        TipsView(message.content)
                    }
                    if (position == list.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
            if (list.isNotEmpty()) {
                LaunchedEffect(key1 = list) {
                    // 当 list 更新时，自动滚动到底部
                    scrollState.animateScrollToItem(list.size - 1)
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
                viewModel.sendContent(text)
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

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftView(content: String) {
    Row(modifier = Modifier.padding(start = 10.dp, end = 60.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_gpt),
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        SelectionContainer() {
            Card(modifier = Modifier.padding(start = 7.dp, top = 10.dp)) {
                if (content.isEmpty()) {
                    TextCursorBlinking(
                        text = content,
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = content, color = Color.Black, modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightView(content: String) {
    Row(
        modifier = Modifier
            .padding(end = 10.dp, start = 60.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SelectionContainer() {
            Card(modifier = Modifier.padding(end = 7.dp, top = 10.dp)) {
                Text(
                    text = content, color = Color.Black, modifier = Modifier.padding(15.dp)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_me),
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun TipsView(content: String) {
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
                    .wrapContentWidth(Alignment.CenterHorizontally),
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
    TipsView("555")
}