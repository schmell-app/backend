package no.schmell.backend.services.auth

import mu.KLogging
import no.schmell.backend.dtos.auth.UpdateUserDto
import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.entities.auth.User
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException


@Service
class AuthService(
    val userRepository: UserRepository,
    val filesService: FilesService) {

    companion object: KLogging()

    fun getAll(): List<UserDto> = userRepository.findAll().map { user -> user.toUserDto(filesService) }

    fun getById(id: Int): UserDto {

        val user = userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        return user.toUserDto(filesService)
    }

    fun create(dto: UserDto): UserDto {
        val userToSave = User(
            null,
            dto.email,
            dto.phoneNumber,
            dto.firstName,
            dto.lastName,
            dto.alertsForTasks,
            dto.alertsForDeadlines,
            null,
            dto.auth0Id
        )
        return userRepository.save(userToSave).toUserDto(filesService)
    }

    fun update(id: Int, userDto: UpdateUserDto): UserDto {
        return if(userRepository.existsById(id)) {
            val userToUpdate = this.getById(id)
            val updatedUser = User(
                userToUpdate.id,
                userDto.email ?: userToUpdate.email,
                userDto.phoneNumber ?: userToUpdate.phoneNumber,
                userDto.firstName ?: userToUpdate.firstName,
                userDto.lastName ?: userToUpdate.lastName,
                userDto.alertsForTasks ?: userToUpdate.alertsForTasks,
                userDto.alertsForDeadlines ?: userToUpdate.alertsForDeadlines,
                userToUpdate.profilePicture,
                userToUpdate.auth0Id
            )
            userRepository.save(updatedUser).toUserDto(filesService)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun addProfilePicture(id: Int, file: MultipartFile): UserDto {
        val user = userRepository.findByIdOrNull(id)
        return if (user != null) {
            val uploadedFile = filesService.saveFile(file, "schmell-files", "profilePictures")
            userRepository.save(User(
                user.id,
                user.email,
                user.phoneNumber,
                user.firstName,
                user.lastName,
                user.alertsForTasks,
                user.alertsForDeadlines,
                uploadedFile?.fileName,
                user.auth0Id
            )).toUserDto(filesService)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun getUserByAuth0Id(auth0Id: String): UserDto {
        logger.info { "auth0Id" }
        val user = userRepository.findByAuth0Id(auth0Id)
        return user?.toUserDto(filesService) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}