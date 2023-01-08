package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme

@Composable
fun SettingsScreen(openDrawer: () -> Unit) {

    val vm = remember { vmSettings }

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = { openDrawer() } ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {
            Text(text = "Language:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstLanguages_,
                selectedItemIndexStateFlow = vm.selectedLangIndex_,
                selectedItemText = { vm.selectedLang.langname },
                dropdownItemText = { it.langname }
            )
            Text(text = "Voice:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstVoices_,
                selectedItemIndexStateFlow = vm.selectedVoiceIndex_,
                selectedItemText = { vm.selectedVoice.voicename },
                dropdownItemText = { it.voicename }
            )
            Text(text = "Dictionary(Reference):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsReference_,
                selectedItemIndexStateFlow = vm.selectedDictReferenceIndex_,
                selectedItemText = { vm.selectedDictReference.dictname },
                dropdownItemText = { it.dictname }
            )
            Text(text = "Dictionary(Note):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsNote_,
                selectedItemIndexStateFlow = vm.selectedDictNoteIndex_,
                selectedItemText = { vm.selectedDictNote.dictname },
                dropdownItemText = { it.dictname }
            )
            Text(text = "Dictionary(Translation):")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstDictsTranslation_,
                selectedItemIndexStateFlow = vm.selectedDictTranslationIndex_,
                selectedItemText = { vm.selectedDictTranslation.dictname },
                dropdownItemText = { it.dictname }
            )
            Text(text = "Textbook:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstTextbooks_,
                selectedItemIndexStateFlow = vm.selectedTextbookIndex_,
                selectedItemText = { vm.selectedTextbook.textbookname },
                dropdownItemText = { it.textbookname }
            )
            Text(text = "Units:")
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstUnits_,
                selectedItemIndexStateFlow = vm.selectedUnitFromIndex_,
                selectedItemText = { vm.selectedTextbook.textbookname },
                dropdownItemText = { it.label }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstParts_,
                selectedItemIndexStateFlow = vm.selectedPartFromIndex_,
                selectedItemText = { vm.selectedTextbook.textbookname },
                dropdownItemText = { it.label }
            )
            Row() {
                Spinner(
                    itemsStateFlow = vm.lstUnits_,
                    selectedItemIndexStateFlow = vm.selectedUnitToIndex_,
                    selectedItemText = { vm.selectedTextbook.textbookname },
                    dropdownItemText = { it.label }
                )
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Previous")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Next")
                }
            }
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstUnits_,
                selectedItemIndexStateFlow = vm.selectedUnitToIndex_,
                selectedItemText = { vm.selectedTextbook.textbookname },
                dropdownItemText = { it.label }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth(),
                itemsStateFlow = vm.lstParts_,
                selectedItemIndexStateFlow = vm.selectedPartToIndex_,
                selectedItemText = { vm.selectedTextbook.textbookname },
                dropdownItemText = { it.label }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    LollyAndroidTheme {
        SettingsScreen() {}
    }
}
