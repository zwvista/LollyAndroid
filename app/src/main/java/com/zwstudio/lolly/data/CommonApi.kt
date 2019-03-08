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

fun extractTextFrom(html: String, transform: String, template: String, templateHandler: (String, String) -> String): String {
    val dic = mapOf("<delete>" to "", "\\t" to "\t", "\\r" to "\r", "\\n" to "\n")

    var text = ""
    do {
        if (transform.isEmpty()) break
        val arr = transform.split("\r\n")
        var regex = Regex(arr[0])
        val m = regex.find(html)
        if (m == null) break
        text = m.groupValues[0]

        fun f(replacer: String) {
            var replacer = replacer
            for ((key, value) in dic)
                replacer = replacer.replace(key, value)
            text = regex.replace(text, replacer)
        }

        f(arr[1])
        for (i in 2 until arr.size)
            if (i % 2 == 0)
                regex = Regex(arr[i])
            else
                f(arr[i])

        if (template.isEmpty()) break
        text = templateHandler(text, template)

    } while (false)
    return text
}