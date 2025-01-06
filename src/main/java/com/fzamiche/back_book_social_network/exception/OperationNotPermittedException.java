package com.fzamiche.back_book_social_network.exception;

public class OperationNotPermittedException extends RuntimeException{


    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
