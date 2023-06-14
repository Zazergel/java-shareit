package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, ItemRequestAddDto itemRequestCreateDto) {
        log.info("Создание запроса вещи {} пользователем с id {}.", itemRequestCreateDto, userId);
        return post("", userId, itemRequestCreateDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long id) {
        log.info("Вывод запроса вещи с id {} пользователем с id {}.", id, userId);
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getByRequesterId(Long userId) {
        log.info("Вывод всех запросов вещей пользователем с id {}.", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> getAll(Long userId, Integer from, Integer size) {
        log.info("Вывод всех запросов вещей постранично from={} size={}.", from, size);

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }
}