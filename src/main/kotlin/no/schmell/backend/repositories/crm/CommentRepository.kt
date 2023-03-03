package no.schmell.backend.repositories.crm

import no.schmell.backend.entities.crm.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Int>