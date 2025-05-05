package com.aniket.BookMyShow.controller.utils;

import com.aniket.BookMyShow.dto.UserSignUpRequestDTO;
import com.aniket.BookMyShow.dto.UserSignUpResponseDTO;
import com.aniket.BookMyShow.models.User;

public class UserContollerUtil {
    public static void validateSignUpRequestDTO(UserSignUpRequestDTO userSignUpRequestDTO){
        // validation logic
        // if anything fails, throw an exception
    }
    public static UserSignUpResponseDTO convertUserToUserResponseDTO(User user){
        UserSignUpResponseDTO userSignUpResponseDTO= new UserSignUpResponseDTO();
        userSignUpResponseDTO.setId(user.getId());
        userSignUpResponseDTO.setName(user.getName());
        userSignUpResponseDTO.setEmail(user.getEmail());
        userSignUpResponseDTO.setTickets(user.getTickets());
        return userSignUpResponseDTO;
    }
}
