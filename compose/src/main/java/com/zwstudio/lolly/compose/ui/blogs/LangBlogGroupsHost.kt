package com.zwstudio.lolly.compose.ui.blogs

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zwstudio.lolly.common.getPreferredRangeFromArray
import com.zwstudio.lolly.compose.ui.common.INDEX_KEY
import com.zwstudio.lolly.compose.ui.common.LangBlogGroupsScreens
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsContentViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LangBlogGroupsHost(openDrawer: () -> Unit) {

    val navController = rememberNavController()
    val vm = koinViewModel<LangBlogGroupsViewModel>()
    NavHost(navController = navController, startDestination = LangBlogGroupsScreens.LangBlogGroups.route) {
        composable(route = LangBlogGroupsScreens.LangBlogGroups.route) {
            LangBlogGroupsScreen(vm, navController, openDrawer)
        }
        composable(
            route = LangBlogGroupsScreens.LangBlogGroupsDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            LangBlogGroupsDetailScreen(vm.lstLangBlogGroups[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(route = LangBlogGroupsScreens.LangBlogPostsList.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            LangBlogPostsListScreen(vm, navController, vm.lstLangBlogGroups[it.arguments!!.getInt(INDEX_KEY)])
        }
        composable(
            route = LangBlogGroupsScreens.LangBlogPostsDetail.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            LangBlogPostsDetailScreen(vm.lstLangBlogPosts[it.arguments!!.getInt(INDEX_KEY)], navController)
        }
        composable(
            route = LangBlogGroupsScreens.LangBlogPostsContent.route + "/{$INDEX_KEY}",
            arguments = listOf(navArgument(INDEX_KEY) {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments!!.getInt(INDEX_KEY)
            val (start, end) = getPreferredRangeFromArray(index, vm.lstLangBlogPosts.size, 50)
            val lstLangBlogPosts = vm.lstLangBlogPosts.subList(start, end)
            LangBlogPostsContentScreen(LangBlogPostsContentViewModel(lstLangBlogPosts, index), navController)
        }
    }
}
