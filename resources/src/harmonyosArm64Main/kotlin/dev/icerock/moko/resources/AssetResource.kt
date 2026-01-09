/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.malloc
import platform.posix.rewind
import kotlinx.cinterop.set
import kotlin.experimental.ExperimentalNativeApi

actual class AssetResource(val path: String) {

    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("AssetResource_getInputStream")
    fun getInputStream(): CPointer<platform.posix.FILE>? {
        val fullPath = "/res/raw/${path.removeFirstSlash()}" // 鸿蒙Native资源路径适配
        return fopen(fullPath, "rb")
    }

    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("AssetResource_readText")
    fun readText(): String {
        val file = getInputStream() ?: throw IllegalStateException("Asset file not found: $path") // 替换Android专属异常
        try {
            fseek(file, 0, platform.posix.SEEK_END)
            val fileSize = ftell(file)
            rewind(file)
            val buffer = malloc(fileSize.toULong() + 1u) ?: throw IllegalStateException("Failed to allocate memory")
            val bytesRead = fread(buffer, 1u, fileSize.toULong(), file)
            if (bytesRead != fileSize.toULong()) {
                throw IllegalStateException("Failed to read entire file")
            }
            val byteBuffer = buffer as CPointer<ByteVar>
            byteBuffer[fileSize.toInt()] = 0.toByte()
//            (buffer as CPointer<ByteVar>)[fileSize.toInt()] = 0.toByte()
            return buffer.toKString()
        } finally {
            fclose(file)
        }
    }
    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("AssetResource_equals")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AssetResource) return false

        if (path != other.path) return false

        return true
    }
    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("AssetResource_hashCode")
    override fun hashCode(): Int {
        return path.hashCode()
    }

    actual val originalPath: String by ::path
}