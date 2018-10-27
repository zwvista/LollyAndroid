package com.zwstudio.lolly.data

import android.util.Log
import org.androidannotations.annotations.EBean
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

@EBean
class SearchViewModel : BaseViewModel2() {
    var lstWords = mutableListOf<String>()
    var selectedWordIndex = 0
    val selectWord: String
        get() = lstWords[selectedWordIndex]

    val urlString: String?
        get() = vmSettings.selectedDictOnline.urlString(selectWord, vmSettings.lstAutoCorrect)

}
