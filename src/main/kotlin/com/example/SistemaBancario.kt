package com.example

interface SistemaBancario {
    /**
     * La transferencia devuelve un String vacío si todo funcionó ok
     * o un mensaje de error en caso contrario.
     *
     * En caso de transferir 3855,50 pesos debemos indicar montoEntero = 3855 y montoDecimales = 50
     *
     * Siempre va depositoInmediato y nunca 24 hs.
     * */
    fun transferir(
        cuentaOrigenId: String,
        cuentaDestinoId: String,
        montoEntero: Int,
        montoDecimales: Int,
        depositoInmediato: Boolean = true,
        deposito24hs: Boolean = false
    ): String
}