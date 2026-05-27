#let project(body) = {
  // Set the document's basic properties.
  set page(paper: "a4", margin:(top: 2.5cm, bottom: 2.5cm, left: 3cm, right: 3cm))
  set text(font: "Calibri", lang: "pt", region: "PT", size: 10pt)

  // Main body.
  set par(justify: true)
  
  // Remove dots from outlines
  show outline.entry: set outline.entry(fill: none)

  set heading(numbering: "1.")
  
  show heading: it => {
    if it.level == 1 {
      pagebreak()
      block(below: 5em)
    }
    it
  }

  show heading: set block(above: 2.5em, below: 1em)

  show link: set text(fill: blue.darken(30%))
  show link: underline.with(evade: false)

  show figure.where(kind: "attachment"): it => {
    heading(outlined: false, numbering: none, level: 2)[#it.caption]
    it.body
  }

  show heading.where(level: 1): it => { text(size:18pt, it) }
  show heading.where(level: 2): it => { text(size:16pt, it) }
  
  body
}

#let attachment = figure.with(kind: "attachment", numbering: "1", supplement: [Anexo])

#let prompt_box(
  id: "",
  title: "",
  objetivo: "",
  prompt: "",
  avaliacao: "",
  modificacoes: ""
) = {
  
  block(
    width: 100%,
    stroke: 0.5pt + luma(200),
    radius: 3pt,
    fill: luma(254),
    clip: true,
    stack(
      // Cabeçalho
      block(
        fill: purple.darken(15%),
        width: 100%,
        inset: 10pt,
        text(white, weight: "bold", size: 1.1em)[Prompt #id: #title]
      ),
      
      pad(12pt)[
        #set align(left)
        #set par(justify: true, leading: 0.8em)
        
        // Objetivo
        #text(weight: "bold", size: 0.95em, purple.darken(15%))[Objetivo:] \
        #v(2pt)
        #objetivo
        #v(2pt)
        #line(length: 100%, stroke: (dash: "dashed", paint: luma(200), thickness: 0.5pt))
        #v(10pt)
        
        // Prompt
        #text(weight: "bold", size: 0.95em, purple.darken(15%))[Prompt:] \
        #v(2pt)
        #prompt
        #v(2pt)
        #line(length: 100%, stroke: (dash: "dashed", paint: luma(200), thickness: 0.5pt))
        #v(10pt)
        
        // Avaliação
        #if avaliacao != "" [
          #text(weight: "bold", size: 0.95em, purple.darken(15%))[Avaliação:] \
          #v(2pt)
          #avaliacao
          #v(2pt)
          #line(length: 100%, stroke: (dash: "dashed", paint: luma(200), thickness: 0.5pt))
          #v(10pt)
        ]
        
        // Modificações
        #if modificacoes != "" [
          #text(weight: "bold", size: 0.95em, purple.darken(15%))[Modificações:] \
          #v(2pt)
          #modificacoes
        ]
      ]
    )
  )
}

#let user_story(
  id: "", 
  title: "", 
  fonte: "", 
  requisitos: (), 
  criterios: (),
  texto
) = {
  
  block(
    width: 100%,
    stroke: 0.5pt + luma(200),
    radius: 3pt,
    fill: luma(254),
    clip: true,
    stack(
      // Cabeçalho
      block(
        fill: blue.darken(20%),
        width: 100%,
        inset: 10pt,
        text(white, weight: "bold", size: 1.1em)[US-#id: #title]
      ),
      
      // Metadados
      let reqs = if type(requisitos) == array { requisitos } else { (requisitos,) },

      pad(12pt)[
        #set align(left)
        
        // 1. CRITÉRIOS DE ACEITAÇÃO
        #if criterios.len() > 0 [
          #text(weight: "bold", size: 0.95em, blue.darken(20%))[Critérios de Aceitação:]
          #set text(size: 0.9em)
          #list(..criterios)
          #v(10pt)
          #line(length: 100%, stroke: (dash: "dashed", paint: luma(200), thickness: 0.5pt))
          #v(5pt)
        ]

        // 2. TEXTO / NARRATIVA
        #text(weight: "bold", size: 0.95em, luma(100))[Descrição Narrativa:] \
        #v(2pt)
        #set par(justify: true, leading: 0.8em)
        #texto
      ]
    )
  )
}

