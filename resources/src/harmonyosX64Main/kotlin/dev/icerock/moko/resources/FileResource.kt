/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.free
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.malloc
import platform.posix.rewind
import kotlin.experimental.ExperimentalNativeApi

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

            if (fileSize <= 0) return ""

            val buffer = malloc(fileSize.toULong())
                ?: throw IllegalStateException("Failed to allocate memory")
            try {
                val bytesRead = fread(buffer, 1u, fileSize.toULong(), file)
                if (bytesRead != fileSize.toULong()) {
                    throw IllegalStateException("Failed to read entire file")
                }

                // ✅ 修复：正确处理 UTF-8 编码
                val bytePtr = buffer as CPointer<ByteVar>
                val byteArray = ByteArray(fileSize.toInt()) { index ->
                    bytePtr[index]
                }

                return decodeUtf8WithBomHandling(byteArray)
            } finally {
                free(buffer)
            }
        } finally {
            fclose(file)
        }
    }

    /**
     * 解码 UTF-8 字节数组，处理可能存在的 BOM
     */
    private fun decodeUtf8WithBomHandling(bytes: ByteArray): String {
        // UTF-8 BOM: EF BB BF
        val hasUtf8Bom = bytes.size >= 3 &&
                bytes[0] == 0xEF.toByte() &&
                bytes[1] == 0xBB.toByte() &&
                bytes[2] == 0xBF.toByte()

        val startIndex = if (hasUtf8Bom) 3 else 0

        return if (startIndex > 0) {
            bytes.decodeToString(startIndex, bytes.size)
        } else {
            bytes.decodeToString()
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