package ru.practicum.shareit.booking.chainSearcher.owner;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.chainSearcher.Searcher;
import ru.practicum.shareit.booking.enums.State;

import java.time.LocalDateTime;
import java.util.List;

public class SearcherByOwnerIdAndStateAll extends Searcher {
    @Override
    public Boolean shouldSearch(State state) {
        return state.equals(State.ALL);
    }

    @Override
    public List<Booking> findBooking(Long userId, Pageable pageable,
                                     LocalDateTime dateTime, BookingRepository bookingRepository) {
        return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable).toList();
    }
}