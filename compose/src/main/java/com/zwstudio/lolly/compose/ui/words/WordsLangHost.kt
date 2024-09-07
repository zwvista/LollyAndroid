package com.zwstudio.lolly.compose.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.words.WordsDictViewModel
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
            val index = it.arguments!!.getInt(INDEX_KEY)
            val (start, end) = getPreferredRangeFromArray(index, vm.lstWords.size, 50)
            val lstWords = vm.lstWords.subList(start, end).map { it.word }
            WordsDictScreen(WordsDictViewModel(lstWords, index), navController)
        }
    }
}
