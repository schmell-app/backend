package no.schmell.backend.controllers.auth

import no.schmell.backend.dtos.auth.UpdateUserDto
import no.schmell.backend.dtos.auth.UserDto
import no.schmell.backend.services.auth.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("users")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class AuthController(val authService: AuthService) {

    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@PathVariable("id") id: String): UserDto = authService.getById(id.toInt())

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getUsers(): List<UserDto> = authService.getAll()

    @GetMapping("/find/")
    @ResponseStatus(HttpStatus.OK)
    fun getUserByAuth0Id(
        @RequestParam(value = "auth0Id", required = true) auth0Id: String)
    : UserDto = authService.getUserByAuth0Id(auth0Id)

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: UserDto): UserDto = authService.create(dto)

    @PatchMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@PathVariable("id") id: String, @RequestBody dto: UpdateUserDto): UserDto = authService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("id") id: String) = authService.delete(id.toInt())

    @PostMapping("/{id}/files/")
    @ResponseStatus(HttpStatus.OK)
    fun addProfilePicture(@PathVariable("id") id: String, @RequestBody file: MultipartFile): UserDto = authService.addProfilePicture(id.toInt(), file)

}