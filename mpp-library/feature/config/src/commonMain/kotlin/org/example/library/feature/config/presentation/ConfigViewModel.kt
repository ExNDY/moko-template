/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.feature.config.presentation

import dev.icerock.moko.fields.core.validate
import dev.icerock.moko.fields.livedata.FormField
import dev.icerock.moko.fields.livedata.liveBlock
import dev.icerock.moko.mvvm.flow.CFlow
import dev.icerock.moko.mvvm.flow.cFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.example.library.feature.config.model.ConfigStore
import org.example.library.feature.config.presentation.ConfigViewModel.Action.RouteToNews

class ConfigViewModel(
    private val configStore: ConfigStore,
    validations: Validations,
    defaultToken: String,
    defaultLanguage: String
) : ViewModel() {

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: CFlow<Action> = _actions.receiveAsFlow().cFlow()


    val apiTokenField: FormField<String, StringDesc> = FormField(
        initialValue = configStore.apiToken ?: defaultToken,
        validation = liveBlock(validations::validateToken)
    )
    val languageField: FormField<String, StringDesc> = FormField(
        initialValue = configStore.language ?: defaultLanguage,
        validation = liveBlock(validations::validateLanguage)
    )

    private val fields = listOf(apiTokenField, languageField)

    fun onSubmitPressed() {
        if (!fields.validate()) return

        configStore.apiToken = apiTokenField.value()
        configStore.language = languageField.value()

        viewModelScope.launch {
            _actions.send(RouteToNews)
        }
    }

    interface Validations {
        fun validateToken(value: String): StringDesc?
        fun validateLanguage(value: String): StringDesc?
    }

    sealed interface Action{
        object RouteToNews: Action
    }
}
