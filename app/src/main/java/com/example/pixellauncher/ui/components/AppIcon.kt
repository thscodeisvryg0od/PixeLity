package com.example.pixellauncher.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.pixellauncher.R
import com.example.pixellauncher.data.model.AppInfo

@Composable
fun AppIcon(
    app: AppInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    showLabel: Boolean = true,
    onAddToDock: (() -> Unit)? = null,
    onRemoveFromDock: (() -> Unit)? = null,
    onHide: (() -> Unit)? = null,
    onAppInfo: (() -> Unit)? = null,
    onUninstall: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { menuExpanded = true }
                )
                .padding(4.dp)
        ) {
            AppIconImage(
                drawable = app.icon,
                contentDescription = app.label,
                size = iconSize
            )
            if (showLabel) {
                Text(
                    text = app.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(iconSize + 16.dp)
                        .padding(top = 4.dp)
                )
            }
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.action_open)) },
                onClick = {
                    menuExpanded = false
                    onClick()
                }
            )
            if (onAddToDock != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.action_add_to_home)) },
                    onClick = {
                        menuExpanded = false
                        onAddToDock()
                    }
                )
            }
            if (onRemoveFromDock != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.action_remove_from_home)) },
                    onClick = {
                        menuExpanded = false
                        onRemoveFromDock()
                    }
                )
            }
            if (onHide != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.action_hide_app)) },
                    onClick = {
                        menuExpanded = false
                        onHide()
                    }
                )
            }
            if (onAppInfo != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.action_app_info)) },
                    onClick = {
                        menuExpanded = false
                        onAppInfo()
                    }
                )
            }
            if (onUninstall != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.action_uninstall)) },
                    onClick = {
                        menuExpanded = false
                        onUninstall()
                    }
                )
            }
        }
    }
}

@Composable
fun AppIconImage(
    drawable: Drawable?,
    contentDescription: String?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(drawable) {
        drawable?.toBitmap(size.value.toInt().coerceAtLeast(1) * 2, size.value.toInt().coerceAtLeast(1) * 2)
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
