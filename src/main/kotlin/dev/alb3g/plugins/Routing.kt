package dev.alb3g.plugins

import dev.alb3g.models.Note
import dev.alb3g.repository.NotesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/notes") {
            get {
                call.respond(NotesRepository.getAll())
            }
            // SELECT
            get("{id}") {
                val noteId = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or malformed ID"
                )

                val note = NotesRepository.getById(noteId.toLong()) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "No note found with Id: $noteId"
                )

                call.respond(note)
            }
            // CREATE
            post {
                try {
                    val note = call.receive<Note>()
                    val savedNote = NotesRepository.save(note)
                    call.respond(HttpStatusCode.Created, savedNote)
                }catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Bad JSON Data Body: ${e.message}")
                }
            }
            // UPDATE
            put {
                try {
                    val note = call.receive<Note>()
                    if(NotesRepository.update(note)) {
                        call.respond(HttpStatusCode.OK, note)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "No note found with Id: ${note.id}")
                    }
                }catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Bad JSON Data Body: ${e.message}")
                }
            }
            // DELETE
            delete("{id}") {
                val noteId = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or malformed ID"
                )
                val note = NotesRepository.getById(noteId.toLong()) ?: return@delete call.respond(
                    HttpStatusCode.NotFound,
                    "No note found with Id: $noteId"
                )
                val deleteOperationResult = NotesRepository.delete(note.id);

                when(deleteOperationResult) {
                    true -> call.respond(HttpStatusCode.OK, note)
                    else -> call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
