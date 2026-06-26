package com.example.signtranslate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun createDataStore(context: Any?): DataStore<Preferences> {
    val docDir = NSFileManager.defaultManager.URLForDirectory(
        directory    = NSDocumentDirectory,
        inDomain     = NSUserDomainMask,
        appropriateForURL = null,
        create       = false,
        error        = null
    )!!.path!!
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
        produceFile = {
            val fileManager = NSFileManager.defaultManager
            val directory = fileManager.URLForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask,
                null,
                true,
                null
            )
            val path = directory!!.path + "/user_session.preferences_pb"
            platform.Foundation.NSURL.fileURLWithPath(path)
        }
    )
}