package com.policat.cat.services;

import com.policat.cat.auth.AuthedUser;
import com.policat.cat.dtos.PasswordChangeDTO;
import com.policat.cat.dtos.UserRegistrationDTO;
import com.policat.cat.entities.User;
import com.policat.cat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new AuthedUser(user);
    }

    public User create(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public AuthedUser findAndAuthenticateUser(String username, String providedPassword) {
        User user = findUser(username);
        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(providedPassword, user.getPassword())) {
            return new AuthedUser(user);
        }

        return null;
    }

    public User registerUserAccount(UserRegistrationDTO userRegistrationDTO) {
        if(findUser(userRegistrationDTO.getUsername()) != null) {
            return null;
        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        return create(user, userRegistrationDTO.getPassword());
    }

    public User changeUserPassword(PasswordChangeDTO passwordChangeDTO, User user) {
        if (!passwordEncoder.matches(passwordChangeDTO.getOld_password(), user.getPassword())) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getPassword()));
        userRepository.save(user);
        return user;
    }
}
