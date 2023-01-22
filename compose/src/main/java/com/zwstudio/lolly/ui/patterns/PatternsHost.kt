package com.zwstudio.lolly.ui.patterns

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.PatternsScreens
import com.zwstudio.lolly.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PatternsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<PatternsViewModel>()
    val vmWP = getViewModel<PatternsWebPagesViewModel>()
    NavHost(navController = navController, startDestination = PatternsScreens.PatternsMain.route) {
        composable(route = PatternsScreens.PatternsMain.route) {
            PatternsScreen(vm, navController, openDrawer)
        }
        composable(
            route = PatternsScreens.PatternsDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsDetailScreen(vm, vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = WordsScreens.WordsUnitAdd.route) {
            PatternsDetailScreen(vm, vm.newPattern(), navController)
        }
        composable(
            route = PatternsScreens.PatternsWebPagesBrowse.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesBrowseScreen(vmWP, vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = PatternsScreens.PatternsWebPagesList.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesListScreen(vmWP, vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = PatternsScreens.PatternsWebPagesDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PatternsWebPagesDetailScreen(vmWP, vmWP.lstWebPages[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
//        composable(route = WordsScreens.WordsUnitAdd.route) {
//            PatternsWebPagesDetailScreen(vmWP, vmWP.newPatternWebPage(), navController)
//        }
    }
}
