package com.example

interface CriterioDistribucionDinero {
    fun distribuir(monto: Double, proyectos: List<Proyecto>): List<Inversion>
}

/**
 * Partes iguales para cada proyecto
 * */
object PartesIguales : CriterioDistribucionDinero {
    override fun distribuir(monto: Double, proyectos: List<Proyecto>): List<Inversion> {
        val cantidadADistribuir = monto / proyectos.size
        return proyectos.map { proyecto -> Inversion(cantidadADistribuir, proyecto) }
    }
}

/**
 * Distribuir el 50% en el primer proyecto, y en partes iguales el resto
 * */
object CincuentaYPartesIguales : CriterioDistribucionDinero {
    override fun distribuir(monto: Double, proyectos: List<Proyecto>): List<Inversion> {
        val inversiones = mutableListOf<Inversion>()
        val primerProyecto = proyectos.first()
        val otrosProyectos = proyectos.drop(1)
        // Distribuir
        val cantidad = monto / 2
        inversiones.add(Inversion(cantidad, primerProyecto))
        inversiones.addAll(PartesIguales.distribuir(cantidad, otrosProyectos))
        return inversiones
    }
}

/**
 * Invertir 500 mangos en un proyecto al azar, y el resto en otro proyecto al azar
 * (recordemos que el mínimo que requiere el proceso son 1.000 mangos pero no necesariamente 1.000)
 */
object QuinientosAlAzarYRestoAlAzar : CriterioDistribucionDinero {
    private var primerMonto = 500.0

    override fun distribuir(monto: Double, proyectos: List<Proyecto>): List<Inversion> {
        val proyectosMezclados = proyectos.shuffled()
        return listOf(
            Inversion(primerMonto, proyectosMezclados.first()),
            Inversion(monto - primerMonto, proyectosMezclados.last())
        )
    }
}