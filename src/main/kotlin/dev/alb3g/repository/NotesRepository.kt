package dev.alb3g.repository

import dev.alb3g.models.Note

object NotesRepository {
    fun getAll(): List<Note> {
        val notes = (1..10).map {
            Note(
                it,
                "Title $it" ,
                "Description $it",
                if(it % 2 == 0) Note.Type.SOUND else Note.Type.TEXT
            )
        }
        return notes;
    }
}