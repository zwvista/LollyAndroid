package com.zwstudio.lolly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.common.onCreateApp
import com.zwstudio.lolly.common.onDestroyApp
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.views.common.Drawer
import com.zwstudio.lolly.views.common.DrawerScreens
import com.zwstudio.lolly.views.misc.LoginScreen
import com.zwstudio.lolly.views.misc.SearchScreen
import com.zwstudio.lolly.views.misc.SettingsScreen
import com.zwstudio.lolly.views.patterns.PatternsScreen
import com.zwstudio.lolly.views.phrases.PhrasesUnitScreen
import com.zwstudio.lolly.views.words.WordsUnitScreen
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
    if (GlobalUserViewModel.isLoggedIn_.observeAsState().value!!) {
        AppMainScreen()
    } else {
        LoginScreen()
    }
}

@Composable
fun AppMainScreen() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colors.background) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
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
        ) {
            NavHost(
                navController = navController,
                startDestination = DrawerScreens.Search.route
            ) {
                composable(DrawerScreens.Search.route) {
                    SearchScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(DrawerScreens.Settings.route) {
                    SettingsScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(DrawerScreens.WordsUnit.route) {
                    WordsUnitScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(DrawerScreens.PhrasesUnit.route) {
                    PhrasesUnitScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
                }
                composable(DrawerScreens.Patterns.route) {
                    PatternsScreen(
                        openDrawer = {
                            openDrawer()
                        }
                    )
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