package no.schmell.backend.repositories.crm

import no.schmell.backend.entities.crm.ContactForm
import org.springframework.data.repository.CrudRepository

interface ContactFormRepository : CrudRepository<ContactForm, Int>