package com.makumatthew.vent.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

object DialogUtils {
    fun showSettingsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            //open settings from utils
            openSettings(context)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
            //todo check and confirm if finish is required here
            //finish();
        }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings(context: Context) {
        val myActivity = context as Activity
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.getPackageName(), null)
        intent.data = uri
        myActivity.startActivityForResult(intent, 101)
    }

}