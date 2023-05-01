package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.UserController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public BookingResponseDto getById(@RequestHeader(UserController.headerUserId) Long userId,
                                      @PathVariable Long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBookerId(
            @RequestHeader(UserController.headerUserId) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_FROM, required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_SIZE, required = false) @Positive Integer size) {

        State stateEnum = State.stringToState(state).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + state));

        return bookingService.getAllByBookerId(userId, stateEnum, PageRequest.of(from / size, size));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByOwnerId(
            @RequestHeader(UserController.headerUserId) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_FROM, required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_SIZE, required = false) @Positive Integer size) {
        State stateEnum = State.stringToState(state).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + state));

        return bookingService.getAllByOwnerId(userId, stateEnum, PageRequest.of(from / size, size));
    }

    @PostMapping
    public BookingResponseDto add(@RequestHeader(UserController.headerUserId) Long userId,
                                     @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.add(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto update(@RequestHeader(UserController.headerUserId) Long userId,
                                    @PathVariable Long id,
                                    @RequestParam() Boolean approved) {
        return bookingService.update(userId, id, approved);
    }
}
