/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

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

/**
 * 字符串资源包 - 管理本地化字符串
 */
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
