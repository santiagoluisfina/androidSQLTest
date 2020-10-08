package com.systango.androidsqlitedbtester

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.*

class AuxDBQueries {
    private val TAG = "dbSQLiteHelper"
    private val dbConn: SQLiteDatabase = Globals.DBConnection

    private fun getLibro(idLibro: String?): Libro {
        val table = "TBL_LIBROS"
        val columnsToReturn = arrayOf(
            "ID_LIBRO",
            "LIBRO",
            "ABREVIATURA",
            "TESTAMENTO",
            "CATEGORIA",
            "CAPITULOS"
        )
        val selection = "id_libro = ?"
        val dbCursor = dbConn.query(
            table,
            columnsToReturn,
            selection,
            arrayOf(idLibro),
            null,
            null,
            null
        )
        val libro = Libro()
        if(dbCursor.moveToFirst()){
            libro.idLibro = dbCursor.getString(0)
            libro.libro = dbCursor.getString(1)
            libro.abrev = dbCursor.getString(2)
            libro.testamento = dbCursor.getString(3)
            libro.categoria = dbCursor.getString(4)
            libro.capitulos = dbCursor.getString(5)
        }
        dbCursor.close()

        return libro
    }

    fun verJoel() {
        val query = "select * from tbl_libros where id_libro = 1"
        val dbCursor = dbConn.rawQuery(query, null)
        dbCursor.moveToFirst()
        Log.e(TAG, "Columnas de tbl_libros:" + dbCursor.getColumnNames().joinToString())
        dbCursor.close()
    }

    fun testUpdate(){
        val strSQLUpd = "UPDATE TBL_LIBROS " +
                " SET ABREVIATURA = ABREVIATURA " +
                " WHERE ID_LIBRO = 1 "

        dbConn.beginTransaction()
        try {
            dbConn.execSQL(strSQLUpd)
            dbConn.setTransactionSuccessful()
        } finally {
            dbConn.endTransaction()
        }
    }
}