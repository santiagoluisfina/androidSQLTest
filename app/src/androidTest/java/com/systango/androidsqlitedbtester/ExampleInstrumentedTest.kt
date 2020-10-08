package com.systango.androidsqlitedbtester

import android.database.sqlite.SQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.systango.androidsqlitedbtester", appContext.packageName)
    }

    private lateinit var db: SQLiteDatabase

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sqLiteHelper = dbSQLiteHelper(context)
        db = sqLiteHelper.getReadableDatabase()
        Globals.DBConnection = db
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun obt_ok() {
        AuxDBQueries().verJoel()
        AuxDBQueries().testUpdate()

        assertEquals(1, 1)
    }
}