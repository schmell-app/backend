package no.schmell.backend.controllers.crm

import no.schmell.backend.dtos.crm.ContactFormFilters
import no.schmell.backend.dtos.crm.CreateContactForm
import no.schmell.backend.lib.enums.ContactType
import no.schmell.backend.services.crm.ContactFormService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v2/crm/contact")
@CrossOrigin(origins = ["http://localhost:3000", "https://admin.dev.schmell.no"])
class  ContactFormController(val contactFormService: ContactFormService) {

    @GetMapping("/{id}/")
    fun getContactForm(@PathVariable("id") id: String) = contactFormService.getById(id.toInt())

    @GetMapping("")
    fun getContactForms(
        @RequestParam(value = "type", required = false) type: String? = null,
        @RequestParam(value = "email", required = false) email: String? = null,
        @RequestParam(value = "acceptedTerms", required = false) acceptedTerms: Boolean? = null,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: String = "1",
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: String = "10"
    ) =
        contactFormService.getAll(
            ContactFormFilters(type?.split("+")?.map { ContactType.valueOf(it) },
                email, acceptedTerms,
                page.toInt(),
                pageSize.toInt()
            )
        )

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createContactForm(@RequestBody dto: CreateContactForm) = contactFormService.create(dto)

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteContactForm(@PathVariable("id") id: String) = contactFormService.delete(id.toInt())

}