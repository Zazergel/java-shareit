package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exceptions.AuthorisationException;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemExtendedDto> getByOwnerId(Long userId) {
        log.info("Вывод всех вещей пользователя с id {}.", userId);

        return itemRepository.getAllByOwnerIdOrderByIdAsc(userId).stream()
                .map(itemMapper::toItemExtendedDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemExtendedDto getById(Long userId, Long id) {
        log.info("Вывод вещи с id {}.", id);

        ItemExtendedDto itemExtendedDto = itemMapper.toItemExtendedDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не существует.")));

        if (!Objects.equals(userId, itemExtendedDto.getOwnerId())) {
            itemExtendedDto.setLastBooking(null);
            itemExtendedDto.setNextBooking(null);
        }

        return itemExtendedDto;
    }

    @Override
    @Transactional
    public ItemDto add(Long userId, ItemDto itemDto) {
        log.info("Создание вещи {} пользователем с id {}.", itemDto, userId);

        userService.getById(userId);

        itemDto.setOwnerId(userId);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto)));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long id, ItemDto itemDto) {
        log.info("Обновление вещи {} с id {} пользователем с id {}.", itemDto, id, userId);

        Item repoItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не существует."));
        if (!Objects.equals(userId, repoItem.getOwner().getId())) {
            throw new AuthorisationException("Изменение вещи доступно только владельцу.");
        }

        itemDto.setOwnerId(userId);
        itemDto.setId(id);
        Item item = itemMapper.toItem(itemDto);

        if (item.getName() != null) {
            repoItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            repoItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            repoItem.setAvailable(item.getAvailable());
        }

        return itemMapper.toItemDto(itemRepository.save(repoItem));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление вещи с id {}.", id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("Поиск вещей с подстрокой \"{}\".", text);

        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.search(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long id, CommentRequestDto commentRequestDto) {
        log.info("Добавление комментария пользователем с id {} вещи с id {}.", userId, id);

        commentRequestDto.setAuthorId(userId);
        commentRequestDto.setItemId(id);
        commentRequestDto.setCreated(LocalDateTime.now());
        Comment comment = itemMapper.commentRequestDtoToComment(commentRequestDto);

        if (bookingRepository.findByItemIdAndBookerIdAndEndIsBeforeAndStatusEquals(
                id, userId, LocalDateTime.now(), Status.APPROVED).isEmpty()) {
            throw new BookingException("Пользователь не брал данную вещь в аренду.");
        }

        return itemMapper.commentToCommentDto(commentRepository.save(comment));
    }
}
