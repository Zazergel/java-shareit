package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MapperService {
    User getUserById(Long id);

    Item getItemById(Long id);

    List<Booking> getItemLastBooking(Long id, LocalDateTime date, Status status);

    List<Booking> getItemNextBooking(Long id, LocalDateTime date, Status status);

    List<Comment> getItemComments(Long id);
}