#let uc_spec(
  id: "",
  nome: "",
  atores: "",
  pre_condicoes: "",
  pos_condicoes: "",
  fluxo_normal: (),
  fluxos_alternativos: (), 
  fluxos_excecao: ()       
) = {
  block(
    width: 100%,
    stroke: 0.5pt + luma(150),
    radius: 4pt,
    inset: 12pt,
    // Forçamos o alinhamento à esquerda para anular o centramento da figure
    align(left)[
      #stack(
        spacing: 10pt,
        text(weight: "bold", size: 1.2em, blue.darken(40%))[UC-#id: #nome],
        line(length: 100%, stroke: 0.5pt + luma(200)),
        
        grid(
          columns: (1.2fr, 3fr),
          row-gutter: 8pt,
          [*Atores:*], [#atores],
          [*Pré-Condições:*], [#pre_condicoes],
          [*Pós-Condições:*], [#pos_condicoes],
        ),
        
        text(weight: "bold", fill: blue.darken(20%))[Fluxo Normal:],
        enum(..fluxo_normal),
        
        if fluxos_alternativos.len() > 0 [
          #text(weight: "bold", fill: orange.darken(20%))[Fluxos Alternativos:]
          #for fa in fluxos_alternativos [
            #block(inset: (left: 10pt), spacing: 6pt)[
              #v(8pt)
              *Condição:* #fa.condicao (Passo #fa.passo)
              
              #list(marker: none, ..fa.fluxo)
            ]
          ]
        ],
        
        if fluxos_excecao.len() > 0 [
          #text(weight: "bold", fill: red.darken(20%))[Fluxos de Exceção:]
          #for fe in fluxos_excecao [
            #block(inset: (left: 10pt), spacing: 6pt)[
              #v(8pt)
              *Condição:* #fe.condicao (Passo #fe.passo)
              
              #list(marker: none, ..fe.fluxo)
            ]
          ]
        ]
      )
    ]
  )
}

#let cor_primaria = rgb("#4A6FA5")
#let cor_fundo = rgb("#F7F9FC")
#let cor_linha = rgb("#E2E8F0")
#let cor_header_texto = white
#let cor_label = rgb("#64748B")

#let requisito(
  id: "",
  titulo: "",
  requisito_utilizador: "",
  fonte: "",
  area_sistema: "",
  requisitos_sistema: (),
  relevancia: ""
) = {
  block(
    width: 100%,
    radius: 10pt,
    clip: true,
    stroke: 1pt + cor_linha,
    {
      block(
        spacing: 4pt,          // ← esta é a chave
        width: 100%,
        fill: cor_primaria,
        inset: (x: 12pt, y: 6pt),
        {
          grid(
            columns: (auto, 1fr),
            column-gutter: 10pt,
            align: horizon,
            box(
              fill: white.transparentize(80%),
              radius: 4pt,
              inset: (x: 7pt, y: 3pt),
              text(weight: "bold", fill: white, size: 9pt, id)
            ),
            text(weight: "bold", fill: white, size: 11pt, titulo)
          )
        }
      )

      let campos = (
        ("Requisito do Utilizador", requisito_utilizador),
        ("Fonte", fonte),
        ("Área do Sistema", area_sistema),
      )

      for (i, campo) in campos.enumerate() {
        let (label, valor) = campo
        block(
          spacing: 6pt,          // ← esta é a chave
          width: 100%,
          fill: if calc.odd(i) { cor_fundo } else { white },
          inset: (x: 12pt, y: 4pt),
          grid(
            columns: (28%, 72%),
            column-gutter: 8pt,
            text(fill: cor_label, size: 9pt, weight: "semibold", label),
            text(size: 10pt, valor)
          )
        )
      }

      block(
        width: 100%,
        fill: cor_fundo,
        inset: (x: 12pt, y: 4pt),
        grid(
          columns: (28%, 72%),
          column-gutter: 8pt,
          text(fill: cor_label, size: 9pt, weight: "semibold", "Requisitos do Sistema"),
          list(
            marker: text(fill: cor_primaria, "▸"),
            ..requisitos_sistema.map(r => text(size: 10pt, r))
          )
        )
      )

      block(
        spacing: 9pt,          // ← esta é a chave
        width: 100%,
        fill: white,
        inset: (left: 12pt, right: 11pt, top: 0pt, bottom: 9pt),
        grid(
          columns: (28%, 72%),
          column-gutter: 8pt,
          text(fill: cor_label, size: 9pt, weight: "semibold", "Relevância"),
          box(
            fill: cor_primaria.lighten(88%),
            stroke: 0.5pt + cor_primaria.lighten(40%),
            radius: 4pt,
            inset: (x: 7pt, y: 3pt),
            text(fill: cor_primaria.darken(20%), size: 10pt, weight: "semibold", relevancia)
          )
        )
      )
    }
  )
}

#let prompt_final(
  id: "",
  title: "",
  prompt: ""
) = {
  block(
    width: 100%,
    stroke: 0.5pt + luma(200),
    radius: 3pt,
    fill: luma(254),
    clip: true,
    stack(
      // Cabeçalho
      block(
        fill: orange.darken(15%),
        width: 100%,
        inset: 10pt,
        text(white, weight: "bold", size: 1.1em)[Prompt #id: #title]
      ),
      
      // Apenas o texto do prompt
      pad(12pt)[
        #set align(left)
        #set par(justify: true, leading: 0.8em)
        #prompt
      ]
    )
  )
}