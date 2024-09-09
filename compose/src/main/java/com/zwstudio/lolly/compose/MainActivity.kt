package com.zwstudio.lolly.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.common.onCreateApp
import com.zwstudio.lolly.common.onDestroyApp
import com.zwstudio.lolly.compose.ui.common.Drawer
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.TopScreens
import com.zwstudio.lolly.compose.ui.misc.LoginScreen
import com.zwstudio.lolly.compose.ui.misc.SearchScreen
import com.zwstudio.lolly.compose.ui.misc.SettingsScreen
import com.zwstudio.lolly.compose.ui.onlinetextbooks.OnlineTextbooksHost
import com.zwstudio.lolly.compose.ui.patterns.PatternsHost
import com.zwstudio.lolly.compose.ui.phrases.PhrasesLangHost
import com.zwstudio.lolly.compose.ui.phrases.PhrasesReviewHost
import com.zwstudio.lolly.compose.ui.phrases.PhrasesTextbookHost
import com.zwstudio.lolly.compose.ui.phrases.PhrasesUnitHost
import com.zwstudio.lolly.compose.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.compose.ui.words.WordsLangHost
import com.zwstudio.lolly.compose.ui.words.WordsReviewHost
import com.zwstudio.lolly.compose.ui.words.WordsTextbookHost
import com.zwstudio.lolly.compose.ui.words.WordsUnitHost
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateApp(this)
        GlobalUserViewModel.load(this)
        setContent {
            LollyAndroidTheme {
                ApplicationSwitcher()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyApp()
    }
}

@Composable
fun ApplicationSwitcher() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = TopScreens.AppMain.route) {
        composable(TopScreens.AppMain.route) { AppMainScreen() }
        composable(TopScreens.Login.route) { LoginScreen() }
    }
    if (GlobalUserViewModel.isLoggedIn_.collectAsState().value)
        navController.navigate(TopScreens.AppMain.route)
    else
        navController.navigate(TopScreens.Login.route)
}

@Composable
fun AppMainScreen() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(280.dp)
                ) {
                    Drawer(
                        onDestinationClicked = { route ->
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = DrawerScreens.Search.route
            ) {
                composable(DrawerScreens.Search.route) {
                    SearchScreen { openDrawer() }
                }
                composable(DrawerScreens.Settings.route) {
                    SettingsScreen { openDrawer() }
                }
                composable(DrawerScreens.WordsUnit.route) {
                    WordsUnitHost { openDrawer() }
                }
                composable(DrawerScreens.PhrasesUnit.route) {
                    PhrasesUnitHost { openDrawer() }
                }
                composable(DrawerScreens.WordsReview.route) {
                    WordsReviewHost { openDrawer() }
                }
                composable(DrawerScreens.PhrasesReview.route) {
                    PhrasesReviewHost { openDrawer() }
                }
                composable(DrawerScreens.WordsTextbook.route) {
                    WordsTextbookHost { openDrawer() }
                }
                composable(DrawerScreens.PhrasesTextbook.route) {
                    PhrasesTextbookHost { openDrawer() }
                }
                composable(DrawerScreens.WordsLang.route) {
                    WordsLangHost { openDrawer() }
                }
                composable(DrawerScreens.PhrasesLang.route) {
                    PhrasesLangHost { openDrawer() }
                }
                composable(DrawerScreens.Patterns.route) {
                    PatternsHost { openDrawer() }
                }
                composable(DrawerScreens.OnlineTextbooks.route) {
                    OnlineTextbooksHost { openDrawer() }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LollyAndroidTheme {
        AppMainScreen()
    }
}