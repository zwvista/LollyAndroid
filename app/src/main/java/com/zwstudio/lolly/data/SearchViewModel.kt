package com.zwstudio.lolly.data

import org.androidannotations.annotations.EBean

@EBean
class SearchViewModel : BaseViewModel2() {
    var lstWords = mutableListOf<String>()
    var selectedWordIndex = 0
    val selectedWord: String
        get() = lstWords[selectedWordIndex]

//    val urlString: String?
//        get() = vmSettings.selectedDictItem.urlString(selectedWord, vmSettings.lstAutoCorrect)

}
