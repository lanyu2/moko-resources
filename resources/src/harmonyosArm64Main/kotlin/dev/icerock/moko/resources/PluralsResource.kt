/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class PluralsResource(val resourceId: Int) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("PluralsResource_getQuantityString")
    fun getQuantityString(number: Int): String {
        // 鸿蒙Native无Resources，需实现复数字符串逻辑，此处返回简单字符串
        return "Quantity string for resource $resourceId, number $number"
    }
}