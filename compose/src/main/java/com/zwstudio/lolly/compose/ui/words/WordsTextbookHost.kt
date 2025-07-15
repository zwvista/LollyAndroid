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
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WordsTextbookHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<WordsUnitViewModel>()
    NavHost(navController = navController, startDestination = WordsScreens.WordsTextbookMain.route) {
        composable(route = WordsScreens.WordsTextbookMain.route) {
            WordsTextbookScreen(vm, navController, openDrawer)
        }
        composable(
            route = WordsScreens.WordsTextbookDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsTextbookDetailScreen(vm, vm.lstWords[it.arguments!!.getInt(INDEX_KEY)], navController)
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
            WordsDictScreen(lstWords, index, navController)
        }
    }
}
