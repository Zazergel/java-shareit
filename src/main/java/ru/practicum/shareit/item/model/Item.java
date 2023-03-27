package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class Item {
    private Long id;
    @NotBlank(message = "Название предмета не должно быть пустым.")
    private String name;
    @NotBlank(message = "Описание предмета не должно быть пустым.")
    private String description;
    @NotNull(message = "При добавлении предмета необходимо указать статус его доступности.")
    private Boolean available;
    private Long owner;
    private Long request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id) || name.equals(item.name) && description.equals(item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
