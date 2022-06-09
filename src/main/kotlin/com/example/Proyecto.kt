package com.example

import java.time.LocalDate
import kotlin.math.roundToInt

abstract class Proyecto(
    var nombre: String,
    var descripcion: String,
    var dineroNecesario: Int,
    var cuentaBancaria: CuentaBancaria,
    var personasResponsables: List<ResponsableDeProyecto>,
    var esNacional: Boolean,
) {

    var cantDonaciones: Int = 0

    init {
        validarNombre()
        validarDineroNecesario()
        validarPersonasResponsables()
    }

    private fun validarPersonasResponsables() {
        if (personasResponsables.isEmpty()) throw NoResponsablesException()
    }

    private fun validarDineroNecesario() {
        if (dineroNecesario < 0) throw NegativeAmountException("El dinero necesario para un proyecto no puede ser negativo")
    }

    private fun validarNombre() {
        if (nombre.isBlank()) throw NoProjectNameException()
    }

    /**
     * Para todos los proyectos es un 10% de la cantidad de plata que necesita para llevarse a cabo
     * a lo que se suma el impacto social extra dependiendo del tipo de proyecto
     * */
    fun impactoSocial(): Int = (dineroNecesario * 0.1).roundToInt() + impactoSocialExtra()

    abstract fun impactoSocialExtra(): Int

    fun invertir(dineroAInvertir: Double, cuentaOrigen: String) {
        val montoString = dineroAInvertir.toString().split('.', limit = 2)
        val montoEntero = montoString.first().toInt()
        val montoDecimales = montoString.lastOrNull()?.toInt() ?: 0

        cuentaBancaria.sistemaBancario.transferir(cuentaOrigen, cuentaBancaria.cuentaId, montoEntero, montoDecimales)
    }
}

class ProyectoSocial(
    nombre: String,
    descripcion: String,
    dineroNecesario: Int,
    cuentaBancaria: CuentaBancaria,
    personasResponsables: List<ResponsableDeProyecto>,
    esNacional: Boolean,
    private var fechaInicio: LocalDate
) : Proyecto(
    nombre,
    descripcion,
    dineroNecesario,
    cuentaBancaria,
    personasResponsables,
    esNacional,
) {

    init {
        validarFechaInicio()
    }

    private fun validarFechaInicio() {
        if (!fechaInicio.isBefore(LocalDate.now())) throw IllegalDateException()
    }

    /**
     * En el caso de los proyectos sociales, 100 puntos por cada año desde su inicio.
     * Por ejemplo, un proyecto que inició el 14/12/2017 tiene actualmente 3 años,
     * serían 300 puntos a junio del 2021 (no se cuenta el año que está transcurriendo).
     */
    override fun impactoSocialExtra(): Int = 100 * aniosDesdeInicio()

    private fun aniosDesdeInicio(): Int = LocalDate.now().minusYears(fechaInicio.year.toLong()).year

}

class ProyectoCooperativa(
    nombre: String,
    descripcion: String,
    dineroNecesario: Int,
    cuentaBancaria: CuentaBancaria,
    personasResponsables: List<ResponsableDeProyecto>,
    esNacional: Boolean,
    private var socios: List<String>
) : Proyecto(
    nombre,
    descripcion,
    dineroNecesario,
    cuentaBancaria,
    personasResponsables,
    esNacional,
) {
    /**
     * En el caso de las cooperativas, son 45 puntos por cada socio que tiene doble apellido
     * o 30 en caso contrario.
     */
    override fun impactoSocialExtra(): Int = socios.sumOf { (if (tieneDobleApellido(it)) 45 else 30).toInt() }

    private fun tieneDobleApellido(apellido: String): Boolean = apellido.split(' ').size == 2
}

class ProyectoEcologico(
    nombre: String,
    descripcion: String,
    dineroNecesario: Int,
    cuentaBancaria: CuentaBancaria,
    personasResponsables: List<ResponsableDeProyecto>,
    esNacional: Boolean,
    private var areaDeterminada: Double
) : Proyecto(
    nombre,
    descripcion,
    dineroNecesario,
    cuentaBancaria,
    personasResponsables,
    esNacional,
) {
    /**
     * En el caso de los proyectos ecológicos, son los metros cuadrados * 10.
     * */
    override fun impactoSocialExtra(): Int = (areaDeterminada * 10).roundToInt()
}