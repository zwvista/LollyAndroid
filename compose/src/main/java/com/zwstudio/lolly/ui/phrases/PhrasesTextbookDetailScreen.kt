package com.zwstudio.lolly.ui.phrases

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel

@Composable
fun PhrasesTextbookDetailScreen(vm: PhrasesUnitViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstPhrases[index]
    val vmDetail = PhrasesUnitDetailViewModel(item)
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "ID:${vmDetail.id}")
            Text(text = "ID:${vmDetail.textbookname}")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "UNIT:")
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text2))
                        .fillMaxWidth(),
                    itemsStateFlow = vmSettings.lstUnits_,
                    selectedItemIndexStateFlow = vmDetail.unitIndex,
                    itemText = { it.label },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "PART:")
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text3))
                        .fillMaxWidth(),
                    itemsStateFlow = vmSettings.lstParts_,
                    selectedItemIndexStateFlow = vmDetail.partIndex,
                    itemText = { it.label },
                )
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = vmDetail.seqnum.collectAsState().value,
                onValueChange = { vmDetail.seqnum.value = it }
            )
            Text(text = "PHRASEID:${vmDetail.phraseid}")
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = vmDetail.phrase.collectAsState().value,
                onValueChange = { vmDetail.phrase.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = vmDetail.translation.collectAsState().value,
                onValueChange = { vmDetail.translation.value = it }
            )
        }
    }
}
