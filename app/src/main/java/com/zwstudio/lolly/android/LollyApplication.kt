package com.zwstudio.lolly.android

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import com.zwstudio.lolly.data.SettingsViewModel
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@EApplication
class LollyApplication : Application() {

    lateinit var retrofitJson: Retrofit
    lateinit var retrofitHtml: Retrofit
    @Bean
    lateinit var vm: SettingsViewModel

    override fun onCreate() {
        super.onCreate()
        retrofitJson = Retrofit.Builder().baseUrl("https://zwvista.tk/lolly/api.php/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
        retrofitHtml = Retrofit.Builder().baseUrl("https://www.google.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        vm.getData().subscribe()

    }

    override fun onTerminate() {
        super.onTerminate()
    }
}

fun yesNoDialog(context: Context, message: String, yesAction: () -> Unit, noAction: () -> Unit) {
    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
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

    val builder = AlertDialog.Builder(context)
    builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener).show()
}

class LollySwipeRefreshLayout : SwipeRefreshLayout {
    private var mScrollingView: View? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun canChildScrollUp(): Boolean {
        return mScrollingView != null && ViewCompat.canScrollVertically(mScrollingView!!, -1)
    }

    fun setScrollingView(scrollingView: View) {
        mScrollingView = scrollingView
    }
}
