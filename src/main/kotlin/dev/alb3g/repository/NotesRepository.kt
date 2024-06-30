package dev.alb3g.repository

import dev.alb3g.models.Note

object NotesRepository {

    private val notes = mutableListOf<Note>()
    private var currentId = 1L

    fun save(note: Note): Note = note.copy(id = currentId++).also { notes.add(it) }

    fun getAll() = notes;

    fun getById(id: Long): Note? = notes.find { it.id == id } // find devuelve un T?

    fun update(note: Note): Boolean  = notes
        .indexOfFirst { it.id == note.id }
        .takeIf { it >= 0 }
        ?.also { notes[it] = note }
        .let { it != null }

    fun delete(id: Long): Boolean = notes.removeIf { note -> note.id == id }
}