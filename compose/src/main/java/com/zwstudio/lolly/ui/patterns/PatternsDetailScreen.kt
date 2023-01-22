package com.zwstudio.lolly.ui.patterns

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
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PatternsDetailScreen(vm: PatternsViewModel, item: MPattern, navController: NavHostController?) {

    val vmDetail = getViewModel<PatternsDetailViewModel> { parametersOf(item) }
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
                label = { Text(stringResource(id = R.string.label_pattern)) },
                value = vmDetail.pattern.collectAsState().value,
                onValueChange = { vmDetail.pattern.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_note)) },
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_tags)) },
                value = vmDetail.tags.collectAsState().value,
                onValueChange = { vmDetail.tags.value = it }
            )
        }
    }
}
