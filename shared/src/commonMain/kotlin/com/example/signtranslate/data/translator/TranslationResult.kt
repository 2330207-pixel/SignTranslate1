package com.example.signtranslate.data.translator

import com.example.signtranslate.currentTimeMillis

/**
 * Resultado de una clasificación de seña.
 *
 * @param label        Seña reconocida (o UNKNOWN si no se detectó ninguna).
 * @param confidence   Nivel de confianza [0.0 – 1.0].
 * @param source       Indica si proviene del clasificador por reglas o de IA.
 * @param timestampMs  Timestamp en ms del momento de clasificación.
 */
data class TranslationResult(
    val label: SignLabel,
    val confidence: Float,
    val source: ClassifierSource = ClassifierSource.RULE_BASED,
    val timestampMs: Long = currentTimeMillis()
) {
    /** Texto listo para mostrar en la UI. */
    val displayText: String get() = label.displayText

    /** Porcentaje para mostrar en la tarjeta de confianza. */
    val confidencePercent: Int get() = (confidence * 100).toInt()

    /** Si la confianza supera el umbral mínimo para mostrarse. */
    fun isReliable(threshold: Float = 0.65f): Boolean =
        label != SignLabel.UNKNOWN && confidence >= threshold

    companion object {
        val EMPTY = TranslationResult(
            label = SignLabel.UNKNOWN,
            confidence = 0f,
            timestampMs = 0L
        )
    }
}

/**
 * FASE 2: cuando se integre TFLite, se añadirá ML_MODEL aquí.
 * La UI no necesita cambios porque consume TranslationResult sin
 * importarle de dónde vino.
 */
enum class ClassifierSource {
    RULE_BASED,   // Fase 1 – reglas geométricas
    ML_MODEL      // Fase 2 – TFLite entrenado con LSM
}
