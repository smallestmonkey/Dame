package com.atos.exceptions;

public class InvalidMoveException extends CheckersException {
    public InvalidMoveException(int code) {
        super(102);
    }
}
