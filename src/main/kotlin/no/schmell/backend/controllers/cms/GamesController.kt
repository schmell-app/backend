package no.schmell.backend.controllers.cms

import no.schmell.backend.dtos.cms.CreateGameDto
import no.schmell.backend.dtos.cms.GameDto
import no.schmell.backend.dtos.cms.GameFilters
import no.schmell.backend.dtos.cms.UpdateGameDto
import no.schmell.backend.lib.enums.GameStatus
import no.schmell.backend.services.cms.GamesService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("cms/game")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no", "https://admin.schmell.no"])
class GamesController(val gamesService: GamesService) {


    @GetMapping("/{id}/")
    fun getGame(@PathVariable("id") id: String): GameDto = gamesService.getById(id.toInt())

    @GetMapping("/")
    fun getGames(
        @RequestParam(value = "name", required = false) name: String?,
        @RequestParam(value = "status", required = false) status: GameStatus?): List<GameDto> {
        return gamesService.getAll(filters = GameFilters(name, status))
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createGame(@RequestBody dto: CreateGameDto): GameDto = gamesService.create(dto)

    @PatchMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateGame(@PathVariable("id") id: String, @RequestBody dto: UpdateGameDto): GameDto = gamesService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGame(@PathVariable("id") id: String) = gamesService.delete(id.toInt())

    @PostMapping("/{id}/files/")
    @ResponseStatus(HttpStatus.OK)
    fun addGameLogo(@PathVariable("id") id: String, @RequestBody file: MultipartFile): GameDto = gamesService.addGameLogo(id.toInt(), file)

}