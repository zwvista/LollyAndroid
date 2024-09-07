package com.zwstudio.lolly.compose.ui.patterns

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.compose.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PatternsDetailScreen(item: MPattern, navController: NavHostController?) {

    val vmDetail = koinViewModel<PatternsDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        navController?.navigateUp()
                    },
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(id = R.string.label_id, vmDetail.item.id))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_pattern)) },
                value = vmDetail.item.pattern,
                onValueChange = {}
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_tags)) },
                value = vmDetail.item.tags,
                onValueChange = {}
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_title)) },
                value = vmDetail.item.title,
                onValueChange = {}
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_url)) },
                value = vmDetail.item.url,
                onValueChange = {}
            )
        }
    }
}
