/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.domain.di

import com.russhwolf.settings.Settings
import dev.icerock.moko.network.exceptionfactory.HttpExceptionFactory
import dev.icerock.moko.network.exceptionfactory.parser.ErrorExceptionParser
import dev.icerock.moko.network.exceptionfactory.parser.ValidationExceptionParser
import dev.icerock.moko.network.generated.apis.NewsApi
import dev.icerock.moko.network.plugins.ExceptionPlugin
import dev.icerock.moko.network.plugins.TokenPlugin
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.example.library.domain.repository.ConfigRepository
import org.example.library.domain.repository.NewsRepository
import org.example.library.domain.storage.KeyValueStorage

class DomainFactory(
    private val settings: Settings,
    private val baseUrl: String,
    private val httpClientEngine: HttpClientEngine?
) {
    private val keyValueStorage: KeyValueStorage by lazy { KeyValueStorage(settings) }

    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }

    private val httpClient: HttpClient by lazy {
        // resolve class properties into local variables to pass them into freeze lambda
        val json: Json = json

        val config: HttpClientConfig<*>.() -> Unit = {
            install(ExceptionPlugin) {
                exceptionFactory = HttpExceptionFactory(
                    defaultParser = ErrorExceptionParser(json),
                    customParsers = mapOf(
                        HttpStatusCode.UnprocessableEntity.value to ValidationExceptionParser(json)
                    )
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(message = message)
                    }
                }
                level = LogLevel.HEADERS
            }
            install(TokenPlugin) {
                tokenHeaderName = "Authorization"
                tokenProvider = TokenPlugin.TokenProvider { keyValueStorage.token }
            }

            // disable standard BadResponseStatus - exceptionfactory do it for us
            expectSuccess = false
        }

        if (httpClientEngine != null) HttpClient(httpClientEngine, config)
        else HttpClient(config)
    }


    private val newsApi: NewsApi by lazy {
        NewsApi(
            basePath = baseUrl,
            httpClient = httpClient,
            json = json
        )
    }

    val newsRepository: NewsRepository by lazy {
        NewsRepository(
            newsApi = newsApi,
            keyValueStorage = keyValueStorage
        )
    }

    val configRepository: ConfigRepository by lazy {
        ConfigRepository(keyValueStorage = keyValueStorage)
    }
}
