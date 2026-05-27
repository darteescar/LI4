package app.ecoRideLN;

import app.common.EcoRideException;
import app.common.Validacoes;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários da classe Validacoes — cobre todos os métodos de validação
 * com casos válidos e inválidos. Não depende de base de dados nem de rede.
 */
@DisplayName("Validacoes")
public class TesteValidacoes {

    // ── naoVazio ──────────────────────────────────────────────────────────────

    @Test @DisplayName("naoVazio: valor válido não lança exceção")
    void naoVazio_valido() {
        assertDoesNotThrow(() -> Validacoes.naoVazio("texto", "Campo"));
    }

    @Test @DisplayName("naoVazio: null lança EcoRideException")
    void naoVazio_null() {
        EcoRideException ex = assertThrows(EcoRideException.class,
                () -> Validacoes.naoVazio(null, "Campo"));
        assertTrue(ex.getMessage().contains("Campo"));
    }

    @Test @DisplayName("naoVazio: string vazia lança EcoRideException")
    void naoVazio_vazio() {
        assertThrows(EcoRideException.class, () -> Validacoes.naoVazio("   ", "Campo"));
    }

    // ── naoNulo ───────────────────────────────────────────────────────────────

    @Test @DisplayName("naoNulo: objeto não-nulo não lança exceção")
    void naoNulo_valido() {
        assertDoesNotThrow(() -> Validacoes.naoNulo(new Object(), "Obj"));
    }

    @Test @DisplayName("naoNulo: null lança EcoRideException")
    void naoNulo_null() {
        assertThrows(EcoRideException.class, () -> Validacoes.naoNulo(null, "Obj"));
    }

    // ── emailValido ───────────────────────────────────────────────────────────

    @Test @DisplayName("emailValido: formato correto aceite")
    void email_valido() {
        assertDoesNotThrow(() -> Validacoes.emailValido("utilizador@dominio.pt"));
    }

    @Test @DisplayName("emailValido: sem @ lança exceção")
    void email_semArroba() {
        assertThrows(EcoRideException.class, () -> Validacoes.emailValido("semArroba.pt"));
    }

    @Test @DisplayName("emailValido: sem TLD lança exceção")
    void email_semTLD() {
        assertThrows(EcoRideException.class, () -> Validacoes.emailValido("a@b"));
    }

    @Test @DisplayName("emailValido: null lança exceção")
    void email_null() {
        assertThrows(EcoRideException.class, () -> Validacoes.emailValido(null));
    }

    @Test @DisplayName("emailValido: espaços internos inválidos")
    void email_espacos() {
        assertThrows(EcoRideException.class, () -> Validacoes.emailValido("a b@dominio.pt"));
    }

    // ── codigoPostal ──────────────────────────────────────────────────────────

    @Test @DisplayName("codigoPostal: formato XXXX-XXX aceite")
    void cp_valido() {
        assertDoesNotThrow(() -> Validacoes.codigoPostal("4710-057"));
    }

    @Test @DisplayName("codigoPostal: sem hífen lança exceção")
    void cp_semHifen() {
        assertThrows(EcoRideException.class, () -> Validacoes.codigoPostal("4710057"));
    }

    @Test @DisplayName("codigoPostal: letras no código lançam exceção")
    void cp_comLetras() {
        assertThrows(EcoRideException.class, () -> Validacoes.codigoPostal("471A-057"));
    }

    // ── numeroPorta ───────────────────────────────────────────────────────────

    @Test @DisplayName("numeroPorta: dígitos simples aceite")
    void porta_simples() {
        assertDoesNotThrow(() -> Validacoes.numeroPorta("12"));
    }

    @Test @DisplayName("numeroPorta: dígitos + letra aceite")
    void porta_comLetra() {
        assertDoesNotThrow(() -> Validacoes.numeroPorta("12B"));
    }

    @Test @DisplayName("numeroPorta: mais de 4 dígitos lança exceção")
    void porta_muitosDigitos() {
        assertThrows(EcoRideException.class, () -> Validacoes.numeroPorta("12345"));
    }

    // ── telemovel ─────────────────────────────────────────────────────────────

    @Test @DisplayName("telemovel: 9 dígitos aceite")
    void telemovel_valido() {
        assertDoesNotThrow(() -> Validacoes.telemovel("912345678"));
    }

