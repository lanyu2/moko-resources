/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import kotlin.experimental.ExperimentalNativeApi

actual interface StringDesc {
    @OptIn(ExperimentalNativeApi::class)
    @CName("StringDesc_localized")
    fun localized(): String // 与 Apple 平台保持一致，无需 Context
    actual sealed class LocaleType {
        actual object System : LocaleType()
        actual class Custom actual constructor(locale: String) : LocaleType()
    }

    actual companion object {
        actual var localeType: LocaleType = LocaleType.System
    }
}