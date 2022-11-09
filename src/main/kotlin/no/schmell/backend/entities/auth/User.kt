package no.schmell.backend.entities.auth

import mu.KLogging
import no.schmell.backend.dtos.auth.SimpleUserDto
import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.services.files.FilesService
import javax.persistence.*

@Entity
@Table(name = "application_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "email", nullable = false)
    val email : String,

    @Column(name = "phone_number", nullable = false)
    val phoneNumber : Long,

    @Column(name = "first_name", nullable = false)
    val firstName : String,

    @Column(name = "last_name", nullable = false)
    val lastName : String,

    @Column(name = "alerts_for_tasks", nullable = false)
    val alertsForTasks : Boolean,

    @Column(name = "alerts_for_deadlines", nullable = false)
    val alertsForDeadlines : Boolean,

    @Column(name = "profile_picture", nullable = true)
    val profilePicture : String?,

    @Column(name = "auth0_id", nullable = false)
    val auth0Id : String
) {

    companion object: KLogging()

    fun toUserDto(filesService: FilesService): UserDto {

        return this.let{
            UserDto(
                it.id,
                it.email,
                it.phoneNumber,
                it.firstName,
                it.lastName,
                it.alertsForTasks,
                it.alertsForDeadlines,
                it.profilePicture,
                it.profilePicture?.let { pic -> filesService.generatePresignedUrl("schmell-files", pic) },
                it.auth0Id
            )
        }
    }

    fun toSimpleUserDto(filesService: FilesService): SimpleUserDto {
        return this.let{
            SimpleUserDto(
                it.id!!,
                it.email,
                "${it.firstName} ${it.lastName}",
                it.profilePicture?.let { pic -> filesService.generatePresignedUrl("schmell-files", pic) }
            )
        }
    }
}

