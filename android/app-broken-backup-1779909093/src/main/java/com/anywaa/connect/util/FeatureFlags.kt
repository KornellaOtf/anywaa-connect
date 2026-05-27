package com.anywaa.connect.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FeatureFlags {
    val PREMIUM_ENABLED: StateFlow<Boolean> = MutableStateFlow(false)
}
