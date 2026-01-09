/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import kotlin.experimental.ExperimentalNativeApi

object Utils {
    @OptIn(ExperimentalNativeApi::class)
    @CName("Utils_processArgs")
    fun processArgs(args: List<Any>): List<Any> {
        return args.map { (it as? StringDesc)?.localized() ?: it } // 鸿蒙Native无Context
    }
}