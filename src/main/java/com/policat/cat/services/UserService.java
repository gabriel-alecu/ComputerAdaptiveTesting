package com.policat.cat.services;

import com.policat.cat.temp_containers.UserRegistrationDTO;
import com.policat.cat.entities.User;
import com.policat.cat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User registerUserAccount(UserRegistrationDTO userRegistrationDTO)  {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(userRegistrationDTO.getPassword());
        return repository.save(user);
    }
}
