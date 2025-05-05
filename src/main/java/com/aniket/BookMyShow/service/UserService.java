package com.aniket.BookMyShow.service;

import com.aniket.BookMyShow.models.User;

public interface UserService {
    User login(String email, String password);
    User signUp(String name, String email, String password);
}
