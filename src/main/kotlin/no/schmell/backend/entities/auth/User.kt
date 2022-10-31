package no.schmell.backend.entities.auth

import mu.KLogging
import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.lib.files.GenerateObjectSignedUrl
import java.net.URL
import javax.persistence.*


@Entity
@Table(name = "application_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int?,

    @Column(name = "username", nullable = false)
    val username : String,

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

) {

    companion object: KLogging()

    fun toUserDto(generateObjectSignedUrl: GenerateObjectSignedUrl): UserDto {

        return this.let{
            UserDto(
                it.id,
                it.username,
                it.email,
                it.phoneNumber,
                it.firstName,
                it.lastName,
                it.alertsForTasks,
                it.alertsForDeadlines,
                it.profilePicture,
                generateObjectSignedUrl.generateSignedUrl(it.profilePicture)
            )
        }
    }
}

