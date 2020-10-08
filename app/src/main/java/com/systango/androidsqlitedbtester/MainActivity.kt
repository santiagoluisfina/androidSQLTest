package com.systango.androidsqlitedbtester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarDatos()

        AuxDBQueries().verJoel()
        AuxDBQueries().testUpdate()
    }

    private fun inicializarDatos() {
        Tools.crearDB(this)
    }
}