package org.example.app.ui

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

class EmptyDataConfig(
    val message: StringDesc,
    val onRetryClick: () -> Unit
) {
    constructor(
        message: String,
        onRetryClick: () -> Unit
    ) : this(message.desc(), onRetryClick)
}
