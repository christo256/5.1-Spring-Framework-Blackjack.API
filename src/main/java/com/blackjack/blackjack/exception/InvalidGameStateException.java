package com.blackjack.blackjack.exception;

public class InvalidGameStateException extends RuntimeException {
    public InvalidGameStateException (String message) {
        super(message);
    }
}
