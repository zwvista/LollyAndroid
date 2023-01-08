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
import com.zwstudio.lolly.viewmodels.phrases.PhrasesUnitViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PhrasesTextbookScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<PhrasesUnitViewModel>()

    LaunchedEffect(Unit, block = {
        vm.getDataInLang()
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
                        Text(text = item.unitstr)
                        Text(text = item.partstr)
                        Text(text = "${item.seqnum}")
                    }
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
fun PhrasesTextbookScreenPreview() {
    LollyAndroidTheme {
        PhrasesTextbookScreen() {}
    }
}
