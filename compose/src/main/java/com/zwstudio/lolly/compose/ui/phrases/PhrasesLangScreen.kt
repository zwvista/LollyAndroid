package com.zwstudio.lolly.compose.ui.phrases

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.PhrasesScreens
import com.zwstudio.lolly.compose.ui.common.SearchView
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhrasesLangScreen(vm: PhrasesLangViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPhrases = vm.lstPhrases_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.PhrasesLang.title,
            onButtonClicked = { openDrawer() },
            actions = {
                IconButton(onClick = {
                    navController?.navigate(PhrasesScreens.PhrasesLangAdd.route)
                }) {
                    Icon(Icons.Filled.Add, null)
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
        if (vm.isBusy) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                itemsIndexed(lstPhrases, key = { _, item -> item.id }) { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { speak(item.phrase) },
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
        }
    }

    if (showItemDialog) {
        val item = lstPhrases[currentItemIndex]
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
                    navController?.navigate(PhrasesScreens.PhrasesLangDetail.route + "/$currentItemIndex")
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
