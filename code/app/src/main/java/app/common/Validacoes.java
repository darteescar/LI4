package app.common;

public class Validacoes {

    private Validacoes() {}

    public static void naoVazio(String valor, String campo) {
        if (valor == null || valor.isBlank())
            throw new EcoRideException(campo + " não pode ser vazio.");
    }

    public static void naoNulo(Object valor, String campo) {
        if (valor == null)
            throw new EcoRideException(campo + " não pode ser nulo.");
    }

    // Formato: parte-local@domínio.tld — cobre a esmagadora maioria dos casos RFC 5322
    public static void emailValido(String email) {
        naoVazio(email, "Email");
        if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new EcoRideException("Email inválido (formato: utilizador@dominio.tld).");
    }

    // Código postal português: XXXX-XXX
    public static void codigoPostal(String cp) {
        naoVazio(cp, "Código postal");
        if (!cp.matches("\\d{4}-\\d{3}"))
            throw new EcoRideException("Código postal inválido. Formato esperado: XXXX-XXX.");
    }

    // Número de porta: 1-4 dígitos opcionalmente seguido de uma letra (ex: 12B)
    public static void numeroPorta(String porta) {
        naoVazio(porta, "Número de porta");
        if (!porta.matches("\\d{1,4}[A-Za-z]?"))
            throw new EcoRideException("Número de porta inválido. Máximo 4 dígitos, opcionalmente seguido de uma letra.");
    }

    // Telemóvel português: 9 dígitos
    public static void telemovel(String t) {
        naoVazio(t, "Telemóvel");
        if (!t.matches("\\d{9}"))
            throw new EcoRideException("Telemóvel inválido. Deve ter exatamente 9 dígitos.");
    }

    // Telefone genérico (mesmo formato que telemóvel — 9 dígitos)
    public static void telefone(String t) {
        naoVazio(t, "Telefone");
        if (!t.matches("\\d{9}"))
            throw new EcoRideException("Telefone inválido. Deve ter exatamente 9 dígitos.");
    }

    // NIF: 9 dígitos
    public static void nif(String nif) {
        naoVazio(nif, "NIF");
        if (!nif.matches("\\d{9}"))
            throw new EcoRideException("NIF inválido. Deve ter exatamente 9 dígitos.");
    }

    // NISS: 11 dígitos
    public static void niss(String niss) {
        naoVazio(niss, "NISS");
        if (!niss.matches("\\d{11}"))
            throw new EcoRideException("NISS inválido. Deve ter exatamente 11 dígitos.");
    }

    // NUS: 9 dígitos
    public static void nus(String nus) {
        naoVazio(nus, "NUS");
        if (!nus.matches("\\d{9}"))
            throw new EcoRideException("NUS inválido. Deve ter exatamente 9 dígitos.");
    }

    // IBAN: 2 letras maiúsculas + 23 dígitos
    public static void iban(String iban) {
        naoVazio(iban, "IBAN");
        if (!iban.matches("[A-Z]{2}\\d{23}"))
            throw new EcoRideException("IBAN inválido. Formato esperado: 2 letras maiúsculas seguidas de 23 dígitos.");
    }

    // Salário deve ser positivo (> 0)
    public static void salario(float valor, String campo) {
        if (valor <= 0)
            throw new EcoRideException(campo + " deve ser um valor positivo.");
    }

    // Valor monetário não negativo (>= 0), usado p.ex. em preços de compra/venda
    public static void valorMonetario(float valor, String campo) {
        if (valor < 0)
            throw new EcoRideException(campo + " não pode ser negativo.");
    }

    public static void inteiroPositivo(int valor, String campo) {
        if (valor <= 0)
            throw new EcoRideException(campo + " deve ser um número inteiro positivo (≥ 1).");
    }

    public static void inteiroNaoNegativo(int valor, String campo) {
        if (valor < 0)
            throw new EcoRideException(campo + " não pode ser negativo.");
    }
}
