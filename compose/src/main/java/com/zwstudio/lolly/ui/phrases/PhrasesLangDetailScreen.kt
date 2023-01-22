package com.zwstudio.lolly.ui.phrases

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PhrasesLangDetailScreen(vm: PhrasesLangViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstPhrases[index]
    val vmDetail = getViewModel<PhrasesLangDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.phrases_lang_detail),
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
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_phrase)) },
                value = vmDetail.phrase.collectAsState().value,
                onValueChange = { vmDetail.phrase.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_translation)) },
                value = vmDetail.translation.collectAsState().value,
                onValueChange = { vmDetail.translation.value = it }
            )
        }
    }
}
