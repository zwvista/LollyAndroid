package com.zwstudio.lolly.common

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import com.zwstudio.lolly.services.misc.AutoCorrectService
import com.zwstudio.lolly.services.misc.DictionaryService
import com.zwstudio.lolly.services.misc.HtmlService
import com.zwstudio.lolly.services.misc.LanguageService
import com.zwstudio.lolly.services.misc.OnlineTextbookService
import com.zwstudio.lolly.services.misc.TextbookService
import com.zwstudio.lolly.services.misc.USMappingService
import com.zwstudio.lolly.services.misc.UserService
import com.zwstudio.lolly.services.misc.UserSettingService
import com.zwstudio.lolly.services.misc.VoiceService
import com.zwstudio.lolly.services.wpp.LangPhraseService
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.services.wpp.PatternService
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.services.wpp.WordFamiService
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksDetailViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesReviewViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitBatchEditViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitBatchEditViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
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
import java.net.URLEncoder

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
    tts = TextToSpeech(context, object : TextToSpeech.OnInitListener {
        override fun onInit(status: Int) {
            if (status != TextToSpeech.SUCCESS) return
        }
    })
}

fun onDestroyApp() {
    tts.shutdown()
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
    single { OnlineTextbookService() }

    single { LangPhraseService() }
    single { LangWordService() }
    single { PatternService() }
    single { UnitPhraseService() }
    single { UnitWordService() }
    single { WordFamiService() }

    viewModel { LoginViewModel() }
    viewModel { parameters -> ReviewOptionsViewModel(options = parameters.get()) }
    viewModel { SearchViewModel() }
    viewModel { parameters -> PatternsDetailViewModel(item = parameters.get()) }
    viewModel { PatternsViewModel() }
    viewModel { PhrasesLangViewModel() }
    viewModel { parameters -> PhrasesLangDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> PhrasesReviewViewModel(doTestAction = parameters.get()) }
    viewModel { PhrasesUnitViewModel() }
    viewModel { parameters -> PhrasesUnitDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> PhrasesUnitBatchEditViewModel(vm = parameters.get()) }
    viewModel { parameters -> OnlineTextbooksDetailViewModel(item = parameters.get()) }
    viewModel { OnlineTextbooksViewModel() }
    viewModel { parameters -> WordsDictViewModel(lstWords = parameters.get(), index = parameters.get()) }
    viewModel { WordsLangViewModel() }
    viewModel { parameters -> WordsLangDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> WordsReviewViewModel(doTestAction = parameters.get()) }
    viewModel { WordsUnitViewModel() }
    viewModel { parameters -> WordsUnitDetailViewModel(item = parameters.get()) }
    viewModel { parameters -> WordsUnitBatchEditViewModel(vm = parameters.get()) }
}

fun speak(text: String) =
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

fun copyText(context: Context, text: String) {
    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    // https://stackoverflow.com/questions/57128725/kotlin-android-studio-var-is-seen-as-val-in-sdk-29
    clipboard.setPrimaryClip(clip)
}

fun openPage(context: Context, url: String) {
    // https://stackoverflow.com/questions/12013416/is-there-any-way-in-android-to-force-open-a-link-to-open-in-chrome
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.`package` = "com.android.chrome"
    try {
        context.startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        // Chrome browser presumably not installed so allow user to choose instead
        intent.`package` = null
        context.startActivity(intent)
    }
}

fun googleString(context: Context, text: String) {
    val url = "https://www.google.com/search?q=" + URLEncoder.encode(text, "UTF-8")
    openPage(context, url)
}

fun getPreferredRangeFromArray(
    index: Int,
    length: Int,
    preferredLength: Int
): Pair<Int, Int> {
    var start: Int
    var end: Int
    if (length < preferredLength) {
        start = 0; end = length
    } else {
        start = index - preferredLength / 2; end = index + preferredLength / 2
        if (start < 0) {
            end -= start; start = 0
        }
        if (end > length) {
            start -= end - length; end = length
        }
    }
    return Pair(start, end)
}
