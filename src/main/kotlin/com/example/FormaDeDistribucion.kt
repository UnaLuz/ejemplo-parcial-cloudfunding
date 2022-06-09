package com.example

interface FormaDeDistribucion {
    fun distribuir(proyectos: List<Proyecto>)
}

/**
 * Partes iguales para cada proyecto
 * */
object PartesIguales : FormaDeDistribucion {
    override fun distribuir(proyectos: List<Proyecto>) {
        TODO("Not yet implemented")
    }
}

/**
 * Distribuir el 50% en el primer proyecto, y en partes iguales el resto
 * */
object CincuentaYPartesIguales : FormaDeDistribucion {
    override fun distribuir(proyectos: List<Proyecto>) {
        val primerProyecto = proyectos.first()
        invertirPorcentaje(primerProyecto, 50)
        val otrosProyectos = proyectos.drop(1)
        PartesIguales.distribuir(otrosProyectos)
    }

    private fun invertirPorcentaje(proyecto: Proyecto, porcentaje: Int) {
        TODO("Not yet implemented")
    }
}

/**
 * Invertir 500 mangos en un proyecto al azar, y el resto en otro proyecto al azar
 * (recordemos que el mínimo que requiere el proceso son 1.000 mangos pero no necesariamente 1.000)
 */
object DosAlAzar : FormaDeDistribucion {
    override fun distribuir(proyectos: List<Proyecto>) {
        val primerRandom = proyectos.random()
        var segundoRandom: Proyecto
        do {
            segundoRandom = proyectos.random()
        } while (primerRandom == segundoRandom)

        // TODO invertir
    }
}