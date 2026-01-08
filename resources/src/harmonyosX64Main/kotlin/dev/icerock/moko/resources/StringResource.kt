/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual data class StringResource(val resourceId: Int) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("StringResource_getString")
    fun getString(): String {
        // 鸿蒙Native无Context，需从资源文件加载字符串，此处返回简单字符串
        return "String for resource $resourceId"
    }
}