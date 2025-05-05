package com.aniket.BookMyShow.controller;

import com.aniket.BookMyShow.controller.utils.UserContollerUtil;
import com.aniket.BookMyShow.dto.UserSignUpRequestDTO;
import com.aniket.BookMyShow.dto.UserSignUpResponseDTO;
import com.aniket.BookMyShow.models.User;
import com.aniket.BookMyShow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public UserSignUpResponseDTO signUp(UserSignUpRequestDTO requestDTO){
        User user;
        UserSignUpResponseDTO userSignUpResponseDTO = new UserSignUpResponseDTO();
        try {
            UserContollerUtil.validateSignUpRequestDTO(requestDTO);
            user = userService.signUp(requestDTO.getName(), requestDTO.getEmail(), requestDTO.getPassword());
            userSignUpResponseDTO = UserContollerUtil.convertUserToUserResponseDTO(user);
            userSignUpResponseDTO.setResponseCode(200);
            userSignUpResponseDTO.setResponseMessage("SUCCESS");
            return userSignUpResponseDTO;
        } catch (Exception e){
            userSignUpResponseDTO.setResponseCode(500);
            userSignUpResponseDTO.setResponseMessage("Internal Server Error");
            return userSignUpResponseDTO;
        }
    }
}
