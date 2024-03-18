package com.wildcodeschool.webook.Auth.domain.service;

import com.wildcodeschool.webook.Auth.domain.dto.PasswordDTO;
import com.wildcodeschool.webook.Auth.domain.dto.UserDTO;
import com.wildcodeschool.webook.Auth.domain.entity.User;
import com.wildcodeschool.webook.Auth.infrastructure.exception.NotFoundException;
import com.wildcodeschool.webook.Auth.infrastructure.repository.UserRepository;
import com.wildcodeschool.webook.book.domain.entity.Book;
import com.wildcodeschool.webook.book.domain.service.CategoryService;
import com.wildcodeschool.webook.book.infrastructure.repository.BookRepository;
import com.wildcodeschool.webook.book.infrastructure.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bcryptPwEncoder;
    private final UserMapper userMapper;
    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public UserService(UserRepository repository, BCryptPasswordEncoder bcryptPwEncoder, UserMapper userMapper,
                       BookRepository bookRepository, CategoryService categoryService) {
        this.repository = repository;
        this.bcryptPwEncoder = bcryptPwEncoder;
        this.userMapper = userMapper;
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    public List<UserDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::transformUserEntityInUserDTO)
                .toList();
    }

    public UserDTO getOneUser(Long id) {
        return repository.findById(id)
                .map(userMapper::transformUserEntityInUserDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UserDTO createUser(User newUser) {
        return userMapper.transformUserEntityInUserDTO(repository.save(newUser));
    }

    public UserDTO updateUser(User newUser, Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setCity(newUser.getCity());
                    user.setZip_code(newUser.getZip_code());

                    if (!newUser.getBooks().isEmpty()) {
                        for (Book book : newUser.getBooks()) {
                            book.setOwner(user);
                            bookRepository.save(book);
                        }
                    }

                    if (!newUser.getPreferences().isEmpty()) {
                        newUser.getPreferences()
                                .stream().map(preference -> categoryService.updateCategory(preference, preference.getId()));
                    }
                    return userMapper.transformUserEntityInUserDTO(repository.save(user));
                })
                .orElseThrow(NotFoundException::new);
    }

    public HttpStatus updatePassword(PasswordDTO passwords, Long id) {
        String userOldPw = repository.findById(id)
                .map(user -> user.getPassword())
                .orElseThrow(NotFoundException::new);
        if (!bcryptPwEncoder.matches(passwords.oldPassword(), userOldPw)) {
            throw new NotFoundException();
        }
        String hashedNewPassword = bcryptPwEncoder.encode(passwords.newPassword());

        return repository.findById(id)
                .map(user -> {
                    user.setPassword(hashedNewPassword);
                    repository.save(user);
                    return HttpStatus.OK;
                })
                .orElseThrow(NotFoundException::new);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public User login(User user) {
        User userEntity = getUserEntityByEmail(user.getEmail());
        if (!bcryptPwEncoder.matches(user.getPassword(), userEntity.getPassword())) {
            throw new NotFoundException();
        }
        user.setRole(userEntity.getRole());
        return user;
    }

    public User getUserEntityByEmail(String email) {
        try {
            return repository.findByEmail(email);

        } catch (Exception e) {
            throw new NotFoundException();
        }
    }
}
