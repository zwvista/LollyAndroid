package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsUnitViewModel

@Composable
fun WordsUnitDetailScreen(vm: WordsUnitViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstWords[index]
    val vmDetail = WordsUnitDetailViewModel(item)
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController
        )
        Column() {
            TextField(
                value = vmDetail.word.collectAsState().value,
                onValueChange = { vmDetail.word.value = it }
            )
            TextField(
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
        }
    }
}
