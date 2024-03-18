package com.wildcodeschool.webook.Auth.domain.service;

import com.wildcodeschool.webook.Auth.domain.dto.UserDTO;
import com.wildcodeschool.webook.Auth.domain.entity.User;
import com.wildcodeschool.webook.Auth.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bcryptPwEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public UserRegistrationService(UserRepository repository, BCryptPasswordEncoder bcryptPwEncoder,
                                   UserMapper userMapper, RoleService roleService) {
        this.repository = repository;
        this.bcryptPwEncoder = bcryptPwEncoder;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    public UserDTO registration(User userData) throws Exception {
        userData.setPassword(bcryptPwEncoder.encode(userData.getPassword()));
        userData.setRole(roleService.getOneRole(1));
        userData.setEnabled(true);

        try {
            User user = repository.save(userData);
            return userMapper.transformUserEntityInUserDTO(user);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
