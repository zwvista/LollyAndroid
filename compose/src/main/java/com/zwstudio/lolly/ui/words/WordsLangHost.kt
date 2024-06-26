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
import org.koin.androidx.compose.koinViewModel

@Composable
fun WordsLangHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<WordsLangViewModel>()
    NavHost(navController = navController, startDestination = WordsScreens.WordsLangMain.route) {
        composable(route = WordsScreens.WordsLangMain.route) {
            WordsLangScreen(vm, navController, openDrawer)
        }
        composable(
            route = WordsScreens.WordsLangDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsLangDetailScreen(vm, vm.lstWords[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = WordsScreens.WordsLangAdd.route) {
            WordsLangDetailScreen(vm, vm.newLangWord(), navController)
        }
        composable(
            route = WordsScreens.WordsDict.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsDictScreen(
                vm.lstWords.map { it.word }, it.arguments!!.getInt(INDEX_KEY), navController
            )
        }
    }
}
