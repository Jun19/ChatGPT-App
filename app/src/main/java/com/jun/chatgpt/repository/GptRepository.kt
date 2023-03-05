package com.jun.chatgpt.repository

import com.jun.chatgpt.model.GptRequest
import com.jun.chatgpt.model.GptResponse
import com.jun.chatgpt.model.Message
import com.jun.chatgpt.model.MessageDTO
import com.jun.chatgpt.repository.local.MessageDao
import com.jun.chatgpt.repository.remote.GptApi
import com.jun.template.common.Constants
import com.jun.template.common.exception.Failure
import com.jun.template.common.net.NetworkHandler

/**
 *
 *
 * @author Jun
 * @time 2023/3/5
 */
class GptRepository(
    private val _gptApi: GptApi,
    private val _gptDao: MessageDao,
    private val _netWorkHandler: NetworkHandler
) {

    suspend fun fetchMessage(messageList: List<MessageDTO>): Result<GptResponse> {
        return handleRemoteException {
            val gptRequest = GptRequest(messageList, Constants.CHAT_MODEL)
            val authKey = "Bearer ${Constants.OPEN_AI_KEY}"
            val response = _gptApi.completions(authKey, gptRequest)
            response
        }
    }

    suspend fun insertMessage(message: Message): Result<Long> {
        return handleException {
            _gptDao.insert(message)
        }
    }

    suspend fun clear(): Result<Unit> {
        return handleException {
            _gptDao.deleteAll()
        }
    }

    suspend fun getAllMessage(): Result<List<Message>> {
        return handleException {
            _gptDao.fetchAll()
        }
    }

    private suspend inline fun <T> handleRemoteException(onCall: () -> T): Result<T> {
        return if (_netWorkHandler.isNetworkAvailable()) {
            try {
                Result.success(onCall.invoke())
            } catch (e: Throwable) {
                //HttpException等错误会走这里
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
            //HttpException等错误会走这里
            Result.failure(Failure.OtherError(e))
        }

    }
}