package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.SearchView
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsTextbookScreen(vm: WordsUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getDataInLang()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsTextbook.title,
            onButtonClicked = { openDrawer() }
        )
        SearchView(
            valueStateFlow = vm.textFilter_
        ) {
        }
        Row {
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .weight(1f),
                itemsStateFlow = vmSettings.lstTextbookFilters_,
                selectedItemIndexStateFlow = vm.textbookFilterIndex_,
                itemText = { it.label }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .weight(1f),
                items = SettingsViewModel.lstScopeWordFilters,
                selectedItemIndexStateFlow = vm.scopeFilterIndex_,
                itemText = { it.label }
            )
        }
        if (vm.isBusy) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                itemsIndexed(lstWords, key = { _, item -> item.id }) { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { speak(item.word) },
                                onLongClick = {
                                    currentItemIndex = index
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
                            Column {
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
                        }
                    }
                }
            }
        }
    }

    if (showItemDialog) {
        val item = lstWords[currentItemIndex]
        AlertDialog(
            onDismissRequest = { showItemDialog = false },
            title = { Text(text = item.word) },
            buttons = {
                TextButton(onClick = {
                    showItemDialog = false
                }) {
                    Text(stringResource(id = R.string.action_delete))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(WordsScreens.WordsTextbookDetail.route + "/$currentItemIndex")
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
                    val url = vmSettings.selectedDictReference.urlString(item.word, vmSettings.lstAutoCorrect)
                    openPage(context, url)
                }) {
                    Text(stringResource(id = R.string.action_online_dict))
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
