package com.wildcodeschool.webook.Auth.domain.dto;

import com.wildcodeschool.webook.book.domain.entity.Book;
import com.wildcodeschool.webook.book.domain.entity.Category;
import com.wildcodeschool.webook.Auth.domain.entity.Role;
import com.wildcodeschool.webook.fileUpload.domain.entity.Media;

import java.util.List;

public record PasswordDTO(String oldPassword, String newPassword) {
}
