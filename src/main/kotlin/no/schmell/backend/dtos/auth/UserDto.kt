package no.schmell.backend.dtos.auth

import no.schmell.backend.entities.auth.User
import java.net.URL

data class UserDto(
    var id : Int?,
    val username : String,
    val email : String,
    val phoneNumber : Long,
    val firstName : String,
    val lastName : String,
    val alertsForTasks : Boolean,
    val alertsForDeadlines : Boolean,
    val profilePicture : String?,
    val profilePictureUrl: URL?,
) {
    fun toUserEntity(): User {
        return this.let {
            User(
                it.id ?: null,
                it.username,
                it.email,
                it.phoneNumber,
                it.firstName,
                it.lastName,
                it.alertsForTasks,
                it.alertsForDeadlines,
                it.profilePicture
            )
        }
    }
}

