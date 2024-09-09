package com.zwstudio.lolly.compose.ui.words

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.LabelledCheckBox
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsUnitBatchEditViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WordsUnitBatchEditScreen(vm: WordsUnitViewModel, navController: NavHostController?) {

    val vmBatchEdit = koinViewModel<WordsUnitBatchEditViewModel> { parametersOf(vm) }
    val isCheckedList = remember { mutableStateListOf(*Array(vm.lstWords.size) { false }) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.words_unit_batch_edit),
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        isCheckedList.forEachIndexed { index, item -> vm.lstWords[index].isChecked = item }
                        vmBatchEdit.save()
                    },
                    enabled = vmBatchEdit.saveEnabled.collectAsState().value
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LabelledCheckBox(
                    checked = vmBatchEdit.unitChecked.collectAsState().value,
                    onCheckedChange = { vmBatchEdit.unitChecked.value = it },
                    label = stringResource(id = R.string.label_unit)
                )
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text2))
                        .fillMaxWidth(),
                    enabled = vmBatchEdit.unitChecked.collectAsState().value,
                    itemsStateFlow = vmSettings.lstUnits_,
                    selectedItemIndexStateFlow = vmBatchEdit.unitIndex,
                    itemText = { it.label },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                LabelledCheckBox(
                    checked = vmBatchEdit.partChecked.collectAsState().value,
                    onCheckedChange = { vmBatchEdit.partChecked.value = it },
                    label = stringResource(id = R.string.label_part)
                )
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text3))
                        .fillMaxWidth(),
                    enabled = vmBatchEdit.partChecked.collectAsState().value,
                    itemsStateFlow = vmSettings.lstParts_,
                    selectedItemIndexStateFlow = vmBatchEdit.partIndex,
                    itemText = { it.label },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                LabelledCheckBox(
                    checked = vmBatchEdit.seqnumChecked.collectAsState().value,
                    onCheckedChange = { vmBatchEdit.seqnumChecked.value = it },
                    label = stringResource(id = R.string.label_seqnum_add)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = vmBatchEdit.seqnumChecked.collectAsState().value,
                    value = vmBatchEdit.seqnum.collectAsState().value,
                    onValueChange = { vmBatchEdit.seqnum.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    itemsIndexed(vm.lstWords, key = { _, item -> item.id }) { index, item ->
                        Card(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    isCheckedList[index] = !isCheckedList[index]
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                Column(modifier = Modifier.weight(1f)) {
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
                                if (isCheckedList[index]) {
                                    Icon(
                                        Icons.Filled.Check,
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
}
