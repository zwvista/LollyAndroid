package com.zwstudio.lolly

import android.app.Application
import com.zwstudio.lolly.common.onCreateApp

class LollyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        onCreateApp(this)
    }
}
