package com.ft.library.exception;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound(String message) {
        super(message);
    }
}
