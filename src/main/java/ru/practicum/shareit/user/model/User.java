package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {

    private Long id;
    @NotBlank(message = "Имя пользователя не должно быть пустым.")
    private String name;
    @Email(message = "Введённая строка не похожа на адрес электронной почты, попробуйте еще разок")
    @NotBlank(message = "Адрес электронной почты не может быть пустым.")
    private String email;


}