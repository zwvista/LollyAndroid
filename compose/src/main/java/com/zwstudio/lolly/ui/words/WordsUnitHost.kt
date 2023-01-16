package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WordsUnitHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    // https://stackoverflow.com/questions/68548488/sharing-viewmodel-within-jetpack-compose-navigation
    val vm = getViewModel<WordsUnitViewModel>()
    NavHost(navController = navController, startDestination = WordsScreens.WordsUnitMain.route) {
        composable(route = WordsScreens.WordsUnitMain.route) {
            WordsUnitScreen(vm, navController, openDrawer)
        }
        composable(
            route = WordsScreens.WordsUnitDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsUnitDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
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
