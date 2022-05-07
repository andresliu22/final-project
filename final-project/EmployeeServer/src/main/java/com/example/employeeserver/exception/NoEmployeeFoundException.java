package com.example.employeeserver.exception;

public class NoEmployeeFoundException extends RuntimeException{
    public NoEmployeeFoundException(String message) { super(message); }
}
