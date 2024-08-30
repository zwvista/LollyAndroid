package com.zwstudio.lolly.ui.webtextbooks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WebTextbooksScreen(vm: WebTextbooksViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWebTextbooks = vm.lstWebTextbooks_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WebTextbooks.title,
            onButtonClicked = { openDrawer() },
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spinner(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.color_text2)),
                itemsStateFlow = vmSettings.lstWebTextbookFilters_,
                selectedItemIndexStateFlow = vm.webTextbookFilterIndex_,
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
                itemsIndexed(lstWebTextbooks, key = { _, item -> item.id }) { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { speak(item.textbookname) },
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.textbookname,
                                    color = colorResource(R.color.color_text2)
                                )
                                Text(
                                    text = item.title,
                                    color = colorResource(R.color.color_text3)
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController?.navigate(WebTextbooksScreens.WebTextbooksWebPage.route + "/$index")
                                }
                            ) {
                                Icon(Icons.Filled.Info, null, tint = MaterialTheme.colors.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showItemDialog) {
        val item = lstWebTextbooks[currentItemIndex]
        AlertDialog(
            onDismissRequest = { showItemDialog = false },
            title = { Text(text = item.title) },
            buttons = {
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(WebTextbooksScreens.WebTextbooksDetail.route + "/$currentItemIndex")
                }) {
                    Text(stringResource(id = R.string.action_edit))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(WebTextbooksScreens.WebTextbooksWebPage.route + "/$currentItemIndex")
                }) {
                    Text(stringResource(id = R.string.action_browse_web_page))
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
