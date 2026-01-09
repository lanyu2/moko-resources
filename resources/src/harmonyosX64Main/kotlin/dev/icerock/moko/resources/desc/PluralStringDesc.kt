/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.PluralsResource
import kotlin.experimental.ExperimentalNativeApi

actual class PluralStringDesc actual constructor(
    val pluralsRes: PluralsResource,
    val number: Int
) : StringDesc{
    @OptIn(ExperimentalNativeApi::class)
    @CName("PluralStringDesc_localized")
    override fun localized(): String {
        return pluralsRes.getQuantityString(number) // 鸿蒙Native无Context
    }
}