package com.systango.androidsqlitedbtester

import android.content.Context

object Tools {
    fun crearDB(context: Context) {
        val sqLiteHelper = dbSQLiteHelper(context)
        val db = sqLiteHelper.getReadableDatabase()
/*
        db.close()
        db = sqLiteHelper.readableDatabase
*/
        Globals.DBConnection = db
    }
}

