/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ReactAppPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val config = project.extensions.create("reactApp", ReactAppExtension::class.java, project)

    project.afterEvaluate {
      val androidConfiguration = project.extensions.getByType(BaseExtension::class.java)
      project.configureDevPorts(androidConfiguration)

      val isAndroidLibrary = project.plugins.hasPlugin("com.android.library")
      val variants =
          if (isAndroidLibrary) {
            project.extensions.getByType(LibraryExtension::class.java).libraryVariants
          } else {
            project.extensions.getByType(AppExtension::class.java).applicationVariants
          }
      variants.all { project.configureReactTasks(variant = it, config = config) }
    }
  }
}
