package com.zwstudio.lolly.ui.patterns

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
import com.zwstudio.lolly.common.copyText
import com.zwstudio.lolly.common.googleString
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.PatternsScreens
import com.zwstudio.lolly.ui.common.SearchView
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PatternsScreen(vm: PatternsViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPatterns = vm.lstPatterns_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.Patterns.title,
            onButtonClicked = { openDrawer() },
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
                items = SettingsViewModel.lstScopePatternFilters,
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
                itemsIndexed(lstPatterns, key = { _, item -> item.id }) { index, item ->
                    Card(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { speak(item.pattern) },
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
                                    text = item.pattern,
                                    color = colorResource(R.color.color_text2)
                                )
                                Text(
                                    text = item.tags,
                                    color = colorResource(R.color.color_text3)
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController?.navigate(PatternsScreens.PatternsWebPage.route + "/$index")
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
        val item = lstPatterns[currentItemIndex]
        AlertDialog(
            onDismissRequest = { showItemDialog = false },
            title = { Text(text = item.pattern) },
            buttons = {
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(PatternsScreens.PatternsDetail.route + "/$currentItemIndex")
                }) {
                    Text(stringResource(id = R.string.action_edit))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    navController?.navigate(PatternsScreens.PatternsWebPage.route + "/$currentItemIndex")
                }) {
                    Text(stringResource(id = R.string.action_browse_web_page))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    copyText(context, item.pattern)
                }) {
                    Text(stringResource(id = R.string.action_copy_pattern))
                }
                TextButton(onClick = {
                    showItemDialog = false
                    googleString(context, item.pattern)
                }) {
                    Text(stringResource(id = R.string.action_google_pattern))
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
