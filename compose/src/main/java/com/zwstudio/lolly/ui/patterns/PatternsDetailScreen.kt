package com.zwstudio.lolly.ui.patterns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel

@Composable
fun PatternsDetailScreen(vm: PatternsViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstPatterns[index]
    val vmDetail = PatternsDetailViewModel(item)
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
            actions = {
                Button(
                    onClick = {
                        navController?.navigateUp()
                    }
                ) {
                    Icon(Icons.Filled.Done, null)
                }
            }
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID:${vmDetail.id}")
            TextField(
                value = vmDetail.pattern.collectAsState().value,
                onValueChange = { vmDetail.pattern.value = it }
            )
            TextField(
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            TextField(
                value = vmDetail.tags.collectAsState().value,
                onValueChange = { vmDetail.tags.value = it }
            )
        }
    }
}
