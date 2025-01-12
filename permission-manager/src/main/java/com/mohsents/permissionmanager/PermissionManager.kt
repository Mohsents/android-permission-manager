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

package com.mohsents.permissionmanager

import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference
import kotlin.jvm.JvmStatic

@ExperimentalPermissionManagerApi
class PermissionManager private constructor(
    activity: WeakReference<ComponentActivity>
) {
    private val singlePermissionRequest = SinglePermissionRequest(activity.get()!!)
    private val multiplePermissionRequest = MultipleRequestPermission(activity.get()!!)

    companion object {
        @JvmStatic
        fun from(activity: ComponentActivity): PermissionManager =
            PermissionManager(WeakReference(activity))
    }

    /**
     * Requests a single permission from user.
     *
     * @param permission permission to request
     * @param onGrant Called when the user grants permission
     * @param onDenied Called when the user denied the permission
     * @param onNeedRationale if an permission denied and and asked later, this will be called.
     * means the permission needs rationale and the user needs more information about why you asked this.
     * @param onNeverAskAgain if permission denied and the user does not want to ask it again, will be called
     */
    fun requestPermission(
        permission: String,
        onGrant: () -> Unit,
        onDenied: () -> Unit,
        onNeedRationale: (SinglePermissionWrapper) -> Unit,
        onNeverAskAgain: () -> Unit
    ) {
        singlePermissionRequest.request(
            permission,
            onGrant,
            onDenied,
            onNeedRationale,
            onNeverAskAgain
        )
    }

    /**
     * Requests multiple permission from user.
     *
     * @param permissions permissions to request
     * @param onResult Called when the result of asked permission backed from user
     * @param onNeedRationale if an permission denied and and asked later, this will be called.
     * means the permission needs rationale and the user needs more information about why you asked this.
     * @param onNeverAskAgain when the result of asked permission returns, will be called.
     * It contains both granted permissions and denied permissions result.
     */
    fun requestMultiplePermissions(
        permissions: Array<String>,
        onResult: (PermissionResult) -> Unit,
        onNeedRationale: (MultiplePermissionWrapper, Array<String>) -> Unit,
        onNeverAskAgain: (Array<String>) -> Unit
    ) {
        multiplePermissionRequest.request(
            permissions,
            onResult,
            onNeedRationale,
            onNeverAskAgain
        )
    }
}
