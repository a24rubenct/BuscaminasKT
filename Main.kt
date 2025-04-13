fun obtenerConfiguraciones(): Triple<Int, Int, Int> {
    println("Introduce las dimensiones del tablero (filas y columnas) y el número de minas:")
    println("Ejemplo: 5 5 5")
    val (filas, columnas, numMinas) = readLine()?.split(" ")?.map { it.toIntOrNull() } ?: listOf(null, null, null)
    if (filas == null || columnas == null || numMinas == null || filas < 1 || columnas < 1 || numMinas < 1) {
        println("Configuración inválida. Inténtalo de nuevo.")
        return obtenerConfiguraciones()
    }
    return Triple(filas, columnas, numMinas)
}

fun main() {
    println("¡Bienvenido a Buscaminas!")
    val (filas, columnas, numMinas) = obtenerConfiguraciones()
    val buscaminas = Buscaminas(filas, columnas, numMinas)

    while (!buscaminas.juegoFinalizado) {
        imprimirTablero(buscaminas.obtenerTablero())
        println("Banderas colocadas: ${buscaminas.contadorBanderas}")
        println("Selecciona una acción: (1) Destapar celda, (2) Poner/Quitar bandera")
        val accion = readLine()?.toIntOrNull()
        println("Introduce fila y columna (ejemplo: 1 2):")
        val (fila, columna) = readLine()?.split(" ")?.map { it.toIntOrNull() } ?: listOf(null, null)

        try {
            if (fila != null && columna != null) {
                when (accion) {
                    1 -> buscaminas.destapar(fila - 1, columna - 1) // Ajustamos para empezar en 1
                    2 -> buscaminas.ponerBandera(fila - 1, columna - 1) // Ajustamos para empezar en 1
                    else -> println("Acción no válida.")
                }
            } else {
                println("Las coordenadas ingresadas no son válidas.")
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
    println("¡Juego terminado!")
}

fun imprimirTablero(tablero: Array<Array<Celda>>) {
    for (fila in tablero) {
        for (celda in fila) {
            when {
                celda.tieneBandera -> print("F ")
                !celda.destapada -> print("# ")
                celda.tieneMina -> print("* ")
                else -> print("${celda.minasAlrededor} ")
            }
        }
        println()
    }
}
