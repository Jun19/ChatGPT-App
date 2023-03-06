package com.jun.chatgpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jun.chatgpt.model.enums.Role
import com.jun.chatgpt.ui.theme.ChatgptTheme
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatgptTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(get())
                }
            }
        }
    }
}

@Composable
fun Greeting(viewModel: MainViewModel) {
    val list by viewModel.localList.observeAsState(emptyList())

    var text by remember { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (view1, view2) = createRefs()
        //列表
        Column(modifier = Modifier
            .constrainAs(view1) {
                top.linkTo(parent.top)
                bottom.linkTo(view2.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
            .background(Color.Black)) {
            Text(
                text = "ChatGPT:Hello! I am ChatGPT,Now you can ask me any questions",
                color = Color.White
            )
            LazyColumn() {
                items(list.size) { position ->
                    val message = list[position]
                    val suffix = if (message.role == Role.ASSISTANT.roleName) "ChatGPT:" else "Me:"
                    Text(
                        text = "$suffix ${message.content}", color = Color.White
                    )
                    leftView(suffix)
                    //TODO 左右头像 加。9图
                }
            }
        }
        //底部栏
        ConstraintLayout(modifier = Modifier.constrainAs(view2) {
            bottom.linkTo(parent.bottom, margin = 0.dp)
            start.linkTo(parent.start, margin = 0.dp)
            width = Dimension.fillToConstraints
            end.linkTo(parent.end)
        }) {
            val (t, send, delete) = createRefs()
            TextField(value = text,
                onValueChange = { text = it },
                modifier = Modifier.constrainAs(t) {
                    start.linkTo(parent.start)
                    end.linkTo(send.start)
                    width = Dimension.fillToConstraints
                })
            Button(onClick = {
                viewModel.sendContent(text)
                text = ""
            }, modifier = Modifier
                .constrainAs(send) {
                    end.linkTo(delete.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .width(100.dp)) {
                Text(text = "Send")
            }
            Button(onClick = {
                viewModel.clear()
            }, modifier = Modifier
                .constrainAs(delete) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .width(70.dp)) {
                Text(text = "Clear")
            }
        }

    }

}

@Composable
fun leftView(content: String) {
    Row(modifier = Modifier.padding(start = 10.dp, end = 50.dp)) {
        Image(
            painter = painterResource(id = R.drawable.gpt_icon),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(text = content, modifier = Modifier.padding(start = 10.dp, top = 12.dp))
    }
}

@Composable
fun rightView(content: String) {
    ConstraintLayout() {
        val (view1, view2) = createRefs()
        Text(text = content, modifier = Modifier
            .constrainAs(view1) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .padding(start = 10.dp, top = 15.dp))
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    rightView("555")
}