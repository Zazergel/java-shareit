package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.markers.Constants;
import ru.practicum.shareit.markers.Create;
import ru.practicum.shareit.markers.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemExtendedDto> getByOwnerId(
            @RequestHeader(Constants.headerUserId) Long userId,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        return itemService.getByOwnerId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/{id}")
    public ItemExtendedDto getById(@RequestHeader(Constants.headerUserId) Long userId,
                                   @PathVariable Long id) {
        return itemService.getById(userId, id);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(Constants.headerUserId) Long userId,
                       @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(Constants.headerUserId) Long userId,
                          @PathVariable Long id,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return itemService.update(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestParam String text,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        return itemService.search(text, PageRequest.of(from / size, size));
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@RequestHeader(Constants.headerUserId) long userId,
                                 @PathVariable long id,
                                 @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemService.addComment(userId, id, commentRequestDto);
    }
}
