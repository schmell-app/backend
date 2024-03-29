package no.schmell.backend.repositories.auth

import no.schmell.backend.entities.auth.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByAuth0Id(auth0Id: String): User?
}