package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel

@Composable
fun WordsReviewMainScreen(vm: WordsReviewViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsReview.title,
            onButtonClicked = { openDrawer() }
        )
    }
}
