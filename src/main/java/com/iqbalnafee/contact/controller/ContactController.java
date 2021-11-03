package com.iqbalnafee.contact.controller;

import com.iqbalnafee.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

//    @GetMapping(value = "/contacts")


}
