/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.PluralsResource
import kotlin.experimental.ExperimentalNativeApi

actual class PluralFormattedStringDesc actual constructor(
    val pluralsRes: PluralsResource,
    val number: Int,
    val args: List<Any>
) : StringDesc {
    @OptIn(ExperimentalNativeApi::class)
    @CName("PluralFormattedStringDesc_localized")
    override fun localized(): String {
        // 鸿蒙Native无Resources，实现格式化逻辑
        val baseString = pluralsRes.getQuantityString(number)
        return Utils.processArgs(args).foldIndexed(baseString) { index, acc, arg ->
            acc.replace("{$index}", arg.toString())
        }
    }
}