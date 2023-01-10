package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WordsLangScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<WordsLangViewModel>()
    NavHost(navController = navController, startDestination = WordsScreens.WordsLangList.route) {
        composable(route = WordsScreens.WordsLangList.route) {
            WordsLangListScreen(vm, navController, openDrawer)
        }
        composable(
            route = WordsScreens.WordsLangDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsLangDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}

