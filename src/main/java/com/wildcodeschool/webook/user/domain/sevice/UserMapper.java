package com.wildcodeschool.webook.user.domain.sevice;

import com.wildcodeschool.webook.user.domain.dto.UserDTO;
import com.wildcodeschool.webook.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDTO transformUserEntityInUserDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getUsername(),
                user.getZip_code(),
                user.getCity(),
                user.getRole(),
                user.getBooks(),
                user.getEnabled());
    }
}
