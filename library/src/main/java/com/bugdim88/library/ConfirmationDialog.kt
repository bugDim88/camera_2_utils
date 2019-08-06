package com.bugdim88.library

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(activity)
            .setMessage(_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                parentFragment?.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                parentFragment?.activity?.finish()
            }
            .create()

    private val _message: String
        get() = arguments?.getString(ARG_MESSAGE)?:resources.getString(R.string.request_permission)

    companion object {

        @JvmStatic
        private val ARG_MESSAGE = "message"

        @JvmStatic
        fun newInstance(confirmMessage: String): ErrorDialog = ErrorDialog().apply {
            arguments = Bundle().apply { putString(ARG_MESSAGE, confirmMessage) }
        }
    }
}