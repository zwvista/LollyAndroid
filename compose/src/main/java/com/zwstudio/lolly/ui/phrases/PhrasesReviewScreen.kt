package com.zwstudio.lolly.ui.phrases

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBar
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme

@Composable
fun PhrasesReviewScreen(openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = DrawerScreens.PhrasesReview.title,
            onButtonClicked = { openDrawer() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhrasesReviewScreenPreview() {
    LollyAndroidTheme {
        PhrasesReviewScreen() {}
    }
}
