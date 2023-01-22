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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WordsUnitDetailScreen(vm: WordsUnitViewModel, item: MUnitWord, navController: NavHostController?) {

    val vmDetail = getViewModel<WordsUnitDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.words_unit_detail),
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
                    },
                    enabled = vmDetail.saveEnabled.collectAsState().value
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(id = R.string.label_id, vmDetail.id))
            Text(text = stringResource(id = R.string.label_unit))
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vmSettings.lstUnits_,
                selectedItemIndexStateFlow = vmDetail.unitIndex,
                itemText = { it.label },
            )
            Text(text = stringResource(id = R.string.label_part))
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vmSettings.lstParts_,
                selectedItemIndexStateFlow = vmDetail.partIndex,
                itemText = { it.label },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_seqnum)) },
                value = vmDetail.seqnum.collectAsState().value,
                onValueChange = { vmDetail.seqnum.value = it }
            )
            Text(text = stringResource(id = R.string.label_wordid, vmDetail.wordid))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_word)) },
                value = vmDetail.word.collectAsState().value,
                onValueChange = { vmDetail.word.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_note)) },
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            Text(text = stringResource(id = R.string.label_famiid, vmDetail.famiid))
            Text(text = "ACCURACY:${vmDetail.accuracy}")
        }
    }
}
