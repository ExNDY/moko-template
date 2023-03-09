/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package org.example.library.feature.list.presentation

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.asState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.example.library.feature.list.model.ListSource
import org.example.library.feature.list.model.News

class ListViewModel<T>(
    private val listSource: ListSource<T>,
    private val strings: Strings,
) : ViewModel() {

    private val _state: MutableLiveData<ResourceState<List<News>, Throwable>> =
        MutableLiveData(initialValue = ResourceState.Empty())

    val state: LiveData<ResourceState<List<News>, StringDesc>> = _state
        .errorTransform {
            // new type inference require set types oO
            map { it.message?.desc() ?: strings.unknownError.desc() }
        }

    init {
        loadList()
    }

    fun onCreated() {
        loadList()
    }

    fun onRetryClick() {
        loadList()
    }

    fun onRefresh(completion: () -> Unit) {
        viewModelScope.launch {
            @Suppress("TooGenericExceptionCaught") // ktor on ios fail with Throwable when no network
            try {
                val items = listSource.getList()

                _state.value = items.asState()
            } catch (error: Exception) {
                Napier.e("can't refresh", throwable = error)
            } finally {
                completion()
            }
        }
    }

    private fun loadList() {
        _state.value = ResourceState.Loading()

        viewModelScope.launch {
            @Suppress("TooGenericExceptionCaught") // ktor on ios fail with Throwable when no network
            try {
                val items = listSource.getList()

                _state.value = items.asState()
            } catch (error: Exception) {
                _state.value = ResourceState.Failed(error)
            }
        }
    }

    interface Strings {
        val unknownError: StringResource
    }
}
