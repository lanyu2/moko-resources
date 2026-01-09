/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.StringResource
import kotlin.experimental.ExperimentalNativeApi

actual data class ResourceFormattedStringDesc actual constructor(
    val stringRes: StringResource,
    val args: List<Any>
) : StringDesc {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ResourceFormattedStringDesc_localized")
    override fun localized(): String {
        // 鸿蒙Native无Resources，实现格式化逻辑
        val baseString = stringRes.getString()
        return Utils.processArgs(args).foldIndexed(baseString) { index, acc, arg ->
            acc.replace("{$index}", arg.toString())
        }
    }
}