package app.IecoRideCA.controllers.funcionarios.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FuncionarioRequest (
     String nome,
     String telemovel,
     String email,
     LocalDate data_nascimento,
     @JsonProperty("NISS") String NISS,
     @JsonProperty("NIF") String NIF,
     @JsonProperty("NUS") String NUS,
     @JsonProperty("IBAN") String IBAN,
     float salario_hora,
     float salario_liquido,
     float salario_bruto,
     int horas_extra,
     String numero_porta,
     String rua,
     String localidade,
     String codigo_postal
) {}
