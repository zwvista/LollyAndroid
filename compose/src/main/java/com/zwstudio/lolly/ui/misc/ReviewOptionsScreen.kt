package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel

@Composable
fun ReviewOptionsScreen(vm: ReviewOptionsViewModel, navController: NavHostController?) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(id = R.string.label_mode))
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                items = SettingsViewModel.lstReviewModes,
                selectedItemIndexStateFlow = vm.mode,
                itemText = { it.label }
            )
        }
    }
}
