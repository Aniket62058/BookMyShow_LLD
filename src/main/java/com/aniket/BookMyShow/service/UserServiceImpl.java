package com.aniket.BookMyShow.service;

import com.aniket.BookMyShow.Exceptions.InvalidCredentialException;
import com.aniket.BookMyShow.Exceptions.UserAlreadyExistException;
import com.aniket.BookMyShow.Exceptions.UserNotFoundException;
import com.aniket.BookMyShow.models.User;
import com.aniket.BookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with given email does not exist : " + email);
        }

        User user = optionalUser.get();
        if(user.getPassword().equals(password)){
            return user;
        } else {
            throw new InvalidCredentialException("Credentials are invalid");
        }
    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User with given email is already present");
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setEmail(email);

        return userRepository.save(newUser);
    }
}
