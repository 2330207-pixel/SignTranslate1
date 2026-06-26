package com.example.signtranslate.classifier

import android.content.Context
import com.example.signtranslate.data.translator.SignLabel
import com.example.signtranslate.data.translator.TranslationResult
import com.example.signtranslate.data.translator.HandLandmark
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * ════════════════════════════════════════════════════════════════
 *  FASE 2 – Clasificador con modelo TensorFlow Lite propio de LSM
 * ════════════════════════════════════════════════════════════════
 *
 * Pasos para activarlo:
 *  1. Entrena tu modelo (input: 63 floats = 21 landmarks × 3 coords x,y,z).
 *  2. Exporta como `lsm_classifier.tflite` y colócalo en `androidApp/src/main/assets/`.
 *  3. Descomenta el código de inferencia abajo.
 */
class MLClassifier(private val context: Context) : SignClassifier {

    // private var interpreter: org.tensorflow.lite.Interpreter? = null
    // private val MODEL_FILE = "lsm_classifier.tflite"
    // private val INPUT_SIZE = 63   // 21 landmarks × 3 coords (x, y, z)
    // private val OUTPUT_SIZE = SignLabel.entries.size - 1  // excluye UNKNOWN

    init {
        // TODO Fase 2: cargar modelo
        // val model = loadModelFile()
        // interpreter = org.tensorflow.lite.Interpreter(model)
    }

    override fun classify(landmarks: List<HandLandmark>): TranslationResult {
        if (landmarks.size < 21) return TranslationResult.EMPTY

        // Mapeo a NormalizedLandmark de MediaPipe para cuando se use el modelo
        /*
        val mpLandmarks = landmarks.map {
            NormalizedLandmark.create(it.x, it.y, it.z)
        }
        */

        // TODO Fase 2: convertir landmarks a FloatArray y ejecutar inferencia
        /*
        val input  = landmarksToFloatArray(landmarks)
        val output = Array(1) { FloatArray(OUTPUT_SIZE) }
        interpreter?.run(input, output)
        val scores = output[0]
        val maxIdx = scores.indices.maxByOrNull { scores[it] } ?: return TranslationResult.EMPTY
        val confidence = scores[maxIdx]
        val label = SignLabel.fromModelIndex(maxIdx)
        return TranslationResult(label, confidence, ClassifierSource.ML_MODEL)
        */

        // Mientras no haya modelo, delega al clasificador por reglas
        return RuleBasedClassifier().classify(landmarks)
    }

    override fun close() {
        // interpreter?.close()
        // interpreter = null
    }

    // ── Helpers Fase 2 ────────────────────────────────────────────────────────

    /*
    private fun landmarksToFloatArray(landmarks: List<HandLandmark>): FloatArray {
        val arr = FloatArray(INPUT_SIZE)
        landmarks.take(21).forEachIndexed { i, lm ->
            arr[i * 3 + 0] = lm.x
            arr[i * 3 + 1] = lm.y
            arr[i * 3 + 2] = lm.z
        }
        return arr
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fd = context.assets.openFd(MODEL_FILE)
        return FileInputStream(fd.fileDescriptor).channel
            .map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength)
    }
    */
}
