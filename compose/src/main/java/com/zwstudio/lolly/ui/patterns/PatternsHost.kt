package com.zwstudio.lolly.ui.patterns

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.PatternsHosts
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PatternsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<PatternsViewModel>()
    val vmWP = getViewModel<PatternsWebPagesViewModel>()
    NavHost(navController = navController, startDestination = PatternsHosts.PatternsMain.route) {
        composable(route = PatternsHosts.PatternsMain.route) {
            PatternsScreen(vm, navController, openDrawer)
        }
        composable(
            route = PatternsHosts.PatternsDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsDetailScreen(vm, it.arguments!!.getInt(INDEX_KEY), navController)
        }
        composable(
            route = PatternsHosts.PatternsWebPagesBrowse.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesBrowseScreen(vmWP, vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = PatternsHosts.PatternsWebPagesList.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesListScreen(vmWP, vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = PatternsHosts.PatternsWebPagesDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesDetailScreen(vmWP, it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}