package com.zwstudio.lolly.ui.common

// https://gist.github.com/Pinaki93/163f293a9c6f7ba3ae5f20bc87d133da

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    dropDownModifier: Modifier = Modifier,
    items: List<T>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    selectedItemFactory: @Composable (Modifier, Int) -> Unit,
    dropdownItemFactory: @Composable (T, Int) -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        selectedItemFactory(
            Modifier
                .clickable { expanded = true },
            selectedItemIndex
        )

        androidx.compose.material.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = dropDownModifier
        ) {
            items.forEachIndexed { index, element ->
                DropdownMenuItem(onClick = {
                    onItemSelected(index)
                    expanded = false
                }) {
                    dropdownItemFactory(element, index)
                }
            }
        }
    }
}

@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    itemsStateFlow: MutableStateFlow<List<T>>,
    selectedItemIndexStateFlow: MutableStateFlow<Int>,
    selectedItemText: () -> String,
    dropdownItemText: (T) -> String,
) {
    Spinner(
        modifier = modifier,
        items = itemsStateFlow.collectAsState().value,
        selectedItemIndex = selectedItemIndexStateFlow.collectAsState().value,
        onItemSelected = { selectedItemIndexStateFlow.value = it },
        selectedItemFactory = { modifier, _ ->
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = selectedItemText(),
                    color = Color.White
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                    contentDescription ="drop down arrow"
                )
            }
        },
        dropdownItemFactory = { item, _ ->
            Text(text = dropdownItemText(item))
        }
    )
}