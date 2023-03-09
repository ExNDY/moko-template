package org.example.app.ui

import androidx.compose.runtime.Composable
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun <T> ResourceContent(
    state: ResourceState<T, StringDesc>,
    emptyDataConfig: EmptyDataConfig?,
    success: @Composable (data: T) -> Unit
) {
    when (state) {
        is ResourceState.Loading -> LoadingContent()
        is ResourceState.Success -> success(state.data)
        is ResourceState.Failed -> ErrorContent(
            errorMessage = state.error,
            onRetryClick = emptyDataConfig?.onRetryClick ?: {}
        )

        is ResourceState.Empty -> EmptyDataContent(config = emptyDataConfig ?: return)
    }
}

@Composable
fun <T> ResourceContent(
    state: ResourceState<T, StringDesc>,
    empty: @Composable () -> Unit,
    success: @Composable (data: T) -> Unit
) {
    when (state) {
        is ResourceState.Loading -> LoadingContent()
        is ResourceState.Success -> success(state.data)
        is ResourceState.Failed -> ErrorContent(
            errorMessage = state.error,
            onRetryClick = {
                //TODO Implement on real project
            }
        )

        is ResourceState.Empty -> empty()
    }
}
