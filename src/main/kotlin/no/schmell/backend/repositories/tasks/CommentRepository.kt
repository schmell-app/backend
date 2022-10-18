package no.schmell.backend.repositories.tasks

import no.schmell.backend.entities.tasks.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Int>