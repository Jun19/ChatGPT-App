package com.jun.chatgpt.repository

import com.jun.chatgpt.model.*
import com.jun.chatgpt.repository.local.MessageDao
import com.jun.chatgpt.repository.local.SessionDao
import com.jun.chatgpt.repository.local.TemplateDao
import com.jun.chatgpt.repository.remote.GptApi
import com.jun.template.common.Constants
import com.jun.template.common.GlobalConfig
import com.jun.template.common.exception.Failure
import com.jun.template.common.net.NetworkHandler

/**
 * 数据仓库
 *
 * @author Jun
 * @time 2023/3/5
 */
class GptRepository(
    private val _gptApi: GptApi,
    private val _messageDao: MessageDao,
    private val _sessionDao: SessionDao,
    private val _templateDao: TemplateDao,
    private val _netWorkHandler: NetworkHandler
) {


    //**Message**//
    suspend fun queryMessageBySID(sessionID: Int): Result<List<Message>> {
        return handleException {
            _messageDao.selectMessageBySessionID(sessionID)
        }
    }

    suspend fun fetchMessage(messageList: List<MessageDTO>): Result<GptResponse> {
        return handleRemoteException {
            val gptRequest = GptRequest(messageList, Constants.CHAT_MODEL)
            val authKey =
                "Bearer ${GlobalConfig.apiKey}"
            val response = _gptApi.completions(authKey, gptRequest)
            response
        }
    }

    suspend fun insertMessage(message: Message): Result<Long> {
        return handleException {
            _messageDao.insert(message)
        }
    }

    suspend fun insertMessages(messages: List<Message>): Result<Unit> {
        return handleException {
            _messageDao.insert(messages)
        }
    }

    suspend fun deleteMessage(message: Message): Result<Unit> {
        return handleException {
            _messageDao.delete(message)
        }
    }

    suspend fun queryAllMessage(): Result<List<Message>> {
        return handleException {
            _messageDao.queryAll()
        }
    }


    //**Session**//
    suspend fun createSession(session: Session): Result<Long> {
        return handleException {
            _sessionDao.insert(session)
        }
    }

    suspend fun queryAllSession(): Result<List<Session>> {
        return handleException {
            _sessionDao.queryAllSession()
        }
    }

    suspend fun queryLeastSession(): Result<Session?> {
        return handleException {
            _sessionDao.queryLeastSession()
        }
    }

    suspend fun updateSessionTime(id: Int, lastSessionTime: Long): Result<Unit> {
        return handleException {
            _sessionDao.updateSessionTime(id, lastSessionTime)
        }
    }

    suspend fun updateSessionTitle(id: Int, title: String): Result<Unit> {
        return handleException {
            _sessionDao.updateSessionTitle(id, title)
        }
    }


    suspend fun clear(session: Session): Result<Unit> {
        return handleException {
            _messageDao.deleteAll(session.id)
            _sessionDao.delete(session)
        }
    }

    suspend fun saveTemplate(name: String, content: String): Result<Long> {
        return handleException {
            _templateDao.insert(Template(tempContent = content, name = name))
        }
    }

    suspend fun updateTemplateName(name: String, id: Int): Result<Unit> {
        return handleException {
            _templateDao.updateTemplateName(name, id)
        }
    }

    suspend fun deleteTemplate(id: Int): Result<Unit> {
        return handleException {
            _templateDao.delete(Template(id = id))
        }
    }

    suspend fun queryAllTemplate(): Result<List<Template>> {
        return handleException {
            _templateDao.queryAll()
        }
    }

    private suspend inline fun <T> handleRemoteException(onCall: () -> T): Result<T> {
        return if (_netWorkHandler.isNetworkAvailable()) {
            try {
                Result.success(onCall.invoke())
            } catch (e: Throwable) {
                e.printStackTrace()
                Result.failure(Failure.OtherError(e))
            }
        } else {
            Result.failure(Failure.NetworkError)
        }
    }

    private suspend inline fun <T> handleException(onCall: () -> T): Result<T> {
        return try {
            Result.success(onCall.invoke())
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(Failure.OtherError(e))
        }

    }
}