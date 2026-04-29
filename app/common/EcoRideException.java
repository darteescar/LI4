package app.common;

public class EcoRideException extends RuntimeException {

    public EcoRideException(String mensagem) {
        super(mensagem);
    }

    public EcoRideException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
