package no.schmell.backend.controllers.auth

import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.services.auth.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v2/auth")
class AuthController(val authService: AuthService) {

    @GetMapping("/{id}/")
    fun getUser(@PathVariable("id") id: String): UserDto = authService.getById(id.toInt())

    @GetMapping("")
    fun getUsers(): List<UserDto> = authService.getAll()

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: UserDto): UserDto = authService.create(dto)

    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateGame(@PathVariable("id") id: String, @RequestBody dto: UserDto): UserDto = authService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGame(@PathVariable("id") id: String) = authService.delete(id.toInt())

    @PostMapping("/{id}/files/")
    @ResponseStatus(HttpStatus.OK)
    fun addProfilePicture(@PathVariable("id") id: String, @RequestBody file: MultipartFile): UserDto = authService.addProfilePicture(id.toInt(), file)
}