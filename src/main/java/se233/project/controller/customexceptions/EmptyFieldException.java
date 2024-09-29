package se233.project.controller.customexceptions;

import java.io.IOException;

public class EmptyFieldException extends RuntimeException {
    public EmptyFieldException(String fieldName) {
        super(fieldName + " is mandatory and cannot be empty.");
    }
}
