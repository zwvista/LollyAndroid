package com.zwstudio.lolly.ui.phrases

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhrasesTextbookScreen(vm: PhrasesUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPhrases = vm.lstPhrases_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf<MUnitPhrase?>(null) }

    LaunchedEffect(Unit, block = {
        vm.getDataInLang()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesTextbook.title,
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
                items = SettingsViewModel.lstScopePhraseFilters,
                selectedItemIndexStateFlow = vm.scopeFilterIndex_,
                itemText = { it.label }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(lstPhrases, key = { _, item -> item.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { navController?.navigate(PhrasesScreens.PhrasesTextbookDetail.route + "/$index") },
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
                                text = item.phrase,
                                color = colorResource(R.color.color_text2)
                            )
                            Text(
                                text = item.translation,
                                color = colorResource(R.color.color_text3)
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
                    }) {
                        Text(stringResource(id = R.string.action_copy_phrase))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
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
