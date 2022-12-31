package com.zwstudio.lolly

import android.app.Application
import com.androidisland.vita.startVita
import com.zwstudio.lolly.common.onCreateApp

class LollyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startVita()
        onCreateApp(this)
    }
}
