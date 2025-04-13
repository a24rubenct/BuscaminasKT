data class Celda(
    var tieneMina: Boolean = false,
    var destapada: Boolean = false,
    var tieneBandera: Boolean = false,
    var minasAlrededor: Int = 0
)

class Buscaminas(private val filas: Int, private val columnas: Int, private val numMinas: Int) {
    private val tablero: Array<Array<Celda>> = Array(filas) { Array(columnas) { Celda() } }
    var juegoFinalizado: Boolean = false
        private set
    var contadorBanderas: Int = 0
        private set

    init {
        if (filas < 1 || columnas < 1) throw IllegalArgumentException("Dimensiones del tablero no válidas.")
        if (numMinas >= filas * columnas) throw IllegalArgumentException("Número de minas no válido.")
        colocarMinas()
        calcularMinasAlrededor()
    }

    private fun colocarMinas() {
        var minasColocadas = 0
        while (minasColocadas < numMinas) {
            val fila = (0 until filas).random()
            val columna = (0 until columnas).random()
            if (!tablero[fila][columna].tieneMina) {
                tablero[fila][columna].tieneMina = true
                minasColocadas++
            }
        }
    }

    private fun calcularMinasAlrededor() {
        for (fila in 0 until filas) {
            for (columna in 0 until columnas) {
                if (!tablero[fila][columna].tieneMina) {
                    tablero[fila][columna].minasAlrededor = contarMinas(fila, columna)
                }
            }
        }
    }

    private fun contarMinas(fila: Int, columna: Int): Int {
        var contador = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val nuevaFila = fila + i
                val nuevaColumna = columna + j
                if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas && tablero[nuevaFila][nuevaColumna].tieneMina) {
                    contador++
                }
            }
        }
        return contador
    }

    fun destapar(fila: Int, columna: Int) {
        if (fila !in 0 until filas || columna !in 0 until columnas) throw IllegalArgumentException("Coordenadas inválidas")
        if (tablero[fila][columna].destapada || tablero[fila][columna].tieneBandera) return

        tablero[fila][columna].destapada = true
        if (tablero[fila][columna].tieneMina) {
            juegoFinalizado = true
            throw Exception("¡Has perdido! Mina encontrada")
        }

        if (tablero[fila][columna].minasAlrededor == 0) {
            for (i in -1..1) {
                for (j in -1..1) {
                    val nuevaFila = fila + i
                    val nuevaColumna = columna + j
                    if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas) {
                        destapar(nuevaFila, nuevaColumna)
                    }
                }
            }
        }

        if (verificarVictoria()) {
            println("¡Felicidades! Has ganado el juego.")
        }
    }

    fun ponerBandera(fila: Int, columna: Int) {
        if (fila !in 0 until filas || columna !in 0 until columnas) throw IllegalArgumentException("Coordenadas inválidas.")
        val celda = tablero[fila][columna]
        if (celda.destapada) return // No se puede colocar banderas en celdas ya destapadas

        if (celda.tieneBandera) {
            celda.tieneBandera = false
            contadorBanderas--
        } else {
            celda.tieneBandera = true
            contadorBanderas++
        }
        if (verificarVictoria()) {
            println("¡Felicidades! Has ganado el juego.")
        }
    }

    fun obtenerTablero(): Array<Array<Celda>> = tablero

    fun verificarVictoria(): Boolean {
        for (fila in tablero) {
            for (celda in fila) {
                if ((celda.tieneMina && !celda.tieneBandera) || (!celda.tieneMina && !celda.destapada)) {
                    return false
                }
            }
        }
        juegoFinalizado = true
        return true
    }
}
