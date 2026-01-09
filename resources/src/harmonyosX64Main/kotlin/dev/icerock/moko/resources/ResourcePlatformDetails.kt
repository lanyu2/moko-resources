/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

actual class ResourcePlatformDetails {
    val resourcesBasePath: String = "/res/raw"

    fun getFullPath(relativePath: String): String = "$resourcesBasePath/$relativePath"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is ResourcePlatformDetails
    }

    override fun hashCode(): Int = resourcesBasePath.hashCode()
}