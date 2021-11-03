package com.iqbalnafee.contact.controller;

import com.iqbalnafee.contact.domain.Contact;
import com.iqbalnafee.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                              @RequestParam(value = "page", defaultValue = "1") int pageNumber) {

        List<Contact> contacts = contactService.findAll(pageNumber, ROW_PER_PAGE);
        long count = contactService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * count) < count;
        model.addAttribute("contacts", contacts);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "contact-list";
    }


}
