/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import platform.posix.FILE
import platform.posix.SEEK_END
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.free
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.malloc
import platform.posix.rewind
import platform.posix.getenv
import kotlinx.cinterop.toKString

/**
 * HarmonyOS/Linux Native 文件读取工具类
 */
@OptIn(ExperimentalForeignApi::class)
internal object FileUtils {

    /**
     * 读取文件内容为字符串（UTF-8 编码）
     */
    fun readFileAsString(filePath: String): String {
        val file = fopen(filePath, "rb")
            ?: throw IllegalStateException("File not found: $filePath")

        return try {
            readFileContent(file)
        } finally {
            fclose(file)
        }
    }

    /**
     * 从文件指针读取内容
     */
    fun readFileContent(file: CPointer<FILE>): String {
        fseek(file, 0, SEEK_END)
        val fileSize = ftell(file)
        rewind(file)

        if (fileSize <= 0) return ""

        val buffer = malloc(fileSize.toULong())
            ?: throw IllegalStateException("Failed to allocate memory")

        return try {
            val bytesRead = fread(buffer, 1u, fileSize.toULong(), file)
            if (bytesRead != fileSize.toULong()) {
                throw IllegalStateException("Failed to read entire file")
            }

            val bytePtr = buffer as CPointer<ByteVar>
            val byteArray = ByteArray(fileSize.toInt()) { index ->
                bytePtr[index]
            }

            decodeUtf8WithBomHandling(byteArray)
        } finally {
            free(buffer)
        }
    }

    /**
     * 解码 UTF-8 字节数组，自动处理 BOM
     */
    fun decodeUtf8WithBomHandling(bytes: ByteArray): String {
        val startIndex = detectBomLength(bytes)

        return if (startIndex > 0) {
            bytes.decodeToString(startIndex, bytes.size)
        } else {
            bytes.decodeToString()
        }
    }

    /**
     * 检测 BOM 长度
     */
    private fun detectBomLength(bytes: ByteArray): Int {
        if (bytes.size < 2) return 0

        // UTF-8 BOM: EF BB BF
        if (bytes.size >= 3 &&
            bytes[0] == 0xEF.toByte() &&
            bytes[1] == 0xBB.toByte() &&
            bytes[2] == 0xBF.toByte()) {
            return 3
        }

        // UTF-16 LE BOM: FF FE
        if (bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte()) {
            return 2
        }

        // UTF-16 BE BOM: FE FF
        if (bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte()) {
            return 2
        }

        return 0
    }

    /**
     * 检查文件是否存在
     */
    fun fileExists(filePath: String): Boolean {
        val file = fopen(filePath, "r")
        return if (file != null) {
            fclose(file)
            true
        } else {
            false
        }
    }

    /**
     * 获取环境变量（Native 兼容）
     */
    fun getEnv(name: String): String? {
        return getenv(name)?.toKString()
    }
}