/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-extended-convention")
    id("multiplatform-android-publish-convention")
    id("apple-main-convention")
    id("detekt-convention")
    id("javadoc-stub-convention")
    id("publication-convention")
}

android {
    namespace = "dev.icerock.moko.resources.test"
}

dependencies {
    commonMainApi(projects.resources)
}
kotlin{
    // 新增 Linux targets（用于 HarmonyOS）
    linuxX64("harmonyosX64") {
        binaries {
            executable {  }
            sharedLib {
                baseName = "mokoResources"
            }
        }
        compilations.all{
            compileTaskProvider.configure {
                compilerOptions{
                    freeCompilerArgs.addAll(
                        listOf(
                            "-linker-options", "-Wl,--as-needed",
                            "-Xoverride-konan-properties=linkerGccFlags=-lgcc"
                        )
                    )
                }
            }
        }
    }

    linuxArm64("harmonyosArm64") {
        binaries {
            executable {  }
            sharedLib {
                baseName = "mokoResources"
            }
        }
        compilations.all{
            compileTaskProvider.configure {
                compilerOptions{
                    freeCompilerArgs.addAll(
                        listOf(
                            "-linker-options", "-Wl,--as-needed",
                            "-Xoverride-konan-properties=linkerGccFlags=-lgcc"
                        )
                    )
                }
            }
        }
    }

}
