/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

/**
 * HarmonyOS Native 平台资源详情
 */
actual class ResourcePlatformDetails(
    val resourcesBasePath: String = "/res/raw"
) {
    /**
     * 获取资源完整路径
     */
    fun getFullPath(relativePath: String): String {
        return "$resourcesBasePath/$relativePath"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResourcePlatformDetails) return false
        return resourcesBasePath == other.resourcesBasePath
    }

    override fun hashCode(): Int = resourcesBasePath.hashCode()
}