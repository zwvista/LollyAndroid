package com.zwstudio.lolly.ui.phrases

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.phrases.PhrasesReviewViewModel

@Composable
fun PhrasesReviewMainScreen(vm: PhrasesReviewViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesReview.title,
            onButtonClicked = { openDrawer() }
        )
    }
}
