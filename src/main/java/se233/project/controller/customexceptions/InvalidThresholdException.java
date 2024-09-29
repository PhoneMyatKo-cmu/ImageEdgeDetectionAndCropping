package se233.project.controller.customexceptions;

public class InvalidThresholdException extends RuntimeException {
    public InvalidThresholdException(int threshold) {
        super("Threshold Out of Bound: Threshold should be between 0 and 255.\nEntered Threshold: " + threshold);
    }

    public InvalidThresholdException(int weakThreshold, int strongThreshold) {
        super("Weak Threshold should be lower than Strong Threshold.\nEntered Weak Threshold: " + weakThreshold + "\nEntered Strong Threshold: " + strongThreshold);
    }
}
