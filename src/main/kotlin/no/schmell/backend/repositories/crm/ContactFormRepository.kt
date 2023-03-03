package no.schmell.backend.repositories.crm

import no.schmell.backend.entities.crm.ContactForm
import no.schmell.backend.lib.enums.ContactType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ContactFormRepository : CrudRepository<ContactForm, Int> {
    @Query("SELECT c FROM ContactForm c WHERE " +
            "((:type) IS NULL OR c.type IN (:type)) AND " +
            "((:email) IS NULL OR c.email = (:email)) AND " +
            "((:acceptedTerms) IS NULL OR c.acceptedTerms = (:acceptedTerms))")
    fun findAllByTypeIsInAndEmailAndAcceptedTerms(
        type: List<ContactType>?,
        email: String?,
        acceptedTerms: Boolean?,
        pageable: Pageable
    ): List<ContactForm>

    @Query("SELECT count (c) FROM ContactForm c WHERE " +
            "((:type) IS NULL OR c.type IN (:type)) AND " +
            "((:email) IS NULL OR c.email = (:email)) AND " +
            "((:acceptedTerms) IS NULL OR c.acceptedTerms = (:acceptedTerms))")
    fun countAllByTypeIsInAndEmailAndAcceptedTerms(
        type: List<ContactType>?,
        email: String?,
        acceptedTerms: Boolean?
    ): Int
}