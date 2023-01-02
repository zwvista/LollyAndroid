package com.zwstudio.lolly.common

import android.content.Context
import android.speech.tts.TextToSpeech
import com.zwstudio.lolly.services.misc.*
import com.zwstudio.lolly.services.wpp.*
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.viewmodels.phrases.*
import com.zwstudio.lolly.viewmodels.words.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun <T : Any> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.applyIO(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.applyIO(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

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
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofitSP = Retrofit.Builder().baseUrl("https://zwvista.com/lolly/sp.php/")
        .client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    retrofitHtml = Retrofit.Builder().baseUrl("https://www.google.com")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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

    viewModel { LoginViewModel() }
    viewModel { parameters -> ReviewOptionsViewModel(options = parameters.get()) }
    viewModel { SearchViewModel() }
    viewModel { parameters -> PatternsDetailViewModel(item = parameters.get()) }
    viewModel { PatternsWebPagesViewModel() }
    viewModel { parameters -> PatternsWebPageDetailViewModel(item = parameters.get()) }
    viewModel { PhrasesLangViewModel() }
    viewModel { parameters -> PhrasesLangDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> PhrasesReviewViewModel(doTestAction = parameters.get()) }
    viewModel { PhrasesUnitViewModel() }
    viewModel { parameters -> PhrasesUnitDetailViewModel(item = parameters.get()) }
    viewModel { PhrasesUnitBatchEditViewModel() }
    viewModel { WordsDictViewModel() }
    viewModel { WordsLangViewModel() }
    viewModel { parameters -> WordsLangDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> WordsReviewViewModel(doTestAction = parameters.get()) }
    viewModel { WordsUnitViewModel() }
    viewModel { parameters -> WordsUnitDetailViewModel(item = parameters.get()) }
    viewModel { WordsUnitBatchEditViewModel() }
}

fun speak(text: String) =
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
