/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.color

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.metadata.resource.ColorMetadata

/**
 * Linux/HarmonyOS Native 平台的颜色资源生成器
 */
internal class LinuxColorResourceGenerator : PlatformResourceGenerator<ColorMetadata> {

    override fun imports(): List<ClassName> = listOf(Constants.graphicsColorName)

    override fun generateInitializer(metadata: ColorMetadata): CodeBlock {
        val lightColor: String = metadata.lightColor.toColorInitializer()
        val darkColor: String? = metadata.darkColor?.toColorInitializer()

        return if (darkColor != null) {
            CodeBlock.of(
                "ColorResource(lightColor = $lightColor, darkColor = $darkColor)"
            )
        } else {
            CodeBlock.of(
                "ColorResource(lightColor = $lightColor, darkColor = null)"
            )
        }
    }

    override fun generateResourceFiles(data: List<ColorMetadata>) {
        // Linux 平台不需要生成额外的资源文件
        // 颜色值直接编码在 Kotlin 代码中
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<ColorMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<ColorMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.colorResourceName
        )
    }

    /**
     * 将颜色值转换为 Color 初始化代码
     */
    private fun ColorMetadata.ColorItem.toColorInitializer(): String {
        return "Color(0x${argbHex})"
    }

    /**
     * 获取 ARGB 十六进制字符串
     */
    private val ColorMetadata.ColorItem.argbHex: String
        get() {
            val a = (alpha * 255).toInt().toString(16).padStart(2, '0')
            val r = red.toString(16).padStart(2, '0')
            val g = green.toString(16).padStart(2, '0')
            val b = blue.toString(16).padStart(2, '0')
            return "$a$r$g$b".uppercase()
        }
}