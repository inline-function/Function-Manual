package com.fangshen.manual.ui

import android.app.Application
import androidx.compose.runtime.staticCompositionLocalOf

val LocalApplication = staticCompositionLocalOf<Application> {
    error("LocalApplication 未提供")
}
