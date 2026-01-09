/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import kotlin.experimental.ExperimentalNativeApi

actual class RawStringDesc actual constructor(val string: String) :
    StringDesc{
    @OptIn(ExperimentalNativeApi::class)
    @CName("RawStringDesc_localized")
    override fun localized() = string // 鸿蒙Native无Context
    }