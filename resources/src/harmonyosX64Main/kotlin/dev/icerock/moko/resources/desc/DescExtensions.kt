/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource

/**
 * HarmonyOS Native 平台的资源描述符扩展
 *
 * 注意：此平台不支持基于 Int ID 的资源访问
 * 请使用以下方式：
 *   - MR.strings.xxx.desc()
 *   - MR.plurals.xxx.desc(number)
 */

// ===== StringResource 扩展 =====

fun StringResource.desc(): ResourceStringDesc = ResourceStringDesc(this)

fun StringResource.desc(vararg args: Any): ResourceFormattedStringDesc =
    ResourceFormattedStringDesc(this, args.toList())

// ===== PluralsResource 扩展 =====

fun PluralsResource.desc(number: Int): PluralStringDesc = PluralStringDesc(this, number)

fun PluralsResource.desc(number: Int, vararg args: Any): PluralFormattedStringDesc =
    PluralFormattedStringDesc(this, number, args.toList())

// ===== 已废弃的 Int 扩展（编译时错误）=====

@Deprecated(
    message = "HarmonyOS Native 不支持基于 Int 的资源访问，请使用 MR.strings.xxx.desc()",
    level = DeprecationLevel.ERROR
)
fun Int.strResDesc(): Nothing =
    throw UnsupportedOperationException("Use MR.strings.xxx.desc() instead")

@Deprecated(
    message = "HarmonyOS Native 不支持基于 Int 的资源访问，请使用 MR.plurals.xxx.desc(number)",
    level = DeprecationLevel.ERROR
)
fun Int.plrResDesc(number: Int): Nothing =
    throw UnsupportedOperationException("Use MR.plurals.xxx.desc(number) instead")