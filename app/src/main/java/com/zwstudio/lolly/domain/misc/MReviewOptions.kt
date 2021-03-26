package com.zwstudio.lolly.domain.misc

enum class ReviewMode {
    ReviewAuto, ReviewManual, Test, Textbook;

    override fun toString() = when(this) {
        ReviewAuto -> "Review(Auto)"
        ReviewManual -> "Review(Manual)"
        Test -> "Test"
        Textbook -> "Textbook"
    }
}

class MReviewOptions {
    val isEmbedded = false
    var mode = ReviewMode.ReviewAuto
    var shuffled = false
    var interval = 5
    var groupSelected = 1
    var groupCount = 1
    var speakingEnabled = true
    var reviewCount = 10
}
