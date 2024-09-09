package com.zwstudio.lolly.compose.ui.common

// https://gist.github.com/Pinaki93/163f293a9c6f7ba3ae5f20bc87d133da

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zwstudio.lolly.compose.R
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
    enabled: Boolean = true,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        selectedItemFactory(
            if (enabled) Modifier.clickable { expanded = true } else Modifier,
            selectedItemIndex
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = dropDownModifier
        ) {
            items.forEachIndexed { index, element ->
                DropdownMenuItem(
                    onClick = { onItemSelected(index) },
                    text = { dropdownItemFactory(element, index) }
                )
            }
        }
    }
}

@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItemIndexStateFlow: MutableStateFlow<Int>,
    itemText: (T) -> String,
    enabled: Boolean = true,
) {
    Spinner(
        modifier = modifier,
        items = items,
        selectedItemIndex = selectedItemIndexStateFlow.collectAsState().value,
        onItemSelected = { selectedItemIndexStateFlow.value = it },
        selectedItemFactory = { modifier, _ ->
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = items.getOrNull(
                            selectedItemIndexStateFlow.collectAsState().value
                        ).let {
                              if (it == null) "" else itemText(it)
                        },
                    color = if (enabled) Color.White else Color.Gray,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                    contentDescription ="drop down arrow"
                )
            }
        },
        dropdownItemFactory = { item, index ->
            Icon(
                Icons.Filled.CheckCircle,
                null,
                modifier = Modifier.alpha(
                    if(index == selectedItemIndexStateFlow.collectAsState().value) 1f else 0f
                ),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = itemText(item))
        },
        enabled = enabled
    )
}

@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    itemsStateFlow: MutableStateFlow<List<T>>,
    selectedItemIndexStateFlow: MutableStateFlow<Int>,
    itemText: (T) -> String,
    enabled: Boolean = true,
) {
    Spinner(
        modifier = modifier,
        items = itemsStateFlow.collectAsState().value,
        selectedItemIndexStateFlow = selectedItemIndexStateFlow,
        itemText = itemText,
        enabled = enabled
    )
}

@JvmName("Spinner1")
@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    itemsStateFlow: MutableStateFlow<MutableList<T>>,
    selectedItemIndexStateFlow: MutableStateFlow<Int>,
    itemText: (T) -> String,
    enabled: Boolean = true,
) {
    Spinner(
        modifier = modifier,
        items = itemsStateFlow.collectAsState().value,
        selectedItemIndexStateFlow = selectedItemIndexStateFlow,
        itemText = itemText,
        enabled = enabled
    )
}
