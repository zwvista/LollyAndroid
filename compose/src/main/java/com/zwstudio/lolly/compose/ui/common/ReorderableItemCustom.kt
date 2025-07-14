package com.zwstudio.lolly.compose.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableState

// https://github.com/aclassen/ComposeReorderable/issues/293
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ReorderableItemCustom(
    reorderableState: ReorderableState<*>,
    key: Any?,
    modifier: Modifier = Modifier,
    index: Int? = null,
    orientationLocked: Boolean = true,
    content: @Composable BoxScope.(isDragging: Boolean) -> Unit,
) = ReorderableItem(reorderableState, key, modifier, Modifier.animateItem(), orientationLocked, index, content)
