package com.zwstudio.lolly.compose.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.compose.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsLangDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WordsLangDetailScreen(vm: WordsLangViewModel, item: MLangWord, navController: NavHostController?) {

    val vmDetail = koinViewModel<WordsLangDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.words_lang_detail),
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.label_id, vmDetail.id))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("WORD") },
                value = vmDetail.word.collectAsState().value,
                onValueChange = { vmDetail.word.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("NOTE") },
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            Text(text = "FAMIID:${vmDetail.famiid}")
            Text(text = "ACCURACY:${vmDetail.accuracy}")
        }
    }
}
