package com.zwstudio.lolly.ui.patterns

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.PatternsScreens
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PatternsScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<PatternsViewModel>()
    NavHost(navController = navController, startDestination = PatternsScreens.PatternsList.route) {
        composable(route = PatternsScreens.PatternsList.route) {
            PatternsListScreen(vm, navController, openDrawer)
        }
        composable(
            route = PatternsScreens.PatternsDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}
