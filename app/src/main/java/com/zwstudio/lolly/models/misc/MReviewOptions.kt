package com.zwstudio.lolly.models.misc

import java.io.Serializable

enum class ReviewMode {
    ReviewAuto, ReviewManual, Test, Textbook;

    override fun toString() = when(this) {
        ReviewAuto -> "Review(Auto)"
        ReviewManual -> "Review(Manual)"
        Test -> "Test"
        Textbook -> "Textbook"
    }
}

class MReviewOptions : Serializable {
    val isEmbedded = false
    var mode = ReviewMode.ReviewAuto
    var shuffled = false
    var interval = 5
    var groupSelected = 1
    var groupCount = 1
    var speakingEnabled = true
    var reviewCount = 10
}
