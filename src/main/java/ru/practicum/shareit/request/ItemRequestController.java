package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader(UserController.headerUserId) Long userId,
                              @Valid @RequestBody ItemRequestAddDto itemRequestCreateDto) {
        return itemRequestService.add(userId, itemRequestCreateDto);
    }

    @GetMapping("/{id}")
    public ItemRequestExtendedDto getById(@RequestHeader(UserController.headerUserId) Long userId,
                                          @PathVariable Long id) {
        return itemRequestService.getById(userId, id);
    }

    @GetMapping
    public List<ItemRequestExtendedDto> getByRequesterId(@RequestHeader(UserController.headerUserId) Long userId) {
        return itemRequestService.getByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestExtendedDto> getAll(
            @RequestHeader(UserController.headerUserId) Long userId,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_FROM, required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = UserController.PAGE_DEFAULT_SIZE, required = false) @Positive Integer size) {
        return itemRequestService.getAll(userId, PageRequest.of(from / size, size));
    }
}
