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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PhrasesReviewHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<PhrasesReviewViewModel> { parametersOf({ self: PhrasesReviewViewModel -> self.run {
        inputFocused.value = true
        if (hasCurrent && isSpeaking.value)
            speak(currentPhrase)
    }})}
    NavHost(navController = navController, startDestination = ReviewScreens.PhrasesReviewMain.route) {
        composable(route = ReviewScreens.PhrasesReviewMain.route) {
            PhrasesReviewScreen(vm, navController, openDrawer)
        }
        composable(route = ReviewScreens.ReviewOptions.route) {
            ReviewOptionsScreen(ReviewOptionsViewModel(vm.options), vm.optionsDone, navController)
        }
    }
}
