package com.example.final_project.utilities;

import com.example.final_project.database.entities.user.User;
import com.example.final_project.dto.UserDTO;

public class UserMapper {
    public static UserDTO userToUserDTO(User user){
        if(user==null) return null;
        return new UserDTO(
                user.getLogin(),
                user.getName(),
                user.getRole(),
                user.getEmail(),
                user.getAge(),
                user.getRegistrationDate(),
                user.getSurname(),
                user.getPhone(),
                user.getPhoto(),
                user.getBlocked_state());
    }
    public static User userDTOToUser(UserDTO userDTO){
        if(userDTO==null) return null;
        return new User(
                userDTO.getLogin(),
                "",
                userDTO.getName(),
                userDTO.getRole(),
                userDTO.getEmail(),
                userDTO.getAge(),
                userDTO.getRegistrationDate(),
                userDTO.getSurname(),
                userDTO.getPhone(),
                userDTO.getPhoto(),
                userDTO.getBlocked_state());
    }
}
