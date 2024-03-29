package no.schmell.backend.lib

import com.sendgrid.*
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import mu.KLogging
import no.schmell.backend.dtos.crm.CreateContactForm
import no.schmell.backend.entities.crm.Task

class Mailer(
    private val apiKey : String,
    private val adminUrl : String
) {

    companion object: KLogging()

    private fun sendMail(from: Email, to: List<Email>, subject: String, content: Content) {
        val sg = SendGrid(apiKey)
        val request = Request()

        to.forEach {
            try {
                val mail = Mail(from, subject, it, content)
                request.method = Method.POST
                request.endpoint = "mail/send"
                request.body = mail.build()
                val response = sg.api(request)

                logger.info {
                    "Mail sent \n" +
                    "Status code: ${response.statusCode} \n" +
                    "Body: ${response.body} \n" +
                    "Headers: ${response.headers}"
                }
            } catch (ex: Exception) {
                logger.error { "Error sending mail: ${ex.message}" }
            }
        }
    }

    fun sendTaskCreatedEmail(recipients: List<String>, task: Task) {
        val from = Email("admin@schmell.no")
        val to = recipients.map { Email(it) }
        val subject = "Ny oppgave opprettet: ${task.title}"
        val content = Content("text/html", "" +
            "<h1>Ny oppgave: ${task.title}</h1>" +
            "<p>Oppgavebeskrivelse: ${task.description}</p>" +
            "<p>Oppgavekategori: ${task.category}</p>" +
            "<p>Oppgaveprioritet: ${task.priority}</p>" +
            "<p>Oppgavefrist: ${task.deadline}</p>" +
            "<p>Oppgaveansvarlig: ${task.responsibleUser.firstName} ${task.responsibleUser.lastName}</p>" +
            "<p>Løs oppgaven her: <a href=\"${adminUrl}/tasks/${task.id}\">${adminUrl}/tasks/${task.id}</a></p>" +
            "<p>Med vennlig hilsen,</p>" +
            "<p>Schmell 🤘🏾</p>"
        )
        this.sendMail(from, to, subject, content)
    }

    fun sendTaskReachingDeadline(recipients: List<String>, task: Task) {
        val from = Email("admin@schmell.no")
        val to = recipients.map { Email(it) }
        val subject = "Oppgave nærmer seg frist: ${task.title}"
        val content = Content(
            "text/html", "" +
                    "<h1>Oppgave nærmer seg frist: ${task.title}</h1>" +
                    "<p>Oppgavebeskrivelse: ${task.description}</p>" +
                    "<p>Oppgavekategori: ${task.category}</p>" +
                    "<p>Oppgaveprioritet: ${task.priority}</p>" +
                    "<p>Oppgavefrist: ${task.deadline}</p>" +
                    "<p>Oppgaveansvarlig: ${task.responsibleUser.firstName} ${task.responsibleUser.lastName}</p>" +
                    "<p>Løs oppgaven her: <a href=\"${adminUrl}/tasks/${task.id}\">${adminUrl}/tasks/${task.id}</a></p>" +
                    "<p>Med vennlig hilsen,</p>" +
                    "<p>Schmell 🤘🏾</p>"
        )
        this.sendMail(from, to, subject, content)
    }

    fun sendContactFormSubmission(recipients: List<String>, contactForm: CreateContactForm) {
        val from = Email("admin@schmell.no")
        val to = recipients.map { Email(it) }
        val subject = "Ny forespørsel gjennom nettsiden: ${contactForm.type}"
        val content = Content(
            "text/html", "" +
                    "<h1>Ny forespørsel gjennom nettsiden: ${contactForm.type}</h1>" +
                    "<p>Type: ${contactForm.type}</p>" +
                    "<p>E-post: ${contactForm.email}</p>" +
                    "<p>Melding: ${contactForm.message}</p>" +
                    "<p>Med vennlig hilsen,</p>" +
                    "<p>Schmell 🤘🏾</p>"
        )
        this.sendMail(from, to, subject, content)
    }
}