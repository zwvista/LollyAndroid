package com.zwstudio.lolly.ui.common

// https://gist.github.com/Pinaki93/163f293a9c6f7ba3ae5f20bc87d133da

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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