package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    @MockBean
    private UserService userService;

    private final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("Test user 1")
            .email("tester1@yandex.ru")
            .build();
    private final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .name("Test user 2")
            .email("tester2@yandex.ru")
            .build();
    private UserDto userDtoToUpdate;
    private UserDto userDtoUpdated;

    @BeforeEach
    public void beforeEach() {
        userDtoToUpdate = UserDto.builder()
                .name("Updated test user 1")
                .email("UpdatedTester1@yandex.ru")
                .build();

        userDtoUpdated = UserDto.builder()
                .id(1L)
                .name("Updated test user 1")
                .email("UpdatedTester1@yandex.ru")
                .build();
    }

    @Nested
    class Add {
        @Test
        public void shouldAdd() throws Exception {
            when(userService.add(ArgumentMatchers.any(UserDto.class))).thenReturn(userDto1);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userDto1)));

            verify(userService, times(1)).add(ArgumentMatchers.any(UserDto.class));
        }
    }

    @Nested
    class GetAll {
        @Test
        public void shouldGet() throws Exception {
            when(userService.getAll()).thenReturn(List.of(userDto1, userDto2));

            mvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(List.of(userDto1, userDto2))));

            verify(userService, times(1)).getAll();
        }

        @Test
        public void shouldGetIfEmpty() throws Exception {
            when(userService.getAll()).thenReturn(List.of());

            mvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(List.of())));

            verify(userService, times(1)).getAll();
        }
    }

    @Nested
    class GetById {
        @Test
        public void shouldGet() throws Exception {
            when(userService.getById(ArgumentMatchers.eq(userDto1.getId()))).thenReturn(userDto1);

            mvc.perform(get("/users/{id}", userDto1.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userDto1)));

            verify(userService, times(1)).getById(ArgumentMatchers.eq(userDto1.getId()));
        }
    }

    @Nested
    class Update {
        @Test
        public void shouldUpdate() throws Exception {
            when(userService.update(ArgumentMatchers.eq(userDtoUpdated.getId()), ArgumentMatchers.any(UserDto.class)))
                    .thenReturn(userDtoUpdated);

            mvc.perform(patch("/users/{id}", userDtoUpdated.getId())
                            .content(mapper.writeValueAsString(userDtoToUpdate))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userDtoUpdated)));

            verify(userService, times(1))
                    .update(ArgumentMatchers.eq(userDtoUpdated.getId()), ArgumentMatchers.any(UserDto.class));
        }
    }

    @Nested
    class Delete {
        @Test
        public void shouldDelete() throws Exception {
            mvc.perform(delete("/users/{id}", 99L))
                    .andExpect(status().isOk());

            verify(userService, times(1)).delete(99L);
        }
    }
}