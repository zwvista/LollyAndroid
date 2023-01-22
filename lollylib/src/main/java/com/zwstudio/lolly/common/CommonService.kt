package com.zwstudio.lolly.common

import android.content.*
import android.net.Uri
import android.speech.tts.TextToSpeech
import com.zwstudio.lolly.services.misc.*
import com.zwstudio.lolly.services.wpp.*
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.viewmodels.phrases.*
import com.zwstudio.lolly.viewmodels.words.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URLEncoder

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
    viewModel { PatternsViewModel() }
    viewModel { PatternsWebPagesViewModel() }
    viewModel { parameters -> PatternsWebPagesDetailViewModel(item = parameters.get()) }
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
