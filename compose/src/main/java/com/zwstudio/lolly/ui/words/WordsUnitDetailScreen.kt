package com.zwstudio.lolly.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme

@Composable
fun WordsUnitDetailScreen(index: Int, navController: NavHostController?) {

    Column(modifier = Modifier.fillMaxSize()) {
    }
}

@Preview(showBackground = true)
@Composable
fun WordsUnitDetailScreenPreview() {
    LollyAndroidTheme {
        WordsUnitDetailScreen(0, null)
    }
}
