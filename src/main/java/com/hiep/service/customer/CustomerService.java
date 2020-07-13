package com.hiep.service.customer;

import com.hiep.model.Customer;
import com.hiep.model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<Customer> findAll(Pageable pageable);

    Customer findById(Long id);

    void save(Customer customer);

    void remove(Long id);

    Page<Customer> findAllByFirstNameContaining(String firstname, Pageable pageable);
    Iterable<Customer> findAllByProvince(Province province);
}
