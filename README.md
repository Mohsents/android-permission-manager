# Android Permission Manager

[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)

## About

Android Permission Manager simplifies requesting and
handling [Runtime Permissions](https://developer.android.com/training/permissions/requesting?authuser=1)
in android.

## Usage

First in your `Activity` before any lifecycle callbacks create a instance of `PermissionManager`.
Note that your `Activity` must extends from `ComponentActivity`:

```
class MainActivity : ComponentActivity() {
    private val permissionManager = PermissionManager.from(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            
        }
    }
```

For requesting single permission:

```
 permissionManager.requestPermission(
            android.Manifest.permission.CAMERA,
            onGrant = {
                // Open camera to take photo.
                cameraLauncher.launch(Uri.EMPTY)
                // Camera permission granted
            },
            onDenied = {
                // Camera permission denied
            },
            onNeedRationale = { singlePermissionWrapper ->
                // Camera permission needs rationale
                // Request permission again
                singlePermissionWrapper.request() 
            },
            onNeverAskAgain = {
                // Camera permission denied with never ask again
                // Means the user denied the permission and checked the box of never ask again
            }
        )
```

For multiple permission request:

```
permissionManager.requestMultiplePermissions(
            arrayOf(
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            onResult = { result: Map<String, Boolean>
                result.map {
                // The key represent the permission and corresponding value means that permission granted or not
                    if (it.value) {
                        // Permission granted
                    } else {
                        // Permission denied
                    }
                }
            },
            onNeedRationale = { multiplePermissionWrapper, permissions ->
                // Request permission again
                multiplePermissionWrapper.request()
                permissions.forEach { permission ->
                    // Permission that needs rationale
                }
            },
            onNeverAskAgain = { permissions ->
                    permissions.foreach {
                        // Permission denied with never ask again
                    }
                }
            })
```

If you are in Compose context, request the permission in `SideEffect {}` when screen recomposed:
```
// You can use SideEffect() to request the permission
SideEffect {
    permissionManager.requestPermission() ...
}
```

### !Currently the artifact not available on any repo. It's be available soon in the future.

## Contact to developer

[Linkedin](https://www.linkedin.com/in/mohsents/)

_Give me feedback to **developer.mohsents@gmail.com**_

Every pull requests are receptive.

## Licence
```
  Copyright (C) 2023 Mohsents
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
```
