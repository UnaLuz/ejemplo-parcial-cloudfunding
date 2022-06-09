package com.example

interface EleccionDeProyectos {
    fun elegir(proyectos: List<Proyecto>): List<Proyecto>
}

/**
 * Tomar los 3 proyectos con más impacto social
 */
object TresConMasImpactoSocial : EleccionDeProyectos {
    override fun elegir(proyectos: List<Proyecto>): List<Proyecto> =
        ordenadosPorImpactoSocial(proyectos).take(3)

    private fun ordenadosPorImpactoSocial(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.sortedByDescending { it.impactoSocial() }
}

/**
 * Elegir el proyecto que más plata necesita y el que menos
 */
object MasYMenosPlata : EleccionDeProyectos {
    override fun elegir(proyectos: List<Proyecto>): List<Proyecto> {
        val proyectosOrdenadosPorDinero = ordenadosPorDineroNecesario(proyectos)
        return listOf(proyectosOrdenadosPorDinero.first(), proyectosOrdenadosPorDinero.last())
    }

    private fun ordenadosPorDineroNecesario(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.sortedByDescending { it.dineroNecesario }
}

/**
 * Elegir únicamente proyectos nacionales
 */
object SoloNacionales : EleccionDeProyectos {
    override fun elegir(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.filter { it.esNacional }
}

/**
 * Una combinatoria de las anteriores (OR)
 */
class Combinado(var formasDeEleccionDeProyectos: List<EleccionDeProyectos>) : EleccionDeProyectos {
    override fun elegir(proyectos: List<Proyecto>): List<Proyecto> {
        TODO("Se combinan todos? o los que se elija?")
    }
}