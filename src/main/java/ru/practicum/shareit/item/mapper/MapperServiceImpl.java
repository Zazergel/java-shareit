package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapperServiceImpl implements MapperService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует."));
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не существует."));
    }

    @Override
    public List<Booking> getItemLastBooking(Long id, LocalDateTime date, Status status) {
        return bookingRepository.findByItemIdAndStartBeforeAndStatusEqualsOrderByStartDesc(
                id, date, status);
    }

    @Override
    public List<Booking> getItemNextBooking(Long id, LocalDateTime date, Status status) {
        return bookingRepository.findByItemIdAndStartAfterAndStatusEqualsOrderByStartAsc(
                id, date, status);
    }

    @Override
    public List<Comment> getItemComments(Long id) {
        return commentRepository.findByItemId(id);
    }
}
