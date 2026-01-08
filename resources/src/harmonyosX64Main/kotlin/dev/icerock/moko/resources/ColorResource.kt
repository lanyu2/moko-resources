/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class ColorResource (val resourceId: Int) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getColor")
    fun getColor(): Int = resourceId // 鸿蒙Native无Context，直接返回资源ID或实现颜色解析逻辑
}