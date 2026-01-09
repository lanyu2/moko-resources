/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 *//*


package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual data class StringResource(
    val key: String,
    val bundle: StringResourceBundle) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("StringResource_getString")
    fun getString(): String {
        return bundle.getString(key) ?: "Missing: $key"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringResource) return false
        return key == other.key && bundle == other.bundle
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + bundle.hashCode()
        return result
    }
}

*/
/**
 * 字符串资源包 - 管理本地化字符串
 *//*

class StringResourceBundle(
    private val bundleName: String
) {
    // 存储已加载的字符串 (实际项目中应从文件加载)
    private val strings: MutableMap<String, String> = mutableMapOf()

    init {
        loadStrings()
    }

    private fun loadStrings() {
        // TODO: 从 /res/raw/ 或其他位置加载实际的字符串资源
        // 这里需要根据 HarmonyOS 的资源结构实现
    }

    fun getString(key: String): String? = strings[key]

    fun putString(key: String, value: String) {
        strings[key] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringResourceBundle) return false
        return bundleName == other.bundleName
    }

    override fun hashCode(): Int = bundleName.hashCode()
}
*/
/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

/**
 * HarmonyOS/Linux Native 字符串资源
 *
 * @param key 资源键名
 * @param bundleName 资源包名称
 */
actual class StringResource(
    val key: String,
    val bundleName: String
) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("StringResource_getString")
    fun getString(): String {
        return StringResourceLoader.getString(bundleName, key) ?: "Missing: $key"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringResource) return false
        return key == other.key && bundleName == other.bundleName
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + bundleName.hashCode()
        return result
    }

    override fun toString(): String = "StringResource(key=$key, bundle=$bundleName)"
}

/**
 * 字符串资源加载器
 */
internal object StringResourceLoader {
    private val bundles: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    fun getString(bundleName: String, key: String): String? {
        val bundle = getOrLoadBundle(bundleName)
        return bundle[key]
    }

    private fun getOrLoadBundle(bundleName: String): Map<String, String> {
        return bundles.getOrPut(bundleName) {
            loadBundle(bundleName)
        }
    }

    private fun loadBundle(bundleName: String): MutableMap<String, String> {
        val result = mutableMapOf<String, String>()

        val currentLocale = getCurrentLocale()
        val localizedPath = if (currentLocale.isNotEmpty()) {
            "/res/raw/localization/${bundleName}_$currentLocale.properties"
        } else {
            "/res/raw/localization/$bundleName.properties"
        }

        loadPropertiesFile(localizedPath, result)

        if (result.isEmpty()) {
            val defaultPath = "/res/raw/localization/$bundleName.properties"
            loadPropertiesFile(defaultPath, result)
        }

        return result
    }

    private fun loadPropertiesFile(path: String, target: MutableMap<String, String>) {
        try {
            val content = FileUtils.readFileAsString(path)
            parseProperties(content, target)
        } catch (e: Exception) {
            // 忽略
        }
    }

    private fun parseProperties(content: String, target: MutableMap<String, String>) {
        content.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("!") }
            .forEach { line ->
                val separatorIndex = line.indexOfFirst { it == '=' || it == ':' }
                if (separatorIndex > 0) {
                    val key = line.substring(0, separatorIndex).trim()
                    val value = line.substring(separatorIndex + 1).trim()
                    target[key] = decodeUnicodeEscapes(value)
                }
            }
    }

    private fun decodeUnicodeEscapes(input: String): String {
        val result = StringBuilder()
        var i = 0
        while (i < input.length) {
            if (i + 5 < input.length && input[i] == '\\' && input[i + 1] == 'u') {
                try {
                    val codePoint = input.substring(i + 2, i + 6).toInt(16)
                    result.append(codePoint.toChar())
                    i += 6
                    continue
                } catch (e: NumberFormatException) {
                    // 保留原样
                }
            }
            result.append(input[i])
            i++
        }
        return result.toString()
    }

    private fun getCurrentLocale(): String {
        return FileUtils.getEnv("LANG")?.substringBefore(".")?.replace("_", "-") ?: ""
    }

    fun clearCache() {
        bundles.clear()
    }
}
