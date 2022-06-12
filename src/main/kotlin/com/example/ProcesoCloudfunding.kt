package com.example

class ProcesoCloudfunding(
    var proyectosDisponibles: List<Proyecto>,
    var sistemaBancario: SistemaBancario,
    var whatsApp: WhatsApp,
    var mailSender: MailSender
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
    fun sugerenciasDeInversion(): List<PropuestaDeProyectos> {
        val proyectosActivos = proyectosDisponibles.filter { it.activo }
        return criteriosSeleccion.map {
            PropuestaDeProyectos(it, it.seleccionar(proyectosActivos))
        }
    }

    fun distribuirDinero(propuestaSeleccionada: PropuestaDeProyectos) {
        validarPropuesta(propuestaSeleccionada)
        inversiones = criterioDistribucionSeleccionado.distribuir(dineroAInvertir, propuestaSeleccionada.proyectos)
    }

    /**
     * El proceso necesita que haya al menos dos proyectos elegibles,
     * en caso contrario el proceso no puede continuar.
     * */
    private fun validarPropuesta(propuestaSeleccionada: PropuestaDeProyectos) {
        if (propuestaSeleccionada.proyectos.size < MIN_CANT_PROYECTOS)
            throw NotEnoughProjectsException("No hay proyectos suficientes para invertir, seleccione una propuesta con $MIN_CANT_PROYECTOS o más")
    }

    fun confirmarDistribucionDinero() {
        validarInversiones(inversiones)
        inversiones.forEach {
            val resultadoTransferencia: String = realizarTransferencia(it.monto, it.proyecto)
            // Si la transferencia falla, el proceso no puede continuar.
            if (resultadoTransferencia.isNotBlank()) throw DonationException("Ocurrió un error al realizar la donacion al proyecto ${it.proyecto.nombre}")
            avisarAInversorSiCorresponde(it.monto, it.proyecto)
            avisarAResponsablesDelProyecto(it.proyecto)
            it.invertir()
        }
    }

    /**
     * Debe enviarse un mail a todas las personas responsables del proyecto
     * (tenemos sus mails) para que tomen nota de la transferencia
     * */
    private fun avisarAResponsablesDelProyecto(proyecto: Proyecto) {
        proyecto.personasResponsables.forEach {
            mailSender.enviar(
                Mail(
                    from = "cloudfunding@mail.com",
                    to = it.email,
                    subject = "Donación recibida",
                    content = "Se a realizado una transferencia al proyecto ${proyecto.nombre}"
                )
            )
        }
    }

    /**
     * Si el monto transferido es mayor a 2.500 mangos,
     * se debe enviar un whatsapp al inversor,
     * avisando el monto invertido y el proyecto involucrado
     * */
    private fun avisarAInversorSiCorresponde(monto: Double, proyecto: Proyecto) {
        if (monto > 2_500)
            whatsApp.enviar("Se realizó una transferencia de $monto mangos a ${proyecto.nombre}")
    }

    private fun realizarTransferencia(monto: Double, proyecto: Proyecto): String {
        val montoAInvertir = monto.toString().split(".", limit = 2)
        return sistemaBancario.transferir(
            cuentaOrigenId = "",
            cuentaDestinoId = proyecto.cuentaBancaria,
            montoEntero = montoAInvertir.first().toInt(),
            montoDecimales = montoAInvertir.last().toInt()
        )
    }

    private fun validarInversiones(inversionesAValidar: List<Inversion>) {
        if (inversionesAValidar.isEmpty()) throw NoInvestmentsException("No hay inversiones posibles para realizar")
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