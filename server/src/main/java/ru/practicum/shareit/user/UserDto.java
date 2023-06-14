package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@ToString
public class UserDto {
    Long id;
    String name;
    String email;
}
