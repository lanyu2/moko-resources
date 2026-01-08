/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import kotlin.experimental.ExperimentalNativeApi

actual class CompositionStringDesc actual constructor(
    val args: Iterable<StringDesc>,
    val separator: String?
) : StringDesc {
    @OptIn(ExperimentalNativeApi::class)
    @CName("CompositionStringDesc_localized")
    override fun localized(): String =
        args.joinToString(separator = separator ?: "") { it.localized() } // 调用每个 StringDesc 的 localized()
}