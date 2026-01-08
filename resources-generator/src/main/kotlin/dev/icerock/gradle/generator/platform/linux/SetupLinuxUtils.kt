/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.platform.linux

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import java.io.File

/**
 * 为 Linux 平台配置 KLib 资源
 *
 * Linux 平台需要将资源文件嵌入到生成的 .so 文件中或放在指定目录
 */
fun setupLinuxKLibResources(
    compileTask: KotlinNativeCompile,
    assetsDirectory: Provider<File>,
    resourcesGenerationDir: Provider<File>
) {
    compileTask.doFirst {
        val assetsDir = assetsDirectory.orNull
        val resourcesDir = resourcesGenerationDir.orNull

        // 记录资源目录信息（用于调试）
        if (assetsDir?.exists() == true) {
            compileTask.logger.lifecycle(
                "[moko-resources] Linux assets directory: ${assetsDir.absolutePath}"
            )
        }

        if (resourcesDir?.exists() == true) {
            compileTask.logger.lifecycle(
                "[moko-resources] Linux resources directory: ${resourcesDir.absolutePath}"
            )
        }
    }

    // 将资源目录添加到编译后的输出中
    compileTask.doLast {
        val outputDir = compileTask.outputFile.get().parentFile
        val assetsDir = assetsDirectory.orNull
        val resourcesDir = resourcesGenerationDir.orNull

        // 复制资源到编译输出目录（与 .klib 同级）
        if (assetsDir?.exists() == true) {
            val targetAssetsDir = File(outputDir, "assets")
            targetAssetsDir.mkdirs()
            assetsDir.copyRecursively(targetAssetsDir, overwrite = true)
            compileTask.logger.lifecycle(
                "[moko-resources] Copied assets to: ${targetAssetsDir.absolutePath}"
            )
        }

        if (resourcesDir?.exists() == true) {
            val targetResourcesDir = File(outputDir, "resources")
            targetResourcesDir.mkdirs()
            resourcesDir.copyRecursively(targetResourcesDir, overwrite = true)
            compileTask.logger.lifecycle(
                "[moko-resources] Copied resources to: ${targetResourcesDir.absolutePath}"
            )
        }
    }
}