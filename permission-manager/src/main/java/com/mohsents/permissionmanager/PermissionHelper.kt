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

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal object PermissionHelper {

    fun arePermissionsGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun arePermissionNeedsRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    fun storeDeniedPermission(context: Context, permission: String) {
        val shredPreference = getSharedPreferences(context)
        val editor = shredPreference.edit()
        editor.putBoolean(permission, true).apply()
    }

    fun removeDeniedPermission(context: Context, permission: String) {
        val shredPreference = getSharedPreferences(context)
        val editor = shredPreference.edit()
        editor.remove(permission).apply()
    }

    fun arePermissionDeniedWithNeverAskAgain(context: Context, permission: String): Boolean {
        return arePermissionDeniedBefore(context, permission) && !arePermissionNeedsRationale(
            context as Activity,
            permission
        )
    }

    private fun arePermissionDeniedBefore(context: Context, permission: String): Boolean {
        val shredPreference = getSharedPreferences(context)
        return shredPreference.getBoolean(permission, false)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private const val PREFERENCE_NAME = "denied-permissions"
}
