package de.leanovate.dose.customer.service;

import de.leanovate.dose.customer.domain.Customer;
import de.leanovate.dose.customer.model.LoginRequest;
import de.leanovate.dose.customer.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest login) {

        final Customer customer = customerRepository.findByEmail(login.email);

        if (customer != null) {
            return new LoginResponse(customer.checkPassword(login.password), customer.getCustomerId());
        }
        return new LoginResponse(false, null);
    }
}
