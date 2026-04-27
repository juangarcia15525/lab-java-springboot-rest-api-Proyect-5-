package org.example.service;

import org.example.exception.CustomerNotFoundException;
import org.example.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();

    public Customer createCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer getCustomerByEmail(String email) {
        return customers.stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado: " + email));
    }

    public Customer updateCustomer(String email, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerByEmail(email);
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setAge(updatedCustomer.getAge());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        return existingCustomer;
    }

    public void deleteCustomer(String email) {
        Customer customer = getCustomerByEmail(email);
        customers.remove(customer);
    }
}
