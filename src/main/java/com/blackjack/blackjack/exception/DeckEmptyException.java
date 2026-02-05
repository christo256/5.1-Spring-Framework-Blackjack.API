package com.blackjack.blackjack.exception;

public class DeckEmptyException extends RuntimeException {

    public DeckEmptyException() {
        super("The deck is empty");
    }
    public DeckEmptyException(String message) {
        super(message);
    }
}
