package com.example

class ProcesoDeInversion(
    var eleccionDeProyectos: EleccionDeProyectos, // Strategy
    var formaDeDistribucion: FormaDeDistribucion, // Strategy
    var proyectosSeleccionados: List<Proyecto>
) {
    var dineroAInvertir: Double = MIN_DINERO_A_INVERTIR
        set(value) {
            if (value < MIN_DINERO_A_INVERTIR) throw IllegalArgumentException()
            field = value
        }

    fun seleccionarProyectos(proyectosDisponibles: List<Proyecto>) {
        proyectosSeleccionados = eleccionDeProyectos.elegir(proyectosDisponibles)
    }

    fun distribuirDinero() {
        formaDeDistribucion.distribuir(proyectosSeleccionados)
    }

    fun iniciarProceso(proyectosDisponibles: List<Proyecto>){
        seleccionarProyectos(proyectosDisponibles)
        distribuirDinero()
    }

    fun confirmarInversion(cuentaID: String){
        proyectosSeleccionados.forEach{
            it.invertir(dineroAInvertir, cuentaID)
        }
    }

    companion object {
        private const val MIN_DINERO_A_INVERTIR: Double = 1000.0
    }
}