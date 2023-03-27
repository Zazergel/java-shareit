package ru.practicum.shareit.exceptions.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exceptions.model.Violation;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<Violation> violations;
}