    @Test @DisplayName("telemovel: 8 dígitos lança exceção")
    void telemovel_curtoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.telemovel("91234567"));
    }

    @Test @DisplayName("telemovel: com letras lança exceção")
    void telemovel_comLetras() {
        assertThrows(EcoRideException.class, () -> Validacoes.telemovel("9123X5678"));
    }

    @Test @DisplayName("telemovel: 10 dígitos lança exceção")
    void telemovel_longoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.telemovel("9123456780"));
    }

    // ── nif ───────────────────────────────────────────────────────────────────

    @Test @DisplayName("nif: 9 dígitos aceite")
    void nif_valido() {
        assertDoesNotThrow(() -> Validacoes.nif("123456789"));
    }

    @Test @DisplayName("nif: 8 dígitos lança exceção")
    void nif_curtoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.nif("12345678"));
    }

    @Test @DisplayName("nif: com letras lança exceção")
    void nif_comLetras() {
        assertThrows(EcoRideException.class, () -> Validacoes.nif("12345678X"));
    }

    // ── niss ──────────────────────────────────────────────────────────────────

    @Test @DisplayName("niss: 11 dígitos aceite")
    void niss_valido() {
        assertDoesNotThrow(() -> Validacoes.niss("12345678901"));
    }

    @Test @DisplayName("niss: 10 dígitos lança exceção")
    void niss_curtoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.niss("1234567890"));
    }

    // ── nus ───────────────────────────────────────────────────────────────────

    @Test @DisplayName("nus: 9 dígitos aceite")
    void nus_valido() {
        assertDoesNotThrow(() -> Validacoes.nus("123456789"));
    }

    @Test @DisplayName("nus: 8 dígitos lança exceção")
    void nus_curtoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.nus("12345678"));
    }

    // ── iban ──────────────────────────────────────────────────────────────────

    @Test @DisplayName("iban: formato PT correto aceite")
    void iban_valido() {
        // 2 letras maiúsculas + 23 dígitos = 25 chars
        assertDoesNotThrow(() -> Validacoes.iban("PT50000201231234567890154"));
    }

    @Test @DisplayName("iban: letras minúsculas no prefixo lançam exceção")
    void iban_minusculas() {
        assertThrows(EcoRideException.class, () -> Validacoes.iban("pt50000201231234567890154"));
    }

    @Test @DisplayName("iban: dígitos a menos lança exceção")
    void iban_curtoDemais() {
        assertThrows(EcoRideException.class, () -> Validacoes.iban("PT5000020123123456789015"));
    }

    // ── salario ───────────────────────────────────────────────────────────────

    @Test @DisplayName("salario: valor positivo aceite")
    void salario_valido() {
        assertDoesNotThrow(() -> Validacoes.salario(10.0f, "Salário"));
    }

    @Test @DisplayName("salario: zero lança exceção")
    void salario_zero() {
        assertThrows(EcoRideException.class, () -> Validacoes.salario(0f, "Salário"));
    }

    @Test @DisplayName("salario: negativo lança exceção")
    void salario_negativo() {
        assertThrows(EcoRideException.class, () -> Validacoes.salario(-1f, "Salário"));
    }

    // ── valorMonetario ────────────────────────────────────────────────────────

    @Test @DisplayName("valorMonetario: zero aceite")
    void valorMonetario_zero() {
        assertDoesNotThrow(() -> Validacoes.valorMonetario(0f, "Preço"));
    }

    @Test @DisplayName("valorMonetario: negativo lança exceção")
    void valorMonetario_negativo() {
        assertThrows(EcoRideException.class, () -> Validacoes.valorMonetario(-0.01f, "Preço"));
    }

    // ── inteiroPositivo ───────────────────────────────────────────────────────

    @Test @DisplayName("inteiroPositivo: 1 aceite")
    void inteiroPositivo_valido() {
        assertDoesNotThrow(() -> Validacoes.inteiroPositivo(1, "Qty"));
    }

    @Test @DisplayName("inteiroPositivo: 0 lança exceção")
    void inteiroPositivo_zero() {
        assertThrows(EcoRideException.class, () -> Validacoes.inteiroPositivo(0, "Qty"));
    }

    // ── inteiroNaoNegativo ────────────────────────────────────────────────────

    @Test @DisplayName("inteiroNaoNegativo: 0 aceite")
    void inteiroNaoNegativo_zero() {
        assertDoesNotThrow(() -> Validacoes.inteiroNaoNegativo(0, "Qty"));
    }

    @Test @DisplayName("inteiroNaoNegativo: -1 lança exceção")
    void inteiroNaoNegativo_negativo() {
        assertThrows(EcoRideException.class, () -> Validacoes.inteiroNaoNegativo(-1, "Qty"));
    }
}
