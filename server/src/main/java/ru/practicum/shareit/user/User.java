package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USERS", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column(nullable = false)
    String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
