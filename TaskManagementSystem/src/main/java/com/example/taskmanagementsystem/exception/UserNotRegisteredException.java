package com.example.taskmanagementsystem.exception;

/**
 * Исключение, выбрасываемое при попытке зарегистрировать пользователя, но регистрация не завершена успешно.
 */
public class UserNotRegisteredException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение об ошибке, объясняющее причину исключения.
     */
    public UserNotRegisteredException(String message) {
        super(message);
    }
}
