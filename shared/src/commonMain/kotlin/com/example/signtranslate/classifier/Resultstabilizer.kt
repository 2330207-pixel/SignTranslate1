package com.example.signtranslate.classifier

import com.example.signtranslate.data.translator.SignLabel
import com.example.signtranslate.data.translator.TranslationResult

/**
 * Estabilizador temporal de resultados para evitar parpadeos en la UI.
 *
 * Estrategia: ventana deslizante (sliding window).
 * El resultado se confirma solo si aparece en al menos [minConsecutive]
 * de los últimos [windowSize] frames con confianza ≥ [minConfidence].
 *
 * @param windowSize     Número de frames en la ventana (default 10 ≈ 333ms a 30fps).
 * @param minConsecutive Cuántos de esos frames deben coincidir (default 7 = 70%).
 * @param minConfidence  Confianza mínima para considerar un frame válido.
 */
class ResultStabilizer(
    private val windowSize: Int = 10,
    private val minConsecutive: Int = 7,
    private val minConfidence: Float = 0.65f
) {
    private val window = ArrayDeque<TranslationResult>(windowSize)
    private var lastStableResult: TranslationResult = TranslationResult.EMPTY

    /**
     * Recibe un nuevo resultado del clasificador y devuelve el resultado
     * estabilizado listo para mostrar en la UI.
     */
    fun feed(result: TranslationResult): TranslationResult {
        // Agrega a la ventana y mantiene el tamaño máximo
        window.addLast(result)
        if (window.size > windowSize) window.removeFirst()

        val stable = computeStable()
        if (stable != null) lastStableResult = stable
        return lastStableResult
    }

    /** Limpia el historial (útil al cambiar de modo o pausar la cámara). */
    fun reset() {
        window.clear()
        lastStableResult = TranslationResult.EMPTY
    }

    // ─────────────────────────────────────────────────────────────────────────

    private fun computeStable(): TranslationResult? {
        if (window.size < minConsecutive) return null

        // Filtra solo frames con confianza suficiente
        val reliable = window.filter { it.confidence >= minConfidence }

        // Cuenta las ocurrencias de cada label
        val counts = reliable.groupingBy { it.label }.eachCount()
        val (topLabel, topCount) = counts.maxByOrNull { it.value } ?: return null

        if (topLabel == SignLabel.UNKNOWN || topCount < minConsecutive) return null

        // Promedia la confianza de los frames ganadores
        val avgConfidence = reliable
            .filter { it.label == topLabel }
            .map { it.confidence }
            .average()
            .toFloat()

        return reliable.first { it.label == topLabel }.copy(confidence = avgConfidence)
    }
}