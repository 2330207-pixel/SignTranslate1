package com.example.signtranslate.data.translator

/**
 * Catálogo centralizado de señas reconocibles.
 *
 * FASE 1 – Clasificador por reglas (implementado).
 * FASE 2 – Modelo TFLite propio: los mismos labels se usarán
 *           como índices de salida del clasificador de IA.
 */
enum class SignLabel(
    val displayText: String,
    val category: SignCategory
) {
    // ── Palabras / frases ─────────────────────────────────────
    HOLA("Hola", SignCategory.WORD),
    GRACIAS("Gracias", SignCategory.WORD),
    SI("Sí", SignCategory.WORD),
    NO("No", SignCategory.WORD),
    AYUDA("Ayuda", SignCategory.WORD),
    ADIOS("Adiós", SignCategory.WORD),

    // ── Letras del abecedario LSM ─────────────────────────────
    LETTER_A("A", SignCategory.LETTER),
    LETTER_B("B", SignCategory.LETTER),
    LETTER_C("C", SignCategory.LETTER),
    LETTER_D("D", SignCategory.LETTER),
    LETTER_E("E", SignCategory.LETTER),

    // ── Estado especial ───────────────────────────────────────
    UNKNOWN("Esperando señas...", SignCategory.NONE);

    companion object {
        /**
         * Convierte el índice de salida de un modelo TFLite al enum
         * correspondiente. El orden debe coincidir con las clases del
         * modelo entrenado en Fase 2.
         */
        fun fromModelIndex(index: Int): SignLabel =
            entries.getOrNull(index) ?: UNKNOWN
    }
}

enum class SignCategory { WORD, LETTER, NONE }