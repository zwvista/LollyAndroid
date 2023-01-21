package com.zwstudio.lolly.ui.patterns

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.common.*
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PatternsScreen(vm: PatternsViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstPatterns = vm.lstPatterns_.collectAsState().value
    var showItemDialog by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf<MPattern?>(null) }

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.Patterns.title,
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
                items = SettingsViewModel.lstScopePatternFilters,
                selectedItemIndexStateFlow = vm.scopeFilterIndex_,
                itemText = { it.label }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(lstPatterns, key = { _, item -> item.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { navController?.navigate(PatternsHosts.PatternsDetail.route + "/$index") },
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
//                                navController?.navigate(PatternsHosts.PatternsWebPagesBrowse.route + "/$index")
                                navController?.navigate(PatternsHosts.PatternsWebPagesList.route + "/$index")
                            }
                        ) {
                            Icon(Icons.Filled.Info, null, tint = MaterialTheme.colors.primary)
                        }
                    }
                }
            }
        }
        if (showItemDialog) {
            val item = currentItem!!
            AlertDialog(
                onDismissRequest = { showItemDialog = false },
                title = { Text(text = item.pattern) },
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
