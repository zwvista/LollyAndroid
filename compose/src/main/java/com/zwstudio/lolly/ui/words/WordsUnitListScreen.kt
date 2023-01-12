package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
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
import com.zwstudio.lolly.common.speak
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.ui.common.WordsScreens
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel

@Composable
fun WordsUnitListScreen(vm: WordsUnitViewModel, navController: NavHostController?, openDrawer: () -> Unit) {

    val lstWords = vm.lstWords_.collectAsState().value
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, block = {
        vm.getDataInTextbook()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.WordsUnit.title,
            onButtonClicked = { openDrawer() },
            actions = {
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, null, tint = MaterialTheme.colors.surface)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                vm.isEditMode = false
                                expanded = false
                            }
                        ) {
                            Text(text = "Normal Mode")
                            Spacer(Modifier.weight(1f))
                            if (!vm.isEditMode_.collectAsState().value) {
                                Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colors.primary)
                            }
                        }
                        DropdownMenuItem(
                            onClick = {
                                vm.isEditMode = true
                                expanded = false
                            }
                        ) {
                            Text(text = "Edit Mode")
                            Spacer(Modifier.weight(1f))
                            if (vm.isEditMode_.collectAsState().value) {
                                Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colors.primary)
                            }
                        }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = "Add") }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = "Retrieve All Notes") }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = "Retrieve Notes If Empty") }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = "Clear All Notes") }
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            }
                        ) { Text(text = "Clear Notes If Empty") }
                    }
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(lstWords) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (vm.isEditMode)
                                navController?.navigate(WordsScreens.WordsUnitDetail.route + "/$index")
                            else
                                speak(item.word)
                        },
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
                        if (!vm.isEditMode_.collectAsState().value) {
                            Spacer(Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    navController?.navigate(WordsScreens.WordsDict.route + "/$index")
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
}
