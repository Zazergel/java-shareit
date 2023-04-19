package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto getById(Long userId, Long id) {
        log.info("Вывод бронирования с id {}.", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с таким id не существует."));

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Просмотр бронирования доступно только автору или владельцу.");
        }

        return bookingMapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllByBookerId(Long userId, String state) {
        log.info("Вывод всех бронирований пользователя {} и статусом {}.", userId, state);

        State stateEnum = getState(state);
        userService.getById(userId);

        List<Booking> bookings;
        LocalDateTime dateTime = LocalDateTime.now();

        switch (stateEnum) {
            case ALL:
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, dateTime, dateTime);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(
                        userId, dateTime, Status.APPROVED);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(
                        userId, dateTime);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDesc(
                        userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusEqualsOrderByStartDesc(
                        userId, Status.REJECTED);
                break;
            default:
                throw new BookingException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllByOwnerId(Long userId, String state) {
        log.info("Вывод всех вещей пользователя {} и статусом {}.", userId, state);

        State stateEnum = getState(state);
        userService.getById(userId);

        List<Booking> bookings;
        LocalDateTime dateTime = LocalDateTime.now();

        switch (stateEnum) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, dateTime, dateTime);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndBeforeAndStatusEqualsOrderByStartDesc(
                        userId, dateTime, Status.APPROVED);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(
                        userId, dateTime);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusEqualsOrderByStartDesc(
                        userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusEqualsOrderByStartDesc(
                        userId, Status.REJECTED);
                break;
            default:
                throw new BookingException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDto add(Long userId, BookingRequestDto bookingRequestDto) {
        log.info("Создание бронирования {} пользователем с id {}.", bookingRequestDto, userId);

        bookingRequestDto.setBookerId(userId);
        bookingRequestDto.setStatus(Status.WAITING);
        Booking booking = bookingMapper.requestDtoToBooking(bookingRequestDto);

        if (!booking.getItem().getAvailable()) {
            throw new BookingException("Предмет недоступен для бронирования.");
        }
        if (userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Владелец не может бронировать собственную вещь.");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingException("Недопустимое время брони.");
        }
        if (booking.getEnd().isEqual(booking.getStart())) {
            throw new BookingException("Недопустимое время брони.");
        }

        return bookingMapper.bookingToBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Long userId, Long id, Boolean approved) {
        log.info("Обновление статуса бронирования {}.", id);

        Booking repoBooking = bookingMapper.responseDtoToBooking(getById(userId, id));
        Item item = repoBooking.getItem();

        if (!userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Изменение статуса бронирования доступно только владельцу.");
        }
        if (!repoBooking.getStatus().equals(Status.WAITING)) {
            throw new BookingException("Ответ по бронированию уже дан.");
        }

        repoBooking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingMapper.bookingToBookingResponseDto(bookingRepository.save(repoBooking));
    }

    private State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BookingException("Unknown state: " + state);
        }
    }
}
