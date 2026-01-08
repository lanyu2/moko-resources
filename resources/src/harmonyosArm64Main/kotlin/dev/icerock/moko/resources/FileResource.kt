/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.set
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.malloc
import platform.posix.rewind
import kotlin.experimental.ExperimentalNativeApi


actual class FileResource (val rawResId: Int) {
    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("FileResource_readText")
    fun readText(): String {
        val fullPath = "/res/raw/$rawResId" // 鸿蒙Native资源路径适配，假设资源ID对应文件名
        val file = fopen(fullPath, "rb") ?: throw IllegalStateException("File resource not found: $rawResId") // 替换Android专属异常
        try {
            fseek(file, 0, platform.posix.SEEK_END)
            val fileSize = ftell(file)
            rewind(file)
            val buffer = malloc(fileSize.toULong() + 1u) ?: throw IllegalStateException("Failed to allocate memory")
            val bytesRead = fread(buffer, 1u, fileSize.toULong(), file)
            if (bytesRead != fileSize.toULong()) {
                throw IllegalStateException("Failed to read entire file")
            }
            (buffer as CPointer<ByteVar>)[fileSize.toInt()] = 0.toByte()
//            val byteBuffer = buffer as CPointer<ByteVar>
//            byteBuffer[fileSize.toInt()] = 0.toByte()
            return buffer.toKString()
        } finally {
            fclose(file)
        }
    }
}