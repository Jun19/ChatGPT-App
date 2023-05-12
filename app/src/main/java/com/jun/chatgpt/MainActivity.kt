package com.jun.chatgpt

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jun.chatgpt.ui.main.MainPage
import com.jun.chatgpt.ui.theme.ChatgptTheme
import com.jun.chatgpt.viewmodel.MainPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatgptTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MaterialTheme.colorScheme.primary
                    MainPage(viewModel)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                viewModel.setVolumeState(touchDown = true)
                return true
            }

            KeyEvent.KEYCODE_VOLUME_UP -> {
                viewModel.setVolumeState(touchUp = true)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
