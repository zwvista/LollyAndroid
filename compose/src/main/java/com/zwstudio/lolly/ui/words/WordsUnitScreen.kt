package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.WordsUnitScreens
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WordsUnitScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    // https://stackoverflow.com/questions/68548488/sharing-viewmodel-within-jetpack-compose-navigation
    val vm = getViewModel<WordsUnitViewModel>()
    NavHost(navController = navController, startDestination = WordsUnitScreens.WordsUnitList.route) {
        composable(route = WordsUnitScreens.WordsUnitList.route) {
            WordsUnitListScreen(vm, navController, openDrawer)
        }
        composable(
            route = WordsUnitScreens.WordsUnitDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsUnitDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}

