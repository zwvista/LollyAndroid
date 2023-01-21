package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsLangScreen(vm: WordsLangViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf<MLangWord?>(null) }

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsLang.title,
            onButtonClicked = { openDrawer() }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(lstWords, key = { _, item -> item.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { navController?.navigate(WordsScreens.WordsLangDetail.route + "/$index") },
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
        if (showItemDialog) {
            val item = currentItem!!
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
                    }) {
                        Text(stringResource(id = R.string.action_edit))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_retrieve_note))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_clear_note))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_copy_word))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text(stringResource(id = R.string.action_google_word))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
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
}
