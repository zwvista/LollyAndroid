package com.zwstudio.lolly.ui.webtextbooks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.misc.MWebTextbook
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.webtextbooks.WebTextbooksDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WebTextbooksDetailScreen(item: MWebTextbook, navController: NavHostController?) {

    val vmDetail = koinViewModel<WebTextbooksDetailViewModel> { parametersOf(item) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController,
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(id = R.string.label_id, vmDetail.item.id))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_textbook)) },
                value = vmDetail.item.textbookname,
                onValueChange = {}
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.label_unit)) },
                value = vmDetail.item.unit.toString(),
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
