package org.example.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    errorMessage: StringDesc,
    onRetryClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = errorMessage.toString(LocalContext.current))
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetryClick
            ) {
                Text(text = "Retry")
            }
        }
    }
}
