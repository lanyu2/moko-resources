/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 *//*


package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

*/
/**
 * HarmonyOS Native 字体资源
 *
 * @param filePath 字体文件相对路径 (如 "fonts/custom_font.ttf")
 * @param fontFamily 字体家族名称 (可选)
 *//*

actual class FontResource(
    val filePath: String,
    val fontFamily: String? = null
) {
    */
/**
     * 获取字体文件的完整路径
     *//*

    @OptIn(ExperimentalNativeApi::class)
    @CName("FontResource_getFilePath")
    fun getFilePath(): String {
        return "/res/raw/$filePath"
    }

    */
/**
     * 获取字体家族名称
     *//*

    @OptIn(ExperimentalNativeApi::class)
    @CName("FontResource_getFontFamily")
    fun getFontFamily(): String {
        return fontFamily ?: filePath.substringAfterLast("/").substringBeforeLast(".")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FontResource) return false
        return filePath == other.filePath
    }

    override fun hashCode(): Int = filePath.hashCode()

    override fun toString(): String = "FontResource(filePath=$filePath, fontFamily=$fontFamily)"
}*/
/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class FontResource(
    val filePath: String,
    val fontFamily: String? = null
) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("FontResource_getFilePath")
    fun getFilePath(): String = "/res/raw/$filePath"

    @OptIn(ExperimentalNativeApi::class)
    @CName("FontResource_getFontFamily")
    fun getFontFamily(): String {
        return fontFamily ?: filePath
            .substringAfterLast("/")
            .substringBeforeLast(".")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FontResource) return false
        return filePath == other.filePath
    }

    override fun hashCode(): Int = filePath.hashCode()
}