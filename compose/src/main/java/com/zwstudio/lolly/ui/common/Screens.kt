package com.zwstudio.lolly.ui.common

sealed class TopScreens(val route: String) {
    object Main : TopScreens("Main")
    object Login : TopScreens("Login")
}
