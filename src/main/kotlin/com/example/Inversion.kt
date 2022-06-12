package com.example

data class Inversion(val monto: Double, val proyecto: Proyecto){
    fun invertir() {
        proyecto.recibirDonacion(monto)
    }
}
