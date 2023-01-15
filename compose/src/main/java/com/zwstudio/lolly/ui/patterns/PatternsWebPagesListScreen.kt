package com.zwstudio.lolly.ui.patterns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.common.PatternsScreens
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel

@Composable
fun PatternsWebPagesListScreen(vm: PatternsWebPagesViewModel, item: MPattern,  navController: NavHostController?) {

    LaunchedEffect(Unit, block = {
        vm.getWebPages(item.id)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(vm.lstWebPages, key = { _, item -> item.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable { navController?.navigate(PatternsScreens.PatternsDetail.route + "/$index") },
                    elevation = 8.dp,
                    backgroundColor = Color.White,
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = item.title,
                                color = colorResource(R.color.color_text2)
                            )
                            Text(
                                text = item.url,
                                color = colorResource(R.color.color_text3)
                            )
                        }
                    }
                }
            }
        }
    }
}
