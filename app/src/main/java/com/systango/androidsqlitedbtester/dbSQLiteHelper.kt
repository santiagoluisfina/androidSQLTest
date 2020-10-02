package com.systango.androidsqlitedbtester

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import android.util.Log
import java.io.*

class dbSQLiteHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TAG = "dbSQLiteHelper"

    private fun installDatabaseFromAssets(context: Context) {
        val inputStream = context.applicationContext.assets.open(DATABASE_NAME)

        try {
            val outputFile = File(context.getDatabasePath(DATABASE_NAME).path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(TAG, "Creando la DB a partir de Assets")
        context.deleteDatabase(DATABASE_NAME)
        installDatabaseFromAssets(context)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(TAG, "Updating table from $oldVersion to $newVersion")
    }

    companion object {
        const val DATABASE_NAME = "DBtester.db"
        private const val DATABASE_VERSION = 1
    }
}