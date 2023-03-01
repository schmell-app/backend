package no.schmell.backend.dtos.crm

import no.schmell.backend.lib.enums.ContactType

data class CreateContactForm(
    val type: ContactType,
    val message: String,
    val email: String?,
    val acceptedTerms: Boolean,
)

data class ContactFormFilters(
    val type: ContactType?,
    val email: String?,
    val acceptedTerms: Boolean?,
)