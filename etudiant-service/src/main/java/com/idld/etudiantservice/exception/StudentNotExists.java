package com.idld.etudiantservice.exception;

public class StudentNotExists extends RuntimeException{
    public StudentNotExists(String message) {
        super(message);
    }
}
