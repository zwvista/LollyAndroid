package com.zwstudio.lolly.compose.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.theme.LollyAndroidTheme

sealed class DrawerScreens(@StringRes val titleRes: Int, val route: String) {
    object Search : DrawerScreens(R.string.search, "Search")
    object Settings : DrawerScreens(R.string.settings, "Settings")
    object WordsUnit : DrawerScreens( R.string.words_unit, "WordsUnit")
    object PhrasesUnit : DrawerScreens( R.string.phrases_unit, "PhrasesUnit")
    object WordsReview : DrawerScreens( R.string.words_review, "WordsReview")
    object PhrasesReview : DrawerScreens( R.string.phrases_review, "PhrasesReview")
    object WordsTextbook : DrawerScreens( R.string.words_textbook, "WordsTextbook")
    object PhrasesTextbook : DrawerScreens( R.string.phrases_textbook, "PhrasesTextbook")
    object WordsLang : DrawerScreens( R.string.words_lang, "WordsLang")
    object PhrasesLang : DrawerScreens( R.string.phrases_lang, "PhrasesLang")
    object Patterns : DrawerScreens( R.string.patterns, "Patterns")
    object OnlineTextbooks : DrawerScreens( R.string.onlinetextbooks, "OnlineTextbooks")
    object UnitBlogPosts : DrawerScreens( R.string.unit_blog_posts, "UnitBlogPosts")
    object LangBlogGroups : DrawerScreens( R.string.lang_blog_groups, "LangBlogGroups")
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
    DrawerScreens.OnlineTextbooks,
    DrawerScreens.UnitBlogPosts,
    DrawerScreens.LangBlogGroups,
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
                text = stringResource(id = screen.titleRes),
                style = MaterialTheme.typography.titleLarge,
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