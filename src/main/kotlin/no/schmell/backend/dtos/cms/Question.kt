package no.schmell.backend.dtos.cms

import no.schmell.backend.lib.enums.GroupSize

data class QuestionDto(
    val id: Int?,
    val activeWeeks: List<Int>?,
    val questionDescription: String,
    val phase: Int,
    val function: QuestionFunctionDto?,
    val punishment: Int?,
    val questionPicture: String?,
    val relatedGame: Int,
    val questionPictureUrl: String?,
    val questionType: QuestionTypeDto,
    val groupSize: GroupSize,
    val dislikesCount: Int,
)

data class QuestionPaginatedResponse(
    val questions: List<QuestionDto>,
    val total: Int,
    val page: Int,
    val lastPage: Int
)

data class CreateQuestionDto(
    var activeWeeks: List<Int>?,
    val questionDescription: String,
    val phase: Int,
    val function: CreateQuestionFunctionDto?,
    val punishment: Int?,
    val relatedGame: Int,
    val relatedQuestionType: Int,
    val groupSize: GroupSize?
)

data class UpdateQuestionDto(
    val questionDescription: String?,
    val phase: Int?,
    val function: UpdateQuestionFunction?,
    val punishment: Int?,
    val activeWeeks: List<Int>?,
    val relatedQuestionType: Int,
    val groupSize: GroupSize?,
)

data class GamePlayResponse(
    val uneditedQuestions: List<QuestionDto>,
    val editedQuestions: List<QuestionDto>
)
data class GamePlayParams(
    val relatedGame: Int,
    val weekNumber: Int,
    val players: List<String>
)

data class QuestionFilter(
    val relatedGame: Int?,
    val weekNumbers: List<Int>?,
    val questionType: Int?,
    val questionSearch: String?,
    val hasDislikes: Boolean?,
    val dislikesGreaterThan: Int?,
    val page: Int,
    val pageSize: Int,
)

data class AddPlayersInGameParams(
    val players: List<String>,
    val currentIndex: Int,
    val uneditedQuestions: List<QuestionDto>,
    val editedQuestions: List<QuestionDto>,
)