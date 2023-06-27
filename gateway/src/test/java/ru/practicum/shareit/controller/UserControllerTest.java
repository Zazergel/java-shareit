package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    @MockBean
    private UserClient userClient;

    private final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("Test user 1")
            .email("tester1@yandex.ru")
            .build();
    private UserDto userDtoToUpdate;

    @BeforeEach
    public void beforeEach() {
        userDtoToUpdate = UserDto.builder()
                .name("Updated test user 1")
                .email("UpdatedTester1@yandex.ru")
                .build();
    }

    @Nested
    class Add {
        @Test
        public void shouldCreate() throws Exception {
            when(userClient.add(ArgumentMatchers.any(UserDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(userClient, times(1)).add(ArgumentMatchers.any(UserDto.class));
        }

        @Test
        public void shouldThrowExceptionIfEmailIsNull() throws Exception {
            userDto1.setEmail(null);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userClient, never()).add(ArgumentMatchers.any());
        }

        @Test
        public void shouldThrowExceptionIfEmailIsEmpty() throws Exception {
            userDto1.setEmail("");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userClient, never()).add(ArgumentMatchers.any());
        }

        @Test
        public void shouldThrowExceptionIfEmailIsBlank() throws Exception {
            userDto1.setEmail(" ");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userClient, never()).add(ArgumentMatchers.any());
        }

        @Test
        public void shouldThrowExceptionIfIsNotEmail() throws Exception {
            userDto1.setEmail("tester1yandex.ru");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(userDto1))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userClient, never()).add(ArgumentMatchers.any());
        }
    }

    @Nested
    class GetAll {
        @Test
        public void shouldGet() throws Exception {
            when(userClient.getAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mvc.perform(get("/users"))
                    .andExpect(status().isOk());

            verify(userClient, times(1)).getAll();
        }
    }

    @Nested
    class GetById {
        @Test
        public void shouldGet() throws Exception {
            when(userClient.getById(ArgumentMatchers.eq(userDto1.getId()))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mvc.perform(get("/users/{id}", userDto1.getId()))
                    .andExpect(status().isOk());

            verify(userClient, times(1)).getById(ArgumentMatchers.eq(userDto1.getId()));
        }
    }

    @Nested
    class Update {
        @Test
        public void shouldUpdate() throws Exception {
            when(userClient.update(ArgumentMatchers.eq(userDto1.getId()), ArgumentMatchers.any(UserDto.class)))
                    .thenReturn(new ResponseEntity<>(HttpStatus.OK));

            mvc.perform(patch("/users/{id}", userDto1.getId())
                            .content(mapper.writeValueAsString(userDtoToUpdate))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(userClient, times(1))
                    .update(ArgumentMatchers.eq(userDto1.getId()), ArgumentMatchers.any(UserDto.class));
        }

        @Test
        public void shouldThrowExceptionIfNotEmail() throws Exception {
            userDtoToUpdate.setEmail("UpdatedTester1yandex.ru");

            mvc.perform(patch("/users/{id}", userDto1.getId())
                            .content(mapper.writeValueAsString(userDtoToUpdate))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userClient, never()).update(ArgumentMatchers.any(), ArgumentMatchers.any());
        }
    }

    @Nested
    class Delete {
        @Test
        public void shouldDelete() throws Exception {
            mvc.perform(delete("/users/{id}", 99L))
                    .andExpect(status().isOk());

            verify(userClient, times(1)).delete(99L);
        }
    }
}