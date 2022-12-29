package com.zwstudio.lolly.services.misc

import android.speech.tts.TextToSpeech
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit

object Global {
    var userid = ""
}

enum class UnitPartToType {
    Unit, Part, To
}

enum class DictWebViewStatus {
    Ready, Navigating, Automating
}

fun extractTextFrom(html: String, transform: String, template: String, templateHandler: (String, String) -> String): String {
    val dic = mapOf("<delete>" to "", "\\t" to "\t", "\\n" to "\n")

    var text = html
    do {
        if (transform.isEmpty()) break
        var lst = transform.split("\r\n")
        if (lst.size % 2 == 1) lst = lst.dropLast(1)

        for (i in lst.indices step 2) {
            val regex = Regex(lst[i])
            var replacer = lst[i + 1]
            if (replacer.startsWith("<extract>")) {
                replacer = replacer.drop("<extract>".length)
                val ms = regex.findAll(text)
                text = ms.joinToString { m -> m.groupValues[0] }
                if (text.isEmpty()) break
            }
            for ((key, value) in dic)
                replacer = replacer.replace(key, value)
            text = regex.replace(text, replacer)
        }

        if (template.isEmpty()) break
        text = templateHandler(text, template)

    } while (false)
    return text
}

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyIO(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.applyIO(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

lateinit var retrofitJson: Retrofit
lateinit var retrofitSP: Retrofit
lateinit var retrofitHtml: Retrofit
lateinit var vmSettings: SettingsViewModel
lateinit var tts: TextToSpeech

fun speak(text: String) =
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
