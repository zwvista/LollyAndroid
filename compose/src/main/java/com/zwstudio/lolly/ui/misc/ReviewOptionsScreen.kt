package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel

@Composable
fun ReviewOptionsScreen(vm: ReviewOptionsViewModel, navController: NavHostController?) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
        )
    }
}
