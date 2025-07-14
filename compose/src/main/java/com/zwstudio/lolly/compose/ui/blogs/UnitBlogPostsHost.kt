package com.zwstudio.lolly.compose.ui.blogs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.compose.ui.common.UnitBlogPostsScreens
import com.zwstudio.lolly.viewmodels.blogs.UnitBlogPostsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UnitBlogPostsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<UnitBlogPostsViewModel>()
    NavHost(navController = navController, startDestination = UnitBlogPostsScreens.UnitBlogPosts.route) {
        composable(route = UnitBlogPostsScreens.UnitBlogPosts.route) {
            UnitBlogPostsScreen(vm, navController, openDrawer)
        }
    }
}
