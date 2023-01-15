package com.zwstudio.lolly.ui.common

sealed class TopScreens(val route: String) {
    object AppMain : TopScreens("AppMain")
    object Login : TopScreens("Login")
}

sealed class WordsScreens(val route: String) {
    object WordsUnitMain : WordsScreens("WordsUnitMain")
    object WordsUnitDetail : WordsScreens("WordsUnitDetail")
    object WordsTextbookMain : WordsScreens("WordsTextbookMain")
    object WordsTextbookDetail : WordsScreens("WordsTextbookDetail")
    object WordsLangMain : WordsScreens("WordsLangMain")
    object WordsLangDetail : WordsScreens("WordsLangDetail")
    object WordsDict : WordsScreens("WordsDict")
}

sealed class PhrasesScreens(val route: String) {
    object PhrasesUnitMain : PhrasesScreens("PhrasesUnitMain")
    object PhrasesUnitDetail : PhrasesScreens("PhrasesUnitDetail")
    object PhrasesTextbookMain : PhrasesScreens("PhrasesTextbookMain")
    object PhrasesTextbookDetail : PhrasesScreens("PhrasesTextbookDetail")
    object PhrasesLangMain : PhrasesScreens("PhrasesLangMain")
    object PhrasesLangDetail : PhrasesScreens("PhrasesLangDetail")
}

sealed class PatternsScreens(val route: String) {
    object PatternsMain : PatternsScreens("PatternsMain")
    object PatternsDetail : PatternsScreens("PatternsDetail")
}

const val INDEX_KEY = "INDEX_KEY"


sealed class ReviewScreens(val route: String) {
    object WordsReviewMain : ReviewScreens("WordsReviewMain")
    object PhrasesReviewMain : ReviewScreens("PhrasesReviewMain")
    object ReviewOptions : ReviewScreens("ReviewOptions")
}
