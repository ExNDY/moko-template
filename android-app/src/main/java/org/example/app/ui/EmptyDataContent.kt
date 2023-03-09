package org.example.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun EmptyDataContent(config: EmptyDataConfig) = EmptyDataContent(
    message = config.message,
    onRetryClick = config.onRetryClick
)

// TODO: Realise empty data state without project link
@Composable
fun EmptyDataContent(
    modifier: Modifier = Modifier,
    message: StringDesc,
    onRetryClick: () -> Unit
) {
    ErrorContent(
        modifier = modifier,
        errorMessage = message,
        onRetryClick = onRetryClick
    )
}
