package com.jun.chatgpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jun.chatgpt.model.GptResponse
import com.jun.chatgpt.model.Message
import com.jun.chatgpt.model.MessageDTO
import com.jun.chatgpt.model.Session
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

    //当前会话
    private val _currentSession = MutableLiveData<Session>()
    val currentSession: LiveData<Session> = _currentSession

    //会话列表
    private val _sessionList = MutableLiveData<List<Session>>()
    val sessionList: LiveData<List<Session>> = _sessionList

    //消息列表
    private val _messageList = MutableLiveData<List<Message>>()
    var messageList: LiveData<List<Message>> = _messageList

    init {
        //获取上一次的session会话
        queryLeastSession()
    }

    private fun queryLeastSession() {
        viewModelScope.launch {
            //查询最新的会话
            _gptRepository.queryLeastSession().onSuccess {
                setSessionDetail(it)
            }
        }
    }

    //开启一个新的会话
    fun startNewSession() {
        setSessionDetail(null)
    }

    fun switchSession(session: Session?) {
        setSessionDetail(session)
    }


    //查询所有的会话
    private fun queryAllSession() {
        viewModelScope.launch {
            //查询最新的会话
            _gptRepository.queryAllSession().onSuccess {
                _sessionList.value = it
            }
        }
    }

    private fun setSessionDetail(session: Session?) {
        viewModelScope.launch {
            if (session == null) {
                //说明还没会话记录 新建一个
                val newSession = Session(0, "", System.currentTimeMillis())
                _gptRepository.createSession(newSession).onSuccess {
                    //把生成的id覆盖实体上
                    _currentSession.value = newSession.copy(id = it.toInt())
                    _messageList.value = listOf()
                }
            } else {
                //如果有的话就直接用
                _currentSession.value = session!!
                _gptRepository.queryMessageBySID(session.id).onSuccess { messages ->
                    _messageList.value = messages
                }
            }
            queryAllSession()
        }
    }

    private fun getAllMessage() {
        viewModelScope.launch {
            _gptRepository.getAllMessage().onSuccess {
                _messageList.value = it
                L.d(it.toString())
            }.onFailure { }
        }
    }

    private fun updateList(onUpdate: (MutableList<Message>) -> Unit) {
        _messageList.value = _messageList.value?.toMutableList()?.apply {
            onUpdate.invoke(this)
        }
    }

    private fun insertMessage(message: Message) {
        viewModelScope.launch {
            //同时更新会话记录
            val currentSession = _currentSession.value!!
            if (message.role == Role.USER.roleName) {
                updateSessionTitle(currentSession.id, message.content)
            }
            //更新会话的最后时间
            updateSessionTime(currentSession.id)
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
                        if (it.size > 1) {
                            //删除最后一个
                            it.removeAt(it.size - 1)
                        }
                    }
                    updateList {
                        it.add(message)
                    }
                }
                L.d("insert message $message")
            }.onFailure { }
        }
    }

    //更新会话操作时间
    private suspend fun updateSessionTime(sessionId: Int) {
        viewModelScope.launch {
            val time = System.currentTimeMillis()
            _gptRepository.updateSessionTime(id = sessionId, time).onSuccess {
                _currentSession.value = _currentSession.value!!.copy(lastSessionTime = time)
            }
        }
    }

    //更新会话标题
    private suspend fun updateSessionTitle(sessionId: Int, title: String) {
        viewModelScope.launch {
            _gptRepository.updateSessionTitle(id = sessionId, title).onSuccess {
                _currentSession.value = _currentSession.value!!.copy(title = title)
                queryAllSession()
            }
        }
    }

    //发送消息
    fun sendContent(content: String) {
        val message = Message(
            sessionId = _currentSession.value!!.id,
            content = content,
            role = Role.USER.roleName
        )
        viewModelScope.launch {
            _gptRepository.fetchMessage(mutableListOf<MessageDTO>().apply {
                //把之前聊天记录给带上 并且不带上系统提示
                _messageList.value?.filter { it.role != Role.SYSTEAM.roleName }?.forEach {
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
            _gptRepository.clear(_currentSession.value!!).onSuccess {
                queryLeastSession()
            }.onFailure {

            }
        }
    }

}