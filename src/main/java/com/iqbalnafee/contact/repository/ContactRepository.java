package com.iqbalnafee.contact.repository;

import com.iqbalnafee.contact.domain.Contact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactRepository extends PagingAndSortingRepository<Contact,Long>, JpaSpecificationExecutor<Contact> {
}
