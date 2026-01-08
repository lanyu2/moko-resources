/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class FontResource (val fontResourceId: Int) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("FontResource_getTypeface")
    fun getTypeface(): String? {
        // 鸿蒙Native无Typeface，返回字体文件路径或标识符
        return "/res/raw/$fontResourceId" // 假设资源ID对应字体文件路径
    }
}