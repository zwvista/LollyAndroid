package com.zwstudio.lolly.compose.ui.onlinetextbooks

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnlineTextbooksScreen(vm: OnlineTextbooksViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstOnlineTextbooks = vm.lstOnlineTextbooks_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun onRefresh() = coroutineScope.launch {
        vm.getData()
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = stringResource(id = DrawerScreens.OnlineTextbooks.titleRes),
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
                onRefresh = { onRefresh() },
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    itemsIndexed(lstOnlineTextbooks, key = { _, item -> item.id }) { index, item ->
                        Card(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { speak(item.textbookname) },
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

    if (showItemDialog) {
        val item = lstOnlineTextbooks[selectedItemIndex]
        BasicAlertDialog(
            onDismissRequest = { showItemDialog = false },
        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.title)
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(onClick = {
                        showItemDialog = false
                        navController?.navigate(OnlineTextbooksScreens.OnlineTextbooksDetail.route + "/$selectedItemIndex")
                    }) {
                        Text(stringResource(id = R.string.action_edit))
                    }
                    TextButton(onClick = {
                        showItemDialog = false
                        navController?.navigate(OnlineTextbooksScreens.OnlineTextbooksWebPage.route + "/$selectedItemIndex")
                    }) {
                        Text(stringResource(id = R.string.action_browse_web_page))
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
