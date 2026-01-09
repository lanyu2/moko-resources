/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

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

    /**
     * 根据数量获取复数形式
     * 简化的英语规则，实际项目中需要支持更多语言
     */
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
}