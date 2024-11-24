package com.zwstudio.lolly.compose.ui.words

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.LabelledCheckBox
import com.zwstudio.lolly.compose.ui.common.ReviewScreens
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.words.WordsReviewViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun WordsReviewScreen(vm: WordsReviewViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit, block = {
        if (vm.showOptions) {
            vm.showOptions = false
            navController?.navigate(ReviewScreens.ReviewOptions.route)
        }
        if (vm.optionsDone.value) {
            vm.optionsDone.value = false
            vm.newTest()
        }
        vm.inputFocused.onEach {
            focusRequester.requestFocus()
        }.launchIn(this)
    })
    DisposableEffect(lifecycleOwner) {
        onDispose {
            vm.stopTimer()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsReview.title,
            onButtonClicked = { openDrawer() },
            actions = {
                Button(
                    onClick = { navController?.navigate(ReviewScreens.ReviewOptions.route) }
                ) {
                    Text(text = stringResource(id = R.string.newtest))
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vm.indexString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.indexVisible.collectAsState().value) 1f else 0f
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = vm.accuracyString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.accuracyVisible.collectAsState().value) 1f else 0f
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    Text(
                        text = stringResource(id = R.string.text_correct),
                        modifier = Modifier.alpha(
                            if (vm.correctVisible.collectAsState().value) 1f else 0f
                        ),
                        style = TextStyle(color = Color.Green),
                    )
                    Text(
                        text = stringResource(id = R.string.text_incorrect),
                        modifier = Modifier.alpha(
                            if (vm.incorrectVisible.collectAsState().value) 1f else 0f
                        ),
                        style = TextStyle(color = Color.Red),
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { speak(vm.currentWord) }) {
                    Text(text = stringResource(id = R.string.speak))
                }
                Spacer(modifier = Modifier.weight(1f))
                LabelledCheckBox(
                    checked = vm.isSpeaking.collectAsState().value,
                    onCheckedChange = { vm.isSpeaking.value = it },
                    label = stringResource(id = R.string.text_speak)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { vm.check(true) },
                    enabled = vm.checkNextEnabled.collectAsState().value
                ) {
                    Text(text = stringResource(id = vm.checkNextStringRes.collectAsState().value))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                LabelledCheckBox(
                    checked = vm.onRepeat.collectAsState().value,
                    onCheckedChange = { vm.onRepeat.value = it },
                    label = stringResource(id = R.string.text_on_repeat),
                    modifier = Modifier.alpha(
                        if (vm.onRepeatVisible.collectAsState().value) 1f else 0f
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                LabelledCheckBox(
                    checked = vm.moveForward.collectAsState().value,
                    onCheckedChange = { vm.moveForward.value = it },
                    label = stringResource(id = R.string.text_move_forward),
                    modifier = Modifier.alpha(
                        if (vm.moveForwardVisible.collectAsState().value) 1f else 0f
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { vm.check(false) },
                    enabled = vm.checkPrevEnabled.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.checkPrevVisible.collectAsState().value) 1f else 0f
                    )
                ) {
                    Text(text = stringResource(id = vm.checkPrevStringRes.collectAsState().value))
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vm.wordTargetString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.wordTargetVisible.collectAsState().value) 1f else 0f
                    ),
                    style = TextStyle(color = colorResource(R.color.color_text2), fontSize = 30.sp),
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vm.noteTargetString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.noteTargetVisible.collectAsState().value) 1f else 0f
                    ),
                    style = TextStyle(color = colorResource(R.color.color_text3), fontSize = 20.sp),
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vm.wordHintString.collectAsState().value,
                    modifier = Modifier.alpha(
                        if (vm.wordHintVisible.collectAsState().value) 1f else 0f
                    ),
                    style = TextStyle(fontSize = 20.sp),
                )
            }
            Text(
                text = vm.translationString.collectAsState().value,
            )
            TextField(
                value = vm.wordInputString.collectAsState().value,
                onValueChange = { vm.wordInputString.value = it },
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
        }
    }
}
