package dev.alb3g.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(val id: Long, val title: String, val description: String, val type: Type) {
    enum class Type { TEXT, SOUND }
}