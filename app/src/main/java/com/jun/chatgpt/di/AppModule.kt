package com.jun.chatgpt.di

import com.jun.chatgpt.MainViewModel
import com.jun.chatgpt.repository.GptRepository
import com.jun.chatgpt.repository.local.AppDatabase
import com.jun.chatgpt.repository.remote.GptApi
import com.jun.template.common.Constants
import com.jun.template.common.net.NetworkHandler
import com.jun.template.common.utils.L
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 注入
 *
 * @author Jun
 * @time 2022/2/18
 */
val appModule = module {
    single {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message: String -> L.d("http log: $message") }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }
    single {
        Retrofit.Builder().baseUrl(Constants.BASE_API_URL).client(get())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    single { NetworkHandler(get()) }

}
val dataModule = module {
    single { (get() as Retrofit).create(GptApi::class.java) }
    single { AppDatabase.getDBInstance(get()).messageDao() }
    single { GptRepository(get(), get(), get()) }
}
val viewModelModule = module {
    single { MainViewModel(get()) }
}

val allModules = appModule + dataModule + viewModelModule