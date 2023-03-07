package com.jun.chatgpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jun.chatgpt.model.GptResponse
import com.jun.chatgpt.model.Message
import com.jun.chatgpt.model.MessageDTO
import com.jun.chatgpt.model.enums.Role
import com.jun.chatgpt.repository.GptRepository
import com.jun.template.common.utils.L
import kotlinx.coroutines.launch

/**
 * Main ViewModel
 *
 * @author Jun
 * @time 2022/2/18
 */
class MainPageViewModel(private val _gptRepository: GptRepository) : ViewModel() {
    private val _localList = MutableLiveData<List<Message>>()
    var localList: LiveData<List<Message>> = _localList

    init {
        getAllMessage()
    }

    private fun getAllMessage() {
        viewModelScope.launch {
            _gptRepository.getAllMessage().onSuccess {
                _localList.value = it
                L.d(it.toString())
            }.onFailure { }
        }
    }

    private fun updateList(onUpdate: (MutableList<Message>) -> Unit) {
        _localList.value = _localList.value?.toMutableList()?.apply {
            onUpdate.invoke(this)
        }
    }

    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            _gptRepository.insertMessage(message).onSuccess {
                if (message.role == Role.USER.roleName) {
                    updateList {
                        it.add(message)
                    }
                    //添加一个空的响应
                    updateList {
                        it.add(Message(content = "", role = Role.ASSISTANT.roleName))
                    }
                } else {
                    updateList {
                        //删除最后一个
                        it.removeAt(it.size - 1)
                    }
                    updateList {
                        it.add(message)
                    }
                }
                L.d("insert message $message")
            }.onFailure { }
        }
    }

    //发送消息
    fun sendContent(content: String) {
        val message = Message(content = content, role = Role.USER.roleName)
        viewModelScope.launch {
            _gptRepository.fetchMessage(mutableListOf<MessageDTO>().apply {

                //把之前聊天记录给带上 并且不带上系统提示
                _localList.value?.filter { it.role != Role.SYSTEAM.roleName }?.forEach {
                    add(it.toDTO())
                }
                //把自己写的放到数据库
                insertMessage(message = message)
                add(message.toDTO())
            }).onSuccess { it ->
                dataProcess(it)
            }.onFailure {
                //错误提示
                insertMessage(Message(content = it.toString(), role = Role.SYSTEAM.roleName))
            }
        }
    }

    private fun dataProcess(gtpResponse: GptResponse) {
        //错误
        if (gtpResponse.error != null) {
            insertMessage(
                Message(
                    content = gtpResponse.error.message, role = Role.SYSTEAM.roleName
                )
            )
        } else {
            gtpResponse.choices.forEach {
                //拿到数据后 插入到数据库
                insertMessage(
                    Message(
                        content = filterDrayMessage(it.message.content), role = it.message.role
                    )
                )
            }
        }

    }

    //过滤开头为\n的数据
    private fun filterDrayMessage(message: String): String {
        var newMessage = message
        val nextLineSymbol = "\n"
        while (newMessage.startsWith(nextLineSymbol)) {
            val index = newMessage.indexOf("\n")
            newMessage = newMessage.substring(index + nextLineSymbol.length, newMessage.length)
        }
        return newMessage
    }

    fun clear() {
        viewModelScope.launch {
            _gptRepository.clear().onSuccess {
                getAllMessage()
            }.onFailure {

            }
        }
    }

}