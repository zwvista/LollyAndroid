package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.ReviewScreens
import com.zwstudio.lolly.ui.misc.ReviewOptionsScreen
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WordsReviewScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<WordsReviewViewModel>()
    NavHost(navController = navController, startDestination = ReviewScreens.WordsReviewMain.route) {
        composable(route = ReviewScreens.WordsReviewMain.route) {
            WordsReviewMainScreen(vm, navController, openDrawer)
        }
        composable(
            route = ReviewScreens.ReviewOptions.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            ReviewOptionsScreen(ReviewOptionsViewModel(vm.options), navController)
        }
    }
}
