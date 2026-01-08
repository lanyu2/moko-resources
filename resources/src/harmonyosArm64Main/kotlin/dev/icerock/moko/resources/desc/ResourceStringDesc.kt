/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.StringResource
import kotlin.experimental.ExperimentalNativeApi

actual class ResourceStringDesc actual constructor(val stringRes: StringResource) :
    StringDesc{
    @OptIn(ExperimentalNativeApi::class)
    @CName("ResourceStringDesc_toString")
    override fun localized(): String {
        return stringRes.getString() // 鸿蒙Native无Context
    }
    }