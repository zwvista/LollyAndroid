package com.zwstudio.lolly.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme

sealed class DrawerScreens(val title: String, val route: String) {
    object Search : DrawerScreens("Search", "Search")
    object Settings : DrawerScreens("Settings", "Settings")
    object WordsUnit : DrawerScreens( "Words in Unit", "WordsUnit")
    object PhrasesUnit : DrawerScreens( "Phrases in Unit", "PhrasesUnit")
    object WordsReview : DrawerScreens( "Words Review", "WordsReview")
    object PhrasesReview : DrawerScreens( "Phrases Review", "PhrasesReview")
    object WordsTextbook : DrawerScreens( "Words in Textbook", "WordsTextbook")
    object PhrasesTextbook : DrawerScreens( "Phrases in Textbook", "PhrasesTextbook")
    object WordsLang : DrawerScreens( "Words in Language", "WordsLang")
    object PhrasesLang : DrawerScreens( "Phrases in Language", "PhrasesLang")
    object Patterns : DrawerScreens( "Patterns in Language", "Patterns")
    object WebTextbooks : DrawerScreens( "WebTextbooks", "WebTextbooks")
}

private val screens = listOf(
    DrawerScreens.Search,
    DrawerScreens.Settings,
    DrawerScreens.WordsUnit,
    DrawerScreens.PhrasesUnit,
    DrawerScreens.WordsReview,
    DrawerScreens.PhrasesReview,
    DrawerScreens.WordsTextbook,
    DrawerScreens.PhrasesTextbook,
    DrawerScreens.WordsLang,
    DrawerScreens.PhrasesLang,
    DrawerScreens.Patterns,
    DrawerScreens.WebTextbooks,
)

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_baseline_android_24),
            contentDescription = "App icon"
        )
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.clickable {
                    onDestinationClicked(screen.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun DrawerPreview() {
    LollyAndroidTheme {
        Drawer{}
    }
}