package com.zwstudio.lolly.ui.phrases

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.ui.common.ReviewScreens
import com.zwstudio.lolly.ui.misc.ReviewOptionsScreen
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesReviewViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PhrasesReviewHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = getViewModel<PhrasesReviewViewModel> { parametersOf({ self: PhrasesReviewViewModel -> self.run {
        if (hasCurrent && isSpeaking.value)
            speak(currentPhrase)
    }})}
    NavHost(navController = navController, startDestination = ReviewScreens.PhrasesReviewMain.route) {
        composable(route = ReviewScreens.PhrasesReviewMain.route) {
            PhrasesReviewScreen(vm, navController, openDrawer)
        }
        composable(route = ReviewScreens.ReviewOptions.route) {
            ReviewOptionsScreen(ReviewOptionsViewModel(vm.options), navController)
        }
    }
}
