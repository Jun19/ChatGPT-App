package com.jun.chatgpt.di

import com.jun.chatgpt.net.MyHttpClient
import com.jun.chatgpt.repository.GptRepository
import com.jun.chatgpt.repository.local.AppDatabase
import com.jun.chatgpt.repository.remote.GptApi
import com.jun.chatgpt.viewmodel.MainPageViewModel
import com.jun.template.common.Constants
import com.jun.template.common.net.NetworkHandler
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 注入
 *
 * @author Jun
 * @time 2023/3/5
 */
val appModule = module {
    single {
        MyHttpClient()
    }
    single {
        Retrofit.Builder().baseUrl(Constants.BASE_API_URL)
            .client((get() as MyHttpClient).getClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    single { NetworkHandler(get()) }

}
val dataModule = module {
    single { (get() as Retrofit).create(GptApi::class.java) }
    single { AppDatabase.getDBInstance(get()).getMessageDao() }
    single { AppDatabase.getDBInstance(get()).getSessionDao() }
    single { AppDatabase.getDBInstance(get()).getTemplateDao() }
    single { GptRepository(get(), get(), get(), get(), get()) }
}
val viewModelModule = module {
    single { MainPageViewModel(get()) }
}

val allModules = appModule + dataModule + viewModelModule