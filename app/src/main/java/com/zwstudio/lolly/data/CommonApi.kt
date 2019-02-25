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

fun unitsFrom(unitinfo: String): List<String> {
    var m = Regex("UNITS,(\\d+)").find(unitinfo)
    if (m != null) {
        val units = m.groupValues[1].toInt()
        return (1..units).map { it.toString() }
    }
    m = Regex("PAGES,(\\d+),(\\d+)").find(unitinfo)
    if (m != null) {
        val n1 = m.groupValues[1].toInt()
        val n2 = m.groupValues[2].toInt()
        val units = (n1 + n2 - 1) / n2
        return (1..units).map { "${it * n2 - n2 + 1}~${it * n2}" }
    }
    m = Regex("CUSTOM,(.+)").find(unitinfo)
    if (m != null)
        return m.groupValues[1].split(",")
    return listOf()
}

fun partsFrom(parts: String) = parts.split(",")