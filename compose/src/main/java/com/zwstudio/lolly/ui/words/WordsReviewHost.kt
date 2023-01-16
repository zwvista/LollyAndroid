package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.ui.common.ReviewScreens
import com.zwstudio.lolly.ui.misc.ReviewOptionsScreen
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WordsReviewHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<WordsReviewViewModel> { parametersOf({ self: WordsReviewViewModel -> self.run {
        if (hasCurrent && isSpeaking.value)
            speak(currentWord)
    }})}
    NavHost(navController = navController, startDestination = ReviewScreens.WordsReviewMain.route) {
        composable(route = ReviewScreens.WordsReviewMain.route) {
            WordsReviewScreen(vm, navController, openDrawer)
        }
        composable(route = ReviewScreens.ReviewOptions.route) {
            ReviewOptionsScreen(ReviewOptionsViewModel(vm.options), vm.optionsDone, navController)
        }
    }
}
