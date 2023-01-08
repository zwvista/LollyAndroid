package com.zwstudio.lolly.ui.patterns

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
import com.zwstudio.lolly.viewmodels.patterns.PatternsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PatternsScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<PatternsViewModel>()

    LaunchedEffect(Unit, block = {
        vm.getData()
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Patterns",
            onButtonClicked = { openDrawer() }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()) {
            items(vm.lstPatterns) { item ->
                Row() {
                    Column() {
                        Text(text = item.pattern)
                        Text(text = item.tags)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatternsScreenPreview() {
    LollyAndroidTheme {
        PatternsScreen() {}
    }
}
