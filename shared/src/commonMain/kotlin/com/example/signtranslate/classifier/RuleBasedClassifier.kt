package com.example.signtranslate.classifier

import com.example.signtranslate.data.translator.ClassifierSource
import com.example.signtranslate.data.translator.SignLabel
import com.example.signtranslate.data.translator.TranslationResult
import com.example.signtranslate.data.translator.HandLandmark
import kotlin.math.sqrt

/**
 * Clasificador basado en reglas geométricas sobre los landmarks.
 */
class RuleBasedClassifier : SignClassifier {

    override fun classify(landmarks: List<HandLandmark>): TranslationResult {
        if (landmarks.size < 21) return TranslationResult.EMPTY

        val candidates = listOf(
            ::detectHola,
            ::detectGracias,
            ::detectSi,
            ::detectNo,
            ::detectAyuda,
            ::detectAdios,
            ::detectA,
            ::detectB,
            ::detectC,
            ::detectD,
            ::detectE
        ).mapNotNull { detector -> detector(landmarks) }

        return candidates.maxByOrNull { it.confidence } ?: TranslationResult.EMPTY
    }

    private fun isFingerUp(tip: HandLandmark, mcp: HandLandmark): Boolean =
        tip.y < mcp.y

    private fun isThumbUp(
        landmarks: List<HandLandmark>,
        handedness: Float = landmarks[0].x
    ): Boolean {
        val tip = landmarks[4]
        val ip  = landmarks[3]
        return if (handedness < 0.5f) tip.x < ip.x else tip.x > ip.x
    }

    private fun isThumbDown(landmarks: List<HandLandmark>): Boolean {
        val tip = landmarks[4]
        val mcp = landmarks[2]
        return tip.y > mcp.y
    }

    private fun distance(a: HandLandmark, b: HandLandmark): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    private fun allFingersUp(lm: List<HandLandmark>): Boolean =
        isFingerUp(lm[8], lm[5]) &&
                isFingerUp(lm[12], lm[9]) &&
                isFingerUp(lm[16], lm[13]) &&
                isFingerUp(lm[20], lm[17])

    private fun allFingersClosed(lm: List<HandLandmark>): Boolean =
        !isFingerUp(lm[8], lm[5]) &&
                !isFingerUp(lm[12], lm[9]) &&
                !isFingerUp(lm[16], lm[13]) &&
                !isFingerUp(lm[20], lm[17])

    private fun detectHola(lm: List<HandLandmark>): TranslationResult? {
        val allUp = allFingersUp(lm)
        val thumbOut = isThumbUp(lm)
        if (!allUp || !thumbOut) return null
        val spread = distance(lm[8], lm[20]) > 0.25f
        val confidence = if (spread) 0.85f else 0.72f
        return TranslationResult(SignLabel.HOLA, confidence, ClassifierSource.RULE_BASED)
    }

    private fun detectGracias(lm: List<HandLandmark>): TranslationResult? {
        val allUp = allFingersUp(lm)
        val thumbNotOut = !isThumbUp(lm)
        if (!allUp || !thumbNotOut) return null
        val fingersTogether = distance(lm[8], lm[20]) < 0.18f
        if (!fingersTogether) return null
        return TranslationResult(SignLabel.GRACIAS, 0.80f, ClassifierSource.RULE_BASED)
    }

    private fun detectSi(lm: List<HandLandmark>): TranslationResult? {
        val fistClosed = allFingersClosed(lm)
        val thumbWrapped = isThumbDown(lm)
        if (!fistClosed || !thumbWrapped) return null
        return TranslationResult(SignLabel.SI, 0.78f, ClassifierSource.RULE_BASED)
    }

    private fun detectNo(lm: List<HandLandmark>): TranslationResult? {
        val indexUp  = isFingerUp(lm[8], lm[5])
        val middleUp = isFingerUp(lm[12], lm[9])
        val ringDown  = !isFingerUp(lm[16], lm[13])
        val pinkyDown = !isFingerUp(lm[20], lm[17])
        if (!indexUp || !middleUp || !ringDown || !pinkyDown) return null
        val together = distance(lm[8], lm[12]) < 0.07f
        val confidence = if (together) 0.80f else 0.65f
        return TranslationResult(SignLabel.NO, confidence, ClassifierSource.RULE_BASED)
    }

