package com.zwstudio.lolly.ui.common

sealed class TopScreens(val route: String) {
    object AppMain : TopScreens("AppMain")
    object Login : TopScreens("Login")
}

sealed class WordsUnitScreens(val route: String) {
    object WordsUnitList : WordsUnitScreens("WordsUnitList")
    object WordsUnitDetail : WordsUnitScreens("WordsUnitDetail")
}

const val INDEX_KEY = "INDEX_KEY"