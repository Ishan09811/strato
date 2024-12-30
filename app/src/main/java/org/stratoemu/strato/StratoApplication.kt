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

        private val _themeChangeFlow = MutableSharedFlow<Int>(replay = 1)
        val themeChangeFlow = _themeChangeFlow.asSharedFlow()

        fun setTheme(newValue: Boolean) {
            if (newValue) {
                _themeChangeFlow.tryEmit(R.style.AppTheme_MaterialYou)
            } else {
                _themeChangeFlow.tryEmit(R.style.AppTheme)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        System.loadLibrary("skyline")
        setTheme(getSettings().useMaterialYou)
    }
}
