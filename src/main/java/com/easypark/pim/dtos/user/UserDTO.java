package com.easypark.pim.dtos.user;

import com.easypark.pim.entities.User;
import com.easypark.pim.entities.enums.UserRole;

public record UserDTO(Long id, String nome, String login, UserRole userRole) {

    public UserDTO(User user) {
        this(user.getIdUsuario(), user.getNome(), user.getLogin(), user.getRole());
    }
}