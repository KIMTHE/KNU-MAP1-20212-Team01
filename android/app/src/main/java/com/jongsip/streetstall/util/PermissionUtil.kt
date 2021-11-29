package com.jongsip.streetstall.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {

    private const val PERMISSION_CODE_ACCEPTED = 1
    private const val PERMISSION_CODE_NOT_AVAILABLE = 0

    // 위치권한요청
    fun requestLocationPermission(context: Context): Int {
        val locationPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                // request permission
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_CODE_ACCEPTED
                )
            }
        } else {
            // already granted
            return PERMISSION_CODE_ACCEPTED
        }
        // not available
        return PERMISSION_CODE_NOT_AVAILABLE
    }

    // 외부저장소 접근권한요청
    fun requestStoragePermission(context: Context): Int {
        val readPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (readPermission == PackageManager.PERMISSION_DENIED) {
            // request permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_CODE_ACCEPTED
            )
        } else {
            // already granted
            return PERMISSION_CODE_ACCEPTED
        }
        // not available
        return PERMISSION_CODE_NOT_AVAILABLE
    }
}