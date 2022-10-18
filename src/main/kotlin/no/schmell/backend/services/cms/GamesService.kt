package no.schmell.backend.services.cms

import mu.KLogging
import no.schmell.backend.repositories.cms.GameRepository
import no.schmell.backend.dtos.cms.*
import no.schmell.backend.entities.cms.Game
import no.schmell.backend.services.files.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class GamesService(
    val gameRepository: GameRepository,
    val fileService: FileService) {

    companion object: KLogging()

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    fun getAll(filters: GameFilters): List<GameDto> {
        logger.info { filters.status ?: "Null" }

        var games = gameRepository.findAll()
            .filter { it.name.contains(filters.name ?: "") }
        logger.info { games.size }
        games = games.filter {
            if (filters.status != null) it.status == filters.status
            else filters.status!= it.status
        }
        logger.info { games.size }

        return games.map { game -> game.toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile) }
    }

    fun getById(id: Int): GameDto {
        val game = gameRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return game.toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile)
    }

    fun create(dto: GameDto): GameDto = gameRepository.save(dto.toGameEntity()).toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile)

    fun update(id: Int, game: GameDto): GameDto {
        return if (gameRepository.existsById(id)) {
            gameRepository.save(game.toGameEntity()).toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile)
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
            val uploadedFile = fileService.uploadFile(file, "game_pictures")

            gameRepository.save(Game(
                game.id,
                game.name,
                game.description,
                game.lastUpdated,
                game.status,
                uploadedFile.fileName,
                game.releaseDate
            )).toGameDto(gcpProjectId, gcpBucketId, gcpConfigFile)

        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}