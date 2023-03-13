package org.example.app.di

import org.example.library.SharedFactory

object AppComponent {
    lateinit var factory: SharedFactory
        private set

    fun initialize(factory: SharedFactory) {
        this.factory = factory
    }
}
