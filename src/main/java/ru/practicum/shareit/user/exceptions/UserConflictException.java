package ru.practicum.shareit.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserConflictException extends RuntimeException {
    public UserConflictException(final String message) {
        super(message);
    }

    public UserConflictException() {
        super();
    }
}
