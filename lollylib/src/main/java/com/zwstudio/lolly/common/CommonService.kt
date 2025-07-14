package com.zwstudio.lolly.common

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.services.blogs.LangBlogGPService
import com.zwstudio.lolly.services.blogs.LangBlogGroupService
import com.zwstudio.lolly.services.blogs.LangBlogPostContentService
import com.zwstudio.lolly.services.blogs.LangBlogPostService
import com.zwstudio.lolly.services.blogs.UnitBlogPostService
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
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsDetailViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsContentViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsDetailViewModel
import com.zwstudio.lolly.viewmodels.blogs.UnitBlogPostsViewModel
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksDetailViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksWebPageViewModel
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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
    singleOf(::LangBlogGPService)
    singleOf(::LangBlogGroupService)
    singleOf(::LangBlogPostContentService)
    singleOf(::LangBlogPostService)
    singleOf(::UnitBlogPostService)

    singleOf(::AutoCorrectService)
    singleOf(::DictionaryService)
    singleOf(::HtmlService)
    singleOf(::LanguageService)
    singleOf(::OnlineTextbookService)
    singleOf(::TextbookService)
    singleOf(::UserService)
    singleOf(::UserSettingService)
    singleOf(::USMappingService)
    singleOf(::VoiceService)

    singleOf(::LangPhraseService)
    singleOf(::LangWordService)
    singleOf(::PatternService)
    singleOf(::UnitPhraseService)
    singleOf(::UnitWordService)
    singleOf(::WordFamiService)

    viewModelOf(::LangBlogGroupsDetailViewModel)
    viewModelOf(::LangBlogGroupsViewModel)
    viewModelOf(::LangBlogPostsContentViewModel)
    viewModelOf(::LangBlogPostsDetailViewModel)
    viewModelOf(::UnitBlogPostsViewModel)

    viewModelOf(::LoginViewModel)
    viewModelOf(::ReviewOptionsViewModel)
    viewModelOf(::SearchViewModel)

    viewModelOf(::OnlineTextbooksDetailViewModel)
    viewModelOf(::OnlineTextbooksViewModel)
    viewModelOf(::OnlineTextbooksWebPageViewModel)

    viewModelOf(::PatternsDetailViewModel)
    viewModelOf(::PatternsViewModel)
    viewModelOf(::PatternsWebPageViewModel)

    viewModelOf(::PhrasesLangDetailViewModel)
    viewModelOf(::PhrasesLangViewModel)
    viewModelOf(::PhrasesReviewViewModel)
    viewModelOf(::PhrasesUnitBatchEditViewModel)
    viewModelOf(::PhrasesUnitDetailViewModel)
    viewModelOf(::PhrasesUnitViewModel)

    viewModelOf(::WordsDictViewModel)
    viewModelOf(::WordsLangDetailViewModel)
    viewModelOf(::WordsLangViewModel)
    viewModelOf(::WordsReviewViewModel)
    viewModelOf(::WordsUnitBatchEditViewModel)
    viewModelOf(::WordsUnitDetailViewModel)
    viewModelOf(::WordsUnitViewModel)
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

fun Any.logDebug(message: String) {
    Log.d(this::class.java.simpleName, message)
}

fun Int.logCreate(): Int =
    also { logDebug("✅ Created new item, result=$this") }

fun Any.logUpdate(id: Int) =
    logDebug("\uD83D\uDCDD Updated item, ID=$id, result=$this")

fun Int.logDelete() =
    logDebug("❌ Deleted item, result=$this")

fun List<List<MSPResult>>.logCreateResult(): Int =
    let {
        logDebug("Created new item, result=$it")
        it[0][0].newid!!.toInt()
    }

fun List<List<MSPResult>>.logUpdateResult(id: Int) =
    logDebug("Updated item ID=${id}, result=$id")

fun List<List<MSPResult>>.logDeleteResult(): Unit =
    logDebug("Deleted item, result=$this")
