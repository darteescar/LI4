package app.common;

public class EcoRideException extends RuntimeException {
    public EcoRideException(String message) {
        super(message);
    }

    public EcoRideException(String message, Throwable cause) {
        super(message, cause);
    }
}
