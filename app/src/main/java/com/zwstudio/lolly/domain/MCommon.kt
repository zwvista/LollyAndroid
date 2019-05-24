package com.zwstudio.lolly.domain

import java.io.Serializable

class MSelectItem(val value: Int, val label: String): Serializable

enum class ReviewMode {
    ReviewAuto, Test, ReviewManual;

    override fun toString(): String {
        return when(this) {
            ReviewAuto -> "Review(Auto)"
            Test -> "Test"
            ReviewManual -> "Review(Manual)"
        }
    }
}
