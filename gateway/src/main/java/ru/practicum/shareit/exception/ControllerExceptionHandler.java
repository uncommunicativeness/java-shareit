package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.exception.exceptions.ConflictException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;

import java.util.Map;

@Slf4j
@Generated
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleException(NotFoundException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleException(ConflictException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleException(BadRequestException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(Map.of("error", "Передан некорректный объект"), HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> handleException(FeignException e) {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.readTree(e.contentUTF8()).get("error").asText();
        log.warn(message);

        return new ResponseEntity<>(Map.of("error", message), HttpStatus.valueOf(e.status()));

    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Map<String, String>> handleException(Throwable e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
