package dev.alb3g.plugins

import dev.alb3g.repository.NotesRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/notes") {
            get {
                call.respond(NotesRepository.getAll())
            }
        }
    }
}
