package com.iqbalnafee.contact.controller;

import com.iqbalnafee.contact.domain.Contact;
import com.iqbalnafee.contact.exception.ResourceNotFoundException;
import com.iqbalnafee.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private ContactService contactService;

    @Value("${msg.title}")
    private String title;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        //we’re adding title attribute to the Model so that they can be accessed from the template
        model.addAttribute("title", title);
        return "index";
        //The function returning String,which is the template name which will be used to render the response
        //The template that will be rendered in this function is index.html
        // which is available in Thymeleaf default templates location in src/main/resources/templates/
    }

    @GetMapping(value = "/contacts")
    public String getContacts(Model model,
                              @RequestParam(name = "page", defaultValue = "1") int pageNumber) {

        List<Contact> contacts = contactService.findAll(pageNumber, ROW_PER_PAGE);
        long count = contactService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("contacts", contacts);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "contact-list";
    }

    @GetMapping(value = {"/contacts/add"})
    public String showAddContact(Model model) {
        Contact contact = new Contact();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);
        return "contact-edit";
    }

    @PostMapping(value = "/contacts/add")
    public String addContact(Model model,
                             @ModelAttribute("contact") Contact contact) {

        try {
            Contact newContact = contactService.save(contact);
            return "redirect:/contacts/" + newContact.getId();
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "contact-edit";
        }

    }

    @GetMapping(value = {"/contacts/{contactId}/edit"})
    public String showEditContact(Model model, @PathVariable long contactId) {
        Contact contact = null;
        try {
            contact = contactService.findById(contactId);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("contact", contact);
        return "contact-edit";
    }

    @PostMapping(value = {"/contacts/{contactId}/edit"})
    public String updateContact(Model model,
                                @PathVariable long contactId,
                                @ModelAttribute("contact") Contact contact){


        try{
            contact.setId(contactId);
            contactService.update(contact);
            return "redirect:/contacts/"+contact.getId();
        }
        catch (Exception ex){
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add",false);
            return "contact-edit";
        }


    }

    @GetMapping(value = {"/contacts/{contactId}"})
    public String getContactById(Model model,
                                 @PathVariable long contactId){

        try{
            Contact contact = contactService.findById(contactId);
            model.addAttribute("contact",contact);
        }
        catch (ResourceNotFoundException exception){
            model.addAttribute("errorMessage","Contact not found");
        }
        return "contact";
    }

    @GetMapping(value ={"/contacts/{contactId}/delete"})
    public String showDeleteContactById(Model model,
                                        @PathVariable long contactId){

        Contact contact = null;
        try {
            contact = contactService.findById(contactId);
            model.addAttribute("contact",contact);
            model.addAttribute("allowDelete",true);
        }
        catch (ResourceNotFoundException exception){
            model.addAttribute("errorMessage","Contact not found");
        }
        return "contact";

    }

    @PostMapping(value = {"/contacts/{contactId}/delete"})
    public String deleteContactById(Model model,
                                   @PathVariable long contactId){

        try{
            contactService.deleteById(contactId);
            return "redirect:/contacts";
        }
        catch (ResourceNotFoundException exception){
            String errorMessage = exception.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "contact";
        }

    }


}
