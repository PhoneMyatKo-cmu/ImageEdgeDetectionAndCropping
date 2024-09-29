package se233.project.controller.customexceptions;

import java.util.List;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(List<String> invalidFiles) {
        super("Only jpg or png files are supported.\nThe zip file includes: " + invalidFiles.toString() + "\nThose files are not included");
    }
}
