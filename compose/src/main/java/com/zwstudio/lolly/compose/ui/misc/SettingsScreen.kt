package com.zwstudio.lolly.compose.ui.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.compose.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel

@Composable
fun SettingsScreen(openDrawer: () -> Unit) {

    val vm = vmSettings

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.Settings.title,
            onButtonClicked = { openDrawer() }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text(text = "Language:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstLanguages_,
                selectedItemIndexStateFlow = vm.selectedLangIndex_,
                itemText = { it.langname }
            )
            Text(text = "Voice:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstVoices_,
                selectedItemIndexStateFlow = vm.selectedVoiceIndex_,
                itemText = { it.voicelang }
            )
            Text(text = "Dictionary(Reference):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsReference_,
                selectedItemIndexStateFlow = vm.selectedDictReferenceIndex_,
                itemText = { it.dictname }
            )
            Text(text = "Dictionary(Note):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsNote_,
                selectedItemIndexStateFlow = vm.selectedDictNoteIndex_,
                itemText = { it.dictname }
            )
            Text(text = "Dictionary(Translation):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsTranslation_,
                selectedItemIndexStateFlow = vm.selectedDictTranslationIndex_,
                itemText = { it.dictname }
            )
            Text(text = "Textbook:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstTextbooks_,
                selectedItemIndexStateFlow = vm.selectedTextbookIndex_,
                itemText = { it.textbookname }
            )
            Text(text = "Units:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstUnits_,
                selectedItemIndexStateFlow = vm.selectedUnitFromIndex_,
                itemText = { it.label }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstParts_,
                selectedItemIndexStateFlow = vm.selectedPartFromIndex_,
                itemText = { it.label },
                enabled = vm.partFromEnabled.collectAsState().value
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text2)),
                    items = SettingsViewModel.lstToTypes,
                    selectedItemIndexStateFlow = vm.toTypeIndex_,
                    itemText = { it.label }
                )
                Button(
                    onClick = { vm.previousUnitPart() },
                    enabled = vm.previousEnabled.collectAsState().value
                ) {
                    Text(text = "Previous")
                }
                Button(
                    onClick = { vm.nextUnitPart() },
                    enabled = vm.nextEnabled.collectAsState().value
                ) {
                    Text(text = "Next")
                }
            }
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstUnits_,
                selectedItemIndexStateFlow = vm.selectedUnitToIndex_,
                itemText = { it.label },
                enabled = vm.unitToEnabled.collectAsState().value
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstParts_,
                selectedItemIndexStateFlow = vm.selectedPartToIndex_,
                itemText = { it.label },
                enabled = vm.partToEnabled.collectAsState().value
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    LollyAndroidTheme {
        SettingsScreen {}
    }
}
