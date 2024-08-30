package com.zwstudio.lolly.ui.webtextbooks

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.WebTextbooksScreens
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WebTextbooksHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<WebTextbooksViewModel>()
    NavHost(navController = navController, startDestination = WebTextbooksScreens.WebTextbooksMain.route) {
        composable(route = WebTextbooksScreens.WebTextbooksMain.route) {
            WebTextbooksScreen(vm, navController, openDrawer)
        }
        composable(
            route = WebTextbooksScreens.WebTextbooksDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WebTextbooksDetailScreen(vm.lstWebTextbooks[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = WebTextbooksScreens.WebTextbooksWebPage.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WebTextbooksWebPageScreen(vm.lstWebTextbooks[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
    }
}
