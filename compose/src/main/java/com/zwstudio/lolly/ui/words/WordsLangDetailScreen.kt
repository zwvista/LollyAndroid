package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.zwstudio.lolly.viewmodels.words.WordsLangDetailViewModel
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel

@Composable
fun WordsLangDetailScreen(vm: WordsLangViewModel, index: Int, navController: NavHostController?) {

    val item = vm.lstWords[index]
    val vmDetail = WordsLangDetailViewModel(item)
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
            Row() {
                Text(text = "ID:")
                Text(text = vmDetail.id.toString())
            }
            TextField(
                value = vmDetail.word.collectAsState().value,
                onValueChange = { vmDetail.word.value = it }
            )
            TextField(
                value = vmDetail.note.collectAsState().value,
                onValueChange = { vmDetail.note.value = it }
            )
            Row() {
                Text(text = "FAMIID:${vmDetail.famiid}")
            }
            Text(text = "ACCURACY:${vmDetail.accuracy}")
        }
    }
}
