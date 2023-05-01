package ru.practicum.shareit.booking.chainSearcher.booker;


import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.chainSearcher.Searcher;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

public class SearcherByBookerIdAndStateCurrent extends Searcher {
    @Override
    public Boolean shouldSearch(State state) {
        return state.equals(State.CURRENT);
    }

    @Override
    public List<Booking> findBooking(Long userId, Pageable pageable,
                                     LocalDateTime dateTime, BookingRepository bookingRepository) {
        return bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                userId, dateTime, dateTime, pageable).toList();
    }
}