package no.schmell.backend.services.auth

import mu.KLogging
import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.entities.auth.User
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.services.files.FileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException


@Service
class AuthService(val userRepository: UserRepository, val fileService: FileService) {

    companion object: KLogging()

    fun getAll(): List<UserDto> = userRepository.findAll().map { user -> user.toUserDto() }

    fun getById(id: Int): UserDto {

        val user = userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        return user.toUserDto()
    }

    fun create(dto: UserDto): UserDto = userRepository.save(dto.toUserEntity()).toUserDto()

    fun update(id: Int, user: UserDto): UserDto {
        return if(userRepository.existsById(id)) {
            user.id = id
            logger.info {user.id}
            userRepository.save(user.toUserEntity()).toUserDto()
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
            val uploadedFile = fileService.uploadFile(file, "profile_pictures")
            userRepository.save(User(
                user.id,
                user.username,
                user.email,
                user.phoneNumber,
                user.firstName,
                user.lastName,
                user.alertsForTasks,
                user.alertsForDeadlines,
                uploadedFile.fileName,
            )).toUserDto()
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}