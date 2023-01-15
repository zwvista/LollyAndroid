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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PatternsWebPagesDetailScreen(vm: PatternsWebPagesViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstWebPages[index]
    val vmDetail = getViewModel<PatternsWebPagesDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        vmDetail.save()
                        if (item.id == 0)
                            vm.createWebPage(item)
                        else
                            vm.updateWebPage(item)
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
            Text(text = "ID:${vmDetail.id}")
            Text(text = "PATTERNID:${vmDetail.patternid}")
            Text(text = "PATTERN:${vmDetail.pattern}")
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("SEQNUM") },
                value = vmDetail.seqnum.collectAsState().value,
                onValueChange = { vmDetail.seqnum.value = it }
            )
            Text(text = "WEBPAGEID:${vmDetail.webpageid}")
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("NOTE") },
                value = vmDetail.title.collectAsState().value,
                onValueChange = { vmDetail.title.value = it }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("TAGS") },
                value = vmDetail.url.collectAsState().value,
                onValueChange = { vmDetail.url.value = it }
            )
        }
    }
}
