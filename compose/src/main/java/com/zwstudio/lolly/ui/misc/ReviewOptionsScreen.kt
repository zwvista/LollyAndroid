package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.ui.common.LabelledCheckBox
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.misc.ReviewOptionsViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ReviewOptionsScreen(
    vm: ReviewOptionsViewModel,
    optionsDone: MutableStateFlow<Boolean>
    , navController: NavHostController?
) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.review_options),
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        optionsDone.value = true
                        navController?.navigateUp()
                    }
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
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
            LabelledCheckBox(
                checked = vm.shuffled.collectAsState().value,
                onCheckedChange = { vm.shuffled.value = it },
                label = stringResource(id = R.string.label_shuffled)
            )
            LabelledCheckBox(
                checked = vm.speakingEnabled.collectAsState().value,
                onCheckedChange = { vm.speakingEnabled.value = it },
                label = stringResource(id = R.string.label_speak_enabled)
            )
            LabelledCheckBox(
                checked = vm.onRepeat.collectAsState().value,
                onCheckedChange = { vm.onRepeat.value = it },
                label = stringResource(id = R.string.label_on_repeat)
            )
            LabelledCheckBox(
                checked = vm.moveForward.collectAsState().value,
                onCheckedChange = { vm.moveForward.value = it },
                label = stringResource(id = R.string.label_move_forward)
            )
            TextField(
                value = vm.interval.collectAsState().value.toString(),
                onValueChange = { vm.interval.value = it.toInt() },
                label = { Text(text = stringResource(id = R.string.label_interval)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = vm.groupSelected.collectAsState().value.toString(),
                onValueChange = { vm.groupSelected.value = it.toInt() },
                label = { Text(text = stringResource(id = R.string.label_group)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = vm.groupCount.collectAsState().value.toString(),
                onValueChange = { vm.groupCount.value = it.toInt() },
                label = { Text(text = stringResource(id = R.string.label_groups)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = vm.reviewCount.collectAsState().value.toString(),
                onValueChange = { vm.reviewCount.value = it.toInt() },
                label = { Text(text = stringResource(id = R.string.label_review)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
