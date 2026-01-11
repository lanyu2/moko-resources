/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.asset

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.metadata.resource.AssetMetadata
import java.io.File

/**
 * Linux/HarmonyOS Native 平台的 Asset 资源生成器
 */
internal class LinuxAssetResourceGenerator(
    private val assetsGenerationDir: File,
) : PlatformResourceGenerator<AssetMetadata> {

    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: AssetMetadata): CodeBlock {
        // 使用相对路径字符串
        val relativePath = metadata.pathRelativeToBase.invariantSeparatorsPath
        return CodeBlock.of(
            "AssetResource(path = %S)",
            relativePath
        )
    }

    override fun generateResourceFiles(data: List<AssetMetadata>) {
        data.forEach { metadata ->
            val targetFile = File(assetsGenerationDir, buildAssetPath(metadata))
            targetFile.parentFile?.mkdirs()
            metadata.filePath.copyTo(targetFile, overwrite = true)
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<AssetMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<AssetMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.assetResourceName
        )
    }

    private fun buildAssetPath(metadata: AssetMetadata): String {
        return metadata.pathRelativeToBase.path.replace('\\', '/')
    }
}