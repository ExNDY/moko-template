/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.feature.config

import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.Settings
import dev.icerock.moko.mvvm.test.TestViewModelScopeRule
import dev.icerock.moko.test.cases.InstantTaskRule
import dev.icerock.moko.test.cases.TestCases
import io.ktor.client.engine.mock.respondBadRequest
import org.example.library.SharedFactory
import org.example.library.createSharedFactory
import org.example.library.feature.config.presentation.ConfigViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigViewModelTests : TestCases() {
    override val rules: List<Rule> = listOf(
        InstantTaskRule(),
        TestViewModelScopeRule()
    )

    private lateinit var settings: MockSettings
    private lateinit var listenerEvents: List<String>

    @BeforeTest
    fun setup() {
        val events = mutableListOf<String>()

        settings = MockSettings()
        listenerEvents = events
    }

    @Test
    fun `test default fields`() {
        val viewModel = createConfigViewModel(
            settings = settings,
        )

        assertEquals(
            expected = "ed155d0a445e4b4fbd878fe1f3bc1b7f",
            actual = viewModel.apiTokenField.value()
        )
        assertEquals(
            expected = "us",
            actual = viewModel.languageField.value()
        )
    }

    @Test
    fun `test saved fields`() {
        settings.putString("pref_token", "test")
        settings.putString("pref_language", "ru")

        val viewModel = createConfigViewModel(
            settings = settings,
        )

        assertEquals(
            expected = "test",
            actual = viewModel.apiTokenField.value()
        )
        assertEquals(
            expected = "ru",
            actual = viewModel.languageField.value()
        )
    }

    private fun createConfigViewModel(
        settings: Settings,
    ): ConfigViewModel {
        val factory: SharedFactory = createSharedFactory(settings) {
            respondBadRequest()
        }
        return factory.configFactory.createConfigViewModel()
    }
}
