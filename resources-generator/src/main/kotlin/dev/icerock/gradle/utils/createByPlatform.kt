/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("Filename")

package dev.icerock.gradle.utils

import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.konan.target.KonanTarget

@Suppress("LongParameterList")
internal fun <T> createByPlatform(
    kotlinPlatformType: KotlinPlatformType,
    konanTarget: () -> KonanTarget,
    createCommon: () -> T,
    createAndroid: () -> T,
    createApple: () -> T,
    createJvm: () -> T,
    createJs: () -> T,
    createWasm: () -> T,
    createLinux: () -> T = createJvm, // 新增参数，默认复用 JVM 的实现
): T {
    return when (kotlinPlatformType) {
        KotlinPlatformType.common -> createCommon()
        KotlinPlatformType.jvm -> createJvm()
        KotlinPlatformType.androidJvm -> createAndroid()
        KotlinPlatformType.js -> createJs()
        KotlinPlatformType.native -> when (konanTarget()) {
            KonanTarget.IOS_ARM64,
            KonanTarget.IOS_SIMULATOR_ARM64,
            KonanTarget.IOS_X64,

            KonanTarget.MACOS_ARM64,
            KonanTarget.MACOS_X64,

            KonanTarget.TVOS_ARM64,
            KonanTarget.TVOS_SIMULATOR_ARM64,
            KonanTarget.TVOS_X64,

            KonanTarget.WATCHOS_ARM32,
            KonanTarget.WATCHOS_ARM64,
            KonanTarget.WATCHOS_DEVICE_ARM64,
            KonanTarget.WATCHOS_SIMULATOR_ARM64,
            KonanTarget.WATCHOS_X64 -> createApple()

            // 新增 Linux 支持
            KonanTarget.LINUX_X64,
            KonanTarget.LINUX_ARM64 -> createLinux()

            else -> error("${konanTarget()} not supported by moko-resources now")
        }

        KotlinPlatformType.wasm -> createWasm()
    }
}
