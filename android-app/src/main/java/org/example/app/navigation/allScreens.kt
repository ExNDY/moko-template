package org.example.app.navigation

import org.example.app.features.config.ConfigScreen
import org.example.app.features.news.NewsScreen

val allScreens: List<Screen> = listOf(
    ConfigScreen,
    NewsScreen
)

inline fun <reified S : Screen> getScreenName(): String =
    allScreens.find { it is S }!!.screenName

fun Screen.defaultScreenName(): String = this::class.java.simpleName

fun Screen.defaultScreenNameWithParams(vararg params: String): String {
    return defaultScreenName() + params.joinToString { "/{$it}" }
}

fun Screen.defaultScreenNameWithOptionalParams(
    params: List<String>,
    optionalParams: List<String>
): String {
    val optionals = optionalParams.joinToString { "?$it={$it}" }
    return defaultScreenName() + params.joinToString { "/{$it}" } + optionals
}

fun Screen.defaultScreenNameWithOptionalParams(optionalParams: List<String>): String {
    val optionals = optionalParams.joinToString { "?$it={$it}" }
    return defaultScreenName() + optionals
}

fun Screen.screenNameWithParams(vararg params: Any): String {
    return defaultScreenName() + params.joinToString { "/$it" }
}

fun Screen.screenNameWithOptionalParams(
    params: List<Any>,
    optionalParams: List<Pair<String, Any>>
): String {
    val optionals = optionalParams.joinToString { "?${it.first}=${it.second}" }
    return defaultScreenName() + params.joinToString { "/$it" } + optionals
}

fun Screen.screenNameWithOptionalParams(optionalParams: List<Pair<String, Any>>): String {
    val optionals = optionalParams.joinToString { "?${it.first}=${it.second}" }
    return defaultScreenName() + optionals
}
