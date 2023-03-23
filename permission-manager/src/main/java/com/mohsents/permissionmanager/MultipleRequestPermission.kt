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
import com.mohsents.permissionmanager.PermissionHelper.arePermissionDeniedWithNeverAskAgain
import com.mohsents.permissionmanager.PermissionHelper.arePermissionNeedsRationale
import com.mohsents.permissionmanager.PermissionHelper.arePermissionsGranted
import com.mohsents.permissionmanager.PermissionHelper.removeDeniedPermission
import com.mohsents.permissionmanager.PermissionHelper.storeDeniedPermission

internal class MultipleRequestPermission(private val activity: ComponentActivity) {
    private var onResultCallback: (PermissionResult) -> Unit = {}
    private var requestedPermission: Array<String> = arrayOf("")
    private val permissionLauncher = activity.registerForMultiplePermissionRequest { result ->
        result.forEach { if (!it.value) storeDeniedPermission(activity, it.key) }
        onResultCallback(result)
    }

    fun request(
        permissions: Array<String>,
        onResult: (PermissionResult) -> Unit,
        onNeedRationale: (MultiplePermissionWrapper, Array<String>) -> Unit,
        onNeverAskAgain: (Array<String>) -> Unit
    ) {
        requestedPermission = permissions
        onResultCallback = onResult
        // Remove any stored permission if user grant it in app's settings.
        permissions.forEach {
            if (arePermissionsGranted(activity, it)) removeDeniedPermission(activity, it)
        }

        when {
            permissions.all {
                arePermissionsGranted(
                    activity,
                    it
                )
            } -> onResult(permissions.associateWith { true })
            permissions.any { arePermissionNeedsRationale(activity, it) } -> onNeedRationale(
                MultiplePermissionWrapper(
                    permissionLauncher,
                    whichPermissionNeedsRationale(activity, permissions)
                ), whichPermissionNeedsRationale(activity, permissions)
            )
            permissions.any {
                arePermissionDeniedWithNeverAskAgain(
                    activity,
                    it
                )
            } -> onNeverAskAgain(permissions.filter {
                arePermissionDeniedWithNeverAskAgain(
                    activity,
                    it
                )
            }.toTypedArray())
            else -> permissionLauncher.launch(permissions)
        }
    }

    private fun whichPermissionNeedsRationale(
        activity: ComponentActivity,
        permissions: Array<String>
    ): Array<String> {
        return permissions.filter { arePermissionNeedsRationale(activity, it) }.toTypedArray()
    }
}

typealias PermissionResult = Map<String, Boolean>
