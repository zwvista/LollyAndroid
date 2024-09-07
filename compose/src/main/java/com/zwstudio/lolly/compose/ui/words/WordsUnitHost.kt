package com.zwstudio.lolly.compose.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WordsUnitHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    // https://stackoverflow.com/questions/68548488/sharing-viewmodel-within-jetpack-compose-navigation
    val vm = koinViewModel<WordsUnitViewModel>()
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
            WordsUnitDetailScreen(vm, vm.lstWords[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = WordsScreens.WordsUnitAdd.route) {
            WordsUnitDetailScreen(vm, vm.newUnitWord(), navController)
        }
        composable(
            route = WordsScreens.WordsDict.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments!!.getInt(INDEX_KEY)
            val lstWords = vm.lstWords.map { it.word }
            WordsDictScreen(WordsDictViewModel(lstWords, index), navController)
        }
        composable(route = WordsScreens.WordsUnitBatchEdit.route) {
            WordsUnitBatchEditScreen(vm, navController)
        }
    }
}
