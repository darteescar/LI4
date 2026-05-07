package app.IecoRideCA.controllers.funcionarios.dto;

import java.time.LocalDate;

public record FuncionarioRequest (
     String nome,
     String telemovel,
     String email,
     LocalDate data_nascimento,
     String NISS,
     String NIF,
     String NUS,
     String IBAN,
     float salario_hora,
     float salario_liquido,
     float salario_bruto,
     int horas_extra,
     String numero_porta,
     String rua,
     String localidade,
     String codigo_postal
) {}
