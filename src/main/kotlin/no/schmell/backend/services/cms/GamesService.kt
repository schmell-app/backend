package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.dtos.cms.*
import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.files.GenerateObjectSignedUrl
import no.schmell.backend.services.files.FileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class GamesService(
    val gameRepository: GameRepository,
    val filesService: FileService,
    val generateObjectSignedUrl: GenerateObjectSignedUrl) {

    companion object: KLogging()

    fun getAll(filters: GameFilters): List<GameDto> {
        var games = gameRepository.findAll()

        if (filters.name != null) games = games.filter { it.name.contains(filters.name) }
        if (filters.status != null) games = games.filter { it.status == filters.status }

        return games.map { game -> game.toGameDto(generateObjectSignedUrl) }
    }

    fun getById(id: Int): GameDto {
        val game = gameRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return game.toGameDto(generateObjectSignedUrl)
    }

    fun create(dto: GameDto): GameDto = gameRepository.save(dto.toGameEntity()).toGameDto(generateObjectSignedUrl)

    fun update(id: Int, game: GameDto): GameDto {
        return if (gameRepository.existsById(id)) {
            gameRepository.save(game.toGameEntity()).toGameDto(generateObjectSignedUrl)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun delete(id: Int) {
        return if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun addGameLogo(id: Int, file: MultipartFile): GameDto {
        val game = gameRepository.findByIdOrNull(id)

        return if (game != null) {
            val uploadedFile = filesService.uploadFile(file, "game_pictures")

            gameRepository.save(Game(
                game.id,
                game.name,
                game.description,
                game.lastUpdated,
                game.status,
                uploadedFile?.fileName,
                game.releaseDate
            )).toGameDto(generateObjectSignedUrl)

        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}