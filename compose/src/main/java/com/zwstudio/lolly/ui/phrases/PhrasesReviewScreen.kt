package com.zwstudio.lolly.ui.phrases

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.LabelledCheckBox
import com.zwstudio.lolly.ui.common.ReviewScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.phrases.PhrasesReviewViewModel

@Composable
fun PhrasesReviewScreen(vm: PhrasesReviewViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    LaunchedEffect(Unit, block = {
        if (vm.showOptions) {
            vm.showOptions = false
            navController?.navigate(ReviewScreens.ReviewOptions.route)
        }
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesReview.title,
            onButtonClicked = { openDrawer() }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vm.indexString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.indexVisible.collectAsState().value) 1f else 0f
                    )
                )
                Text(text = "")
                Box {
                    Text(
                        text = stringResource(id = R.string.text_correct),
                        modifier = Modifier.alpha(
                            if (vm.correctVisible.collectAsState().value) 1f else 0f
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.text_incorrect),
                        modifier = Modifier.alpha(
                            if (vm.incorrectVisible.collectAsState().value) 1f else 0f
                        )
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.speak))
                }
                LabelledCheckBox(
                    checked = vm.isSpeaking.collectAsState().value,
                    onCheckedChange = { vm.isSpeaking.value = it },
                    label = stringResource(id = R.string.text_speak)
                )
                Button(
                    onClick = { /*TODO*/ },
                    enabled = vm.checkNextEnabled.collectAsState().value
                ) {
                    Text(text = vm.checkNextString.collectAsState().value)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                LabelledCheckBox(
                    checked = vm.onRepeat.collectAsState().value,
                    onCheckedChange = { vm.onRepeat.value = it },
                    label = stringResource(id = R.string.text_on_repeat)
                )
                LabelledCheckBox(
                    checked = vm.moveForward.collectAsState().value,
                    onCheckedChange = { vm.moveForward.value = it },
                    label = stringResource(id = R.string.text_move_forward)
                )
                Button(
                    onClick = { /*TODO*/ },
                    enabled = vm.checkPrevEnabled.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.checkPrevVisible.collectAsState().value) 1f else 0f
                    )
                ) {
                    Text(text = vm.checkPrevString.collectAsState().value)
                }
            }
            Text(
                text = vm.phraseTargetString.collectAsState().value,
                modifier = Modifier.alpha(
                    if (vm.phraseTargetVisible.collectAsState().value) 1f else 0f
                )
            )
            Text(
                text = vm.translationString.collectAsState().value,
            )
            TextField(
                value = vm.phraseInputString.collectAsState().value,
                onValueChange = { vm.phraseInputString.value = it }
            )
        }
    }
}
