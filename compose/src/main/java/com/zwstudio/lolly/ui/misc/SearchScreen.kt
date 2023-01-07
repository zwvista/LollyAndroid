package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<SearchViewModel>()
    LaunchedEffect(Unit, block = {
        vmSettings.getData()
    })

    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Search") },
            navigationIcon = {
                IconButton(onClick = { openDrawer() } ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            },
            actions = {
                IconButton(onClick = {
                    GlobalUserViewModel.remove(context)
                }) {
                    Icon(Icons.Filled.ExitToApp, null)
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        Column(
            modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                Spinner(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    dropDownModifier = Modifier.wrapContentSize(),
                    items = vmSettings.lstLanguages_.collectAsState().value,
                    selectedItem = vmSettings.selectedLang,
                    onItemSelected = {},
                    selectedItemFactory = { modifier, item ->
                        Row(
                            modifier = modifier
                                .padding(8.dp)
                                .wrapContentSize()
                        ) {
                            Text(item.langname)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                contentDescription ="drop down arrow"
                            )
                        }
                    },
                    dropdownItemFactory = { item, _ ->
                        Text(text = item.langname)
                    }
                )
                Spinner(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    dropDownModifier = Modifier.wrapContentSize(),
                    items = vmSettings.lstDictsReference_.collectAsState().value,
                    selectedItem = vmSettings.selectedDictReference,
                    onItemSelected = {},
                    selectedItemFactory = { modifier, item ->
                        Row(
                            modifier = modifier
                                .padding(8.dp)
                                .wrapContentSize()
                        ) {
                            Text(item.dictname)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                contentDescription ="drop down arrow"
                            )
                        }
                    },
                    dropdownItemFactory = { item, _ ->
                        Text(text = item.dictname)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    LollyAndroidTheme {
        SearchScreen() {}
    }
}
