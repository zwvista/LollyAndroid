package com.zwstudio.lolly.ui.common

sealed class TopScreens(val route: String) {
    object AppMain : TopScreens("AppMain")
    object Login : TopScreens("Login")
}

sealed class WordsScreens(val route: String) {
    object WordsUnitList : WordsScreens("WordsUnitList")
    object WordsUnitDetail : WordsScreens("WordsUnitDetail")
    object WordsTextbookList : WordsScreens("WordsTextbookList")
    object WordsTextbookDetail : WordsScreens("WordsTextbookDetail")
    object WordsLangList : WordsScreens("WordsLangList")
    object WordsLangDetail : WordsScreens("WordsLangDetail")
}

sealed class PhrasesScreens(val route: String) {
    object PhrasesUnitList : PhrasesScreens("PhrasesUnitList")
    object PhrasesUnitDetail : PhrasesScreens("PhrasesUnitDetail")
    object PhrasesTextbookList : PhrasesScreens("PhrasesTextbookList")
    object PhrasesTextbookDetail : PhrasesScreens("PhrasesTextbookDetail")
    object PhrasesLangList : PhrasesScreens("PhrasesLangList")
    object PhrasesLangDetail : PhrasesScreens("PhrasesLangDetail")
}

sealed class PatternsScreens(val route: String) {
    object PatternsList : PatternsScreens("PatternsList")
    object PatternsDetail : PatternsScreens("PatternsDetail")
}

const val INDEX_KEY = "INDEX_KEY"