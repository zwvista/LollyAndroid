package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.ReviewScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel

@Composable
fun WordsReviewScreen(vm: WordsReviewViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    LaunchedEffect(Unit, block = {
        if (vm.showOptions) {
            vm.showOptions = false
            navController?.navigate(ReviewScreens.ReviewOptions.route)
        }
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsReview.title,
            onButtonClicked = { openDrawer() }
        )
    }
}
