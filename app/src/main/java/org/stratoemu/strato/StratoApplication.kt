/*
 * SPDX-License-Identifier: MPL-2.0
 * Copyright © 2020 Skyline Team and Contributors (https://github.com/skyline-emu/)
 */

package org.stratoemu.strato

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import org.stratoemu.strato.di.getSettings
import java.io.File
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @return The optimal directory for putting public files inside, this may return a private directory if a public directory cannot be retrieved
 */
fun Context.getPublicFilesDir() : File = getExternalFilesDir(null) ?: filesDir

@HiltAndroidApp
class StratoApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance : StratoApplication
            private set

        val context : Context get() = instance.applicationContext

        private val _themeChangeFlow = MutableSharedFlow<Int>()
        val themeChangeFlow = _themeChangeFlow.asSharedFlow()

        fun setTheme(newValue: Boolean) {
            val newTheme = if (newValue) R.style.AppTheme_MaterialYou else R.style.AppTheme
            CoroutineScope(Dispatchers.Main).launch { _themeChangeFlow.emit(newTheme) }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        System.loadLibrary("skyline")
    }
}
