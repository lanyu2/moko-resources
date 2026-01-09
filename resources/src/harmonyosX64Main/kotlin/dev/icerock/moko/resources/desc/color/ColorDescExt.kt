/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc.color

import dev.icerock.moko.graphics.Color
import kotlin.experimental.ExperimentalNativeApi

/**
 * 获取颜色值 (ARGB Int)
 */
@OptIn(ExperimentalNativeApi::class)
@CName("ColorDesc_getColor")
fun ColorDesc.getColor(): Int {
    return when (this) {
        is ColorDescResource -> this.resource.getColor()
        is ColorDescSingle -> this.color.argb.toInt()
        is ColorDescThemed -> {
            // HarmonyOS Native 暂不支持主题检测，返回浅色
            this.lightColor.argb.toInt()
        }
        else -> throw IllegalArgumentException("Unknown ColorDesc type: ${this::class}")
    }
}

/**
 * 获取指定主题的颜色值
 */
@OptIn(ExperimentalNativeApi::class)
@CName("ColorDesc_getColorForTheme")
fun ColorDesc.getColorForTheme(isDarkTheme: Boolean): Int {
    return when (this) {
        is ColorDescResource -> {
            if (isDarkTheme) this.resource.getDarkColor() else this.resource.getLightColor()
        }
        is ColorDescSingle -> this.color.argb.toInt()
        is ColorDescThemed -> {
            if (isDarkTheme) this.darkColor.argb.toInt() else this.lightColor.argb.toInt()
        }
        else -> throw IllegalArgumentException("Unknown ColorDesc type: ${this::class}")
    }
}