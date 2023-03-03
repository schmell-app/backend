package no.schmell.backend.controllers.crm

import no.schmell.backend.dtos.crm.CommentDto
import no.schmell.backend.dtos.crm.CommentFilters
import no.schmell.backend.dtos.crm.CreateCommentDto
import no.schmell.backend.services.crm.CommentsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/crm/tasks/comment")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no"])
class CommentsController(val commentsService: CommentsService) {

    @GetMapping("/{id}/")
    fun getComment(@PathVariable("id") id: String): CommentDto = commentsService.getById(id.toInt())

    @GetMapping("")
    fun getComments(
        @RequestParam(value = "relatedTask", required = false) relatedTask: String?): List<CommentDto> =
        commentsService.getAll(CommentFilters(relatedTask?.toInt()))

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(@RequestBody(required = true) dto: CreateCommentDto): CommentDto = commentsService.create(dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(@PathVariable("id") id: String) = commentsService.delete(id.toInt())
}