package com.systango.androidsqlitedbtester

/**
 * Created by Santiago Fina on 13/06/2020.
 */
data class Usuario (
    var primeraVez: Int = 1,
    var conPublicidad: Int = 1,
    var nombre: String? = null,
    var apodo: String? = null,
    var mail: String? = null,
    var idUsuario: String? = null
)