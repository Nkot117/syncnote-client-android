package com.nkot117.syncnoteclientapp.util

import android.util.Log

object LogUtil {
    private const val TAG = "SyncNoteClientAppLog"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun e(message: String) {
        Log.e(TAG, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }

    fun v(message: String) {
        Log.v(TAG, message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
    }
}