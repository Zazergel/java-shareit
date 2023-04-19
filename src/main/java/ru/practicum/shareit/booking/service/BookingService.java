package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto getById(Long userId, Long id);

    List<BookingResponseDto> getAllByBookerId(Long userId, String state);

    List<BookingResponseDto> getAllByOwnerId(Long userId, String state);

    BookingResponseDto add(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto update(Long userId, Long id, Boolean approved);
}
