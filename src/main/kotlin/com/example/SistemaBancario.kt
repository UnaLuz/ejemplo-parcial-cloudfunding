package com.example

interface SistemaBancario {
    fun transferir(
        cuentaOrigenId: String,
        cuentaDestinoId: String,
        montoEntero: Int,
        montoDecimales: Int,
        depositoInmediato: Boolean = true,
        deposito24hs: Boolean = false
    ): String
}