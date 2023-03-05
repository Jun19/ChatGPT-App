package com.jun.template.common.extension

import androidx.annotation.NonNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T : Any> toFlowAsync(@NonNull block: suspend () -> T): Flow<T> {
    return flow {
        emit(block())
    }.flowOn(Dispatchers.IO)
}

fun <T : Any> toFlowSync(@NonNull block: () -> T): Flow<T> {
    return flow {
        emit(block.invoke())
    }.flowOn(Dispatchers.Main)
}

