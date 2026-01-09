/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 *//*


package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual data class ImageResource (
    val filePath: String,
    val darkFilePath: String? = null) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getFilePath")
    fun getFilePath(): String {
        // 返回完整的资源路径
        return "/res/raw/$filePath"
    }

    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getDarkFilePath")
    fun getDarkFilePath(): String? {
        return darkFilePath?.let { "/res/raw/$it" }
    }

*/
/*    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageResource) return false
        return filePath == other.filePath && darkFilePath == other.darkFilePath
    }

    override fun hashCode(): Int {
        var result = filePath.hashCode()
        result = 31 * result + (darkFilePath?.hashCode() ?: 0)
        return result
    }*//*

}*/
/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class ImageResource(
    val filePath: String,
    val darkFilePath: String? = null
) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getFilePath")
    fun getFilePath(): String = "/res/raw/$filePath"

    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getDarkFilePath")
    fun getDarkFilePath(): String? = darkFilePath?.let { "/res/raw/$it" }

    @OptIn(ExperimentalNativeApi::class)
    @CName("ImageResource_getCurrentFilePath")
    fun getCurrentFilePath(isDarkTheme: Boolean = false): String {
        return if (isDarkTheme && darkFilePath != null) getDarkFilePath()!! else getFilePath()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageResource) return false
        return filePath == other.filePath && darkFilePath == other.darkFilePath
    }

    override fun hashCode(): Int {
        var result = filePath.hashCode()
        result = 31 * result + (darkFilePath?.hashCode() ?: 0)
        return result
    }
}