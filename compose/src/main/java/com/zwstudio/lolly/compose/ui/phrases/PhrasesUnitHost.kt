package com.zwstudio.lolly.compose.ui.phrases

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.PhrasesScreens
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhrasesUnitHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<PhrasesUnitViewModel>()
    NavHost(navController = navController, startDestination = PhrasesScreens.PhrasesUnitMain.route) {
        composable(route = PhrasesScreens.PhrasesUnitMain.route) {
            PhrasesUnitScreen(vm, navController, openDrawer)
        }
        composable(
            route = PhrasesScreens.PhrasesUnitDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PhrasesUnitDetailScreen(vm, vm.lstPhrases[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = PhrasesScreens.PhrasesUnitAdd.route) {
            PhrasesUnitDetailScreen(vm, vm.newUnitPhrase(), navController)
        }
        composable(route = PhrasesScreens.PhrasesUnitBatchEdit.route) {
            PhrasesUnitBatchEditScreen(vm, navController)
        }
    }
}
