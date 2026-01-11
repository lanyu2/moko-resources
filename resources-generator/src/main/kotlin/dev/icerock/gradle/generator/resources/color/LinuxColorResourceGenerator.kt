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
        // 复用已有的工具函数
        return createColorResourceCodeInitializer(metadata)
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
}