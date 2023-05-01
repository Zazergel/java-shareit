package ru.practicum.shareit.request.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto add(Long userId, ItemRequestAddDto itemRequestCreateDto) {
        log.info("Создание запроса вещи {} пользователем с id {}.", itemRequestCreateDto, userId);

        User user = userService.getUserById(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestCreateDto, user, LocalDateTime.now());

        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestExtendedDto getById(Long userId, Long id) {
        log.info("Вывод запроса вещи с id {} пользователем с id {}.", id, userId);

        userService.getUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запроса вещи с таким id не существует."));

        List<ItemDto> items = itemRequest.getItems().stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());

        return itemRequestMapper.toItemRequestExtendedDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestExtendedDto> getByRequesterId(Long userId) {
        log.info("Вывод всех запросов вещей пользователем с id {}.", userId);

        userService.getUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterId_IdOrderByCreatedAsc(userId);

        return itemRequests.stream()
                .map((itemRequest) -> itemRequestMapper.toItemRequestExtendedDto(
                        itemRequest,
                        itemRequest.getItems()
                                .stream()
                                .map(itemMapper::toItemDto)
                                .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestExtendedDto> getAll(Long userId, Pageable pageable) {
        log.info("Вывод всех запросов вещей постранично {}.", pageable);

        userService.getUserById(userId);
        Page<ItemRequest> itemRequests = itemRequestRepository.findByRequesterId_IdNot(userId, pageable);

        return itemRequests.stream()
                .map((itemRequest) -> itemRequestMapper.toItemRequestExtendedDto(
                        itemRequest,
                        itemRequest.getItems()
                                .stream()
                                .map(itemMapper::toItemDto)
                                .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());
    }
}
