package com.zwstudio.lolly.compose.ui.phrases

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.PhrasesScreens
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhrasesLangHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<PhrasesLangViewModel>()
    NavHost(navController = navController, startDestination = PhrasesScreens.PhrasesLangMain.route) {
        composable(route = PhrasesScreens.PhrasesLangMain.route) {
            PhrasesLangScreen(vm, navController, openDrawer)
        }
        composable(
            route = PhrasesScreens.PhrasesLangDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            PhrasesLangDetailScreen(vm, vm.lstPhrases[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = PhrasesScreens.PhrasesLangAdd.route) {
            PhrasesLangDetailScreen(vm, vm.newLangPhrase(), navController)
        }
    }
}
