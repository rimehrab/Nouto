package org.kaorun.nouto

import android.app.Application
import org.kaorun.nouto.ui.utils.ThemeHelper

class NoutoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeHelper.apply(this)
    }
}