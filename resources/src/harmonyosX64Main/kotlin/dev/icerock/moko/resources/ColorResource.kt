/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 *//*


package dev.icerock.moko.resources

import dev.icerock.moko.graphics.Color
import kotlin.experimental.ExperimentalNativeApi

*/
/**
 * HarmonyOS Native 颜色资源
 * 支持浅色/深色主题
 *
 * @param lightColor 浅色主题颜色
 * @param darkColor 深色主题颜色 (可选，默认与浅色相同)
 *//*

actual class ColorResource(
    val lightColor: Color,
    val darkColor: Color? = null
) {
    */
/**
     * 获取当前主题的颜色值 (ARGB)
     * HarmonyOS Native 暂不支持主题检测，默认返回浅色
     *//*

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getColor")
    fun getColor(): Int {
        // TODO: 检测 HarmonyOS 系统主题设置
        // 目前简化处理，返回浅色主题颜色
        return lightColor.argb.toInt()
    }

    */
/**
     * 获取浅色主题颜色值
     *//*

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getLightColor")
    fun getLightColor(): Int = lightColor.argb.toInt()

    */
/**
     * 获取深色主题颜色值
     *//*

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getDarkColor")
    fun getDarkColor(): Int = (darkColor ?: lightColor).argb.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ColorResource) return false
        return lightColor == other.lightColor && darkColor == other.darkColor
    }

    override fun hashCode(): Int {
        var result = lightColor.hashCode()
        result = 31 * result + (darkColor?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "ColorResource(light=$lightColor, dark=$darkColor)"
}*/
/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import dev.icerock.moko.graphics.Color
import kotlin.experimental.ExperimentalNativeApi

actual class ColorResource(
    val lightColor: Color,
    val darkColor: Color? = null
) {
    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getColor")
    fun getColor(): Int = lightColor.argb.toInt()

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getLightColor")
    fun getLightColor(): Int = lightColor.argb.toInt()

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getDarkColor")
    fun getDarkColor(): Int = (darkColor ?: lightColor).argb.toInt()

    @OptIn(ExperimentalNativeApi::class)
    @CName("ColorResource_getColorForTheme")
    fun getColorForTheme(isDarkTheme: Boolean): Int {
        return if (isDarkTheme) getDarkColor() else getLightColor()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ColorResource) return false
        return lightColor == other.lightColor && darkColor == other.darkColor
    }

    override fun hashCode(): Int {
        var result = lightColor.hashCode()
        result = 31 * result + (darkColor?.hashCode() ?: 0)
        return result
    }
}