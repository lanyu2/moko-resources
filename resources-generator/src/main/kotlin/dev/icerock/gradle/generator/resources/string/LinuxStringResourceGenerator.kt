/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.string

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addEmptyPlatformResourceProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.generator.localization.LanguageType
import dev.icerock.gradle.metadata.resource.StringMetadata
import dev.icerock.gradle.utils.convertXmlStringToLocalization
import java.io.File

/**
 * Linux/HarmonyOS Native 平台的字符串资源生成器
 */
internal class LinuxStringResourceGenerator(
    private val flattenClassPackage: String,
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<StringMetadata> {

    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: StringMetadata): CodeBlock {
        return CodeBlock.of(
            "StringResource(key = %S, bundleName = %S)",
            metadata.key,
            getBundlePath()
        )
    }

    override fun generateResourceFiles(data: List<StringMetadata>) {
        data.processLanguages().forEach { (lang, strings) ->
            generateLanguageFile(
                language = LanguageType.fromLanguage(lang),
                strings = strings
            )
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<StringMetadata>,
        modifier: KModifier?,
    ) {
        builder.addEmptyPlatformResourceProperty(modifier)

        // 添加 bundle 路径常量
        builder.addLinuxStringsBundleProperty(
            bundlePropertyName = STRINGS_BUNDLE_PROPERTY_NAME,
            bundlePath = getBundlePath()
        )
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<StringMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.stringResourceName
        )
    }

    private fun generateLanguageFile(language: LanguageType, strings: Map<String, String>) {
        val fileDirName = "${getBundlePath()}${language.linuxResourcesSuffix}"

        val localizationDir = File(resourcesGenerationDir, LOCALIZATION_DIR)
        localizationDir.mkdirs()

        val stringsFile = File(localizationDir, "$fileDirName.properties")

        val content: String = strings.map { (key, value) ->
            "$key = ${value.convertXmlStringToLocalization()}"
        }.joinToString("\n")

        stringsFile.writeText(content, Charsets.UTF_8)
    }

    private fun getBundlePath(): String = "${flattenClassPackage}_$STRINGS_BUNDLE_NAME"

    private companion object {
        const val STRINGS_BUNDLE_PROPERTY_NAME = "stringsBundle"
        const val STRINGS_BUNDLE_NAME = "mokoBundle"
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

/**
 * 为 Linux 添加字符串 bundle 属性
 */
private fun Builder.addLinuxStringsBundleProperty(
    bundlePropertyName: String,
    bundlePath: String
) {
    val property = com.squareup.kotlinpoet.PropertySpec.builder(
        name = bundlePropertyName,
        type = com.squareup.kotlinpoet.STRING,
        com.squareup.kotlinpoet.KModifier.PRIVATE
    ).initializer(com.squareup.kotlinpoet.CodeBlock.of("%S", bundlePath))
        .build()

    addProperty(property)
}