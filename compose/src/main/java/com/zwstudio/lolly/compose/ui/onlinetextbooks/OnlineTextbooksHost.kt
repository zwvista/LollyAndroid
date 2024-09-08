package com.zwstudio.lolly.compose.ui.onlinetextbooks

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.OnlineTextbooksScreens
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksWebPageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnlineTextbooksHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<OnlineTextbooksViewModel>()
    NavHost(navController = navController, startDestination = OnlineTextbooksScreens.OnlineTextbooksMain.route) {
        composable(route = OnlineTextbooksScreens.OnlineTextbooksMain.route) {
            OnlineTextbooksScreen(vm, navController, openDrawer)
        }
        composable(
            route = OnlineTextbooksScreens.OnlineTextbooksDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            OnlineTextbooksDetailScreen(vm.lstOnlineTextbooks[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = OnlineTextbooksScreens.OnlineTextbooksWebPage.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments!!.getInt(INDEX_KEY)
            val (start, end) = getPreferredRangeFromArray(index, vm.lstOnlineTextbooks.size, 50)
            val lstOnlineTextbooks = vm.lstOnlineTextbooks.subList(start, end)
            OnlineTextbooksWebPageScreen(OnlineTextbooksWebPageViewModel(lstOnlineTextbooks, index), navController)
        }
    }
}
