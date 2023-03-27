package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.dtos.cms.*
import no.schmell.backend.dtos.cms.game.CreateGameDto
import no.schmell.backend.dtos.cms.game.GameDto
import no.schmell.backend.dtos.cms.game.GameFilters
import no.schmell.backend.dtos.cms.game.UpdateGameDto
import no.schmell.backend.entities.cms.Game
import no.schmell.backend.lib.enums.GameStatus
import no.schmell.backend.services.files.FilesService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class GamesService(
    val gameRepository: GameRepository,
    val filesService: FilesService) {

    companion object: KLogging()

    fun getAll(filters: GameFilters): List<GameDto> {
        var games = gameRepository.findAll()

        if (filters.name != null) games = games.filter { it.name.contains(filters.name) }
        if (filters.status != null) games = games.filter { it.status == filters.status }

        return games.map { game -> game.toGameDto(filesService) }
    }

    fun getById(id: Int): GameDto {
        val game = gameRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return game.toGameDto(filesService)
    }

    fun create(dto: CreateGameDto): GameDto {
        val game = Game(
            null,
            dto.name,
            dto.description,
            LocalDateTime.now(),
            dto.status ?: GameStatus.DEVELOPMENT,
            null,
        null,
            null,
            dto.isFamilyFriendly
        )
        return gameRepository.save(game).toGameDto(filesService)
    }

    fun update(id: Int, dto: UpdateGameDto): GameDto {
        val gameToUpdate = gameRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val updatedGame = Game(
            gameToUpdate.id,
            gameToUpdate.name,
            dto.description ?: gameToUpdate.description,
            LocalDateTime.now(),
            dto.status ?: gameToUpdate.status,
            gameToUpdate.logo,
            gameToUpdate.weeks,
            gameToUpdate.questions,
            gameToUpdate.isFamilyFriendly
        )
        return gameRepository.save(updatedGame).toGameDto(filesService)
    }

    fun delete(id: Int) {
        return if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun addGameLogo(id: Int, file: MultipartFile): GameDto {
        val game = gameRepository.findByIdOrNull(id)

        return if (game != null) {
            val uploadedFile = filesService.saveFile(file, "schmell-files", "gamePictures")

            gameRepository.save(Game(
                game.id,
                game.name,
                game.description,
                game.lastUpdated,
                game.status,
                uploadedFile?.fileName,
                game.weeks,
                game.questions,
                game.isFamilyFriendly,
            )).toGameDto(filesService)

        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}