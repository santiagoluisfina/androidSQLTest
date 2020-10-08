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
    private val preferences: SharedPreferences = context.getSharedPreferences("${context.packageName}.database_versions", Context.MODE_PRIVATE)

    companion object {
        const val DATABASE_NAME = "DBtester.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(TAG, "Ejecutando onCreate")
/*
        Log.e(TAG, "Creando la DB a partir de Assets")
        context.deleteDatabase(DATABASE_NAME)
        installDatabaseFromAssets(context)
*/
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(TAG, "Upgrading DB from $oldVersion to $newVersion")
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        for (i in oldVersion until newVersion) {
            val migrationName = String.format("DB_from_%d_to_%d.sql", i, i + 1)
            Log.d(TAG, "Looking for migration file: $migrationName")
            readAndExecuteSQLScript(db, migrationName)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.e(TAG, "Downgrading DB from $oldVersion to $newVersion")
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        for (i in oldVersion downTo newVersion step 1) {
            val migrationName = String.format("DB_downgrade_%d_to_%d.sql", i, i - 1)
            Log.d(TAG, "Looking for migration file: $migrationName")
            readAndExecuteSQLScript(db!!, migrationName)
        }
    }

    private fun executeSQLScript(db: SQLiteDatabase, reader: BufferedReader?) {
        var statement = StringBuilder()

        reader?.useLines { lines ->
            lines.forEach {line ->
                statement.append(line)
                if (line.endsWith(";")) {
                    db.execSQL(statement.toString())
                    statement = StringBuilder()
                }
            }
        }
    }

    private fun readAndExecuteSQLScript(db: SQLiteDatabase, fileName: String) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty")
            return
        }

        val cursor: Cursor = db.rawQuery("select sqlite_version() AS sqlite_version", null)
        var sqliteVersion = ""
        while (cursor.moveToNext()) {
            sqliteVersion += cursor.getString(0)
            Log.d(TAG, "SQLite version:" + sqliteVersion)
        }
        cursor.close()

        Log.d(TAG, "Script found. Executing...")
        var reader: BufferedReader? = null
        try {
            val inputStream: InputStream = context.applicationContext.assets.open(fileName)
            val isr = InputStreamReader(inputStream)
            reader = BufferedReader(isr)
            executeSQLScript(db, reader)
        } catch (e: IOException) {
            Log.e(TAG, "IOException:", e)
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e(TAG, "IOException:", e)
                }
            }
        }
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installIfNecessary()
        return super.getReadableDatabase()
    }

    @Synchronized
    private fun installIfNecessary() {
        Log.e(TAG, "DBVersion en Preferences: " + preferences.getInt(DATABASE_NAME, 0))
        if (preferences.getInt(DATABASE_NAME, 0) == 0) {
            installDatabaseFromAssets()
        }

        writeDatabaseVersionInPreferences()
    }

    private fun writeDatabaseVersionInPreferences() {
        preferences.edit().apply {
            putInt(DATABASE_NAME, DATABASE_VERSION)
            apply()
        }
    }

    private fun installDatabaseFromAssets() {
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

}