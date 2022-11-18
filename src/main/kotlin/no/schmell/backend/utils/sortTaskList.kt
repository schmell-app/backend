package no.schmell.backend.utils

import no.schmell.backend.entities.tasks.Task

/**
 * Function to sort tasks based on parameter.
 * - PRIORITY_DESC = Prioritet (høy til lav)
 * - PRIORITY_ASC = Prioritet (lav til høy)
 * - DEADLINE_DESC = Frist (eldst til nyest)
 * - DEADLINE_ASC = Frist (nyest til eldst)
 * - CATEGORY_ASC = Kategori (A til Å)
 * - CATEGORY_DESC = Kategori (Å til A)
 * @param listToSort - sortValue
 * @return sortedList
 */
fun sortTaskList(listToSort: List<Task>, sortValue: String): List<Task> {
    when (sortValue) {
        "PRIORITY_DESC" -> return listToSort.sortedBy { it.priority }
        "PRIORITY_ASC" -> return listToSort.sortedByDescending { it.priority }
        "DEADLINE_DESC" -> return listToSort.sortedByDescending { it.deadline }
        "DEADLINE_ASC" -> return listToSort.sortedBy { it.deadline }
        "CATEGORY_ASC" -> return listToSort.sortedBy { it.category }
        "CATEGORY_DESC" -> return listToSort.sortedByDescending { it.category }
    }
    return listToSort
}