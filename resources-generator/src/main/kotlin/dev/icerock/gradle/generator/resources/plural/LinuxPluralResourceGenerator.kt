/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.plural

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.generator.localization.LanguageType
import dev.icerock.gradle.metadata.resource.PluralMetadata
import java.io.File

/**
 * Linux/HarmonyOS Native 平台的复数资源生成器
 */
internal class LinuxPluralResourceGenerator(
    private val flattenClassPackage: String,
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<PluralMetadata> {

    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: PluralMetadata): CodeBlock {
        return CodeBlock.of(
            "PluralsResource(key = %S, bundleName = %S)",
            metadata.key,
            getBundlePath()
        )
    }

    override fun generateResourceFiles(data: List<PluralMetadata>) {
        data.processLanguages().forEach { (lang, plurals) ->
            generateLanguageFile(
                language = LanguageType.fromLanguage(lang),
                plurals = plurals
            )
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<PluralMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)

        // 添加 bundle 路径常量
        val property = PropertySpec.builder(
            name = PLURALS_BUNDLE_PROPERTY_NAME,
            type = STRING,
            KModifier.PRIVATE
        ).initializer(CodeBlock.of("%S", getBundlePath()))
            .build()

        builder.addProperty(property)
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<PluralMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.pluralsResourceName
        )
    }

    private fun generateLanguageFile(language: LanguageType, plurals: Map<String, Map<String, String>>) {
        val fileDirName = "${getBundlePath()}${language.linuxResourcesSuffix}"

        val localizationDir = File(resourcesGenerationDir, LOCALIZATION_DIR)
        localizationDir.mkdirs()

        val pluralsFile = File(localizationDir, "$fileDirName.properties")

        // 生成复数资源文件
        // 格式: key_zero = ..., key_one = ..., key_other = ...
        val content: String = plurals.flatMap { (key, forms) ->
            forms.map { (quantity, value) ->
                "${key}_$quantity = $value"
            }
        }.joinToString("\n")

        pluralsFile.writeText(content, Charsets.UTF_8)
    }

    private fun getBundlePath(): String = "${flattenClassPackage}_$PLURALS_BUNDLE_NAME"

    private companion object {
        const val PLURALS_BUNDLE_PROPERTY_NAME = "pluralsBundle"
        const val PLURALS_BUNDLE_NAME = "mokoPluralsBundle"
        const val LOCALIZATION_DIR = "localization"
    }
}

/**
 * 获取 Linux 资源文件后缀
 */
private val LanguageType.linuxResourcesSuffix: String
    get() = when (this) {
        LanguageType.Base -> ""
        is LanguageType.Locale -> "_${language()}"
    }