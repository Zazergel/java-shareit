package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@ToString
public class BookingRequestDto {
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;

}
