package no.schmell.backend.dtos.auth

data class UserDto(
    var id : Int?,
    val email : String,
    val phoneNumber : Long,
    val firstName : String,
    val lastName : String,
    val alertsForTasks : Boolean,
    val alertsForDeadlines : Boolean,
    val profilePicture : String?,
    val profilePictureUrl : String?,
    val auth0Id : String
)
data class SimpleUserDto(
    val id : Int,
    val email : String,
    val fullName : String,
    val profilePictureUrl : String?
)
data class UpdateUserDto(
    val email: String?,
    val phoneNumber: Long?,
    val firstName: String?,
    val lastName: String?,
    val alertsForTasks: Boolean?,
    val alertsForDeadlines: Boolean?,
)