package com.zwstudio.lolly.ui.common

sealed class TopScreens(val route: String) {
    object AppMain : TopScreens("AppMain")
    object Login : TopScreens("Login")
}
