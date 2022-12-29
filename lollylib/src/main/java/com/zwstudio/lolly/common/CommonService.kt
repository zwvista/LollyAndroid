package com.zwstudio.lolly.common

import android.content.Context
import android.speech.tts.TextToSpeech
import com.zwstudio.lolly.services.misc.*
import com.zwstudio.lolly.services.wpp.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object GlobalUser {
    var userid = ""
}

lateinit var retrofitJson: Retrofit
lateinit var retrofitSP: Retrofit
lateinit var retrofitHtml: Retrofit
lateinit var vmSettings: SettingsViewModel
lateinit var tts: TextToSpeech

fun onCreateApp(context: Context) {
    val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    retrofitJson = Retrofit.Builder().baseUrl("https://zwvista.com/lolly/api.php/records/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofitSP = Retrofit.Builder().baseUrl("https://zwvista.com/lolly/sp.php/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofitHtml = Retrofit.Builder().baseUrl("https://www.google.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    vmSettings = SettingsViewModel()
    // https://github.com/InsertKoinIO/koin/issues/1076
    startKoin {
        // Koin Android logger
        androidLogger(Level.ERROR)
        //inject Android context
        androidContext(context)
        // use modules
        modules(lollyModule)
    }
}

val lollyModule = module {
    single { AutoCorrectService() }
    single { DictionaryService() }
    single { HtmlService() }
    single { LanguageService() }
    single { TextbookService() }
    single { UserService() }
    single { UserSettingService() }
    single { USMappingService() }
    single { VoiceService() }

    single { LangPhraseService() }
    single { LangWordService() }
    single { PatternService() }
    single { PatternWebPageService() }
    single { UnitPhraseService() }
    single { UnitWordService() }
    single { WebPageService() }
    single { WordFamiService() }
}

fun speak(text: String) =
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
