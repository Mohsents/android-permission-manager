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

internal class SinglePermissionRequest(private val activity: ComponentActivity) {
    private var onGrantResultCallback: () -> Unit = {}
    private var onDeniedResultCallback: () -> Unit = {}
    private var requestedPermission: String = ""
    private val permissionLauncher = activity.registerForSinglePermissionRequest { result ->
        if (result) {
            onGrantResultCallback()
        } else {
            storeDeniedPermission(activity, requestedPermission)
            onDeniedResultCallback()
        }
    }

    fun request(
        permission: String,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
        onNeedRationale: (SinglePermissionWrapper) -> Unit,
        onNeverAskAgain: () -> Unit
    ) {
        requestedPermission = permission
        onGrantResultCallback = onGranted
        onDeniedResultCallback = onDenied

        when {
            arePermissionsGranted(activity, permission) -> { removeDeniedPermission(activity, permission); onGrantResultCallback() }
            arePermissionNeedsRationale(activity, permission) -> onNeedRationale(
                SinglePermissionWrapper(
                    permissionLauncher, permission
                )
            )
            arePermissionDeniedWithNeverAskAgain(activity, permission) -> onNeverAskAgain()
            else -> permissionLauncher.launch(permission)
        }
    }
}
