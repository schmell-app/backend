package no.schmell.backend.entities.crm

import no.schmell.backend.lib.enums.ContactType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "contact_form")
class ContactForm (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: ContactType,

    @Column(name = "message", nullable = false)
    val message: String,

    @Column(name = "email", nullable = true)
    val email: String?,

    @Column(name = "accepted_terms", nullable = false)
    val acceptedTerms: Boolean,

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdDate: LocalDateTime,
)