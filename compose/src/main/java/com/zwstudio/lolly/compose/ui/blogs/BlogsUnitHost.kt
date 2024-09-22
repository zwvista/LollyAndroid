package com.zwstudio.lolly.compose.ui.blogs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zwstudio.lolly.compose.ui.common.BlogsUnitScreens
import com.zwstudio.lolly.viewmodels.blogs.UnitBlogPostsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlogsUnitHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<UnitBlogPostsViewModel>()
    NavHost(navController = navController, startDestination = BlogsUnitScreens.UnitBlogPosts.route) {
        composable(route = BlogsUnitScreens.UnitBlogPosts.route) {
            UnitBlogPostsScreen(vm, navController, openDrawer)
        }
    }
}
