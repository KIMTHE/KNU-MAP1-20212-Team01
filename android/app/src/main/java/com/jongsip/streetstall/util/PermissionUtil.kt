package com.jongsip.streetstall.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jongsip.streetstall.activity.CustomerMainActivity

object PermissionUtil {

    const val PERMISSION_CODE_ACCEPTED = 1
    const val PERMISSION_CODE_NOT_AVAILABLE = 0

    // 위치권한요청
    fun requestLocationPermission(context: Context): Int {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                // request permission
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
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
}