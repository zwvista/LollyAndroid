package com.zwstudio.lolly.ui.phrases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.LabelledCheckBox
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitBatchEditViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PhrasesUnitBatchEditScreen(vm: PhrasesUnitViewModel, navController: NavHostController?) {

    val vmBatchEdit = koinViewModel<PhrasesUnitBatchEditViewModel> { parametersOf(vm) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.phrases_unit_batch_edit),
            navController = navController,
            actions = {
                Button(
                    onClick = {
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
    }
}
