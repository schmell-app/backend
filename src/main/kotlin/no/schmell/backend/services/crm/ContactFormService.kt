package no.schmell.backend.services.crm

import mu.KLogging
import no.schmell.backend.dtos.crm.ContactFormFilters
import no.schmell.backend.dtos.crm.CreateContactForm
import no.schmell.backend.entities.crm.ContactForm
import no.schmell.backend.lib.Mailer
import no.schmell.backend.repositories.auth.UserRepository
import no.schmell.backend.repositories.crm.ContactFormRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ContactFormService(
    private val contactFormRepository: ContactFormRepository,
    private val userRepository: UserRepository
) {

    companion object: KLogging()

    @Value("\${spring.mail.api-key}")
    lateinit var apiKey: String

    @Value("\${spring.mail.admin-url}")
    lateinit var adminUrl: String

    fun getAll(filters: ContactFormFilters): List<ContactForm> {
        var contactForms = contactFormRepository.findAll()

        if (filters.type != null) contactForms = contactForms.filter { it.type == filters.type }

        if (filters.email != null) contactForms = contactForms.filter { it.email == filters.email }

        if (filters.acceptedTerms != null) contactForms = contactForms.filter { it.acceptedTerms == filters.acceptedTerms }

        return contactForms.map { contactForm -> contactForm }
    }

    fun getById(id: Int): ContactForm {
        return contactFormRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun create(contactFormDto: CreateContactForm): ContactForm {
        val mailer = Mailer(apiKey, adminUrl)
        mailer.sendContactFormSubmission(getUserEmails(), contactFormDto)

        return contactFormRepository.save(ContactForm(
            null,
            contactFormDto.type,
            contactFormDto.message,
            contactFormDto.email,
            contactFormDto.acceptedTerms
        ))
    }

    fun delete(id: Int) {
        return if (contactFormRepository.existsById(id)) {
            contactFormRepository.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    private fun getUserEmails(): List<String> {
        return userRepository.findAll().filter { it.alertsForTasks }.map { it.email }
    }
}