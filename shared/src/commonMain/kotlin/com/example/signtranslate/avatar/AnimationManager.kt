package com.example.signtranslate.avatar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * AnimationManager
 * ─────────────────
 * • Mapea palabras en español → nombres de animación en el .glb
 * • Encola varias señas para reproducirlas en secuencia
 * • Cada animación dura ~1.5 s; si no hay mapeo usa la animación "idle"
 */
class AnimationManager {

    // ─── Mapeo texto → nombre de animación en el GLB ──────────────────────────
    private val ANIMATION_MAP: Map<String, String> = mapOf(
        // Saludos
        "hola"          to "Hola",
        "buenos dias"   to "BuenosDias",
        "buenas tardes" to "BuenasTardes",
        "buenas noches" to "BuenasNoches",
        "adios"         to "Adios",
        "gracias"       to "Gracias",
        "por favor"     to "PorFavor",
        "perdon"        to "Perdon",
        "si"            to "Si",
        "no"            to "No",
        // Números (0‑9)
        "cero"  to "Num0", "uno"   to "Num1", "dos"   to "Num2",
        "tres"  to "Num3", "cuatro" to "Num4", "cinco" to "Num5",
        "seis"  to "Num6", "siete"  to "Num7", "ocho"  to "Num8",
        "nueve" to "Num9",
        // Colores básicos
        "rojo"   to "ColorRojo",   "azul"   to "ColorAzul",
        "verde"  to "ColorVerde",  "amarillo" to "ColorAmarillo",
        "negro"  to "ColorNegro",  "blanco"   to "ColorBlanco",
        // Familia
        "mama"   to "Mama",   "papa"    to "Papa",
        "hermano" to "Hermano", "hermana" to "Hermana",
        "amigo"   to "Amigo",
        // Verbos frecuentes
        "quiero"  to "Quiero", "necesito" to "Necesito",
        "ayuda"   to "Ayuda",  "agua"     to "Agua",
        "comer"   to "Comer",  "dormir"   to "Dormir",
        // Letras del alfabeto dactilológico
        "a" to "LetraA", "b" to "LetraB", "c" to "LetraC",
        "d" to "LetraD", "e" to "LetraE", "f" to "LetraF",
        "g" to "LetraG", "h" to "LetraH", "i" to "LetraI",
        "j" to "LetraJ", "k" to "LetraK", "l" to "LetraL",
        "m" to "LetraM", "n" to "LetraN", "o" to "LetraO",
        "p" to "LetraP", "q" to "LetraQ", "r" to "LetraR",
        "s" to "LetraS", "t" to "LetraT", "u" to "LetraU",
        "v" to "LetraV", "w" to "LetraW", "x" to "LetraX",
        "y" to "LetraY", "z" to "LetraZ"
    )

    private val IDLE_ANIMATION = "Idle"
    private val DEFAULT_ANIM_DURATION_MS = 1500L

    private val queue = ArrayDeque<String>()
    private val mutex = Mutex()
    private var isPlaying = false

    // ─── API ──────────────────────────────────────────────────────────────────

    /**
     * Recibe un texto, lo divide en palabras/frases y encola las animaciones.
     * [onPlay] se invoca con el nombre de animación justo antes de reproducirla.
     */
    suspend fun enqueueText(text: String, onPlay: (String) -> Unit) {
        val tokens = tokenize(text)
        mutex.withLock {
            tokens.forEach { queue.add(it) }
        }
        if (!isPlaying) playNext(onPlay)
    }

    /** Vacía la cola inmediatamente. */
    fun clearQueue() {
        queue.clear()
        isPlaying = false
    }

    /** Devuelve el nombre de animación para una palabra, o null si no existe. */
    fun resolveAnimation(word: String): String? =
        ANIMATION_MAP[normalize(word)]

    // ─── Interno ──────────────────────────────────────────────────────────────

    private suspend fun playNext(onPlay: (String) -> Unit) {
        withContext(Dispatchers.Main) {
            while (true) {
                val next = mutex.withLock { 
                    if (queue.isNotEmpty()) queue.removeFirst() else null 
                } ?: break
                isPlaying = true
                val animName = ANIMATION_MAP[normalize(next)] ?: IDLE_ANIMATION
                onPlay(animName)
                delay(DEFAULT_ANIM_DURATION_MS)
            }
            isPlaying = false
            onPlay(IDLE_ANIMATION)
        }
    }

    /**
     * Tokeniza el texto buscando primero frases compuestas (2 palabras) y
     * luego palabras sueltas, para maximizar el reconocimiento.
     */
    private fun tokenize(text: String): List<String> {
        val words = text.lowercase()
            .replace(Regex("[áàä]"), "a")
            .replace(Regex("[éèë]"), "e")
            .replace(Regex("[íìï]"), "i")
            .replace(Regex("[óòö]"), "o")
            .replace(Regex("[úùü]"), "u")
            .replace(Regex("[^a-z0-9 ]"), "")
            .trim()
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }

        val result = mutableListOf<String>()
        var i = 0
        while (i < words.size) {
            // Intenta frase de 2 palabras primero
            if (i + 1 < words.size) {
                val bigram = "${words[i]} ${words[i + 1]}"
                if (ANIMATION_MAP.containsKey(bigram)) {
                    result.add(bigram); i += 2; continue
                }
            }
            result.add(words[i]); i++
        }
        return result
    }

    private fun normalize(word: String): String = word
        .lowercase().trim()
        .replace(Regex("[áàä]"), "a")
        .replace(Regex("[éèë]"), "e")
        .replace(Regex("[íìï]"), "i")
        .replace(Regex("[óòö]"), "o")
        .replace(Regex("[úùü]"), "u")
        .replace(Regex("[^a-z0-9 ]"), "")
}
