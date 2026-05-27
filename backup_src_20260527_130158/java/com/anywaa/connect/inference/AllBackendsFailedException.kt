package com.anywaa.connect.inference

/**
 * Exception thrown when all attempted inference backends (e.g., Nexa and MediaPipe) fail to load a model.
 */
class AllBackendsFailedException(message: String) : Exception(message)
