package de.leanovate.dose.customer.service;

import de.leanovate.dose.customer.domain.Customer;
import de.leanovate.dose.customer.model.RegistrationRequest;
import de.leanovate.dose.customer.model.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public RegistrationResponse registerCustomer(@RequestBody RegistrationRequest registration) {

        final Customer customer = customerRepository.save(new Customer(registration));

        return new RegistrationResponse(true, customer.getId());
    }
}
