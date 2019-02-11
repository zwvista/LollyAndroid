package com.zwstudio.lolly.data

import android.content.*
import android.net.Uri
import android.view.View
import java.net.URLEncoder

fun View.copyText(text: String) {
    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    clipboard.primaryClip = clip
}

fun View.googleString(text: String) {
    // https://stackoverflow.com/questions/12013416/is-there-any-way-in-android-to-force-open-a-link-to-open-in-chrome
    val urlString = "https://www.google.com/search?q=" + URLEncoder.encode(text, "UTF-8")
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
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