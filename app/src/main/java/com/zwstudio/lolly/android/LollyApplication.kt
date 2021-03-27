package com.zwstudio.lolly.android

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidisland.vita.startVita
import com.zwstudio.lolly.data.misc.SettingsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LollyApplication : Application() {

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
        vmSettings = SettingsViewModel()
        startVita()
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

lateinit var retrofitJson: Retrofit
lateinit var retrofitSP: Retrofit
lateinit var retrofitHtml: Retrofit
lateinit var vmSettings: SettingsViewModel

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

// https://stackoverflow.com/questions/50754523/how-to-get-a-result-from-fragment-using-navigation-architecture-component
// https://stackoverflow.com/questions/56624895/android-jetpack-navigation-component-result-from-dialog/62054347#62054347
fun <T> Fragment.setNavigationResult(value: T, key: String = "result") =
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)

fun <T> Fragment.getNavigationResult(key: String = "result", onResult: (result: T) -> Unit) {
    val navBackStackEntry = findNavController().currentBackStackEntry!!

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && navBackStackEntry.savedStateHandle.contains(key)
        ) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            result?.let(onResult)
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}
