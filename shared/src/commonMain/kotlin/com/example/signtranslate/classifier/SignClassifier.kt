package com.example.signtranslate.classifier

import com.example.signtranslate.data.translator.TranslationResult
import com.example.signtranslate.data.translator.HandLandmark

/**
 * Contrato del clasificador de señas.
 *
 * Esta interfaz permite intercambiar la implementación por reglas (Fase 1)
 * por un modelo TFLite propio (Fase 2) sin tocar el ViewModel ni la UI.
 */
interface SignClassifier {
    /**
     * Clasifica la seña a partir de los 21 landmarks de una mano.
     *
     * @param landmarks Lista de 21 [HandLandmark].
     * @return [TranslationResult] con la seña detectada y su confianza.
     */
    fun classify(landmarks: List<HandLandmark>): TranslationResult

    /**
     * Libera recursos (modelos TFLite, intérpretes, etc.).
     */
    fun close() {}
}
