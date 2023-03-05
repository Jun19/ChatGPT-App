package com.jun.chatgpt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainViewModel(private val _gptRepository: GptRepository) : ViewModel() {
    private val _localList = MutableLiveData<List<Message>>()
    var localList: LiveData<List<Message>> = _localList

    //    private val _gptResponse=
    init {
        getAllMessage()
    }

    private fun getAllMessage() {
        viewModelScope.launch {
            _gptRepository.getAllMessage().onSuccess {
                _localList.value = it
            }.onFailure { }
        }
    }

    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            _gptRepository.insertMessage(message)
                .onSuccess {
                getAllMessage()
                L.d("insert id $it")
            }.onFailure { }
        }
    }

    fun sendContent(content: String) {
        val message = Message(content = content, role = Role.USER.roleName)
        viewModelScope.launch {
            _gptRepository.fetchMessage(mutableListOf<MessageDTO>().apply {
                //把之前聊天记录给带上
                _localList.value?.forEach {
                    add(it.toDTO())
                }
                //把自己写的放到数据库
                insertMessage(message = message)
                add(message.toDTO())
            }).onSuccess {
                it.choices.forEach {
                    //然后数据更新查询
                    insertMessage(Message(content = it.message.content, role = it.message.role))
                }
            }.onFailure {
                //no thing
            }
        }
    }

    fun clear(){
        viewModelScope.launch {
            _gptRepository.clear().onSuccess {
                getAllMessage()
            }.onFailure {

            }
        }
    }

}