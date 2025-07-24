package com.zwstudio.lolly.compose.ui.words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.openPage
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.ReorderableItemCustom
import com.zwstudio.lolly.compose.ui.common.SearchView
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.compose.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WordsUnitScreen(vm: WordsUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value
    val state = rememberReorderableLazyListState(onMove = { _,_ -> }, canDragOver = { _,_ -> true })
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    suspend fun onRefresh() = vm.getDataInTextbook()

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = stringResource(id = DrawerScreens.WordsUnit.titleRes),
            onButtonClicked = { openDrawer() },
            actions = { WordsUnitActions(vm, navController) }
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
                items = SettingsViewModel.lstScopeWordFilters,
                selectedItemIndexStateFlow = vm.scopeFilterIndex_,
                itemText = { it.label }
            )
        }
        if (vm.isBusy_.collectAsState().value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PullToRefreshBox(
                isRefreshing = vm.isBusy_.collectAsState().value,
                onRefresh = { vm.viewModelScope.launch { onRefresh() } },
            ) {
                LazyColumn(
                    state = state.listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .reorderable(state)
                ) {
                    itemsIndexed(lstWords, key = { _, item -> item.id }) { index, item ->
                        ReorderableItemCustom(state, item.id) { dragging ->
                            Card(
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = {
                                            if (vm.isEditMode)
                                                navController?.navigate(WordsScreens.WordsUnitDetail.route + "/$index")
                                            else
                                                speak(item.word)
                                        },
                                        onLongClick = {
                                            selectedItemIndex = index
                                            showItemDialog = true
                                        },
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                            text = item.word,
                                            color = colorResource(R.color.color_text2),
                                            style = TextStyle(fontSize = 25.sp)
                                        )
                                        Text(
                                            text = item.note,
                                            color = colorResource(R.color.color_text3),
                                            style = TextStyle(fontSize = 20.sp)
                                        )
                                    }
                                    if (vm.isEditMode_.collectAsState().value) {
                                        Icon(
                                            Icons.Filled.Menu,
                                            null,
                                            modifier = Modifier.detectReorderAfterLongPress(state),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        IconButton(
                                            onClick = {
                                                navController?.navigate(WordsScreens.WordsDict.route + "/$index")
                                            }
                                        ) {
                                            Icon(
                                                Icons.Filled.Info,
                                                null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showItemDialog) {
        val item = lstWords[selectedItemIndex]
        BasicAlertDialog(
            onDismissRequest = { showItemDialog = false }
        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.word)
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_delete))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        navController?.navigate(WordsScreens.WordsUnitDetail.route + "/$selectedItemIndex")
                    }) {
                        Text(stringResource(id = R.string.action_edit))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        vm.viewModelScope.launch {
                            vm.getNote(item)
                        }
                    }) {
                        Text(stringResource(id = R.string.action_get_note))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        vm.viewModelScope.launch {
                            vm.clearNote(item)
                        }
                    }) {
                        Text(stringResource(id = R.string.action_clear_note))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        copyText(context, item.word)
                    }) {
                        Text(stringResource(id = R.string.action_copy_word))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        googleString(context, item.word)
                    }) {
                        Text(stringResource(id = R.string.action_google_word))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        val url = vmSettings.selectedDictReference.urlString(
                            item.word,
                            vmSettings.lstAutoCorrect
                        )
                        openPage(context, url)
                    }) {
                        Text(stringResource(id = R.string.action_online_dict))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_cancel))
                    }
                }
            }
        }
    }
}

@Composable
fun WordsUnitActions(vm: WordsUnitViewModel, navController: NavHostController?) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Filled.MoreVert, null, tint = MaterialTheme.colorScheme.primary)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    vm.isEditMode = false
                },
                text = { Text(text = stringResource(id = R.string.normal_mode)) },
                trailingIcon = {
                    if (!vm.isEditMode_.collectAsState().value)
                        Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                }
            )
            DropdownMenuItem(
                onClick = {
                    vm.isEditMode = true
                },
                text = { Text(text = stringResource(id = R.string.edit_mode)) },
                trailingIcon = {
                    if (vm.isEditMode_.collectAsState().value)
                        Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                }
            )
            DropdownMenuItem(
                onClick = {
                    navController?.navigate(WordsScreens.WordsUnitAdd.route)
                },
                text = { Text(text = stringResource(id = R.string.action_add)) }
            )
            DropdownMenuItem(
                onClick = {
                    vm.getNotes(false, {_ ->}, {})
                },
                text = { Text(text = stringResource(id = R.string.action_get_notes_all)) }
            )
            DropdownMenuItem(
                onClick = {
                    vm.getNotes(true, {_ ->}, {})
                    expanded = false
                },
                text = { Text(text = stringResource(id = R.string.action_get_notes_empty)) }
            )
            DropdownMenuItem(
                onClick = {
                    vm.clearNotes(false, {_ ->}, {})
                    expanded = false
                },
                text = { Text(text = stringResource(id = R.string.action_clear_notes_all)) }
            )
            DropdownMenuItem(
                onClick = {
                    vm.clearNotes(true, {_ ->}, {})
                    expanded = false
                },
                text = { Text(text = stringResource(id = R.string.action_clear_notes_empty)) }
            )
            DropdownMenuItem(
                onClick = {
                    navController?.navigate(WordsScreens.WordsUnitBatchEdit.route)
                    expanded = false
                },
                text = { Text(text = stringResource(id = R.string.action_batch)) }
            )
        }
    }
}
