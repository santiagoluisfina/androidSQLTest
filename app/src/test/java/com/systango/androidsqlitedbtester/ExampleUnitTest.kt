package com.systango.androidsqlitedbtester

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    private lateinit var db: SQLiteDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sqLiteHelper = dbSQLiteHelper(context)
        db = sqLiteHelper.writableDatabase
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun obt_ok() {
        val libro = getLibro("2")
        assertEquals(1, 1)
    }

    private fun getLibro(idLibro: String?): Libro {
        val table = "TBL_LIBROS"
        val columnsToReturn = arrayOf("ID_LIBRO", "LIBRO", "ABREVIATURA")
        val selection = "id_libro = ?"
        val dbCursor = db.query(table, columnsToReturn, selection, arrayOf(idLibro), null, null, null)
        val libro = Libro()
        if(dbCursor.moveToFirst()){
            libro.idLibro = dbCursor.getString(0)
            libro.libro = dbCursor.getString(1)
            libro.abrev = dbCursor.getString(2)
        }
        dbCursor.close()

        return libro
    }
}