package com.zwstudio.lolly.ui.phrases

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhrasesUnitScreen(vm: PhrasesUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPhrases = vm.lstPhrases_.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    val state = rememberReorderableLazyListState(onMove = { _,_ -> }, canDragOver = { _,_ -> true })
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf<MUnitPhrase?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getDataInTextbook()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesUnit.title,
            onButtonClicked = { openDrawer() },
            actions = {
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, null, tint = MaterialTheme.colors.surface)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                vm.isEditMode = false
                                expanded = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.normal_mode))
                            if (!vm.isEditMode_.collectAsState().value) {
                                Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colors.primary)
                            }
                        }
                        DropdownMenuItem(
                            onClick = {
                                vm.isEditMode = true
                                expanded = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.edit_mode))
                            if (vm.isEditMode_.collectAsState().value) {
                                Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colors.primary)
                            }
                        }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = stringResource(id = R.string.action_add)) }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = stringResource(id = R.string.action_batch)) }
                    }
                }
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            SearchView(
                valueStateFlow = vm.textFilter_,
                modifier = Modifier.weight(1f)
            ) {
            }
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2)),
                items = SettingsViewModel.lstScopePhraseFilters,
                selectedItemIndexStateFlow = vm.scopeFilterIndex_,
                itemText = { it.label }
            )
        }
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .reorderable(state)
        ) {
            itemsIndexed(lstPhrases, key = { _, item -> item.id }) { index, item ->
                ReorderableItem(state, item.id) { dragging ->
                    Card(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    if (vm.isEditMode)
                                        navController?.navigate(PhrasesScreens.PhrasesUnitDetail.route + "/$index")
                                    else
                                        speak(item.phrase)
                                },
                                onLongClick = {
                                    currentItem = item
                                    showItemDialog = true
                                },
                            ),
                        elevation = 8.dp,
                        backgroundColor = Color.White,
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides TextStyle(fontSize = 11.sp),
                                LocalContentColor provides colorResource(R.color.color_text1)
                            ) {
                                Column(modifier = Modifier.padding(end = 16.dp)) {
                                    Text(text = item.unitstr)
                                    Text(text = item.partstr)
                                    Text(text = "${item.seqnum}")
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.phrase,
                                    color = colorResource(R.color.color_text2)
                                )
                                Text(
                                    text = item.translation,
                                    color = colorResource(R.color.color_text3)
                                )
                            }
                            if (vm.isEditMode_.collectAsState().value) {
                                Icon(
                                    Icons.Filled.Menu,
                                    null,
                                    modifier = Modifier.detectReorderAfterLongPress(state),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showItemDialog) {
            val item = currentItem!!
            AlertDialog(
                onDismissRequest = { showItemDialog = false },
                title = { Text(text = item.phrase) },
                buttons = {
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_delete))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_edit))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        copyText(context, item.phrase)
                    }) {
                        Text(stringResource(id = R.string.action_copy_phrase))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        googleString(context, item.phrase)
                    }) {
                        Text(stringResource(id = R.string.action_google_phrase))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_cancel))
                    }
                },
            )
        }
    }
}
