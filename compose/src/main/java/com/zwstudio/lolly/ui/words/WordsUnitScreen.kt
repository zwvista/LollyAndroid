package com.zwstudio.lolly.ui.words

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.ui.common.INDEX_KEY
import com.zwstudio.lolly.ui.common.WordsUnitScreens
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme

@Composable
fun WordsUnitScreen(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WordsUnitScreens.WordsUnitList.route) {
        composable(route = WordsUnitScreens.WordsUnitList.route) {
            WordsUnitListScreen(navController, openDrawer)
        }
        composable(
            route = WordsUnitScreens.WordsUnitDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            WordsUnitDetailScreen(it.arguments!!.getInt(INDEX_KEY), navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordsUnitScreenPreview() {
    LollyAndroidTheme {
        WordsUnitScreen() {}
    }
}
