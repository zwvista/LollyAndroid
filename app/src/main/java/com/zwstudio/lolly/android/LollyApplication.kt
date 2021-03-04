package com.zwstudio.lolly.android

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.EApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@EApplication
class LollyApplication : Application() {

    companion object {
        lateinit var retrofitJson: Retrofit
        lateinit var retrofitSP: Retrofit
        lateinit var retrofitHtml: Retrofit

        lateinit var retrofitJson2: Retrofit
        lateinit var retrofitSP2: Retrofit
        lateinit var retrofitHtml2: Retrofit
    }

    val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        retrofitJson = Retrofit.Builder().baseUrl("https://zwvista.tk/lolly/api.php/records/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitSP = Retrofit.Builder().baseUrl("https://zwvista.tk/lolly/sp.php/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
        retrofitHtml = Retrofit.Builder().baseUrl("https://www.google.com")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        retrofitJson2 = Retrofit.Builder().baseUrl("https://zwvista.tk/lolly/api.php/records/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitSP2 = Retrofit.Builder().baseUrl("https://zwvista.tk/lolly/sp.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitHtml2 = Retrofit.Builder().baseUrl("https://www.google.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
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

    val builder = AlertDialog.Builder(context)
    builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
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
