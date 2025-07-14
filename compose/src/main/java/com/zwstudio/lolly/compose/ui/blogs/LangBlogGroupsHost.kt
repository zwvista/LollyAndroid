package com.zwstudio.lolly.compose.ui.blogs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.compose.ui.common.LangBlogGroupsScreens
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LangBlogGroupsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<LangBlogGroupsViewModel>()
    NavHost(navController = navController, startDestination = LangBlogGroupsScreens.LangBlogGroups.route) {
        composable(route = LangBlogGroupsScreens.LangBlogGroups.route) {
            LangBlogGroupsScreen(vm, navController, openDrawer)
        }
    }
}
