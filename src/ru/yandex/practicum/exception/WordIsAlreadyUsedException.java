package ru.yandex.practicum.exception;

public class WordIsAlreadyUsedException extends Exception {
    public WordIsAlreadyUsedException(String message) {
        super(message);
    }
}
