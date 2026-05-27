package com.anywaa.connect.util

object LlmInference {
    object Backend {
        const val ONNX = "ONNX"
        const val MEDIAPIPE = "MediaPipe"
    }

    fun infer(prompt: String, onResult: (String) -> Unit) {
        onResult("This is a stub response. Real inference is disabled.")
    }

    fun setAgentToolsEnabled(enabled: Boolean) {
        // No-op
    }
}
