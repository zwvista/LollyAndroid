package com.zwstudio.lolly.compose.ui.patterns

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.PatternsScreens
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PatternsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<PatternsViewModel>()
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
            PatternsDetailScreen(vm.lstPatterns[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = PatternsScreens.PatternsWebPage.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments!!.getInt(INDEX_KEY)
            val (start, end) = getPreferredRangeFromArray(index, vm.lstPatterns.size, 50)
            val lstPatterns = vm.lstPatterns.subList(start, end)
            PatternsWebPageScreen(lstPatterns, index, navController)
        }
    }
}
