package com.zwstudio.lolly.data

import android.util.Log
import org.androidannotations.annotations.EBean
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

@EBean
class SearchViewModel : BaseViewModel2() {
    var word = ""

    val urlString: String?
        get() {
            var wordUrl: String? = null
            try {
                wordUrl = vmSettings.selectedDict.url!!.replace("{0}", URLEncoder.encode(word, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            Log.d("RepoDictionary", "urlString: " + wordUrl!!)
            return wordUrl
        }

}
