package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zwstudio.lolly.ui.common.TopBar
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.words.WordsLangViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun WordsLangScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<WordsLangViewModel>()

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Words in Unit",
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()) {
            items(vm.lstWords) { item ->
                Row() {
                    Column() {
                        Text(text = item.word)
                        Text(text = item.note)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordsLangScreenScreenPreview() {
    LollyAndroidTheme {
        WordsLangScreen() {}
    }
}
