package com.server.controller;

import java.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.server.dto.CustomerDto;
import com.server.dto.StatusDto;
import com.server.entity.Customer;
import com.server.entity.Address;
import com.server.entity.User;
import com.server.exception.UserServiceException;
import com.server.service.UserService;

public class UserRegistrationController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(path = "/register", consumes = "multipart/form-data", produces = "application/json")
	public StatusDto register(@RequestBody CustomerDto customerDto) {
		try {
			
			Customer customer = new Customer();
			
            //BeanUtils.copyProperties(customerDto, customer);
            //Redundant code, will be place in a helper method
            customer.setName(customerDto.getFirstName() + " " + customerDto.getLastName());
            customer.setContactNo(customerDto.getContactNo());
            customer.setDateOfBirth(customerDto.getDateOfBirth());
            customer.setEmailId(customerDto.getEmailId());

            Address address = new Address();
            address.setCity(customerDto.getCity());
            address.setPincode(customerDto.getPincode());
            address.setLandMark(customerDto.getLandmark());
            
            customer.setAddress(address);

            User user = new User();
            user.setEmailId(customerDto.getEmailId());
            user.setPassword(customerDto.getPassword());
            user.setCustomer(customer);
            user.setLastPasswordSet(LocalDate.now());
            user.setRole("USER");
            
            userService.register(user);
			
			StatusDto status = new StatusDto();
			status.setMessage("Registered successfully!");
			status.setStatus(StatusDto.StatusType.SUCCESS);
			return status;

		} catch (Exception e) {
			e.printStackTrace();
			StatusDto status = new StatusDto();

			if (e instanceof UserServiceException) {
				status.setMessage(e.getMessage());
			} else {
				status.setMessage(e.getMessage());
			}

			status.setStatus(StatusDto.StatusType.FAILURE);
			return status;
		}

	}

}
