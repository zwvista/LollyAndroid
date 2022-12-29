package com.zwstudio.lolly.views

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidisland.vita.startVita
import com.zwstudio.lolly.services.misc.*
import com.zwstudio.lolly.services.wpp.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LollyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
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
        startVita()
        // https://github.com/InsertKoinIO/koin/issues/1076
        startKoin {
            // Koin Android logger
            androidLogger(Level.ERROR)
            //inject Android context
            androidContext(this@LollyApplication)
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
}

fun yesNoDialog(context: Context, message: String, yesAction: () -> Unit, noAction: () -> Unit) {
    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                //Yes button clicked
                yesAction()
            DialogInterface.BUTTON_NEGATIVE -> {
                //No button clicked
                noAction()
            }
        }
    }

    AlertDialog.Builder(context)
        .setMessage(message).setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener).show()
}

class LollySwipeRefreshLayout : SwipeRefreshLayout {
    private var mScrollingView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canChildScrollUp(): Boolean {
        return mScrollingView != null && mScrollingView!!.canScrollVertically(-1)
    }

    fun setScrollingView(scrollingView: View) {
        mScrollingView = scrollingView
    }
}
