
package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Item item, Long owner);

    Item get(Long itemId);

    Item update(Item item, Long owner, Long itemId);

    List<Item> getAll();

    List<Item> getByOwner(Long ownerId);

    List<Item> searchItems(String text);

}