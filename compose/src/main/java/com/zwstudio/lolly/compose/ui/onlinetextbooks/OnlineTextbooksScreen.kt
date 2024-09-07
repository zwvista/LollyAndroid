package com.zwstudio.lolly.compose.ui.onlinetextbooks

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.OnlineTextbooksScreens
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnlineTextbooksScreen(vm: OnlineTextbooksViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstOnlineTextbooks = vm.lstOnlineTextbooks_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.OnlineTextbooks.title,
            onButtonClicked = { openDrawer() },
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spinner(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.color_text2)),
                itemsStateFlow = vmSettings.lstOnlineTextbookFilters_,
                selectedItemIndexStateFlow = vm.onlineTextbookFilterIndex_,
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
                itemsIndexed(lstOnlineTextbooks, key = { _, item -> item.id }) { index, item ->
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
                                    navController?.navigate(OnlineTextbooksScreens.OnlineTextbooksWebPage.route + "/$index")
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
        val item = lstOnlineTextbooks[currentItemIndex]
        AlertDialog(
            onDismissRequest = { showItemDialog = false },
            title = { Text(text = item.title) },
            buttons = {
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(OnlineTextbooksScreens.OnlineTextbooksDetail.route + "/$currentItemIndex")
                }) {
                    Text(stringResource(id = R.string.action_edit))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(OnlineTextbooksScreens.OnlineTextbooksWebPage.route + "/$currentItemIndex")
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
