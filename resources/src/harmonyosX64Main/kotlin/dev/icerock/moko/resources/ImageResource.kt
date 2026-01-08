/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual data class ImageResource (val drawableResId: Int) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getDrawable")
    fun getDrawable(): String? {
        // 鸿蒙Native无Drawable，返回图片文件路径
        return "/res/raw/$drawableResId" // 假设资源ID对应图片文件路径
    }
}