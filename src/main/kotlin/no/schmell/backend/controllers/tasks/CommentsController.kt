package no.schmell.backend.controllers.tasks

import no.schmell.backend.dtos.tasks.CommentDto
import no.schmell.backend.dtos.tasks.CommentFilters
import no.schmell.backend.dtos.tasks.CreateCommentDto
import no.schmell.backend.services.tasks.CommentsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/tasks/comment")
@CrossOrigin("http://localhost:3000")
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