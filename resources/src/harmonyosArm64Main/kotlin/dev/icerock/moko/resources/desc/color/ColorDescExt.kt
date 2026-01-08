/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc.color

import dev.icerock.moko.graphics.Color
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@CName("ColorDesc_getColor")
fun ColorDesc.getColor(): Int {
    return when (this) {
        is ColorDescResource -> this.resource.getColor() // 调用鸿蒙Native实现
        is ColorDescSingle -> color.argb.toInt()
        is ColorDescThemed -> {
            // 鸿蒙Native无UI_MODE，简化处理返回浅色主题
            lightColor.argb.toInt()
        }
        else -> throw IllegalArgumentException("unknown class ${this::class}")
    }
}