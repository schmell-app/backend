package no.schmell.backend.dtos.crm

import no.schmell.backend.entities.crm.ContactForm
import no.schmell.backend.lib.enums.ContactType

data class CreateContactForm(
    val type: ContactType,
    val message: String,
    val email: String?,
    val acceptedTerms: Boolean,
)

data class ContactFormFilters(
    val type: List<ContactType>?,
    val email: String?,
    val acceptedTerms: Boolean?,
    val page: Int,
    val pageSize: Int,
)

data class ContactFormPaginatedResponse(
    val contactForms: List<ContactForm>,
    val total: Int,
    val page: Int,
    val lastPage: Int,
)