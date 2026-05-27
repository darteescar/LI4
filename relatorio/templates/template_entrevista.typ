#let entrevista_template(
  id: "",
  data: "",
  hora_inicio: "",
  hora_fim: "",
  local: "",
  entrevistador: "",
  entrevistado: "",
  cargo_entrevistado: "",
  cor_primaria: rgb("#658AC0"),
  body
) = {
  // Configurações de Página e Texto
  set page(
    paper: "a4", 
    margin: (x: 2.5cm, y: 3cm),
    // Adição do Rodapé e Numeração
    footer: [
      #set text(size: 8pt, fill: gray)
      #line(length: 100%, stroke: 0.5pt + gray)
      #grid(
        columns: (1fr, 1fr),
        [Confidencial - Apenas para uso interno],
        align(right)[
          #context counter(page).display("1 de 1", both: true)
        ]
      )
    ]
  )
  
  set text(font: "Calibri", size: 10pt, lang: "pt")
  
  // Cabeçalho
  grid(
    columns: (1fr, 1fr),
    text(cor_primaria, weight: "bold")[Registo de Entrevista | Projeto Interno],
    align(right)[Relatório de Entrevista Individual]
  )
  line(length: 100%, stroke: 0.5pt + gray)
  v(1em)

  // Tabela de Identificação
  table(
    columns: (1fr, 1.5fr, 1fr, 1.5fr),
    fill: (x, y) => if calc.even(x) { luma(245) },
    stroke: 0.5pt + gray,
    [**ID Entrevista**], [#id], [**Data**], [#data],
    [**Hora Início**], [#hora_inicio], [**Hora Fim**], [#hora_fim],
    [**Local/Meio**], [#local], [**Estado**], [Finalizada]
  )
  v(1em)

  // Tabela de Intervenientes
  table(
    columns: (1fr, 2fr),
    fill: (x, y) => if y == 0 { cor_primaria } else { none },
    stroke: 0.5pt + gray,
    [*Intervenientes*], [*Cargo/Função*],
    [Entrevistador(a)], [#entrevistador],
    [Entrevistado(a)], [#entrevistado - #cargo_entrevistado]
  )
  
  body
}

// --- Uso do Template ---
#show: body => entrevista_template(
  id: "#001",
  data: "26 de Fevereiro de 2026",
  hora_inicio: "10:00",
  hora_fim: "11:15",
  local: "Sala de Reuniões B",
  entrevistador: "O Teu Nome",
  entrevistado: "Nome do Colaborador",
  cargo_entrevistado: "Diretor de Operações",
  body
)

== 1. Enquadramento e Objetivos
- Levantar os principais _bottlenecks_ no fluxo de aprovação de faturas.
- Validar a necessidade de integração entre o ERP e CRM.

== 2. Transcrição / Notas da Entrevista
*E:* Pode descrever brevemente a sua função e responsabilidades?

*R:* Atualmente giro a equipa de logística, focando-me na otimização da cadeia de abastecimento.

== 3. Análise e Observações Gerais
- *Tom de Voz*: O entrevistado demonstrou abertura, mas alguma frustração com os sistemas de IT atuais.
- *Pontos Críticos*: Falta de interoperabilidade entre sistemas.

== 3. Próximos Passos
#table(
  columns: (2fr, 1fr, 1fr),
  fill: (x, y) => if y == 0 { gray.lighten(50%) },
  [*Ação*], [*Responsável*], [*Prazo*],
  [Validar fluxo de dados com equipa de IT], [Teu Nome], [05/03/2026]
)