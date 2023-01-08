package com.zwstudio.lolly.ui.phrases

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
import com.zwstudio.lolly.viewmodels.phrases.PhrasesLangViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PhrasesLangScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<PhrasesLangViewModel>()

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Phrases in Unit",
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()) {
            items(vm.lstPhrases) { item ->
                Row() {
                    Column() {
                        Text(text = item.phrase)
                        Text(text = item.translation)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhrasesLangScreenPreview() {
    LollyAndroidTheme {
        PhrasesLangScreen() {}
    }
}
