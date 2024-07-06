package dev.alb3g.repository

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.alb3g.database.AppDatabase
import dev.alb3g.database.DbNote
import dev.alb3g.models.Note
import java.io.File

private const val DATABASE_NAME = "database.db"

object NotesRepository {
    private val notesDb = JdbcSqliteDriver("jdbc:sqlite:$DATABASE_NAME").let {
        if(!File(DATABASE_NAME).exists()) { AppDatabase.Schema.create(it) }
        AppDatabase(it)
    }.noteQueries

    fun save(note: Note): Note  {
        notesDb.insert(note.title, note.description, note.type.name)
        return notesDb.selectLastNoteInserted().executeAsOne().toNote()
    }

    fun getAll() = notesDb.select().executeAsList().map { it.toNote() }

    fun getById(id: Long): Note? = notesDb.selectById(id).executeAsOneOrNull()?.toNote()

    fun update(note: Note): Boolean {
        if(getById(note.id) == null) return false
        notesDb.update(note.title, note.description, note.type.name, note.id)
        return true
    }

    fun delete(id: Long): Boolean {
        if(getById(id) == null) return false
        notesDb.delete(id)
        return true
    }
}

private fun DbNote.toNote() = Note(
    id = id,
    title = title,
    description = description,
    type = Note.Type.valueOf(type)
)
