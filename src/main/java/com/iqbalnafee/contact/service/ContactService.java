package com.iqbalnafee.contact.service;

import com.iqbalnafee.contact.domain.Contact;
import com.iqbalnafee.contact.exception.ResourceNotFoundException;
import com.iqbalnafee.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    private boolean existsById(Long id){
        return contactRepository.existsById(id);
    }

    public Contact findById(Long id) throws ResourceNotFoundException {
        Contact contact = contactRepository.findById(id).orElse(null);
        if(contact == null){
            throw  new ResourceNotFoundException("Can not found contact with id: "+id);
        }
        else{
            return contact;
        }
    }

    public List<Contact> findAll(int pageNumber, int rowPerPage){

        List<Contact> contacts = new ArrayList<>();
        Pageable sortedByIdAsc = (Pageable) PageRequest.of(pageNumber-1,rowPerPage, Sort.by("id").ascending());
        //contactRepository.findAll().forEach(contact -> contacts.add(contact));
        contactRepository.findAll((org.springframework.data.domain.Pageable) sortedByIdAsc).forEach(contacts::add);
        return contacts;

    }

}
