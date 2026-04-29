package app.common;

public class Validacoes {

    public static void naoVazio(String valor, String campo) {
        if (valor == null || valor.isBlank())
            throw new EcoRideException(campo + " não pode ser vazio.");
    }

    public static void emailValido(String email) {
        naoVazio(email, "Email");
        if (!email.contains("@") || !email.contains("."))
            throw new EcoRideException("Email inválido.");
    }

    public static void codigoPostal(String cp) {
        naoVazio(cp, "Código postal");
        if (!cp.matches("\\d{4}-\\d{3}"))
            throw new EcoRideException("Código postal inválido. Formato: XXXX-XXX.");
    }

    public static void telemovel(String t) {
        naoVazio(t, "Telemóvel");
        if (!t.matches("\\d{9}"))
            throw new EcoRideException("Telemóvel inválido. Deve ter 9 dígitos.");
    }

    public static void nif(String nif) {
        naoVazio(nif, "NIF");
        if (!nif.matches("\\d{9}"))
            throw new EcoRideException("NIF inválido. Deve ter 9 dígitos.");
    }

    public static void niss(String niss) {
        naoVazio(niss, "NISS");
        if (!niss.matches("\\d{11}"))
            throw new EcoRideException("NISS inválido. Deve ter 11 dígitos.");
    }

    public static void iban(String iban) {
        naoVazio(iban, "IBAN");
        if (!iban.matches("[A-Z]{2}\\d{23}"))
            throw new EcoRideException("IBAN inválido.");
    }

    public static void valorMonetario(float valor, String campo) {
        if (valor < 0)
            throw new EcoRideException(campo + " não pode ser negativo.");
    }

    public static void inteiroPositivo(int valor, String campo) {
        if (valor <= 0)
            throw new EcoRideException(campo + " deve ser um inteiro positivo.");
    }

    public static void inteiroNaoNegativo(int valor, String campo) {
        if (valor < 0)
            throw new EcoRideException(campo + " não pode ser negativo.");
    }

    public static void naoNulo(Object valor, String campo) {
        if (valor == null)
            throw new EcoRideException(campo + " não pode ser nulo.");
    }
}
