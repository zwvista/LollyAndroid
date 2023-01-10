package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel

@Composable
fun WordsTextbookDetailScreen(vm: WordsUnitViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstWords[index]
    val vmDetail = WordsUnitDetailViewModel(item)
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        vmDetail.save()
                        if (item.id == 0)
                            vm.create(item)
                        else
                            vm.update(item)
                        navController?.navigateUp()
                    }
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID:${vmDetail.id}")
            Text(text = "ID:${vmDetail.textbookname}")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vmSettings.lstUnits_,
                selectedItemIndexStateFlow = vmDetail.unitIndex,
                itemText = { it.label },
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vmSettings.lstParts_,
                selectedItemIndexStateFlow = vmDetail.partIndex,
                itemText = { it.label },
            )
            TextField(
                value = vmDetail.seqnum.collectAsState().value,
                onValueChange = { vmDetail.seqnum.value = it }
            )
            Row() {
                Text(text = "WORDID:")
                Text(text = vmDetail.wordid.toString())
            }
            TextField(
                value = vmDetail.word.collectAsState().value,
                onValueChange = { vmDetail.word.value = it }
            )
            TextField(
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            Row() {
                Text(text = "FAMIID:${vmDetail.famiid}")
            }
            Text(text = "ACCURACY:${vmDetail.accuracy}")
        }
    }
}