    private fun detectAyuda(lm: List<HandLandmark>): TranslationResult? {
        val fistClosed = allFingersClosed(lm)
        val thumbExtended = isFingerUp(lm[4], lm[2])
        if (!fistClosed || !thumbExtended) return null
        return TranslationResult(SignLabel.AYUDA, 0.82f, ClassifierSource.RULE_BASED)
    }

    private fun detectAdios(lm: List<HandLandmark>): TranslationResult? {
        val allUp = allFingersUp(lm)
        if (!allUp) return null
        val pinkySeparated = distance(lm[20], lm[16]) > 0.09f
        val thumbDown = !isThumbUp(lm)
        if (!pinkySeparated || !thumbDown) return null
        return TranslationResult(SignLabel.ADIOS, 0.75f, ClassifierSource.RULE_BASED)
    }

    private fun detectA(lm: List<HandLandmark>): TranslationResult? {
        val fistClosed = allFingersClosed(lm)
        val thumbSide = isThumbUp(lm)
        val thumbNotHigh = lm[4].y > lm[5].y
        if (!fistClosed || !thumbSide || !thumbNotHigh) return null
        return TranslationResult(SignLabel.LETTER_A, 0.80f, ClassifierSource.RULE_BASED)
    }

    private fun detectB(lm: List<HandLandmark>): TranslationResult? {
        val allUp = allFingersUp(lm)
        val thumbFolded = !isThumbUp(lm) && lm[4].x > lm[5].x
        val fingersTight = distance(lm[8], lm[20]) < 0.15f
        if (!allUp || !thumbFolded || !fingersTight) return null
        return TranslationResult(SignLabel.LETTER_B, 0.78f, ClassifierSource.RULE_BASED)
    }

    private fun detectC(lm: List<HandLandmark>): TranslationResult? {
        val indexCurved  = lm[8].y < lm[6].y  && !isFingerUp(lm[8], lm[5])
        val middleCurved = lm[12].y < lm[10].y && !isFingerUp(lm[12], lm[9])
        val ringCurved   = lm[16].y < lm[14].y && !isFingerUp(lm[16], lm[13])
        val pinkyCurved  = lm[20].y < lm[18].y && !isFingerUp(lm[20], lm[17])
        if (!indexCurved || !middleCurved || !ringCurved || !pinkyCurved) return null
        val opening = distance(lm[4], lm[8]) > 0.10f && distance(lm[4], lm[8]) < 0.30f
        if (!opening) return null
        return TranslationResult(SignLabel.LETTER_C, 0.75f, ClassifierSource.RULE_BASED)
    }

    private fun detectD(lm: List<HandLandmark>): TranslationResult? {
        val indexUp   = isFingerUp(lm[8], lm[5])
        val middleDown = !isFingerUp(lm[12], lm[9])
        val ringDown   = !isFingerUp(lm[16], lm[13])
        val pinkyDown  = !isFingerUp(lm[20], lm[17])
        if (!indexUp || !middleDown || !ringDown || !pinkyDown) return null
        val thumbTouchesMiddle = distance(lm[4], lm[12]) < 0.08f
        if (!thumbTouchesMiddle) return null
        return TranslationResult(SignLabel.LETTER_D, 0.82f, ClassifierSource.RULE_BASED)
    }

    private fun detectE(lm: List<HandLandmark>): TranslationResult? {
        val indexBent  = lm[8].y > lm[6].y
        val middleBent = lm[12].y > lm[10].y
        val ringBent   = lm[16].y > lm[14].y
        val pinkyBent  = lm[20].y > lm[18].y
        if (!(indexBent && middleBent && ringBent && pinkyBent)) return null
        val thumbIn = lm[4].y > lm[3].y
        if (!thumbIn) return null
        return TranslationResult(SignLabel.LETTER_E, 0.77f, ClassifierSource.RULE_BASED)
    }
}
