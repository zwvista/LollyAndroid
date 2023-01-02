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

data class MReviewOptions(
    var mode: ReviewMode = ReviewMode.ReviewAuto,
    var shuffled: Boolean = false,
    var interval: Int = 5,
    var groupSelected: Int = 1,
    var groupCount: Int = 1,
    var speakingEnabled: Boolean = true,
    var reviewCount: Int = 10,
    var onRepeat: Boolean = true,
    var moveForward: Boolean = true,
) : Serializable
