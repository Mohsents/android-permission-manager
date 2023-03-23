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

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohsents.permissionmanager.ui.theme.PermissionManagerDemoTheme

class MainActivity : ComponentActivity() {
    private val permissionManager = PermissionManager.from(this)
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionManagerDemoTheme {
                Surface {
                    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                        Column {
                            Button(onClick = { requestCameraPermission() }) {
                                Text(text = "Request Camera Permission")
                            }
                            Spacer(modifier = Modifier.size(20.dp))
                            Button(onClick = { requestMultiplePermission() }) {
                                Text(text = "Request Multiple Permission")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showRationaleDialog(action: () -> Unit) {
        AlertDialog.Builder(this).apply {
            setTitle("Permission Rationale")
            setMessage("We need this permission, please Grant it!")
        }.setPositiveButton("Request Permission Again") { _, _ ->
            action()
        }.show()
    }

    private fun requestMultiplePermission() {
        permissionManager.requestMultiplePermissions(
            arrayOf(
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            onResult = {
                it.map { result ->
                    val text = if (result.value) "Granted" else "Denied"
                    Toast.makeText(
                        this,
                        "${result.key.substringAfterLast('.')}: $text",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onNeedRationale = { multiplePermissionWrapper, permissions ->
                showRationaleDialog { multiplePermissionWrapper.request() }
                permissions.forEach { permission ->
                    Toast.makeText(
                        this,
                        "${permission.substringAfterLast('.')} needs rationale",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onNeverAskAgain = { result ->
                result.map { permission ->
                    Toast.makeText(
                        this,
                        "${permission.substringAfterLast('.')} denied with never ask again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun requestCameraPermission() {
        permissionManager.requestPermission(
            android.Manifest.permission.CAMERA,
            onGrant = {
                // Open camera to take photo.
                cameraLauncher.launch(Uri.EMPTY)
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            },
            onDenied = {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            },
            onNeedRationale = { singlePermissionWrapper ->
                showRationaleDialog { singlePermissionWrapper.request() }
                Toast.makeText(this, "Camera permission needs rationale", Toast.LENGTH_SHORT).show()
            },
            onNeverAskAgain = {
                Toast.makeText(
                    this,
                    "Camera permission denied with never ask again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}
