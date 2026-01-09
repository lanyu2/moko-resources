/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 *//*


package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class PluralsResource(
    val key: String,
    val bundle: PluralsResourceBundle) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("PluralsResource_getQuantityString")
    fun getQuantityString(number: Int): String {
        val pluralForm = getPluralForm(number)
        return bundle.getString(key, pluralForm) ?: "Missing plural: $key[$pluralForm]"
    }

    */
/**
     * 根据数量获取复数形式
     * 简化的英语规则，实际项目中需要支持更多语言
     *//*

    private fun getPluralForm(number: Int): PluralForm {
        return when {
            number == 0 -> PluralForm.ZERO
            number == 1 -> PluralForm.ONE
            number == 2 -> PluralForm.TWO
            number in 3..10 -> PluralForm.FEW
            number in 11..99 -> PluralForm.MANY
            else -> PluralForm.OTHER
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PluralsResource) return false
        return key == other.key && bundle == other.bundle
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + bundle.hashCode()
        return result
    }
}

enum class PluralForm {
    ZERO, ONE, TWO, FEW, MANY, OTHER
}

class PluralsResourceBundle(
    private val bundleName: String
) {
    private val plurals: MutableMap<String, Map<PluralForm, String>> = mutableMapOf()

    init {
        loadPlurals()
    }

    private fun loadPlurals() {
        // TODO: 从资源文件加载复数字符串
    }

    fun getString(key: String, form: PluralForm): String? {
        return plurals[key]?.get(form) ?: plurals[key]?.get(PluralForm.OTHER)
    }

    fun putPlural(key: String, forms: Map<PluralForm, String>) {
        plurals[key] = forms
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PluralsResourceBundle) return false
        return bundleName == other.bundleName
    }

    override fun hashCode(): Int = bundleName.hashCode()
}*/
/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import kotlin.experimental.ExperimentalNativeApi

actual class PluralsResource(
    val key: String,
    val bundleName: String
) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("PluralsResource_getQuantityString")
    fun getQuantityString(number: Int): String {
        val pluralForm = getPluralForm(number)
        val fullKey = "${key}_$pluralForm"
        return PluralsResourceLoader.getString(bundleName, fullKey)
            ?: PluralsResourceLoader.getString(bundleName, "${key}_other")
            ?: "Missing plural: $key[$pluralForm]"
    }

    private fun getPluralForm(number: Int): String {
        return when {
            number == 0 -> "zero"
            number == 1 -> "one"
            number == 2 -> "two"
            number in 3..10 -> "few"
            number in 11..99 -> "many"
            else -> "other"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PluralsResource) return false
        return key == other.key && bundleName == other.bundleName
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + bundleName.hashCode()
        return result
    }
}

internal object PluralsResourceLoader {
    private val bundles: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    fun getString(bundleName: String, key: String): String? {
        val bundle = getOrLoadBundle(bundleName)
        return bundle[key]
    }

    private fun getOrLoadBundle(bundleName: String): Map<String, String> {
        return bundles.getOrPut(bundleName) { loadBundle(bundleName) }
    }

    private fun loadBundle(bundleName: String): MutableMap<String, String> {
        val result = mutableMapOf<String, String>()
        val currentLocale = FileUtils.getEnv("LANG")?.substringBefore(".")?.replace("_", "-") ?: ""

        val localizedPath = if (currentLocale.isNotEmpty()) {
            "/res/raw/localization/${bundleName}_$currentLocale.properties"
        } else {
            "/res/raw/localization/$bundleName.properties"
        }

        loadPropertiesFile(localizedPath, result)
        if (result.isEmpty()) {
            loadPropertiesFile("/res/raw/localization/$bundleName.properties", result)
        }
        return result
    }

    private fun loadPropertiesFile(path: String, target: MutableMap<String, String>) {
        try {
            val content = FileUtils.readFileAsString(path)
            content.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() && !it.startsWith("#") }
                .forEach { line ->
                    val idx = line.indexOfFirst { it == '=' || it == ':' }
                    if (idx > 0) {
                        target[line.substring(0, idx).trim()] = line.substring(idx + 1).trim()
                    }
                }
        } catch (e: Exception) { }
    }

    fun clearCache() { bundles.clear() }
}