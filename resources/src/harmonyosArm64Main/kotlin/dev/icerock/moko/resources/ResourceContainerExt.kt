/*
 * Copyright 2026 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

/**
 * 在 Linux 平台上不支持通过文件名动态获取图片资源。
 * 请使用 [getImageByFileNameWithFallback] 或直接访问 MR.images.xxx
 */
actual fun ResourceContainer<ImageResource>.getImageByFileName(
    fileName: String
): ImageResource? = null

actual fun ResourceContainer<AssetResource>.getAssetByFilePath(
    filePath: String
): AssetResource? = AssetResource(filePath.removeFirstSlash())

/**
 * Linux 平台专用：带回退的图片获取方法
 */
fun ResourceContainer<ImageResource>.getImageByFileNameWithFallback(
    fileName: String,
    fallback: ImageResource
): ImageResource = getImageByFileName(fileName) ?: fallback