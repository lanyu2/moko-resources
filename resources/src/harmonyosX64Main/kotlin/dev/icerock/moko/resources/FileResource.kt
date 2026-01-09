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
import platform.posix.free
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.malloc
import platform.posix.rewind
import kotlin.experimental.ExperimentalNativeApi

/**
 * HarmonyOS Native 文件资源
 *
 * @param filePath 文件相对路径
 */
actual class FileResource(val filePath: String) {

    @OptIn(ExperimentalNativeApi::class)
    @CName("FileResource_getFilePath")
    fun getFilePath(): String = "/res/raw/$filePath"

    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    @CName("FileResource_readText")
    fun readText(): String {
        val fullPath = getFilePath()
        val file = fopen(fullPath, "rb")
            ?: throw IllegalStateException("File resource not found: $filePath")
        try {
            fseek(file, 0, platform.posix.SEEK_END)
            val fileSize = ftell(file)
            rewind(file)
            val buffer = malloc(fileSize.toULong() + 1u)
                ?: throw IllegalStateException("Failed to allocate memory")
            try {
                val bytesRead = fread(buffer, 1u, fileSize.toULong(), file)
                if (bytesRead != fileSize.toULong()) {
                    throw IllegalStateException("Failed to read entire file")
                }
                val byteBuffer = buffer as CPointer<ByteVar>
                byteBuffer[fileSize.toInt()] = 0.toByte()
                return byteBuffer.toKString()
            } finally {
                free(buffer)
            }
        } finally {
            fclose(file)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileResource) return false
        return filePath == other.filePath
    }

    override fun hashCode(): Int = filePath.hashCode()

    override fun toString(): String = "FileResource(filePath=$filePath)"
}