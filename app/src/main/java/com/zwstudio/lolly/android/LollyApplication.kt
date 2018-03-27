package com.zwstudio.lolly.android

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import com.zwstudio.lolly.data.SettingsViewModel
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@EApplication
class LollyApplication : Application() {

    lateinit var retrofit: Retrofit
    @Bean
    lateinit var vm: SettingsViewModel

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder().baseUrl("http://13.231.236.234/lolly/apimysql.php/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        vm.getData { }

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