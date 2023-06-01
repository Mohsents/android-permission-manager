/*
 * Copyright (C) 2023 Mohsents
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            version("lib", "0.0.9")
            version("compose", "1.2.0")
            library("kotlin-stdlib","org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
            library("androidx-core","androidx.core:core-ktx:1.9.0")
            library("androidx-appcompat","androidx.appcompat:appcompat:1.6.0")
            library("androidx-material","com.google.android.material:material:1.7.0")
            library("androidx-lifecycle-runtime","androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
            library("androidx-activity-compose","androidx.activity:activity-compose:1.6.1")
            library("compose-bom","androidx.compose:compose-bom:2023.01.00")
            library("compose-material3","androidx.compose.material3:material3:1.1.0-alpha08")
        }
    }
}

rootProject.name = "Permission Manager"
include(":demo", ":permission-manager")
