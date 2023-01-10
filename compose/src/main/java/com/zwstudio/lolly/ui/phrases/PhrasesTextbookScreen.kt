package com.zwstudio.lolly.ui.phrases

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.PhrasesScreens
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PhrasesTextbookScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<PhrasesUnitViewModel>()
    NavHost(navController = navController, startDestination = PhrasesScreens.PhrasesTextbookList.route) {
        composable(route = PhrasesScreens.PhrasesTextbookList.route) {
            PhrasesTextbookListScreen(vm, navController, openDrawer)
        }
        composable(
            route = PhrasesScreens.PhrasesTextbookDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PhrasesTextbookDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}

