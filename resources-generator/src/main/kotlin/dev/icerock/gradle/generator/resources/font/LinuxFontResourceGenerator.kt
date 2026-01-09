/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.font

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.metadata.resource.FontMetadata
import java.io.File

/**
 * Linux/HarmonyOS Native 平台的字体资源生成器
 */
internal class LinuxFontResourceGenerator(
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<FontMetadata> {

    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: FontMetadata): CodeBlock {
        val fileName = "${metadata.key}.${metadata.filePath.extension}"
        val fontFamily = metadata.key.replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

        return CodeBlock.of(
            "FontResource(filePath = %S, fontFamily = %S)",
            "$FONTS_DIR/$fileName",
            fontFamily
        )
    }

    override fun generateResourceFiles(data: List<FontMetadata>) {
        val fontsDir = File(resourcesGenerationDir, FONTS_DIR)
        fontsDir.mkdirs()

        data.forEach { metadata ->
            val fileName = "${metadata.key}.${metadata.filePath.extension}"
            val targetFile = File(fontsDir, fileName)
            metadata.filePath.copyTo(targetFile, overwrite = true)
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<FontMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<FontMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.fontResourceName
        )
    }

    private companion object {
        const val FONTS_DIR = "fonts"
    }
}