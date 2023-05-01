package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.chainSearcher.booker.ChainSearcherByBooker;
import ru.practicum.shareit.booking.chainSearcher.owner.ChainSearcherByOwner;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto getById(Long userId, Long id) {
        log.info("Вывод бронирования с id {}.", id);

        Booking booking = getBookingById(id);
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Просмотр бронирования доступно только автору или владельцу.");
        }

        return bookingMapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllByBookerId(Long userId, State state, Pageable pageable) {
        log.info("Вывод всех бронирований пользователя {} и статусом {}.", userId, state);

        userService.getUserById(userId);

        LocalDateTime dateTime = LocalDateTime.now();
        ChainSearcherByBooker chainSearcherByBooker = new ChainSearcherByBooker();

        return chainSearcherByBooker.search(userId, state, pageable, dateTime, bookingRepository).stream()
                .map(bookingMapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllByOwnerId(Long userId, State state, Pageable pageable) {
        log.info("Вывод всех вещей пользователя {} и статусом {}.", userId, state);

        userService.getUserById(userId);

        LocalDateTime dateTime = LocalDateTime.now();
        ChainSearcherByOwner chainSearcherByOwner = new ChainSearcherByOwner();

        return chainSearcherByOwner.search(userId, state, pageable, dateTime, bookingRepository).stream()
                .map(bookingMapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDto add(Long userId, BookingRequestDto bookingRequestDto) {
        log.info("Создание бронирования {} пользователем с id {}.", bookingRequestDto, userId);

        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart())) {
            throw new BookingException("Недопустимое время брони.");
        }
        if (bookingRequestDto.getEnd().isEqual(bookingRequestDto.getStart())) {
            throw new BookingException("Недопустимое время брони.");
        }

        Item item = itemService.getItemById(bookingRequestDto.getItemId());

        if (!item.getAvailable()) {
            throw new BookingException("Предмет недоступен для бронирования.");
        }
        if (!bookingRepository.findByItemIdAndStartBeforeAndEndAfterAndStatusEqualsOrderByStartAsc(item.getId(),
                bookingRequestDto.getStart(),
                bookingRequestDto.getEnd(), Status.APPROVED).isEmpty()
                || !bookingRepository.findByItemIdAndStartAfterAndEndBeforeAndStatusEqualsOrderByStartAsc(item.getId(),
                bookingRequestDto.getStart(),
                bookingRequestDto.getEnd(), Status.APPROVED).isEmpty()
                || bookingRepository.findByItemId(item.getId())
                .stream().anyMatch(booking -> booking.getStart().isBefore(bookingRequestDto.getStart()) &&
                        booking.getEnd().isBefore(bookingRequestDto.getEnd()) &&
                        booking.getEnd().isAfter(bookingRequestDto.getStart()))
                || bookingRepository.findByItemId(item.getId())
                .stream().anyMatch(booking -> booking.getStart().isAfter(bookingRequestDto.getStart()) &&
                        booking.getStart().isBefore(bookingRequestDto.getEnd()) &&
                        booking.getEnd().isAfter(bookingRequestDto.getEnd()))) {

            throw new BookingException("Предмет недоступен для бронирования. В это время его еще кто-то использует!");
        }

        User user = userService.getUserById(userId);

        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Владелец не может бронировать собственную вещь.");
        }
        Booking booking = bookingMapper.requestDtoToBooking(bookingRequestDto, item, user, Status.WAITING);
        return bookingMapper.bookingToBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Long userId, Long id, Boolean approved) {
        log.info("Обновление статуса бронирования {}.", id);

        Booking repoBooking = getBookingById(id);

        if (!userId.equals(repoBooking.getItem().getOwner().getId())) {
            throw new NotFoundException("Изменение статуса бронирования доступно только владельцу.");
        }
        if (!repoBooking.getStatus().equals(Status.WAITING)) {
            throw new BookingException("Ответ по бронированию уже дан.");
        }


        repoBooking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingMapper.bookingToBookingResponseDto(bookingRepository.save(repoBooking));
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с таким id не существует."));
    }

}
