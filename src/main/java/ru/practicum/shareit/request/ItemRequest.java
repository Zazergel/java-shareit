package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ItemRequest {
    private Long id;
    private String description;
    private Long creatorId;
    private LocalDateTime created;
}