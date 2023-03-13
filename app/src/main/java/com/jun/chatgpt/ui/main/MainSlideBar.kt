package com.jun.chatgpt.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jun.chatgpt.R
import com.jun.chatgpt.model.Session

/**
 * 侧边栏
 *
 * @author Jun
 * @time 2023/3/7
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSideBar(
    list: List<Session>,
    currentSession: Session,
    onButtonClick: () -> Unit,
    onOutSideClick: () -> Unit,
    onItemClick: (Session) -> Unit,
    onDeleteClick: (Session) -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0f))
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { onOutSideClick.invoke() }
    ) {
        val (topR, bottomR) = createRefs()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
        ) {
            Button(
                onClick = { onButtonClick.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = com.jun.chatgpt.R.string.bar_new_session))
            }
            Text(
                text = stringResource(id = com.jun.chatgpt.R.string.bar_session),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            val scrollState = rememberLazyListState()
            LazyColumn(state = scrollState) {
                items(list.size) { position ->
                    val session = list[position]
                    val isMe = session.id == currentSession.id
                    Card(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            onItemClick.invoke(session)
                        }) {
                        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                            val (textR, cleaR) = createRefs()
                            val title = if (session.title.isNullOrEmpty()) {
                                stringResource(id = R.string.bar_session_item) + (position + 1)
                            } else {
                                session.title
                            }
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
                                            onDeleteClick.invoke(session)
                                        }) {
                                        Icon(Icons.Filled.Clear, contentDescription = null)
                                    }
                                }

                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
        Card(modifier = Modifier.constrainAs(bottomR) {
        }) {
            Text(text = "Url")
        }
    }

}


@Composable
@Preview
fun preview() {
    val currentSession =
        Session(
            id = 1,
            title = "你号asdada吗啊asdasda 啊啊 ",
            lastSessionTime = System.currentTimeMillis()
        )
    MainSideBar(mutableListOf<Session>().apply {
        add(Session(title = "你号吗dadada啊 啊啊 ", lastSessionTime = System.currentTimeMillis()))
        add(Session(title = "你号吗dada啊 啊啊asdadas ", lastSessionTime = System.currentTimeMillis()))
        add(Session(title = "你号吗dada啊 啊啊 ", lastSessionTime = System.currentTimeMillis()))
        add(Session(id = 1, title = "你号dada吗啊 啊啊 ", lastSessionTime = System.currentTimeMillis()))
    }, currentSession, onButtonClick = { }, {}, {}) {

    }
}
