package no.schmell.backend.controllers.tasks

import no.schmell.backend.dtos.tasks.CommentDto
import no.schmell.backend.dtos.tasks.CommentFilters
import no.schmell.backend.dtos.tasks.CommentListDto
import no.schmell.backend.dtos.tasks.CreateCommentParams
import no.schmell.backend.services.tasks.CommentsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/tasks/comment")
class CommentsController(val commentsService: CommentsService) {

    @GetMapping("/{id}/")
    fun getComment(@PathVariable("id") id: String): CommentDto = commentsService.getById(id.toInt())

    @GetMapping("")
    fun getComments(
        @RequestParam(value = "relatedTask", required = false) relatedTask: Int?): List<CommentListDto> =
        commentsService.getAll(CommentFilters(relatedTask))

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(@RequestBody(required = true) dto: CreateCommentParams): CommentDto = commentsService.create(dto)

    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    fun updateComment(@PathVariable("id") id: String, @RequestBody(required = true) dto: CommentDto): CommentDto =
        commentsService.update(id.toInt(), dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(@PathVariable("id") id: String) = commentsService.delete(id.toInt())
}