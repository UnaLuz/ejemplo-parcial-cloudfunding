package com.example

class ProcesoCloudfunding(
    var proyectosDisponibles: List<Proyecto>
) {
    private var inversiones: List<Inversion> = listOf()
    var criterioDistribucionSeleccionado: CriterioDistribucionDinero = PartesIguales
    var dineroAInvertir: Double = MIN_DINERO_A_INVERTIR
        set(value) {
            validarDineroAInvertir(value)
            field = value
        }

    private fun validarDineroAInvertir(dinero: Double) {
        if (dinero < MIN_DINERO_A_INVERTIR) throw MoneyValueTooLowException("El dinero a invertir no debe ser menor que $MIN_DINERO_A_INVERTIR")
    }

    /**
     * La interfaz de usuario está fuera del alcance del examen,
     * pero sí sabemos que el sistema le muestra los proyectos seleccionados en cada propuesta
     * */
    fun sugerenciasDeInversion(): List<PropuestaDeProyectos> =
        criteriosSeleccion.map {
            PropuestaDeProyectos(it, it.seleccionar(proyectosDisponibles))
        }

    fun distribuirDinero(propuestaSeleccionada: PropuestaDeProyectos) {
        validarPropuesta(propuestaSeleccionada)
        inversiones = criterioDistribucionSeleccionado.distribuir(dineroAInvertir, propuestaSeleccionada.proyectos)
    }

    private fun validarPropuesta(propuestaSeleccionada: PropuestaDeProyectos) {
        if (propuestaSeleccionada.proyectos.size < MIN_CANT_PROYECTOS)
            throw NotEnoughtProjectsException("No hay proyectos suficientes para invertir, seleccione una propuesta con $MIN_CANT_PROYECTOS o más")
    }

    fun confirmarDistribucionDinero() {
        validarInversiones()
        inversiones.forEach { it.invertir() }
    }

    private fun validarInversiones() {
        if (inversiones.isEmpty()) throw NoInvestmentsException("No hay inversiones posibles para realizar")
    }

    companion object {
        private const val MIN_DINERO_A_INVERTIR: Double = 1000.0
        private const val MIN_CANT_PROYECTOS: Int = 2
        val criteriosSeleccion = mutableListOf(
            TresConMasImpactoSocial,
            MasYMenosPlata,
            SoloNacionales,
            Combinado
        )
        val criteriosDistribucionDinero = mutableListOf(
            PartesIguales, CincuentaYPartesIguales, QuinientosAlAzarYRestoAlAzar
        )
    }
}

data class PropuestaDeProyectos(val criterioSeleccion: CriterioSeleccionProyectos, val proyectos: List<Proyecto>)