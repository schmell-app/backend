package no.schmell.backend.dtos.auth

import no.schmell.backend.entities.auth.User

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
    val profilePictureUrl : String?
) {
    fun toUserEntity(): User {
        return this.let {
            User(
                it.id,
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

