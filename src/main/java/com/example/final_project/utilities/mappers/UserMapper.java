package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.dto.MessageDTO;
import com.example.final_project.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}

