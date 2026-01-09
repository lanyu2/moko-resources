/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.file

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.metadata.resource.FileMetadata
import java.io.File

/**
 * Linux/HarmonyOS Native 平台的文件资源生成器
 */
internal class LinuxFileResourceGenerator(
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<FileMetadata> {

    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: FileMetadata): CodeBlock {
        val relativePath = metadata.path.joinToString("/") +
                ("/" .takeIf { metadata.path.isNotEmpty() } ?: "") +
                metadata.filePath.name

        return CodeBlock.of(
            "FileResource(filePath = %S)",
            "$FILES_DIR/$relativePath"
        )
    }

    override fun generateResourceFiles(data: List<FileMetadata>) {
        data.forEach { metadata ->
            val relativePath = metadata.path.joinToString("/") +
                    ("/" .takeIf { metadata.path.isNotEmpty() } ?: "") +
                    metadata.filePath.name

            val targetFile = File(resourcesGenerationDir, "$FILES_DIR/$relativePath")
            targetFile.parentFile.mkdirs()
            metadata.filePath.copyTo(targetFile, overwrite = true)
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<FileMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<FileMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.fileResourceName
        )
    }

    private companion object {
        const val FILES_DIR = "files"
    }
}