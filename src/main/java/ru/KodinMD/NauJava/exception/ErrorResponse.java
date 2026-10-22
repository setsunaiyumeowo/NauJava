package ru.KodinMD.NauJava.exception;

public record ErrorResponse(String message) {
    public static ErrorResponse create(Throwable e){
        return new ErrorResponse(e.getMessage());
    }
    public static ErrorResponse create(String message){
        return new ErrorResponse(message);
    }
}
