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
        return when (val v = metadata.value) {
            is ColorMetadata.ColorItem.Single -> {
                CodeBlock.of(
                    "ColorResource(lightColor = %L, darkColor = null)",
                    v.color.toColorCode()
                )
            }

            is ColorMetadata.ColorItem.Themed -> {
                CodeBlock.of(
                    "ColorResource(lightColor = %L, darkColor = %L)",
                    v.light.toColorCode(),
                    v.dark.toColorCode()
                )
            }
        }
    }

    override fun generateResourceFiles(data: List<ColorMetadata>) {
        // Linux 平台不需要生成额外的资源文件：颜色值直接编码进 Kotlin
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

/**
 * 生成 moko-graphics Color(...) 的 KotlinPoet 代码
 *
 * 注意：Color 构造参数具体是 Long/Int 取决于你项目里 Constants.graphicsColorName 指向的 Color 类。
 * moko-graphics 通常支持 Color(0xAARRGGBB) 这种形式。
 */
private fun ColorMetadata.Color.toColorCode(): CodeBlock {
    // ColorMetadata.Color.toArgbHex() 返回的是不带 0x 的 aarrggbb
    return CodeBlock.of("Color(0x%L)", this.toArgbHex().uppercase())
}
