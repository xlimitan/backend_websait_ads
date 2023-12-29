package ru.skypro.homework.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAdNotFoundException extends RuntimeException {

    private final int id;
    public String getMessage() {
        return "У пользователя с таким id: " + id + " - объявлений нет";
    }
}