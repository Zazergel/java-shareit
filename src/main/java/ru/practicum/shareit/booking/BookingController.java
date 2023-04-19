package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    public final BookingService bookingService;
    private final String headerUserId = "X-Sharer-User-Id";

    @GetMapping("/{id}")
    public BookingResponseDto getById(@RequestHeader(headerUserId) Long userId,
                                      @PathVariable Long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBookerId(@RequestHeader(headerUserId) Long userId,
                                                     @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByOwnerId(@RequestHeader(headerUserId) Long userId,
                                                    @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllByOwnerId(userId, state);
    }

    @PostMapping
    public BookingResponseDto add(@RequestHeader(headerUserId) Long userId,
                                  @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.add(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto update(@RequestHeader(headerUserId) Long userId,
                                     @PathVariable Long id,
                                     @RequestParam() Boolean approved) {
        return bookingService.update(userId, id, approved);
    }
}
