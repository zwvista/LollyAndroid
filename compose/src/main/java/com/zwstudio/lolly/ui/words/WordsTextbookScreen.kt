package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsTextbookScreen(vm: WordsUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf<MUnitWord?>(null) }

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
        Row() {
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
                            onClick = { navController?.navigate(WordsScreens.WordsTextbookDetail.route + "/$index") },
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
                        Text("Delete")
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text("Edit")
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text("Retrieve Note")
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text("Clear Note")
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text("Copy Word")
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                    }) {
                        Text("Google Word")
                    }
                },
            )
        }
    }
}
