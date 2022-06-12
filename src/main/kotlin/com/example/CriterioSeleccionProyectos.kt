package com.example

interface CriterioSeleccionProyectos {
    fun seleccionar(proyectos: List<Proyecto>): List<Proyecto>
}

/**
 * Tomar los 3 proyectos con más impacto social
 */
object TresConMasImpactoSocial : CriterioSeleccionProyectos {
    override fun seleccionar(proyectos: List<Proyecto>): List<Proyecto> =
        ordenadosPorImpactoSocial(proyectos).take(3)

    private fun ordenadosPorImpactoSocial(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.sortedByDescending { it.impactoSocial() }
}

/**
 * Elegir el proyecto que más plata necesita y el que menos
 */
object MasYMenosPlata : CriterioSeleccionProyectos {
    override fun seleccionar(proyectos: List<Proyecto>): List<Proyecto> {
        val proyectosOrdenadosPorDinero = ordenadosPorDineroNecesario(proyectos)
        return listOf(proyectosOrdenadosPorDinero.first(), proyectosOrdenadosPorDinero.last())
    }

    private fun ordenadosPorDineroNecesario(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.sortedByDescending { it.dineroNecesario }
}

/**
 * Elegir únicamente proyectos nacionales
 */
object SoloNacionales : CriterioSeleccionProyectos {
    override fun seleccionar(proyectos: List<Proyecto>): List<Proyecto> =
        proyectos.filter { it.esNacional }
}

/**
 * Una combinatoria de las anteriores (OR)
 */
object Combinado : CriterioSeleccionProyectos {
    var criteriosDeSeleccion = listOf(TresConMasImpactoSocial, MasYMenosPlata, SoloNacionales) // Los elige un administrador
    override fun seleccionar(proyectos: List<Proyecto>): List<Proyecto> {
        val proyectosSeleccionados = mutableSetOf<Proyecto>()
        criteriosDeSeleccion.forEach {
            proyectosSeleccionados.addAll(it.seleccionar(proyectos))
        }
        return proyectosSeleccionados.toList()
    }
}