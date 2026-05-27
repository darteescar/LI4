#import "cover.typ": cover
#import "template.typ": *
#import "@preview/rexllent:0.4.0": xlsx-parser
#import "@preview/muchpdf:0.1.0": muchpdf

#let promptbox(title, body) = block(
  stroke: 1pt + black,
  radius: 4pt,
  inset: 12pt,
  fill: white,
  [
    #text(weight: "bold")[#title]
    #v(6pt)
    #body
  ]
)
#let promptcode(text) = block(
  stroke: 1pt + black,
  radius: 4pt,
  inset: 10pt,
  fill: luma(245),
  [
    #text(font: "Courier New", size: 9pt)[#text]
  ]
)

#show: project

#cover(title: "EcoRide Solutions", authors: (
  (name: "Duarte Escairo Brandão Reis Silva", number: "a106936"), 
  (name: "Eduardo Freitas Fernandes", number: "a106919"), 
  (name: "Inês Ferreira Ribeiro", number: "a104704"),
  (name: "Luís António Peixoto Soares", number: "a106932"),
  (name: "Tiago Silva Figueiredo", number: "a106856")), 
  "27 Maio 2026")

#set page(numbering: "i", number-align: center)
#counter(page).update(1)

#heading(numbering: none, outlined: false)[Resumo]
O presente relatório documenta o trabalho desenvolvido no âmbito da unidade curricular de Laboratórios de Informática IV, tendo como caso de estudo a empresa EcoRide Solutions — uma oficina sediada em Braga especializada na reparação e manutenção de trotinetes elétricas. A crescente complexidade operacional da empresa, aliada à dependência de processos manuais e registos dispersos, evidenciou a necessidade de uma solução informática integrada capaz de centralizar e automatizar a gestão das suas atividades.

O trabalho foi estruturado em quatro grandes fases. Na primeira, realizou-se a conceção e engenharia de requisitos com recurso a _LLM_, abrangendo a definição do domínio do sistema, a identificação de _stakeholders_, a eliciação de requisitos através de entrevistas, análise do processo operacional e geração de _user stories_, seguida do refinamento e especificação detalhada dos requisitos funcionais e não funcionais, e ainda a produção de use cases e diagramas _UML_. Na segunda fase, procedeu-se ao design do _software_, com a definição da arquitetura em três camadas, a elaboração de diagramas de componentes e de classes organizados por subsistemas, o desenho do modelo de dados relacional com aplicação do padrão _DAO_, e a prototipagem da interface. Na terceira fase, concretizou-se a implementação assistida por _LLM_, recorrendo a _Java_ no _backend_ com exposição de uma _API REST_ via _Javalin_, _React_ com _TypeScript_ no _frontend_, _MySQL_ como sistema de gestão de base de dados, e _Docker_ para orquestração dos serviços. Na quarta fase, procedeu-se à verificação e validação da qualidade do software produzido, com recurso a testes unitários e de integração automatizados implementados com a _framework JUnit 5_, cobrindo os principais fluxos funcionais e cenários de exceção dos oito subsistemas.

O sistema resultante cobre integralmente os oito subsistemas identificados — autenticação, clientes, funcionários, ordens de serviço, reparações, stock, financeiro e notificações — com controlo de acesso baseado em cargos. Todas as funcionalidades previstas nos requisitos foram implementadas e validadas, encontrando-se o sistema num estado funcional e pronto para utilização em ambiente real. 


*Área de Aplicação*: Engenharia de Requisitos e Engenharia de _Software_ Apoiada por Assistentes Artificiais.


*Palavras-Chave*: Engenharia de Requisitos, _LLM_, _UML_, Arquitetura em Três Camadas, _Java_, _React_, _TypeScript_, _MySQL_, Padrão _DAO_, _Docker_, _API REST_ , Controlo de Acesso Baseado em Cargos (_RBAC_), Micromobilidade.

#show outline: it => {
    show heading: set text(size: 18pt)
    it
}

#{
  show outline.entry.where(level: 1): it => {
    v(5pt)
    strong(it)
  }

  outline(
    title: [Índice], 
    indent: auto, 
  )
}

#v(-0.4em)
#outline(
  title: none,
  target: figure.where(kind: "attachment"),
  indent: n => 1em,
)

#outline(
  title: [Lista de Figuras],
  target: figure.where(kind: image),
)

#outline(
  title: [Lista de Tabelas],
  target: figure.where(kind: table),
)

// Make the page counter reset to 1
#set page(numbering: "1", number-align: center)
#counter(page).update(1)

/*
= Sugestões para Escrita do Relatório

== Sugestões Gerais

<\<O presente documento deverá servir de base para a escrita do relatório do trabalho realizado.>>

<\<O tipo de letra a utilizar deverá ser Arial.. Porém recomenda-se em situações de escrita de excertos de programas a utilização do tipo de letra Courier New.>>
 
<\< Alguns estilos documento: Heading1, Heading2, Heading3, Normal e Footnote Text; foram especialmente modificados para os relatórios da presente disciplina.>>
 
<\<Os formatos e estilos de letra não devem estar constantemente a ser modificados ao longo do relatório. Tal situação dará origem a um relatório com um formato e apresentação muito heterogénea e com um aspecto pouco consistente.>>

== Termos Estrangeiros
<\<Os termos estrangeiros utilizados deverão ser apresentados num formato diferente do resto do texto, por exemplo: Data Warehouse (em itálico) ou "Data Warehouses" (entre aspas), devendo ser evitados sempre que se conheça uma tradução correcta para português. Para validação desses termos existem vários dicionários no mercado que poderão ser úteis.>>

== Tabelas e Figuras
<\<Caso seja necessário introduzir figuras ou tabelas no corpo do documento, estas devem seguir os formatos que se apresentam de seguida. Qualquer figura ou tabela deverá ter uma legenda associada, devendo esta estar correctamente apresentada no índice respectivo no início do relatório.>>

#figure(
  caption: "Ilustração de inserção de uma figura e legenda.",
  kind: image,
  image("images/example.png", width: 70%)
)

\

#figure(
  caption: "Ilustração de inserção de uma tabela e sua legenda.",
  kind: table,
  table(
    columns: 5 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
    [*Column 1*], [*Column 2*], [*Column 3*], [*Column 4*], [*Column 5*],
    [Column 1], [Column 2], [Column 3], [Column 4], [Column 5],
    [Column 1], [Column 2], [Column 3], [Column 4], [Column 5],
    [Column 1], [Column 2], [Column 3], [Column 4], [Column 5],
  )
)
*/


= Conceção e Engenharia de Requisitos Assistida por LLM

== Definição do Domínio do Sistema

=== Contextualização

A *EcoRide Solutions* nasceu em 2018 da visão de João Martins, técnico de eletrónica com mais de 15 anos de experiência na reparação de equipamentos industriais. O seu interesse pela mobilidade elétrica surgiu quando decidiu reparar a sua própria trotinete elétrica, momento que lhe permitiu perceber a existência de uma lacuna no mercado local no que diz respeito a serviços técnicos especializados nesta área. Motivado por essa oportunidade e pelo crescimento da micromobilidade, João decidiu abandonar a sua carreira anterior para fundar uma oficina dedicada à reparação e manutenção de trotinetes elétricas, apostando na crescente procura por soluções de transporte sustentáveis e na escassez de profissionais qualificados neste setor.

Atualmente, a empresa opera a partir de um espaço próprio em Braga e conta com uma equipa de quatro colaboradores. Para além do fundador e gerente que trata da logística e controlo de peças, a equipa é composta por dois mecânicos especializados em eletrónica e uma assistente administrativa que assegura o atendimento ao cliente, a faturação e a gestão documental. A estrutura da organização é pequena, mas funcional, permitindo uma comunicação direta entre os diferentes intervenientes e uma resposta ágil às necessidades dos clientes.

#figure(
  caption: "Logótipo da EcoRide Solutions",
  kind: image,
  image("images/logotipo.jpeg", width: 50%)
) <logotipo>

=== Motivação

A necessidade de implementar um sistema informático surge da crescente complexidade das operações da oficina. Os processos atuais, baseados em folhas de cálculo, documentos dispersos e registos manuais, dificultam a rastreabilidade das intervenções e comprometem a fiabilidade da informação. A inexistência de um mecanismo centralizado para gerir clientes, equipamentos, ordens de serviço e peças, conduz a erros frequentes, como discrepâncias no _stock_, perda de histórico técnico e dificuldades na elaboração de relatórios financeiros.

Além disso, a expansão do mercado de micromobilidade exige que a empresa adote ferramentas que permitam maior eficiência operacional e capacidade de resposta. A automatização dos processos administrativos e técnicos permitirá reduzir tempos de execução, melhorar a comunicação entre os mecânicos e a administração e aumentar a transparência perante o cliente. A implementação de um sistema de gestão integrado constitui, assim, um passo estratégico para a sustentabilidade e crescimento da empresa.


=== Objetivos

O *objetivo geral* é desenvolver um sistema de gestão integrado que suporte e otimize os processos operacionais, técnicos e administrativos de uma oficina de reparação de trotinetes.

Os *objetivos específicos* são:
- Garantir o registo estruturado de clientes, equipamentos e histórico de intervenções;
- Controlar o ciclo completo das ordens de serviço, desde o diagnóstico até à entrega;
- Registar peças utilizadas e custos associados;
- Automatizar a gestão de _stock_, incluindo entradas, saídas e alertas de reposição;
- Disponibilizar relatórios financeiros para apoio à decisão;
- Melhorar a comunicação interna e reduzir erros operacionais.

=== Viabilidade

A implementação do sistema apresenta uma viabilidade económica favorável, considerando os benefícios operacionais e administrativos que proporciona. O custo estimado para o desenvolvimento e implementação do sistema situa-se nos *12 000 €*, incluindo análise de requisitos, desenvolvimento, testes, formação e instalação.

Com base na análise dos processos atuais da EcoRide Solutions, estima-se que a automatização dos fluxos de trabalho poderá reduzir em cerca de *25% a 30%* o tempo despendido em tarefas administrativas e operacionais. Esta melhoria traduz-se numa maior eficiência na gestão de ordens de serviço, controlo de _stock_ e emissão de faturas. Adicionalmente, a redução de erros humanos e a melhoria na rastreabilidade das intervenções técnicas poderão contribuir para um aumento de *5% a 8%* na satisfação dos clientes e na retenção dos mesmos.

Assim, o retorno do investimento poderá ser alcançado entre *12 a 18 meses*, logo, o desenvolvimento do sistema é viável.


=== Recursos

A implementação do sistema de gestão da EcoRide Solutions envolveu um conjunto de recursos humanos e materiais que sustentaram todas as fases do projeto, desde a análise de requisitos até à sua conclusão.

Os recursos humanos englobaram todos os funcionários da empresa e a equipa de desenvolvimento responsável pela conceção, implementação, testes e manutenção da solução.

No que diz respeito aos recursos materiais, foram utilizados componentes de _hardware_ e _software_ adequados às exigências do projeto. O _hardware_ incluiu um servidor de base de dados para alojamento centralizado da informação e os computadores utilizados pela equipa de desenvolvimento. Em termos de _software_, foram empregues o sistema de gestão de base de dados _MySQL_ e o _Visual Paradigm_ para a modelação de processos e estruturas com base na linguagem _UML_. Adicionalmente, foram utilizadas diversas ferramentas de desenvolvimento, coordenação e teste de código, tendo a implementação da camada de dados e a lógica de negócio sido feitas na linguagem de programação _Java_ e a camada de apresentação com a _framework React_ .

=== Equipa de Trabalho

A equipa de trabalho envolvida no desenvolvimento do sistema de gestão da EcoRide Solutions foi estruturada em duas categorias principais: pessoal interno e pessoal externo.

O pessoal interno correspondeu a todos os funcionários da empresa, que participaram como utilizadores finais do sistema e contribuíram com conhecimento prático sobre os processos operacionais e administrativos da oficina. Esta colaboração permitiu identificar os requisitos funcionais e validar as funcionalidades implementadas. Cada colaborador desempenhou um papel específico: o gerente supervisionou e aprovou as decisões estratégicas e detalhou o funcionamento do dia a dia da empresa, bem como os fluxos de gestão de _stock_, os mecânicos forneceram informação sobre os procedimentos técnicos e a assistente administrativa descreveu os processos de atendimento, faturação e gestão documental.

O pessoal externo, composto pela equipa de desenvolvimento:

- Duarte Escairo
- Eduardo Fernandes
- Inês Ferreira
- Luís Soares
- Tiago Figueiredo

foi responsável por todo o processo de engenharia de _software_, incluindo o levantamento e análise de requisitos, modelação conceptual e lógica da base de dados, modelação de processos usando a linguagem _UML_, implementação da aplicação em _Java_ e _React_, testes e validação da solução. 

=== Plano de Execução
No início do projeto, foi estabelecido em conjunto com a EcoRide Solutions um plano de execução formal, definindo as principais fases de desenvolvimento, os respetivos marcos temporais e as responsabilidades associadas a cada etapa. Este plano surgiu da necessidade de alinhar as expectativas da empresa cliente com a capacidade da equipa de desenvolvimento, garantindo entregas parciais validadas ao longo do processo e um produto final dentro do prazo acordado.

O plano contempla as fases de levantamento e análise de requisitos, modelação do sistema, implementação, testes e validação, e entrega final, com datas de início e conclusão definidas para cada uma. A calendarização foi pensada de forma a permitir ciclos de revisão com os _stakeholders_ entre fases, assegurando que eventuais desvios face às expectativas fossem detetados e corrigidos atempadamente.

O diagrama de _Gantt_ apresentado de seguida ilustra a distribuição temporal das diversas fases do projeto, permitindo uma leitura clara da sequência de atividades, das dependências entre elas e dos períodos de sobreposição previstos.

#figure(
  image("images/gant.png"),
  caption: [Diagrama de _Gant_ do plano de execução]
)


== Identificação de _Stakeholders_

No contexto do desenvolvimento deste projeto, os _stakeholders_ desempenharam um papel essencial na definição dos objetivos, na validação dos requisitos e na garantia de que o sistema final correspondeu às necessidades reais da organização. Tendo isto em conta, puderam ser divididos em três categorias principais: os internos, os externos e os técnicos.

=== _Stakeholders_ Internos

Os _stakeholders_ internos são os elementos da organização que participam diretamente no desenvolvimento e utilização do sistema. No contexto deste projeto, corresponderam aos funcionários da empresa, cuja colaboração foi essencial para a definição dos objetivos, validação dos requisitos e alinhamento da solução com os processos reais da organização. São também os principais utilizadores do sistema final. 

Os papéis organizacionais definidos no contexto da empresa são os seguintes:
- *Gerente*: responsável pela direção estratégica da EcoRide Solutions e gestão do _stock_ de peças, consumíveis e materiais;
- *Assistente Administrativa*: responsável pelo atendimento ao cliente, criação de ordens de serviço, faturação e arquivo documental;
- *Mecânico*: executa diagnósticos, intervenções e reparações nas trotinetes.

A @stakeholders_internos relaciona estes papéis com os colaboradores que os desempenham atualmente.

#figure(
  caption: "Identificação dos stakeholders internos e respetivos papéis.",
  kind: table,
  table(
    columns: 2 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
    [*Nome*], [*Papel*],
    [João Martins], [Gerente],
    [Silvina Matagal], [Assistente Administrativa],
    [Ramiro Ramalho], [Mecânico],
    [Edgar Novo], [Mecânico],
  )
)<stakeholders_internos>

=== _Stakeholders Externos_

Os _stakeholders_ externos são todos os intervenientes que, não pertencendo à estrutura interna da organização, influenciaram ou foram impactados pelo desenvolvimento do sistema. No contexto deste projeto, incluíram os fornecedores. A sua importância residiu no fornecimento de _feedback_ e na avaliação da qualidade dos serviços, pois, com o fornecimento desta informação, foi possível assegurar que a solução final fosse funcional e adequada às necessidades do negócio.

O papel externo relevante no contexto do projeto é o seguinte:
- *Fornecedor*: fornecem peças e materiais à empresa.

A @stakeholders_externos estabelece a correspondência entre este papel e os nomes das entidades concretas que os desempenham:

#figure(
  caption: "Identificação dos nomes e respetivas responsabilidades dos stakeholders externos.",
  kind: table,
  table(
    columns: 2 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
    [*Nome*], [*Papel*],
    [Correia & Cardoso], [Fornecedor],
    [MotoFreitas], [Fornecedor],
    [Barcelpeças], [Fornecedor],
    [Centrauto], [Fornecedor],
  )
)<stakeholders_externos>

=== _Stakeholders_ Técnicos

_Stakeholders_ técnicos são os intervenientes diretamente envolvidos na conceção, desenvolvimento e validação técnica de um sistema de software. No contexto deste projeto, os _stakeholders_ técnicos incluíram: o coordenador (responsável pela articulação entre os objetivos e prazos do projeto), os programadores (encarregues da implementação do sistema) e os analistas (responsáveis pela recolha e formalização dos requisitos). A sua participação é e foi essencial para garantir que o sistema cumprisse os requisitos funcionais e não funcionais identificados, assegurando a coerência técnica e a qualidade do produto final.

Os papéis técnicos definidos no âmbito do desenvolvimento do sistema são os seguintes:
- *Coordenador*: Encarregado da gestão da equipa de desenvolvimento e dos recursos disponíveis para a criação do sistema.
- *Programador*: Responsável pela implementação e manutenção do software.
- *Analista*: Conduz o levantamento, análise e refinamento de requisitos.

A @stakeholders_tecnicos relaciona estes papéis com os elementos da equipa que os desempenharam.

#figure(
  caption: "Identificação dos stakeholders técnicos e respetivas responsabilidades.",
  kind: table,
  table(
    columns: 2 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
    [*Nome*], [*Papel*],
    [Duarte Escairo], [Programador],
    [Eduardo Fernandes], [Analista],
    [Inês Ferreira], [Coordenadora],
    [Luís Soares], [Analista],
    [Tiago Figueiredo], [Programador],
  )
)<stakeholders_tecnicos>


== Eliciação de Requisitos

Tendo sido previamente definido o domínio do sistema e identificados os respetivos _stakeholders_, procedeu-se à fase de levantamento de requisitos. Esta etapa é de enorme relevância, uma vez que decisões incorretas ou incompletas nesta fase tendem a refletir-se num possível trabalho corretivo  em etapas posteriores do desenvolvimento, implicando custos acrescidos e maior esforço de reformulação. Assim, a equipa optou pela utilização de múltiplos métodos de elicitação, com o objetivo de assegurar uma identificação rigorosa e estruturada dos requisitos, garantindo que todos os aspetos relevantes fossem considerados, que não subsistissem ambiguidades ou contradições entre requisitos e que estes representassem de forma fiel tanto as necessidades do cliente como os objetivos assumidos pela equipa de desenvolvimento.

=== Entrevistas
As entrevistas tiveram como principal objetivo recolher requisitos funcionais diretamente junto dos futuros utilizadores do sistema, permitindo compreender os seus processos de trabalho, necessidades operacionais e principais dificuldades sentidas no desempenho das suas funções.

Para a sua execução, a equipa de desenvolvimento destacou os seus dois analistas, Eduardo Fernandes e Luís Soares, que dirigiram o levantamento das questões e anotaram as respostas das mesmas. Posteriormente, realizaram a transcrição das mesmas nos relatórios desenvolvidos e, depois de analisarem-nos, obtiveram um conjunto de requisitos que foram também transcritos para os relatórios.

De maneira a obter os requisitos mais claros e consistentes possíveis, foi decidido que seria necessário falar com funcionários que pertencessem aos vários setores de operação da empresa, assim, os entrevistados foram: o gerente, João Martins, a assistente administrativa, Silvina Matagal, e um dos mecânicos, Ramiro Ramalho.

#figure(
  image("pdf/entrevista_gerente.pdf", width: 85%),
  caption: [Primeira página da entrevista \#001, feita ao gerente]
  
)

#figure(
  table(
    columns: (0.5fr, 1.5fr, 0.5fr, 1fr, 0.7fr),
    inset: 7pt,
    fill: (x, y) => if y == 0 or y == 1 { luma(245) },
    align: (x, y) => {
      if y == 0 { return center }
      if x in (0, 2, 3, 4) { return center }
      left + horizon
    },
  
    table.cell(colspan: 5, fill: luma(230))[
    #set text(13pt, weight: "bold")
    Requisitos da entrevista 01
    ],

    [*Nr*], [*Descrição*], [*Data*], [*Fonte*], [*Analista*],
  
    [RF01], [O sistema deve permitir registar clientes com nome, email, telemóvel, NIF e múltiplas trotinetes associadas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
    [RF02], [O sistema deve permitir registar marca, modelo, número de série, estado à entrada (com notas textuais), tipo de motor e acessórios de uma trotinete.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
    [RF03], [O sistema deve permitir registar dados pessoais dos funcionários: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS e IBAN.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
    [RF04], [O sistema deve permitir registar salário base (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto), alterações salariais, horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  ),
  caption: [Alguns dos requisitos levantados durante a entrevista \#001]
)

=== Análise do Processo Operacional

Com o intuito de complementar a informação recolhida nas entrevistas e mitigar possíveis falhas ou omissões decorrentes de relatos puramente verbais, o analista Eduardo Fernandes realizou o acompanhamento presencial de um dia de trabalho típico na oficina da EcoRide Solutions. Esta imersão no terreno permitiu observar as operações em contexto real, identificar práticas informais e detetar necessidades que não estavam documentadas, garantindo que o sistema suporte efetivamente o fluxo operacional da organização. A observação incidiu sobre a interação entre os diversos intervenientes - João Martins (Gerente), Silvina Matagal (Secretária Administrativa) e Ramiro Ramalho (Mecânico) - e resultou na identificação de pontos críticos em diferentes áreas de atuação.

#figure(
  image("pdf/apo.pdf", width: 85%),
  caption: [Primeira página da Análise do Processo Operacional]
)

#figure(
table(
  columns: (0.5fr, 1.5fr, 0.5fr, 1fr, 0.6fr),
  inset: 7pt,
  fill: (x, y) => if y == 0 or y == 1{ luma(245) },
  align: (x, y) => {
    if y == 0 { return center }
    if x in (0, 2, 3, 4) { return center }
    left + horizon
  },

    table.cell(colspan: 5, fill: luma(230))[
    #set text(13pt, weight: "bold")
    Requisitos da investigação do processo operacional
    ],
  
  [*Nr*], [*Descrição*], [*Data*], [*Fonte*], [*Analista*],

  [RF46], [O sistema deverá ser possível nomear os itens que foram deixados na hora de entrega da trotinete, como por exemplo, carregador, cadeado e chave.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF47], [O sistema deverá ter um "Quadro de Estado em Tempo Real" acessível na receção. O mecânico deverá identificar a ordem de serviço associada e atualizar o seu estado para que a assistente tenha sempre a resposta imediatamente.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
),
caption: [Os requisitos levantados durante a investigação do processo operacional]
)

=== Geração de _User Stories_

No âmbito da fase de especificação de requisitos para o sistema a desenvolver para a EcoRide Solutions, o recurso a _User Stories_ constitui uma ferramenta fundamental para capturar as necessidades funcionais a partir da perspetiva dos diversos intervenientes. Este processo de engenharia de requisitos permite decompor as funcionalidades complexas em unidades de valor iterativas e compreensíveis, facilitando a comunicação entre a equipa de desenvolvimento e os _stakeholders_. Cada história é formulada para descrever quem necessita da funcionalidade, o que deve ser executado e qual o benefício esperado, sendo complementada por critérios de aceitação que balizam as condições de sucesso para a validação do sistema. A metodologia adotada segue a estrutura narrativa clássica, com a identificação da fonte e os requisitos que cobre. Como exemplo ilustrativo deste processo, considera-se a funcionalidade do registo de uma ordem de serviço, por parte da secretária administrativa, presente na @user_story_01.

#figure(
user_story(
    id: "01",
    title: "Criar Ordem de Serviço",
    fonte: [],
    requisitos: ("RF01", "RF02", "RF03", "RF06"),
    criterios: (
      [A ordem de serviço deve conter dados do cliente, da trotinete e a descrição do problema.],
      [O sistema deve associar automaticamente o estado inicial 'Pendente diagnóstico' à nova ordem de serviço.],
      [A ordem de serviço deve ficar imediatamente disponível para consulta pelos mecânicos.],
    ),
    [
A secretária inicia sessão e seleciona a opção de criar uma nova ordem de serviço. O sistema apresenta um formulário para registar os dados necessários.

Se o cliente já existir no sistema, a secretária procura o cliente pelo nome, NIF ou número de cliente. Após selecionar o cliente correto, os dados são preenchidos automaticamente na ordem de serviço.

Se o cliente não existir, a secretária regista um novo cliente inserindo o nome, contacto e outros dados necessários. O sistema guarda o registo e associa-o à nova ordem de serviço.

Depois de selecionar ou criar o cliente, a secretária regista os dados da trotinete. Introduz a marca, o modelo e o número de série, quando disponível. 

De seguida, descreve o problema relatado pelo cliente. O sistema permite adicionar notas textuais sobre o comportamento da trotinete ou sintomas observados.

Se o cliente tiver deixado acessórios com a trotinete, como carregador, chave ou cadeado, a secretária regista esses itens na ordem de serviço.

Depois de inserir toda a informação necessária, a secretária confirma o registo. O sistema cria a ordem de serviço e atribui automaticamente o estado "Pendente Diagnóstico".

Após a criação, a ordem de serviço fica disponível na lista de ordens pendentes para que um mecânico possa iniciar o diagnóstico.
    ]

),
caption: [Exemplo de _User Story_],
  kind: image,
  supplement: [Figura]
) <user_story_01>

== Refinamento de Requisitos Ambíguos
Após a fase inicial de elicitação, os requisitos do sistema encontravam‑se já identificados e documentados; contudo, muitos deles descreviam o funcionamento da solução de forma demasiado geral, pouco estruturada ou até ambígua. Tornou‑se evidente que, antes de avançar para fases posteriores do desenvolvimento, seria necessário clarificar o significado exato de vários requisitos e garantir que todos os intervenientes partilhavam a mesma interpretação do comportamento esperado do sistema.

O refinamento de requisitos assumiu, assim, um papel central na transformação das necessidades iniciais — frequentemente expressas de forma informal durante entrevistas e observações — num conjunto de especificações claras, objetivas e tecnicamente implementáveis. Este processo envolveu várias operações fundamentais: a comparação sistemática entre requisitos que descreviam comportamentos semelhantes, permitindo uni‑los num único requisito mais completo; a remoção de informação redundante ou irrelevante para o funcionamento do sistema; a decomposição de requisitos demasiado genéricos em unidades funcionais mais pequenas e específicas; e a reorganização de funcionalidades segundo perfis de utilizador, assegurando que cada papel (gerente, gestor de _stock_, secretária e mecânico) possui apenas as responsabilidades que lhe são atribuídas.

 Não querendo entrar em muito detalhe, a equipa de desenvolvimento decidiu que alguns exemplos das alterações efetuadas seria o suficiente para compreender o processo desta fase, e, por isso, de seguida seguem-se o refinamento de alguns requisitos:

#figure(
  block(
    width: 100%,
    stroke: 0.5pt + gray,
    inset: 15pt,
    radius: 4pt,
    fill: white,
    align(left)[
      // Título do Requisito
      #text(12pt, weight: "bold", navy)[A. Criação de uma nova OS]
      #v(0.5em)
      #line(length: 100%, stroke: 0.5pt + silver)
      #v(0.5em)

      *Descrições anteriores:*
      #text(size: 10pt)[
        
        
        \Requisito 06 - O sistema deve permitir registar a entrada de uma trotinete com dados do cliente, dados técnicos da trotinete e descrição do problema numa ordem de serviço nova.,

        \Requisito 08 - O sistema deve atribuir automaticamente o estado "Pendente Diagnóstico" a novas ordens de serviço.

        \Requisito 25 - O sistema deve permitir criar ordens de serviço contendo: nome do cliente, contacto (email ou telefone), NIF (opcional), marca, modelo, número de série (quando disponível) e descrição do problema. Todos os campos devem ser estruturados e validados.,

      ]

      // Secção do Problema
      *Problemas:*
      #text(size: 10pt)[
        - Não deverá haver mais informação que precise ser guardada ao criar uma OS nova?
        - Não deverá haver uma referência única para cada OS nova?
      ]

      #v(1em)
      
      // Secção da Clarificação
      #rect(
        width: 100%,
        fill: blue.lighten(95%),
        stroke: (left: 3pt + navy),
        inset: 10pt,
      )[
        *Requisito Refinado:* \
        #text(style: "italic")[
          "O sistema deverá registar a criação de uma OS obrigatoriamente com: os dados do cliente, os dados técnicos da trotinete, uma descrição do problema e, opcionalmente, por acessórios e estado à entrada (com notas textuais). Para todas as OS que forem criadas, deverá ser atribuído o estado de "Pendente Diagnóstico" e deverá ser criada uma referência única para a mesma."
        ]
      ]
    ]
  ),
  caption: [Refinamento dos requisitos 6, 8 e 25],
  supplement: [Figura],
)

#figure(
  block(
    width: 100%,
    stroke: 0.5pt + gray,
    inset: 15pt,
    radius: 4pt,
    fill: white,
    align(left)[
      // Título do Requisito
      #text(12pt, weight: "bold", navy)[B. Seleção de Reparações e Associação de Peças]
      #v(0.5em)
      #line(length: 100%, stroke: 0.5pt + silver)
      #v(0.5em)

      *Descrição anterior:*
      #text(size: 10pt)[

        \Requisito 31 - O sistema deve permitir ao mecânico registar digitalmente o trabalho realizado e as peças utilizadas.,
        \Requisito 40 - O sistema deve permitir ao mecânico registar os arranjos realizados na trotinete a partir de uma lista de operações previamente definida.
      ]

      // Secção do Problema
      *Problema:*
      #text(size: 10pt)[
        - A seleção de reparações é feita apenas a partir de uma lista predefinida ou pode-se adicionar reparações customizadas?
        - O mecânico consegue consultar o histórico de reparações anteriores da trotinete antes de escolher as novas?
        - É possível desassociar peças após terem sido selecionadas, ou cancelar uma reparação já registada?
        - Qual é o fluxo exato: o mecânico seleciona reparações e depois procura peças, ou procura peças e depois seleciona reparações compatíveis?
        - O sistema faz sugestões automáticas de peças baseado na reparação selecionada?
      ]

      #v(1.2em)
      
      #rect(
        width: 100%,
        fill: blue.lighten(97%),
        stroke: (left: 3pt + navy),
        inset: 10pt,
      )[
        *Requisito Refinado:* \
        #text(style: "italic", size: 10.5pt)[
          "Quando um mecânico selecionar uma OS "Pendente Diagnóstico", deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações que estará disponível, poderá consultar (procurando por referência ou fornecedor) e associar peças do _stock_, e também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela)."
        ]
      ]
    ]
  ),
  caption: [Refinamento do requisito #13],
  supplement: [Figura],
)

== Especificação Detalhada dos Requisitos Funcionais
Após o refinamento dos requisitos funcionais, a equipa concluiu que muitos deles continuavam pouco descritivos e não clarificavam de forma suficiente o comportamento esperado do sistema. Para ultrapassar estas limitações, desenvolveu‑se um conjunto de requisitos mais completos, estruturados e detalhados, capazes de representar com precisão o funcionamento pretendido.

=== Validação dos Requisitos
Contudo, como estas decisões poderiam introduzir desvios face às expectativas reais dos _stakeholders_, tornou‑se essencial validar todo o trabalho de refinamento numa reunião formal com o gerente, a secretária e os mecânicos. O objetivo foi garantir que as interpretações adotadas pela equipa correspondiam ao funcionamento pretendido e que nenhum requisito se afastava das necessidades operacionais da oficina.

Este processo de validação permitiu confirmar a correção das decisões tomadas e consolidar um conjunto final de requisitos funcionais e não funcionais, agora mais claros, específicos e estruturados.

#figure(
  image("pdf/ata_reunião_validacao_requisitos_F.pdf"),
  caption: [Primeira página da Ata de reunião de validação dos requisitos]
)

Durante a especificação detalhada dos requisitos estruturados, tornou‑se evidente que alguns aspetos do sistema exigiam decisões adicionais para garantir um modelo coerente, completo e alinhado com a realidade operacional da oficina. Estas decisões surgiram tanto da análise técnica da equipa como das clarificações obtidas na reunião de validação com os _stakeholders_. Para além de clarificar comportamentos que não estavam totalmente definidos nos requisitos iniciais, foi também necessário acrescentar novos requisitos que assegurassem a consistência do sistema e a sua adequação ao contexto real de utilização. Entre as principais decisões tomadas destacam‑se:

==== Separação das responsabilidades entre gerente e gestor de _stock_  
Optou‑se por distinguir claramente as funções de gerente e gestor de _stock_ dentro do sistema, antecipando a possibilidade de, no futuro, estas tarefas serem desempenhadas por pessoas diferentes. Esta decisão permitiu estruturar permissões específicas para cada papel e melhorar a rastreabilidade das ações, evitando que um único utilizador concentre operações que, operacionalmente, pertencem a áreas distintas.

==== Criação de um mecanismo de autenticação e controlo de acesso  
A definição de perfis distintos tornou necessária a implementação de um sistema de autenticação que assegurasse que cada utilizador acede apenas às funcionalidades que lhe dizem respeito. Para além de reforçar a segurança, esta abordagem permitiu estabelecer um modelo de controlo de acesso coerente com a separação de responsabilidades definida para o sistema.

==== Especificação rigorosa das validações dos campos de dados  
Para garantir consistência, qualidade dos dados e conformidade com a legislação portuguesa, foram definidos formatos e regras de validação para todos os campos das entidades do sistema. Isto inclui, por exemplo, a estrutura da morada, formatos de NISS, NIF, NUS, IBAN, código‑postal, telemóvel, email (seguindo a #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322]), bem como regras numéricas e decimais para salários e preços. Esta decisão permitiu estabelecer um modelo de dados consistente e organizado.

==== Definição de níveis mínimos de stock e criação de listas de encomendas  
Para melhorar a gestão de inventário, foi introduzido um requisito adicional que permite ao sistema identificar automaticamente peças abaixo do nível mínimo e gerar listas de encomenda agrupadas por fornecedor. Esta funcionalidade pôde reduzir o risco de ruturas de _stock_ e otimizar o processo de reposição, tornando‑o mais eficiente e menos dependente de verificações manuais.

==== Introdução de notificações automáticas  
Foram ainda definidos mecanismos de notificação que apoiam o fluxo de trabalho diário, alertando os utilizadores para eventos relevantes como pedidos de aprovação de orçamento, ultrapassagem de valores previstos, conclusão de reparações ou níveis de _stock_ abaixo do mínimo. Estas notificações ajudam a garantir que nenhuma ação importante é ignorada e que o sistema acompanha ativamente o funcionamento da oficina.

=== Requisitos Funcionais
Depois de realizada a reunião de validação do trabalho desenvolvido durante o refinamento e estruturação dos requisitos, foi criada uma tabela que contém os requisitos funcionais que viriam a servir para as fases seguintes de modelação, especificação e implementação do sistema. De forma a exemplificar, na tabela seguinte encontram-se alguns dos requisitos funcionais principais. Para visualizar os requisitos todos, pode consultá-los no @requisitos_estruturados.

#figure(
  requisito(
  id: "REQ-004",
  titulo: "Registo de Dados de Funcionários",
  requisito_utilizador: "O registo de funcionários deverá ser composto obrigatoriamente por: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN e salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto). Dados sensíveis dos funcionários devem ser armazenados de forma encriptada na base de dados.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Recursos Humanos",
  requisitos_sistema: (
    "Na página de criação de funcionários, deverão ser fornecidos: o nome, morada (composta por número de porta, rua, localidade e código-postal), número de telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto) e palavra-passe.",
    "O nome, rua, localidade, email e palavra-passe deverão ser campos textuais não vazios e o email deverá seguir a convenção " + link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] + ".O número de porta deverá ser um número inteiro com, no máximo, 4 dígitos e poderá ser seguido por uma letra; o código postal deverá ser composto por 4 dígitos, um hífen '-' e terminado por 3 dígitos; o número de telemóvel deverá ser um número composto por 9 dígitos; o NISS, um número composto por 11 dígitos; o NIF, um número composto por 9 dígitos; o NUS, um número composto por 9 dígitos; o IBAN, composto por 2 caracteres alfabéticos, seguidos de 23 dígitos; os valores do salário deverão ser números reais positivos com 2 casas decimais.",
    "Os seguintes campos deverão ser armazenados de forma encriptada na base de dados: nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário pago por hora, salário mensal líquido, salário mensal bruto, número de porta, rua, localidade e código-postal. A encriptação deverá garantir que estes dados sensíveis não estão acessíveis em texto plano fora do contexto de operações autorizadas do sistema.",
    "Depois de validados e preenchidos todos os dados de um funcionário, quando o gerente confirmar, o sistema deverá fornecer um identificador único, informar o gerente da criação, apresentando uma página com todos os dados do novo funcionário.",
  ),
  relevancia: "Centraliza toda a informação necessária para a folha de pagamento, segurança social e conformidade legal, garantindo a proteção de dados pessoais sensíveis através de armazenamento encriptado."
),

  caption: [Requisitos funcionais da criação de um Funcionário],
  supplement: [Figura],
)

=== Requisitos Não Funcionais
Os requisitos não funcionais foram definidos durante a fase de refinamento dos requisitos funcionais, tendo sido elaborados pela equipa de desenvolvimento com base no contexto real de utilização do sistema e nas propriedades de qualidade consideradas essenciais para o seu correto funcionamento. Estes requisitos refletem aspetos como segurança, usabilidade, confiabilidade, portabilidade e desempenho, garantindo que o sistema não só cumpre as funcionalidades previstas, mas o faz de forma eficiente e adequada ao ambiente operacional da empresa.

#figure(
  image("pdf/ata_reuniao_requisitos_NF.pdf"),
  caption: [Ata da reunião sobre os requisitos não funcionais]
)

#figure(
table(
  columns: (0.5fr, 1.5fr, 0.5fr, 1fr, 0.6fr),
  inset: 7pt,
  fill: (x, y) => if y == 0 or y == 1{ luma(245) },
  align: (x, y) => {
    if y == 0 { return center }
    if x in (0, 2, 3, 4) { return center }
    left + horizon
  },

  table.cell(colspan: 5, fill: luma(230))[
  #set text(13pt, weight: "bold")
  Alguns Requisitos Não Funcionais
  ],
  [*Nr*], [*Descrição*], [*Data*], [*Fonte*], [*Analista*],
  [RF14],	[Dados sensíveis devem ser armazenados de forma encriptada.],	[26/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF15],	[A interface deve ser simples e intuitiva para permitir que novos funcionários aprendam a utilizá-la rapidamente.],	[26/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF16],	[Os formulários devem validar automaticamente campos obrigatórios e formatos.],	[26/2/26],	[Entrevista ID \#001],	[Luís Soares],

),
  caption: [Alguns requisitos não funcionais],
  supplement: [Figura],
)

== _Use Cases_

=== Metodologia usada
A especificação dos casos de uso seguiu uma abordagem consistente, baseada na análise dos requisitos funcionais e na identificação dos padrões comuns presentes na maioria das operações do sistema. De forma geral, estes casos de uso partilham uma estrutura semelhante: começam por definir as pré‑condições necessárias, descrevem o fluxo principal da ação e incluem os fluxos alternativos e de exceção que resultam das validações e regras de negócio associadas a cada entidade. Esta uniformidade permitiu criar um conjunto coeso e previsível de casos de uso, facilitando tanto a sua leitura como a sua implementação, de seguida, tendo em vista a apresentação da estrutura usada na definição dos casos de uso, segue-se aquele que foi desenvolvido para o registo de um funcionário:

#uc_spec(
    id: "02",
    nome: "Registar Funcionário",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado.",
    pos_condicoes: "Um novo funcionário é registado no sistema com os campos sensíveis armazenados de forma encriptada e um identificador único é atribuído.",
    fluxo_normal: (
      [O gerente seleciona a opção de criar novo funcionário.],
      [O gerente preenche todos os dados obrigatórios: nome, morada completa (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário (valor por hora, valor mensal bruto e valor mensal líquido) e palavra-passe.],
      [O gerente confirma o registo.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o número de porta é um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra.],
      [O sistema valida que a rua é um campo textual não vazio.],
      [O sistema valida que a localidade é um campo textual não vazio.],
      [O sistema valida que o código-postal segue o formato de 4 dígitos, um hífen e 3 dígitos.],
      [O sistema valida que o telemóvel é composto por 9 dígitos.],
      [O sistema valida que o email segue o formato válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322].],
      [O sistema valida que a data de nascimento é uma data válida.],
      [O sistema valida que o NISS é composto por 11 dígitos.],
      [O sistema valida que o NIF é composto por 9 dígitos.],
      [O sistema valida que o NUS é composto por 9 dígitos.],
      [O sistema valida que o IBAN é composto por 2 caracteres alfabéticos seguidos de 23 dígitos.],
      [O sistema valida que o valor por hora é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal bruto é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal líquido é um número real positivo com 2 casas decimais.],
      [O sistema valida que a palavra-passe é um campo textual não vazio.],
      [O sistema encripta os campos sensíveis (nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário pago por hora, salário mensal líquido, salário mensal bruto, número de porta, rua, localidade e código-postal) antes do armazenamento.],
      [O sistema cria o funcionário e atribui um identificador único.],
      [O sistema confirma o registo apresentando os dados do funcionário criado.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela o registo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o gerente que o nome deve ser um campo textual não vazio e impede o registo.],
        ),
      ),
      (
        condicao: "Número de porta inválido",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o gerente que o número de porta deve ser um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra, e impede o registo.],
        ),
      ),
      (
        condicao: "Rua inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o gerente que a rua deve ser um campo textual não vazio e impede o registo.],
        ),
      ),
      (
        condicao: "Localidade inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o gerente que a localidade deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Código-postal inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o gerente que o código-postal deve seguir o formato XXXX-XXX (4 dígitos, hífen, 3 dígitos) e impede o registo.],
        )
      ),
      (
        condicao: "Telemóvel inválido",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o gerente que o telemóvel deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "Email inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o gerente que o email deve seguir o formato de endereço de correio electrónico válido (nome#"@"dominio.com) e impede o registo.],
        )
      ),
      (
        condicao: "Data de nascimento inválida",
        passo: "11",
        fluxo: (
          [11.1 - O sistema informa o gerente que a data de nascimento deve ser uma data válida e impede o registo.],
        )
      ),
      (
        condicao: "NISS inválido",
        passo: "12",
        fluxo: (
          [12.1 - O sistema informa o gerente que o NISS deve ser composto por 11 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "NIF inválido",
        passo: "13",
        fluxo: (
          [13.1 - O sistema informa o gerente que o NIF deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "NUS inválido",
        passo: "14",
        fluxo: (
          [14.1 - O sistema informa o gerente que o NUS deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "IBAN inválido",
        passo: "15",
        fluxo: (
          [15.1 - O sistema informa o gerente que o IBAN deve ser composto por 2 caracteres alfabéticos seguidos de 23 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "Valor por hora inválido",
        passo: "16",
        fluxo: (
          [16.1 - O sistema informa o gerente que o valor por hora deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Valor mensal bruto inválido",
        passo: "17",
        fluxo: (
          [17.1 - O sistema informa o gerente que o valor mensal bruto deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Valor mensal líquido inválido",
        passo: "18",
        fluxo: (
          [18.1 - O sistema informa o gerente que o valor mensal líquido deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Palavra-passe inválida",
        passo: "19",
        fluxo: (
          [19.1 - O sistema informa o gerente que a palavra-passe deve ser um campo textual não vazio e impede o registo.],
        )
      ),
    )
)

Apesar desta base comum, alguns casos de uso destacam‑se pela complexidade adicional que incorporam. Nestes, a lógica de negócio exige comportamentos específicos, interações com múltiplas entidades ou transições de estado que têm impacto direto noutros módulos do sistema. São estes casos que, pela sua natureza, se afastam do padrão geral e requerem uma análise mais detalhada para compreender plenamente o seu funcionamento.

Entre os casos mais complexos encontram‑se:

   - * UC‑16: Editar Devolução de Peça  *
   
    Este caso envolve transições de estado que afetam diretamente o _stock_ e a componente financeira. Dependendo da alteração efetuada, o sistema pode repor quantidades no inventário, registar perdas financeiras ou impedir transições inválidas, o que introduz uma lógica mais rica do que a simples edição de dados.

  - * UC‑22: Editar Lista de Encomendas * 
  
    A complexidade surge das regras associadas aos estados da lista. A passagem de “Rascunho” para “Enviada” é simples, mas a transição para “Recebida” desencadeia validações adicionais e a atualização automática do _stock_.

  - * UC‑31: Editar Ordem de Serviço * 
  
    Este caso de uso agrega múltiplos fluxos alternativos, dependentes do estado atual da OS. A edição pode envolver acessórios, peças, valores orçamentais ou pagamentos, cada um com regras próprias e impacto em diferentes partes do sistema.
\
  - * UC‑34: Executar Diagnóstico de Ordem de Serviço * 
  
    A lógica torna‑se mais específica quando o valor estimado ultrapassa 50€, obrigando a uma interrupção do fluxo normal e à mudança de estado para “Pendente aprovação do orçamento”, com notificação automática à secretária.

  - * UC‑35: Executar Reparação de Ordem de Serviço * 
  
    Este caso articula‑se com vários subsistemas: _stock_, devoluções, notificações e gestão de peças. A ausência de _stock_, a deteção de defeitos ou a necessidade de adicionar peças adicionais desencadeiam fluxos alternativos que alteram o estado da OS e geram novos registos no sistema.

Assim, embora a maioria dos casos de uso siga um padrão bem definido, estes exemplos demonstram como a lógica de negócio da oficina introduz particularidades que exigem uma modelação mais detalhada. Por esse motivo, recomenda‑se a consulta direta dos casos de uso completos (no @use_cases), de forma a compreender plenamente as regras, validações e interações que caracterizam o comportamento global do sistema.

== Diagramas _UML_

=== Modelo de Domínio
Como introdução ao padrão arquitetural desenvolvido, foram elaborados dois modelos de domínio complementares. O primeiro, de natureza mais simplificada, apresenta as principais entidades do sistema e os respetivos relacionamentos, focando-se nos elementos essenciais para a compreensão global do problema.

O segundo modelo aprofunda esta representação, expandindo as entidades principais com informação adicional que suporta a sua caracterização detalhada. Para além das entidades centrais da lógica de negócio, são assim considerados os seus atributos e componentes internos, responsáveis por descrever cada entidade de forma mais completa — por exemplo, no caso de um funcionário, incluem-se dados de identificação e contactos, enquanto que, no caso de uma peça, são considerados elementos como a sua referência e o fornecedor associado.
Desta forma, o segundo modelo não só mantém a estrutura do primeiro, como a enriquece, permitindo uma visão mais detalhada e próxima da realidade do sistema.

\

#figure(
  image("images/modelo_dominio_simples.png", width: 100%),
  caption: [Modelo de Domínio Simplificado]
)

É de notar que, embora o modelo de domínio apresentado seja conciso, foram consideradas algumas decisões adicionais que importa explicitar.

Em primeiro lugar, todos os relacionamentos descritos como *"gere"* devem ser interpretados como uma abstração. Na prática, estes representam três tipos distintos de interação — *registar*, *editar* e *remover* — que partilham exatamente as mesmas multiplicidades. Esta simplificação foi adotada com o objetivo de reduzir a complexidade visual do modelo, sem perda de expressividade ao nível conceptual.

Por fim, importa clarificar as multiplicidades associadas às entidades `Secretária`, `Gestor de Stock`, `Mecânico` e `Gerente`. No modelo, estas surgem frequentemente com cardinalidades como 1 ou 1..2, o que pode sugerir que apenas um ou dois utilizadores podem executar determinadas ações. No entanto, esta representação não limita o uso do sistema a um número reduzido de utilizadores simultâneos; trata-se apenas de uma simplificação do modelo, sendo que, na prática, o sistema suporta a utilização concorrente por múltiplos utilizadores com estes papéis.

É também de notar que ambos os modelos podem ser consultados nos anexos (@modelo_dominio_simples e @modelo_dominio_completo).

=== Diagrama de _Use Cases_

Tendo estabelecido o modelo de domínio e as principais entidades do sistema, bem como os seus relacionamentos, torna-se agora possível analisar o sistema numa perspetiva comportamental.

Neste contexto, apresenta-se de seguida o diagrama de casos de uso, que sintetiza os principais cenários de utilização do sistema e a sua associação aos diferentes tipos de utilizadores.

#figure(
  image("images/diagrama_de_use_cases_geral.png", width: 100%),
  caption: [Diagrama de Use Cases Geral]
)

\

Devido ao elevado número de casos de uso identificados, a sua representação num único diagrama tornaria a leitura excessivamente densa e pouco clara. Por essa razão, optou-se por complementar o diagrama geral com diagramas adicionais organizados por tipo de ator, permitindo uma visualização mais focada e compreensível das responsabilidades de cada perfil.

Os casos de uso "Consultar Estado Financeiro" e "Fazer Autenticação" constituem exceções, sendo apresentados apenas no diagrama geral devido ao facto de serem casos de uso que não podiam ser agrupados.

Importa ainda destacar que, tal como definido no REQ-001, o Gerente possui permissões globais sobre o sistema. Esta característica é refletida no diagrama através de relações de generalização com os restantes atores, indicando que este pode executar todas as funcionalidades disponíveis para eles.

De seguida, apresentam-se os diagramas específicos por ator, onde é possível analisar de forma detalhada as funcionalidades associadas a cada perfil e compreender melhor a distribuição de responsabilidades no sistema. Nomeadamente, é possível perceber que:

- o Gestor de _Stock_ é capaz de registar, editar e criar peças, _stock_ de peças, listas de encomendas, fornecedores e devoluções de peças.

- a Secretária é capaz de registar, editar e criar clientes, trotinetes e ordens de serviço.

- o Gerente é capaz de registar, editar e criar funcionários e serviços de reparação.

- o Mecânico é capaz de executar e diagnosticar ordens de serviço.

\
\

#figure(
  image("images/diagrama_de_use_cases_gestor_de_stock.png", width: 100%),
  caption: [Diagrama de _Use Cases_ do Gestor de _Stock_]
)

#figure(
  image("images/diagrama_de_use_cases_secretaria.png", width: 100%),
  caption: [Diagrama de _Use Cases_ da Secretária]
)

#figure(
  image("images/diagrama_de_use_cases_gerente.png", width: 100%),
  caption: [Diagrama de _Use Cases_ do Gerente]
)

#figure(
  image("images/diagrama_de_use_cases_mecanico.png", width: 80%),
  caption: [Diagrama de _Use Cases_ do Mecânico]
)

=== Diagramas de Atividade
Os diagramas de atividade foram elaborados com o objetivo de complementar a especificação dos casos de uso, oferecendo uma representação dinâmica e sequencial dos fluxos de execução do sistema. Ao contrário dos casos de uso, que descrevem de forma textual o comportamento esperado, os diagramas de atividade permitem visualizar graficamente a ordem das ações, as condições de ramificação e as responsabilidades de cada interveniente, recorrendo a _swimlanes_ para distinguir as ações do utilizador das ações do sistema.

A seleção dos casos de uso a serem transformados em diagramas de atividade incidiu, de forma prioritária, sobre aqueles que apresentam maior complexidade comportamental, nomeadamente os que envolvem múltiplos fluxos alternativos, transições de estado com impacto noutros módulos do sistema ou interações entre atores distintos. São exemplos representativos desta complexidade o UC-35, que articula a gestão de _stock_, o registo de devoluções e a geração de alertas para múltiplos atores, e o UC-34, cujo fluxo desencadeia notificações automáticas e alterações de estado dependentes do resultado do diagnóstico.

#figure(
  image("atividade/use-case30.png"),
  caption: [Diagrama de atividade - Registar Ordem de Serviço]
)

#figure(
  image("atividade/use-case34.png"),
  caption: [Diagrama de atividade - Executar diagnóstico de Ordem de Serviço]
)

#figure(
  image("atividade/use-case35.png"),
  caption: [Diagrama de atividade - Executar conserto de Ordem de Serviço]
)

== Uso de _LLM_
Desde as tarefas mais mecânicas e estruturais até às decisões de maior complexidade conceptual, o _LLM_ foi a ferramenta que conduziu a totalidade da conceção e engenharia de requisitos, permitindo à equipa atingir um nível de detalhe e consistência que dificilmente seria alcançável no mesmo intervalo de tempo através de métodos exclusivamente manuais.

Ainda antes do arranque formal do levantamento de requisitos, o _LLM_ gerou o código _LaTeX_ utilizado para estruturar a transcrição das entrevistas, garantindo desde o início uma apresentação uniforme e profissional de toda a documentação gerada. Após a recolha de informação nas entrevistas e na análise do processo operacional, foi através do _LLM_ que se procedeu à organização e consolidação dos requisitos em bruto, agrupando contributos provenientes de fontes distintas. Na fase de refinação, o _LLM_ identificou e eliminou redundâncias entre requisitos formulados de forma diferente mas com comportamentos sobreponíveis, produzindo um conjunto consolidado e sem duplicações. Foi também o _LLM_ que estabeleceu o formato padronizado adotado para a especificação detalhada dos requisitos funcionais, contemplando a separação entre o requisito do utilizador, os requisitos do sistema e a relevância associada a cada um.

A especificação dos casos de uso foi igualmente produzida com recurso a _LLM_, que gerou a estrutura em _Typst_ e o conteúdo detalhado de cada caso, incluindo fluxos normais, alternativos e condições de exceção. Tanto nos casos de estrutura mais repetitiva — como o registo, edição e remoção de entidades simples como clientes, funcionários, peças, _stock_, fornecedores e encomendas — como nos de maior complexidade, envolvendo transições de estado, interações entre subsistemas ou lógica de negócio mais elaborada, o _LLM_ produziu especificações completas e coerentes, prontas a integrar na documentação.

Por fim, na construção do modelo de domínio completo, o _LLM_ desempenhou um papel determinante ao identificar entidades e relacionamentos que haviam sido omitidos numa primeira análise. A partir da descrição do domínio e dos requisitos já especificados, foi capaz de sugerir associações em falta e clarificar multiplicidades que não estavam suficientemente definidas, assegurando que o modelo final refletia de forma completa e coerente a realidade operacional da EcoRide Solutions e constituía uma base sólida para as fases subsequentes do projeto.

= Arquitetura e Design do Software utilizando LLM

== Definição da arquitetura do sistema
A definição da arquitetura do sistema constituiu uma etapa fundamental para assegurar a robustez e a escalabilidade do software a desenvolver. Esta fase focou-se na organização estrutural dos componentes, garantindo que os requisitos funcionais e não-funcionais fossem atendidos de forma eficiente.

No presente projeto, a arquitetura foi delineada através de uma abordagem que combina padrões consolidados de engenharia de software com o suporte analítico de Modelos de Linguagem de Grande Escala (_LLMs_). Esta integração visou otimizar a tomada de decisão técnica e a validação das estruturas propostas.

Os recursos e estratégias selecionados para a fundamentação deste sistema, bem como as respetivas justificações, apresentam-se de seguida:

- *Padrão Arquitetural de 3 camadas:* A adoção deste modelo justifica-se pela necessidade de garantir uma implementação organizada e modular. A separação de responsabilidades permite que o sistema seja mais fácil de planear, corrigir e manter, isolando a lógica de negócio da interface de utilizador e da persistência de dados.

- *Criação da _API_ de Dados com _Javalin_:* Para a interface da camada de controlo utilizou-se a _framework_ _Javalin_. O uso dela permitiu a criação de uma _API RESTful_ de forma simples e rápida dada a baixa complexidade de aprendizagem para trabalhar com a ferramenta e a fácil integração com o restante sistema.

- *Conetividade via _JDBC_ e _MySQL_:* Para a camada de persistência, utilizou-se código _SQL_ integrado na linguagem _Java_ através da _API JDBC (Java Database Connectivity)_. Esta escolha assegura uma ligação estável e direta à base de dados _MySQL_, permitindo um controlo granular sobre as operações, como consultas, inserções, transações e atualizações.

- *Desenvolvimento da Interface com _Lovable_, _React_ e _TypeScript_:* A implementação da camada de apresentação foi acelerada com o recurso à ferramenta _Lovable_ para a geração inicial da interface. Posteriormente, o código foi refinado, corrigido e integrado manualmente com a _API_ de dados, utilizando _React_ para a construção de componentes modulares e _TypeScript_ para garantir a segurança tipográfica e a manutenibilidade do código _frontend_.

- *Revisão e Implementação Assistida por _LLM_ (_Claude_):* O modelo _Claude_ foi integrado de forma transversal no processo, sendo utilizado não só para a revisão crítica da arquitetura, mas também no auxílio à implementação técnica. O recurso a esta ferramenta permitiu validar a divisão dos subsistemas e mitigar falhas estruturais precocemente. Para além disso, como irá ser futuramente explicado, foi fundamental para a implementação de todas as camadas do sistema.

- *Orquestração de Serviços com _Docker_:* Para garantir a portabilidade e a consistência entre ambientes de desenvolvimento e produção, utilizou-se o _Docker_ para a orquestração de todos os serviços. Na prática, foi desenvolvido um contentor responsável pela orquestração da Base de Dados, da _API_ de dados, da Interface e do servidor de Documentação.

- *Documentação da _API_ via _Swagger_:* A transparência e facilidade de consumo da _API_ de dados são asseguradas pela disponibilização de documentação interativa através do _Swagger_. Esta ferramenta, integrada no servidor, permite a visualização e teste dos _endpoints_ de forma estruturada.

De seguida, apresenta-se o diagrama de componentes inicial, em que é possível observar a adoção da arquitetura em três camadas, sendo a camada de Dados implementada pelo componente `EcoRideCD`, a camada de Controlo pelo componente `EcoRideLN` e a camada de Apresentação pelo componente `EcoRideCA`.

#figure(
  image("images/diagrama_de_componentes_geral.png", width: 30%),
  caption: [Diagrama de Componentes da Arquitetura do sistema]
)


== Camada de Controlo

=== Diagramas de Componentes
Com base nos casos de uso previamente estabelecidos, tornou-se imperativa a análise das responsabilidades inerentes ao funcionamento da oficina de trotinetes. Este levantamento permitiu a identificação e o agrupamento lógico de métodos em subsistemas, de modo a segmentar as operações críticas num modelo organizado e eficiente. Nomeadamente, foram identificados os subsistemas:

- *`SAutenticacao`:* Mantém a segurança, incluindo processos de _login_ e gestão de permissões, de forma isolada. Esta separação permite que uma futura transição de uma base de dados local para um serviço de autenticação externo exija alterações apenas neste componente.  

- *`SClientes`:* Subsistema dedicado à manutenção dos dados dos clientes e das respetivas trotinetes. Facilita o acesso e a edição de informações referentes a entidades externas, centralizando o histórico de equipamentos associados. 

- *`SFinanceiro`:* Responsável pelo registo de movimentos monetários e pelo tratamento analítico dos dados financeiros da empresa. A sua independência garante que eventuais alterações nos métodos de análise estatística da saúde financeira não impactem as restantes áreas do sistema.  

- *`SFuncionarios`:* Armazena exclusivamente os dados dos colaboradores da organização. Esta segregação é essencial para garantir que apenas a entidade patronal ou perfis autorizados consultem informações sensíveis e gerir cálculos salariais de forma segura.   

- *`SNotificacoes`:* Criado para centralizar a transmissão de alertas e avisos entre os diversos intervenientes do processo operacional. A sua arquitetura permite a integração futura com outros meios de comunicação, como serviços de _SMS_, chamadas automáticas ou aplicações de agenda.  

- *`SOrdensServico`:* Desenvolvido para isolar a elevada complexidade associada à execução e fluxo de estados das reparações. Possui uma lógica de negócio muito específica e singular ao contexto da oficina, que beneficia de um ambiente de execução dedicado.  

- *`SReparacoes`:* Responsável pela gestão do catálogo de serviços disponíveis e respetivos custos de execução. Sendo um componente consultado por múltiplos subsistemas, a sua autonomia permite atualizar a tabela de preços e serviços sem que os restantes módulos necessitem de conhecer a implementação interna.  

- *`SStock`:* Apresenta uma elevada complexidade em fluxos de inventário, entradas e devoluções. Ao isolar esta lógica, o sistema expõe apenas as operações necessárias aos outros subsistemas, permitindo correções e otimizações futuras no fluxo de mercadorias sem comprometer a estabilidade global.

Complementarmente à especificação detalhada, a arquitetura lógica foi refletida no diagrama de componentes do sistema. Partindo de uma estrutura inicial baseada no modelo de três camadas, o componente representativo da Camada de Controlo foi decomposto de modo a integrar os subsistemas mencionados, assegurando a rastreabilidade entre os requisitos funcionais e a organização estrutural da aplicação.

#figure(
  image("images/diagrama_de_componentes_simples.png", width: 100%),
  caption: [Diagrama de Componentes com Subsistemas da camada de Controlo]
)

=== Diagramas de Classes
A construção dos diagramas de classes seguiu uma abordagem sistemática e consistente, fundamentada na decomposição do sistema em subsistemas independentes. Esta separação permitiu tratar cada subsistema como uma unidade autónoma de design, cujas responsabilidades e fronteiras foram delimitadas com clareza antes de se avançar para o detalhe das classes.

Para garantir um contrato estável entre subsistemas, foram definidas interfaces para cada `Facade`. Desta forma, o `EcoRideController` que orquestrará todos os `Facades`, não dependerá de implementações concretas, mas sim de abstrações, reduzindo o acoplamento e tornando o sistema mais resiliente a alterações futuras. 

Internamente, cada um recorre a estruturas do tipo `Map` para armazenar as entidades que lhe competem gerir. Esta decisão visou minimizar a complexidade computacional das operações mais frequentes da aplicação — nomeadamente adições, atualizações e remoções — garantindo acesso rápido e direto às entidades através de identificadores únicos.

As classes principais de cada subsistema foram modeladas com base nos casos de uso e nos requisitos do sistema, que serviram de fundamento para determinar os atributos necessários e os seus tipos de dados. A cada entidade foi atribuído um identificador único (_id_), justificado não pelos documentos de requisitos, mas pela necessidade computacional de distinguir inequivocamente cada instância em memória (e, como irá ser visto, em disco) e nas operações do sistema. Os restantes atributos foram definidos com tipos que refletem a natureza e as regras de validação presentes nos requisitos — campos textuais, numéricos, datas, entre outros — e, sempre que a lógica de negócio o exige, foram também estabelecidas associações a classes de outros subsistemas, refletindo as dependências reais entre entidades do domínio.

==== SAutenticacao

O subsistema de autenticação é composto pela interface `ISAutenticacao`, pela sua implementação concreta em `SAutenticacaoFacade`, e pela entidade central deste contexto: a classe `Utilizador`.

*`Utilizador`*

- o `id` - um inteiro que permite identificar univocamente cada utilizador no sistema;
- a `password` - uma _String_ que armazena a credencial de acesso.;
- um _enum_ `Cargo` - que define o papel que esse utilizador desempenha na organização;
- uma associação a um `Funcionario` do subsistema `SFuncionarios`.

A utilização de um _enum_ para identificar o papel do `Utilizador` estabelece um contrato fixo e explícito sobre os cargos existentes no contexto da empresa — Gerente, GestorStock, Secretaria e Mecanico — eliminando a possibilidade de atribuição de valores arbitrários e tornando o sistema de controlo de acesso mais robusto e seguro. A associação a um `Funcionario` do subsistema `SFuncionarios`, garante que a identidade de autenticação está sempre ancorada a um colaborador concreto registado no sistema, e que os dados de acesso se mantêm separados dos dados pessoais e profissionais do funcionário.

A opção por uma classe `Utilizador` distinta, em vez de incorporar diretamente as credenciais de acesso na classe `Funcionario`, foi também motivada por uma perspetiva de extensibilidade futura, porque assim esta arquitetura permite que, numa fase posterior, o sistema venha a suportar perfis de autenticação para clientes, bastando para isso criar novos utilizadores sem cargo associado a funcionários, sem necessidade de alterações estruturais significativas.

O `SAutenticacaoFacade` gere internamente um `Map` de Utilizadores, indexado pelo `idUtilizador`, permitindo a localização imediata de qualquer utilizador durante o processo de autenticação. A interface `ISAutenticacao` define o contrato comportamental deste `Facade`, assegurando que os subsistemas que dele dependam interajam sempre através de uma abstração estável.

#figure(
  image("images/SAutenticacaoANTESSemDAO.png", width: 100%),
  caption: [Diagrama de Classes do SAutenticacao]
)

==== SClientes
O subsistema de clientes segue a mesma abordagem estrutural já descrita: a interface `ISClientes` define o contrato comportamental do subsistema, implementado pelo `SClientesFacade`. Este `Facade` mantém dois `Maps` internos — um indexado por `idCliente` e outro por `idTrotinete` — permitindo o acesso direto e eficiente a qualquer uma das entidades geridas por este subsistema, independentemente do ponto de entrada da operação.
As duas entidades centrais deste subsistema são o `Cliente` e a `Trotinete`, cujos atributos foram derivados diretamente da análise dos casos de uso e requisitos do sistema.

*`Cliente`*

Quanto ao mapeamento de tipos de dados:

- o `nome` e o `email` são naturalmente _String_, por se tratarem de campos textuais;
- o `telemovel`, apesar de ser composto exclusivamente por dígitos, foi igualmente representado como _String_;
- o `NIF`, um número composto por 9 dígitos que, tal como o telemóvel, uma _String_.

 A escolha do tipo _String_ para o `telemovel` e para o `NIF` justifica-se pelo facto da sua natureza ser identificativa e não aritmética, pelo que operações numéricas sobre estes valores não fazem sentido no domínio do problema. Esta opção tem ainda a vantagem prática de preservar eventuais zeros iniciais e de facilitar validações por expressão regular, em conformidade com as regras definidas nos requisitos.
 
*`Trotinete`*

A classe é composta pelos atributos:

- `id`;
- `modelo`;
- `marca`;
- `num_serie`;
- `tipo_motor`.

Estes campos derivam dos dados necessários para identificar e caracterizar o equipamento entregue pelo cliente, presentes nos casos de uso que descrevem o diagnóstico e execução de reparações — nomeadamente, a consulta dos dados da trotinete durante o processo de reparação. Todos os atributos descritivos foram mapeados como _String_, uma vez que representam campos textuais ou alfanuméricos sem semântica aritmética: o `modelo` e a `marca` são designações textuais, o `num_serie` é um identificador alfanumérico do fabricante, o `tipo_motor` é uma descrição qualitativa do equipamento, enquanto que o `id`, foi mapeado para um inteiro, para permitir endereçamento único no `Map`.

Cada `Cliente` mantém uma lista das suas `Trotinetes`, o que permite, a partir de um cliente, aceder rapidamente ao conjunto de equipamentos que lhe estão associados — informação relevante em operações como a criação de uma nova ordem de serviço ou a consulta do historial do cliente. Em sentido inverso, cada `Trotinete` referencia o `Cliente` a que pertence, formalizando a relação de posse. Adicionalmente, cada `Trotinete` mantém uma lista das `OrdemServicos` que lhe foram associadas ao longo do tempo, o que torna o acesso ao seu historial de intervenções imediato, sem necessidade de percorrer o subsistema de ordens de serviço de forma exaustiva.


#figure(
  image("images/SClientesANTESSemDAO.png", width: 100%),
  caption: [Diagrama de Classes do SClientes]
)

==== SFinanceiro
O subsistema financeiro segue a mesma estrutura já estabelecida: a interface `ISFinanceiro` define o contrato comportamental, implementado pelo `SFinanceiroFacade`. Este `Facade` mantém internamente um `Map` de `MovimentoFinanceiro`, indexado por `idMovimento`, garantindo acesso direto e eficiente a qualquer registo financeiro.

A entidade central deste subsistema é a classe `MovimentoFinanceiro`, cujos atributos foram derivados dos requisitos relativos à consulta e análise do estado financeiro da empresa. Os requisitos estabelecem que o sistema deve registar todos os movimentos monetários realizados — pagamentos de salários, gastos com encomendas de peças e receção de pagamentos — e disponibilizar mecanismos de filtragem por intervalo de datas e por tipo de movimento, apresentando os totais por categoria.

*`MovimentoFinanceiro`*

- o `valor`, mapeado como `float`, representa o montante monetário associado ao movimento;
- a `data`, do tipo `LocalDateTime`, regista o momento exato em que o movimento ocorreu; 
- a `descricao`, representado como _String_, permite associar a cada movimento uma nota textual que contextualiza a operação registada;
- o `tipo` é uma instância do _enum_ TipoMovimento, que define as quatro categorias de movimentos financeiros presentes no domínio do negócio: `Salario`, `GastoPecas`, `LucroMaoObra` e `LucroVendaPecas`.

A escolha do tipo de dados `float` para o `valor`, justifica-se pela necessidade de representar valores com casas decimais, em conformidade com os requisitos que definem os valores financeiros como números reais com duas casas decimais. A utilização do tipo `LocalDateTime` para o atributo `data`, em detrimento de uma simples _String_, confere ao sistema a capacidade nativa de realizar comparações e filtragens temporais — operação diretamente exigida pelos requisitos, que prevêm a filtragem de movimentos por intervalo de datas.

Tal como no caso do _enum_ `Cargo` no subsistema de autenticação, a utilização de um _enum_ no `tipo` estabelece um contrato fixo sobre as categorias admissíveis, impedindo a introdução de valores arbitrários e tornando a filtragem por tipo de movimento direta e segura. As quatro categorias refletem diretamente as exigências dos requisitos, que preveem a apresentação de totais separados por salários, gastos com peças, lucros com mão de obra e lucros provenientes da venda de peças.

#figure(
  image("images/SFinanceiroANTESSemDAO.png", width: 100%),
  caption: [Diagrama de Classes do SFinanceiro]
)


==== SFuncionarios
O subsistema de funcionários também segue a mesma estrutura: a interface `ISFuncionarios` define o contrato comportamental, implementado pelo `SFuncionariosFacade`. Este `Facade` mantém internamente um `Map` de `Funcionario`, indexado por `idFuncionario`, garantindo acesso direto e eficiente a qualquer colaborador registado no sistema.

A entidade central deste subsistema é a classe `Funcionario`, cujos atributos foram derivados diretamente dos requisitos e casos de uso relativos ao registo e gestão de funcionários. Os requisitos estabelecem de forma explícita o conjunto de dados obrigatórios a registar para cada colaborador, e a correspondência com os atributos da classe é direta:

*`Funcionario`*

- `nome` — `String`
- `email` — `String`
- `rua` — `String`
- `localidade` — `String`
- `telemovel` — `String`
- `NISS` — `String`
- `salario_hora` — `float`
- `salario_liquido` — `float`
- `salario_bruto` — `float`
- `horas_extra` — `int`
- `NIF` — `String`
- `NUS` — `String`
- `IBAN` — `String`
- `codigo_postal` — `String`
- `numero_porta` — `String`
- `data_nascimento` — `LocalDate`

Os atributos `nome`, `email`, `rua` e `localidade` foram mapeados como _String_ por se tratarem de campos textuais sem semântica aritmética. Os restantes — `telemovel`, `NISS`, `NIF`, `NUS`, `IBAN` e `codigo_postal` — são identificadores ou códigos com formato fixo, sem operações aritméticas associadas, cujo tratamento como texto facilita a validação por expressão regular e preserva eventuais zeros iniciais. O atributo `numero_porta` segue o mesmo raciocínio: apesar de conter maioritariamente dígitos, os requisitos prevêm que possa ser seguido por uma letra, tornando a sua representação como `String` a única opção correta.

O atributo `data_nascimento` foi mapeado como `LocalDate`, em detrimento de uma `String`, pelas mesmas razões que motivaram o uso de `LocalDateTime` no subsistema financeiro, dado que este tipo oferece suporte nativo a validações de datas e comparações temporais, em conformidade com a regra dos requisitos que exige que a data de nascimento seja uma data válida.

Os atributos `salario_hora`, `salario_liquido` e `salario_bruto` foram representados como `float`, uma vez que os requisitos definem estes valores como números reais positivos com duas casas decimais. O atributo `horas_extra`, por sua vez, foi mapeado como int porque os requisitos estabelecem que o valor das horas extraordinárias deve ser um número inteiro, e a sua natureza de acumulador mensal — reposto a zero pelo gerente no momento do processamento salarial — reforça que não existe qualquer necessidade de representação fracionária para este campo.

#figure(
  image("images/SFuncionariosANTESSemDAO.png", width: 50%),
  caption: [Diagrama de Classes do SFuncionarios]
)

==== SNotificacoes
O subsistema de notificações respeita a mesma organização adotada nos restantes subsistemas: a interface `ISNotificacoes` estabelece o contrato comportamental, cuja implementação é assegurada pelo `SNotificacoesFacade`. Internamente, este `Facade` recorre a um `Map` de `Notificacao` indexado por `idNotificacao`, o que permite localizar e aceder a qualquer notificação do sistema de forma direta e eficiente.

A entidade central deste subsistema é a classe `Notificacao`, cujos atributos foram derivados dos requisitos e casos de uso que descrevem a geração e gestão de alertas entre os diferentes intervenientes do processo operacional. Os requisitos estabelecem que determinados eventos do sistema (como transições de estado de ordens de serviço ou deteção de peças com defeito) devem gerar automaticamente alertas dirigidos aos funcionários relevantes, e que cada alerta deve conter informação contextual sobre o evento que o originou.

*`Notificacao`*

- `descricao`: `String`
- `id_remetente`: `int`
- `id_destinatario`: `int`
- `data_emissao`: `DateTime`
- `data_horaTratada`: `DateTime` (com valor por omissão _null_)
- `estado`: `EstadoNotificacao` (_enum_)

A `descricao` corresponde ao texto informativo gerado automaticamente pelo sistema no momento em que o evento desencadeador ocorre. O seu conteúdo é determinado pelo contexto da notificação — por exemplo, o identificador de uma ordem de serviço e o estado para o qual transitou, ou a referência de uma peça com defeito detetada durante uma reparação — dispensando qualquer intervenção manual na sua construção.

O `id_remetente` e `id_destinatario` indicam, respetivamente, o utilizador que originou a notificação e aquele a quem ela se destina. A opção por identificadores inteiros em vez de referências diretas a objetos `Utilizador` reduz o acoplamento entre subsistemas, uma vez que o subsistema de notificações não necessita de conhecer a estrutura interna do subsistema de autenticação para cumprir a sua função.

A `data_emissao` regista o momento exato em que a notificação foi gerada, permitindo que os destinatários consultem os alertas com uma noção temporal clara do evento que os originou.

A `data_horaTratada` guarda o momento em que a notificação foi marcada como resolvida, sendo populada apenas aquando dessa ação. O valor inicial nulo (_null_) serve para indicar de forma inequívoca que nenhum tratamento foi efetuado até ao momento.

O `estado` traduz o ciclo de vida da mensagem através de três valores possíveis: `NAOLIDA`, `LIDA` e `TRATADA`. A utilização de um _enum_ para este efeito, em vez de um simples booleano, oferece uma granularidade superior na representação do fluxo, porque permite distinguir não apenas se a notificação está tratada, mas também se o destinatário já a consultou ou se ela permanece por abrir. Esta distinção é relevante para a organização do trabalho do utilizador, libertando-o da necessidade de memorizar quais os alertas que já foram visualizados ou resolvidos.

#figure(
  image("images/SNotificacoesANTESSemDAO.png", width: 50%),
  caption: [Diagrama de Classes do SNotificacoes]
)

==== SOrdensServico
O subsistema de ordens de serviço é o mais complexo do sistema, refletindo a elevada riqueza de estados e transições inerente ao processo de reparação de uma trotinete. A interface `ISOrdersServico` define o contrato comportamental, implementado pelo `SOrdensServicoFacade`, que mantém internamente um `Map` de `OrdemServico` indexado por `idOrdemServico`.

*`OrdemServico`*

- `descricao`: `String`
- `data_criacao`: `LocalDateTime`
- `acessorios`: `List<String>`
- `cliente`: `Cliente`
- `trotinete`: `Trotinete`
- `criador`: `Funcionario`
- `responsavel`: `Funcionario`
- `estado`: `EstadoOS` (_enum_)
- `metodo_pagamento`: `MetodoPagamento` (_enum_)

A classe `OrdemServico` foi concebida para agregar, numa única entidade, todos os dados que vão sendo preenchidos ao longo das diferentes fases de execução. No momento da criação, o processo é automaticamente colocado no estado `PendenteDiagnostico`.

A `descricao` regista detalhadamente os problemas reportados pelo cliente no momento da entrega da trotinete, enquanto a `data_criacao` guarda o momento exato em que a ordem de serviço foi aberta no sistema.

A lista de `acessorios` funciona como um registo de responsabilidade, salvaguardando os pertences entregues pelo utilizador juntamente com o veículo, tais como cadeados ou carregadores.

As referências `cliente` e `trotinete` ligam a ordem diretamente às entidades correspondentes do subsistema `SClientes`. Da mesma forma, as associações `criador` (o funcionário que registou a ordem) e `responsavel` (o colaborador encarregue da sua execução) asseguram a rastreabilidade dos intervenientes operacionais geridos pelo `SFuncionarios`.

O `estado` formaliza todo o ciclo de vida da ordem de serviço através dos valores `PendenteDiagnostico`, `PendenteAprovacaoOrcamento`, `PendenteReparacao`, `AguardarPecas`, `OrcamentoNaoAprovado`, `PendentePagamento`, `Paga` e `Eliminada`. A utilização de um _enum_ para este fim garante que apenas são permitidas transições para estados válidos e previstos no domínio do negócio.

O `metodo_pagamento` define as formas de pagamento aceites pelo sistema (`NUMERARIO`, `MULTIBANCO` e `MBWAY`), sendo preenchido exclusivamente quando a ordem de serviço transita para o estado final `Paga`.

*`Diagnostico`* 

- `descricao`: `String`
- `orcamento`: `float`
- `reparacoes`: `List<Reparacao>`
- `listaPecas`: `List<PecasDoOrcamento>`

A classe `Diagnostico` representa a fase de avaliação técnica da trotinete. Com base nos dados estruturados nesta entidade, o sistema calcula o orçamento e transita a ordem de serviço para o estado `PendenteAprovacaoOrcamento`. Caso o cliente não aprove a proposta, o processo avança para `OrcamentoNaoAprovado`, impossibilitando a realização de qualquer operação adicional sobre ele.

A `descricao` armazena as observações detalhadas e notas técnicas registadas pelo mecânico durante a avaliação física do veículo.

O `orcamento` é determinado de forma automática pelo sistema com base no cruzamento das reparações previstas e das peças identificadas como necessárias.

A lista de `reparacoes` referencia as instâncias do subsistema `SReparacoes` que se prevê serem necessárias para mitigar as avarias e a `listaPecas` agrega instâncias de `PecasDoOrcamento`, onde cada elemento associa uma peça específica do `SStock` a uma quantidade inteira (`int`).

*`Conserto`* 

- `reparacoes`: `List<Reparacao>`
- `listaPecas`: `List<PecasUsadas>`
- `preco_total`: `float`

A classe `Conserto` é instanciada logo após a aprovação do orçamento, momento em que a ordem de serviço transita para o estado `PendenteReparacao`. Esta entidade tem como objetivo central registar o trabalho que foi efetivamente executado no veículo.

A lista de `reparacoes` contém as instâncias de intervenções técnicas que o mecânico executou. Já a `listaPecas` é composta por instâncias de `PecasUsadas`, associando um elemento de `Stock` do subsistema `SStock` a uma quantidade inteira (`int`). Esta associação foca-se diretamente no _stock_ em vez da peça genérica para permitir que as quantidades sejam devidamente descontadas no momento da aplicação.

O `preco_total` é calculado de forma automática com base nas reparações executadas e nas peças efetivamente utilizadas. Se o mecânico precisar de adicionar intervenções ou componentes não previstos no diagnóstico e o valor resultante exceder o orçamento aprovado, o sistema força o retorno ao estado `PendenteAprovacaoOrcamento` para uma nova validação do cliente. Adicionalmente, caso alguma das peças necessárias não esteja disponível, a ordem transita para o estado `AguardarPecas`, bloqueando o progresso do conserto enquanto não houver o _stock_ necessário.

A classe `CheckList` representa a verificação de segurança obrigatória antes da conclusão da reparação. É composta por seis atributos booleanos — `luzes`, `pneus`, `aceleracao`, `travagem`, `visor` e `teste_pratico` — todos com valor por omissão `false`, refletindo diretamente os itens de verificação definidos nos requisitos. Apenas quando todos os atributos estiverem marcados como `true` é que o mecânico pode concluir a reparação, transitando a ordem para o estado PendentePagamento.

Neste momento, regista-se o método de pagamento utilizado e a ordem transita para o estado final `Paga`.

#figure(
  image("images/SOrdensServicoANTESSemDAO.png", width: 100%),
  caption: [Diagrama de Classe do SOrdensServico]
)

==== SReparacoes

*`Reparacao`* 

- `nomenclatura`: `String`
- `descricao`: `String`
- `preco`: `float`
- `disponivel`: `boolean` (com valor por omissão `true`)

A classe `Reparacao` é a entidade central deste subsistema, tendo os seus atributos sido derivados dos casos de uso relativos ao diagnóstico e à execução de reparações. O sistema foi desenhado para que o mecânico possa associar estas intervenções a uma ordem de serviço, permitindo o cálculo automático do orçamento e do custo final do conserto.

A `nomenclatura` corresponde à designação oficial pela qual a reparação é conhecida e pesquisada na plataforma, respondendo diretamente à exigência de procura do utilizador. Já a `descricao` serve para detalhar o conteúdo prático da intervenção, fornecendo ao mecânico informações adicionais e orientações claras sobre o trabalho a realizar.

O `preco` representa o custo unitário do serviço. A opção pelo tipo `float` justifica-se pela necessidade de precisão na representação de valores monetários com casas decimais, sendo este o valor de referência que o sistema utiliza para calcular automaticamente tanto o orçamento inicial como o custo final da reparação.

O atributo `disponivel` indica se a reparação se encontra atualmente ativa no catálogo de serviços oferecidos pela oficina. A utilização de um `booleano` para este efeito permite desativar temporária ou permanentemente um serviço sem o eliminar fisicamente do sistema. Esta abordagem é fundamental para preservar a integridade do histórico das ordens de serviço antigas que já referenciam essa reparação.

#figure(
  image("images/SReparacoesANTESSemDAO.png", width: 90%),
  caption: [Diagrama de Classe do SReparacoes]
)

==== SStock

O subsistema de _stock_ segue a mesma estrutura já estabelecida nos restantes módulos: a interface `ISStock` define o contrato comportamental, cuja implementação é assegurada pelo `SStockFacade`. Dada a complexidade deste subsistema, o `Facade` mantém internamente quatro `Maps` independentes, indexados pelos identificadores de `Fornecedor`, `Peca`, `Stock` e `Encomenda`, refletindo as entidades raiz com responsabilidades distintas no fluxo de mercadorias.

*`Fornecedor`* 

- `nome`: `String`
- `email`: `String`
- `telemovel`: `String`
- `morada`: `String`

A classe `Fornecedor` centraliza os dados de contacto da entidade externa responsável pelo abastecimento do armazém.

O `nome`, o `email`, o `telemóvel` e a `morada` são todos mapeados como _String_ por se tratarem de campos textuais ou identificadores sem semântica aritmética, alinhando-se com as decisões de modelação dos subsistemas anteriores.

A existência desta classe como uma entidade autónoma, referenciada tanto por `Peca` como por `Encomenda`, permite rastrear com precisão a origem de cada referência do catálogo e associar cada pedido de compra ao respetivo fornecedor. Assim, esta estrutura responde diretamente à necessidade regulada nos requisitos de gerir encomendas e registar devoluções.

*`Peca`* 

- `referencia: String`
- `nome`: `String`
- `descricao`: `String`
- `preco_venda`: `float`
- `quantidade_minima`: `int`
- `disponivel`: `boolean`

A classe `Peca` funciona como o catálogo de referências comercializadas pela oficina, contendo uma associação ao `Fornecedor` para identificar a proveniência de cada item.

A `referencia` corresponde ao identificador comercial do componente junto do fornecedor, servindo como o campo oficial de pesquisa estipulado pelos casos de uso. O `nome` e a `descricao` fornecem a designação e a caracterização detalhada do item no catálogo.

O `preco_venda` regista o valor unitário cobrado ao cliente. Por ser um valor próprio da referência e não de um lote específico, a sua definição garante a consistência de preços e impede que a mesma peça seja faturada por valores diferentes consoante a origem do lote de armazenamento.

A `quantidade_minima` define o limiar crítico abaixo do qual o sistema deve gerar automaticamente um alerta para o gestor de _stock_, cumprindo o requisito de notificação preventiva de rutura.

O atributo `disponivel` serve para desativar uma referência do catálogo sem a eliminar fisicamente, salvaguardando a integridade e o histórico de ordens de serviço antigas que já incluam essa peça.

*`Stock`*

- `preco_compra`: `float`
- `quantidade`: `int`
- `estado`: `EstadoStock` (_enum_)

A classe `Stock` representa uma entrada de inventário associada a um lote específico de peças recebido no armazém.

O `preco_compra` grava o custo unitário de aquisição daquele lote em particular. Esta distinção é necessária para o cálculo correto de margens de lucro e para o registo fiel dos movimentos financeiros de compra de cada entrada.

A `quantidade` controla o número de unidades disponíveis no lote, utilizando o tipo `int` por se tratar de um valor discreto e não fracionável.

O estado acompanha o ciclo de vida do lote desde a sua receção. A utilização de um _enum_ para esta propriedade garante a segurança das transições de fluxo. Como mecanismo obrigatório para a gestão de defeitos, sempre que um mecânico reporta uma peça defeituosa durante um conserto, todos os lotes em armazém dessa mesma referência transitam automaticamente para o estado `PossivelDefeito`, ficando indisponíveis para consumo até à validação do gestor.

*`Devolucao`*

- `motivo`: `String`
- `quantidade`: `int`
- `estado`: `EstadoDevolucao` (_enum_)

A classe `Devolucao` concretiza o processo de devolução parcial ou total de unidades de um determinado lote ao seu fornecedor.

O `motivo` armazena a justificação da devolução, transcrevendo a descrição do defeito detetado pelo mecânico conforme exigido pelos requisitos.

A `quantidade` especifica o número exato de unidades abrangidas pelo pedido. O uso deste campo confere granularidade ao processo, permitindo que o gestor de _stock_ distribua as unidades de um único lote por múltiplas devoluções parciais e independentes.

O `estado` adota os valores `PendenteDevolucao`, `Devolvida` e `Invalida`, mapeando o progresso da operação desde a sua abertura até à confirmação de receção pelo fornecedor ou eventual anulação do pedido.

*`Encomenda`*

- `estado`: `EstadoEncomenda` (_enum_)
- `data_criacao`: `LocalDateTime`
- `data_rececao`: `LocalDateTime`

A classe `Encomenda` agrega o pedido de compra formalizado junto de um fornecedor, mantendo a associação a um único `Fornecedor` para garantir a rastreabilidade na origem. A lista de entradas de `Stock` associada é criada inicialmente com o estado `Encomendado` e muda para `Em_Armazem` no momento da entrega, fechando o ciclo de aprovisionamento.

O `estado` utiliza um _enum_ com as opções `Rascunho`, `Enviada` e `Recebida` para refletir as três fases operacionais do processo de compra desenhadas nos requisitos.

A `data_criacao` e a `data_rececao` registam os momentos exatos de abertura do pedido e da sua chegada física ao armazém, respetivamente. Estes registos temporais são fundamentais para auditar o ciclo de abastecimento e calcular os prazos de entrega dos fornecedores.

#figure(
  image("images/SStockANTESSemDAO.png", width: 100%),
  caption: [Diagrama de Classe do SStock]
)

== Modelo de Dados
=== Introdução e Enquadramento
Os diagramas de classes apresentados nas secções anteriores foram elaborados com foco exclusivo na estrutura lógica da camada de negócio, abstraindo deliberadamente os mecanismos de persistência de modo a manter a representação centrada nas regras de domínio. Esta separação intencional entre lógica de negócio e persistência é uma prática recomendada na engenharia de software, pois permite que os modelos de domínio evoluam independentemente das decisões de armazenamento, tornando o sistema mais coeso, testável e adaptável a futuras mudanças tecnológicas.

Contudo, a implementação de um sistema funcional exige que essa estrutura lógica seja complementada com uma estratégia de persistência concreta que garanta a integridade, consistência e durabilidade dos dados ao longo do tempo. A transição do modelo de domínio para o modelo de dados representa um momento crítico no processo de desenvolvimento, pois é nesta fase que as abstrações do negócio se tornam estruturas físicas armazenadas em disco, com todas as implicações que daí decorrem em termos de desempenho, escalabilidade e manutenção.

Dado que a equipa já possuía experiência consolidada com _JDBC_ e _MySQL_, optou-se por implementar a persistência através do padrão _Data Access Object_ (_DAO_), em que cada _DAO_ é responsável pelas operações de leitura e escrita relativas a uma entidade específica do domínio. Esta abordagem centraliza a lógica de acesso a dados em componentes bem definidos, facilita a substituição futura do mecanismo de persistência sem afetar a lógica de negócio, e promove a reutilização de código de acesso a dados em múltiplos contextos da aplicação.

Para manter a coerência com o modelo sem persistência, cada _DAO_ implementa a interface `java.util.Map`, expondo à camada de negócio exatamente as mesmas operações de consulta e inserção que esta já utilizava — o que permitiu preservar a consistência do modelo e minimizar o impacto da introdução da persistência na lógica de negócio existente. Esta decisão de design revelou-se particularmente vantajosa, uma vez que a camada de negócio não necessitou de ser modificada para suportar a persistência, bastando substituir as implementações em memória pelos respetivos _DAOs_ no momento da inicialização do sistema.

A adoção do _MySQL_ como sistema gestor de base de dados relacional (SGBDR) foi motivada por múltiplos fatores: maturidade e estabilidade comprovadas, forte integração com o ecossistema _Java_ via _JDBC_, suporte robusto a transações _ACID_, e familiaridade da equipa com a tecnologia. A integração com _Java_ é feita diretamente através da _API JDBC_, que permite o controlo granular de consultas _SQL_, gestão de transações e tratamento de exceções de base de dados.

=== Estrutura e Implementação dos _DAOs_

Conforme ilustrado no diagrama de classes da camada de dados, cada entidade persistível possui um _DAO_ correspondente que implementa a interface `java.util.Map<Integer, Entidade>`. Esta interface paramétrica define as operações _CRUD_ fundamentais: `get(Integer id)` para leitura, `put(Integer id, Entidade e)` para inserção e atualização, `remove(Integer id)` para eliminação, e `containsKey(Integer id)` para verificação de existência.

Cada _DAO_ implementa o padrão _Singleton_, garantindo que existe apenas uma instância por tipo de _DAO_ durante o ciclo de vida da aplicação. Este padrão é implementado através de um campo estático privado `singleton` e de um método de fábrica público `getInstance()` que cria a instância na primeira invocação e a devolve nas subsequentes. Esta abordagem evita a criação de múltiplas ligações desnecessárias à base de dados e centraliza a gestão do pool de ligações.

O diagrama de componentes da arquitetura evidencia como os _DAOs_ se integram na estrutura de três camadas: o componente `EcoRideCD` (camada de dados) agrega todos os _DAOs_, que são acedidos pelos respetivos subsistemas da camada de negócio (`EcoRideLN`) através das interfaces `Map` parametrizadas. Esta estrutura garante que a camada de negócio depende apenas de abstrações (as interfaces `Map`), e não de implementações concretas, respeitando o princípio de inversão de dependências.

#figure(
  image("img-dados/componentes.png", width: 91%),
  caption: [Diagrama de componentes completo com as três camadas]
)

=== _DAOs_ Implementados
Em termos da implementação dos _DAOs_, a principal diferença que será facilmente observável em todos os diagramas será a transformação dos _Maps_ internos de cada Facade — utilizados durante a execução do sistema para garantir acesso rápido às entidades — em _DAOs_ (_Data Access Objects_), centralizando em cada um deles a responsabilidade pelo acesso e manipulação dos dados persistidos da entidade correspondente.

No que diz respeito às associações entre entidades de subsistemas distintos, a abordagem adotada consistiu em guardar, na classe que referencia a entidade externa, apenas o identificador único dessa entidade em vez de uma referência direta ao objeto. A reconstituição da associação em memória é então feita recorrendo a instâncias _singleton_ dos _DAOs_ dos subsistemas envolvidos, garantindo que existe um único ponto de acesso à persistência de cada entidade e evitando inconsistências decorrentes de múltiplas instâncias concorrentes do mesmo _DAO_. Esta abordagem mantém o baixo acoplamento entre subsistemas já estabelecido na fase de design, uma vez que cada subsistema continua a não depender da estrutura interna dos outros, interagindo apenas através dos identificadores e dos contratos expostos pelos respetivos _DAOs_.

==== SAutenticacao
No subsistema de autenticação, o `Map` interno do SAutenticacaoFacade foi substituído por um UtilizadorDAO, singleton responsável pela persistência dos utilizadores. A alteração mais relevante na classe Utilizador decorre da associação que anteriormente existia com a classe Funcionario: dado que a persistência obriga a armazenar apenas dados primitivos e identificadores, a referência direta ao objeto Funcionario foi substituída pelo atributo idFuncionario, do tipo int. A reconstituição desta associação em memória é feita através do FuncionariosDAO, igualmente mantido como singleton no Utilizador, permitindo obter o Funcionario correspondente sempre que necessário sem criar dependências estruturais entre subsistemas.

#figure(
  image("images/SAutenticacaoANTESComDAO.png", width: 91%),
  caption: [Diagrama de classe do SAutenticacao com DAO]
)
==== SClientes

No subsistema de clientes, os dois _Maps_ internos do SClientesFacade foram substituídos por dois _DAOs_: o ClienteDAO e o TrotineteDAO. As alterações às classes refletem a mesma necessidade de substituir referências diretas a objetos por identificadores.

Na classe Cliente, a lista de objetos Trotinete foi substituída por uma lista de inteiros codsTrotinetes, que guarda os identificadores das trotinetes pertencentes ao cliente. A reconstituição das trotinetes em memória é feita através do TrotineteDAO, mantido como singleton na classe. Na classe Trotinete, a referência direta ao objeto Cliente foi substituída pelo atributo codCliente, do tipo int, e a lista de objetos OrdemServico foi substituída por uma lista de inteiros codsOrdensServico. A reconstituição das ordens de serviço é feita através do OrdemServicoDAO, igualmente mantido como _singleton_ na Trotinete, assegurando que o histórico de intervenções de cada equipamento pode ser reconstituído sem que o subsistema de clientes dependa da estrutura interna do subsistema de ordens de serviço.

#figure(
  image("images/SClientesANTESComDAO.png", width: 100%),
  caption: [Diagrama de classe do SClientes com DAO]
)

==== SFinanceiro
No subsistema financeiro, a transformação foi direta: o Map interno do SFinanceiroFacade foi substituído pelo MovimentoFinanceiroDAO, singleton responsável pela persistência dos movimentos financeiros. Uma vez que a classe MovimentoFinanceiro não possui associações a entidades de outros subsistemas — os seus atributos são todos tipos primitivos ou enumerados —, não foi necessária qualquer alteração à sua estrutura para suportar a persistência.

#figure(
  image("images/SFinanceiroANTESComDAO.png", width: 60%),
  caption: [Diagrama de classe do SFinanceiro com DAO]
)

==== SFuncionarios
No subsistema de funcionários, a transformação foi igualmente direta: o Map interno do SFuncionariosFacade foi substituído pelo FuncionarioDAO, singleton responsável pela persistência dos colaboradores. Tal como no caso anterior, a classe Funcionario não possui associações a entidades de outros subsistemas, pelo que a sua estrutura não sofreu alterações para efeitos de persistência.

#figure(
  image("images/SFuncionariosANTESComDAO.png", width: 50%),
  caption: [Diagrama de classe do SFuncionarios com DAO]
)


==== SNotificacoes
No subsistema de notificações, a transformação seguiu a mesma lógica dos subsistemas anteriores: o Map interno do SNotificacoesFacade foi substituído por um NotificacaoDAO, singleton responsável pela persistência das notificações. A classe Notificacao não sofreu qualquer alteração estrutural relativamente ao diagrama de classes original.

#figure(
  image("images/SNotificacoesANTESComDAO.png", width: 70%),
  caption: [Diagrama de classe do SNotificacoes com DAO]
)

==== SOrdensServico
No subsistema de ordens de serviço, apesar da elevada quantidade de classes que o compõem, o único _DAO_ introduzido foi o OrdemServicoDAO, singleton responsável pela persistência das ordens de serviço. Esta situação decorre do facto de a entidade raiz gerida pelo SOrdensServicoFacade ser exclusivamente a OrdemServico — as restantes classes, como Diagnostico, Conserto, PecasDoOrcamento e CheckList, são entidades dependentes que existem apenas no contexto de uma ordem de serviço e são persistidas conjuntamente com ela.

As alterações estruturais às classes seguiram a mesma lógica já aplicada nos subsistemas anteriores: as referências diretas a objetos de outros subsistemas foram substituídas por identificadores inteiros, com os _DAOs_ correspondentes mantidos como singletons para permitir a reconstituição das associações em memória.

Na classe OrdemServico, as referências ao Cliente, à Trotinete e ao Funcionario responsável foram substituídas pelos atributos codCliente, codTrotinete e codResponsavel, todos do tipo int. Os _DAOs_ ClienteDAO, TrotineteDAO e FuncionariosDAO são mantidos como singletons na classe para reconstituir estas associações sempre que necessário.

Na classe Diagnostico, a referência ao Funcionario que realizou o diagnóstico foi substituída pelo atributo codMecanico, do tipo int, e os _DAOs_ ReparacaoDAO e PecaDAO são mantidos como singletons. A lista de reparações previstas passou a ser representada como uma lista de inteiros cod_reparacoes, e a lista listaPecas de PecasDoOrcamento mantém a sua estrutura, sendo que cada instância passou a guardar o identificador da peça associada através do atributo codPecas, do tipo int, recorrendo ao PecaDAO para a sua reconstituição.

Na classe Conserto, a mesma abordagem foi aplicada: a referência ao mecânico responsável foi substituída por codMecanico, do tipo int, a lista de reparações executadas passou a ser uma lista de inteiros cod_reparacoes, e a lista de peças utilizadas passou a ser uma lista de inteiros cod_pecas, recorrendo ao StockDAO para reconstituir os lotes de _stock_ correspondentes.

A classe CheckList não possui associações a entidades externas, pelo que não sofreu alterações estruturais para efeitos de persistência, mantendo os seus atributos booleanos inalterados.

#figure(
  image("images/SOrdensServicoANTESComDAO.png", width: 100%),
  caption: [Diagrama de classe do SOrdensServico com DAO]
)

==== SReparacoes
No subsistema de reparações, a transformação foi direta: o _Map_ interno do SReparacoesFacade foi substituído pelo ReparacaoDAO, singleton responsável pela persistência do catálogo de serviços. À semelhança do que aconteceu nos subsistemas financeiro e de funcionários, a classe Reparacao não possui associações a entidades de outros subsistemas, pelo que a sua estrutura manteve-se inalterada.

#figure(
  image("images/SReparacoesANTESComDAO.png", width: 85%),
  caption: [Diagrama de classe do SReparacoes com DAO]
)


==== SStock
No subsistema de _stock_, a transformação seguiu a mesma lógica, mas com maior abrangência dada a quantidade de entidades raiz independentes que o compõem. Os quatro Maps internos do SStockFacade foram substituídos pelos respetivos _DAOs_: FornecedorDAO, PecaDAO, StockDAO, DevolucaoDAO e EncomendaDAO.

A classe Fornecedor não possui associações a entidades externas, pelo que a sua estrutura não sofreu alterações.

Na classe Peca, a referência direta ao objeto Fornecedor foi substituída pelo atributo codFornecedor, do tipo int, sendo o FornecedorDAO mantido como singleton para reconstituir a associação em memória. É ainda de notar que a classe passou a incluir o atributo quantidade, do tipo int, que agrega o total de unidades disponíveis em _stock_ para aquela referência, e que o atributo stock_minimo corresponde ao limiar anteriormente designado por quantidade_minima.

Na classe Stock, a referência à Peca foi substituída pelo atributo codPeca, do tipo int, com o PecaDAO mantido como singleton. Foi ainda adicionado o atributo data_chegada, do tipo LocalDateTime, que regista o momento de receção do lote em armazém, permitindo rastrear temporalmente cada entrada de inventário.

Na classe Devolucao, a referência ao lote de Stock associado foi substituída pelo atributo codStock, do tipo int, com o StockDAO mantido como singleton para reconstituir a associação.

Na classe Encomenda, a referência ao Fornecedor foi substituída pelo atributo codFornecedor, do tipo int, e a lista de entradas de Stock foi substituída por uma lista de inteiros codEntradasStock, recorrendo ao StockDAO para reconstituir os lotes correspondentes. Os atributos data_receção e data_envio, do tipo LocalDateTime, mantêm-se para rastrear o ciclo de vida do pedido de compra.

#figure(
  image("images/SStockANTESComDAO.png", width: 100%),
  caption: [Diagrama de classe do SStock com DAO]
)


==== Arquitetura com _DAOs_
Assim, o sistema resultante, compreende os seguintes _DAOs_, cada um responsável pela persistência de uma entidade específica:

- *FuncionarioDAO*: gestão de funcionários, com operações de pesquisa por NIF e por nome;
- *UtilizadorDAO*: gestão de utilizadores e autenticação, com verificação de credenciais;
- *ClienteDAO*: gestão de clientes, com pesquisa por NIF e por nome;
- *TrotineteDAO*: gestão de trotinetes, com listagem por cliente;
- *OrdemServicoDAO*: o _DAO_ mais complexo, responsável pela gestão transacional do ciclo completo das ordens de serviço;
- *ReparacaoDAO*: gestão do catálogo de serviços de reparação;
- *StockDAO*: gestão de entradas de _stock_, com consultas de disponibilidade por peça;
- *PecaDAO*: gestão do catálogo de peças;
- *FornecedorDAO*: gestão de fornecedores;
- *EncomendaDAO*: gestão de encomendas a fornecedores;
- *DevolucaoDAO*: gestão de devoluções ao fornecedor;
- *MovimentoFinanceiroDAO*: gestão de movimentos financeiros, com consultas por período e por tipo;
- *NotificacoesDAO*: gestão de notificações, com consultas por destinatário e por estado.

#figure(
  image("img-dados/daos.png"),
  caption: [DAO's modelados para o sistema]
)

=== Mudanças arquiteturais

Durante a fase de implementação, e à medida que se progredia da modelação conceptual para a concretização do sistema, tornou-se evidente que alguns dos diagramas de classes anteriormente apresentados padeciam de limitações estruturais que comprometiam a sua viabilidade enquanto base para uma camada de persistência robusta. Estas limitações manifestaram-se sobretudo em duas frentes: na ausência de hierarquias que organizassem entidades com características comuns mas comportamentos especializados, e em associações que, embora corretas do ponto de vista do funcionamento em memória, se revelavam insuficientes para representar inequivocamente, no modelo de dados, as entidades externas referenciadas por algumas classes.

A constatação destas fragilidades motivou um processo de redesenho de vários subsistemas, com o objetivo de preparar o sistema para uma implementação com persistência sólida, normalizada e preparada para acomodar evoluções futuras. As alterações introduzidas centraram-se na introdução de hierarquias de especialização — sempre que se identificou que uma mesma entidade conceptual servia múltiplos contextos com atributos próprios — e na reformulação de associações, de modo a tornar explícita, ao nível do modelo de dados, a entidade exterior a que cada referência se aplica.

Estas reformulações não constituíram uma rutura com as decisões arquiteturais tomadas anteriormente, mas sim um refinamento das mesmas. Os princípios de baixo acoplamento entre subsistemas, separação de responsabilidades e utilização de `Facades` como ponto de entrada para cada subsistema mantiveram-se inalterados. O que se reformulou foi a estrutura interna de algumas classes e das suas relações, de forma a que o modelo relacional resultante respeitasse integralmente as formas normais, beneficiasse das garantias de integridade referencial oferecidas pelo motor de base de dados e oferecesse uma base sólida para a introdução futura de novos tipos de movimentos financeiros, notificações ou outras entidades que beneficiem das hierarquias agora estabelecidas.

Nas subsecções seguintes detalham-se, para os subsistemas que foram necessários ser alterados, as alterações introduzidas, as motivações que as justificam e o impacto que tiveram tanto na estrutura das classes como na sua tradução para o modelo lógico da base de dados.

==== SAutenticacao

No subsistema de autenticação, a alteração mais relevante introduzida durante a reformulação consistiu na adição do atributo `identificador`, do tipo `String`, à classe `Utilizador`. Este novo atributo passou a desempenhar o papel de credencial de acesso ao sistema, substituindo o `id` numérico como elemento de identificação utilizado no momento da autenticação.

A motivação para esta alteração reside na separação clara entre dois conceitos que, na versão anterior, se encontravam fundidos: por um lado, a identificação computacional única de cada utilizador, garantida pelo `id` inteiro e utilizada pelo sistema nas suas operações internas e nas relações entre entidades; por outro, a credencial pública pela qual cada utilizador se identifica perante o sistema no momento do _login_. A utilização de um `id` numérico, atribuído sequencialmente pelo motor de persistência, como credencial de acesso revelava-se pouco prática do ponto de vista do utilizador final e expunha desnecessariamente um identificador interno do sistema. Assim, garantiu-se também o cumprimento dos requisitos, visto que este identificador é determinado pelo gerente no momento da criação de um novo funcionário.

#figure(
  image("SAutenticacaoDEPOISComDAOS.png", width: 100%),
  caption: [Diagrama de Classes do SAutenticacao Final]
)

==== SFinanceiro

No subsistema financeiro, a reformulação introduziu duas alterações estruturais relevantes.

A primeira foi a introdução de uma *hierarquia de especialização* sobre a classe `MovimentoFinanceiro`. Esta classe, anteriormente única, foi mantida como classe base — agregando os atributos comuns a todos os movimentos: `id`, `valor`, `data`, `descricao` e `tipo` — passando a ser estendida por três subclasses especializadas: `MovimentoFuncionario`, que regista o `codFuncionario` do colaborador a quem o movimento respeita; `MovimentoReparacao`, que regista o `codReparacao` da reparação associada; e `MovimentoPeca`, que regista o `codStock` da entrada de _stock_ correspondente.

A motivação para esta hierarquia decorre diretamente das exigências do modelo de dados. Na versão anterior, qualquer associação contextual de um movimento financeiro a outra entidade do sistema teria de ser representada de forma ambígua — por exemplo, através de um único atributo inteiro cujo significado dependeria do `TipoMovimento` —, o que comprometeria a clareza do modelo e impossibilitaria o motor de base de dados de garantir, por si só, a integridade referencial das associações.

Esta organização traduz-se no modelo relacional numa *herança de tabelas* (_class table inheritance_): uma tabela base `MovimentoFinanceiro` com os atributos comuns, e uma tabela por subclasse com o atributo de referência específico, ligada à tabela base por uma chave estrangeira que é simultaneamente chave primária. O campo `tipo` na tabela base serve de *discriminador*, permitindo ao DAO instanciar a subclasse Java adequada em tempo de execução. O `ON DELETE CASCADE` nas tabelas filhas garante que apagar um registo da tabela base remove automaticamente o registo da tabela específica, sem necessidade de lógica adicional na camada de aplicação.

O esquema seguinte reflete diretamente esta decisão: a tabela `MovimentoFinanceiro` concentra os atributos partilhados e o campo `tipo` que discrimina o subtipo; cada tabela filha contém apenas a chave primária — que é simultaneamente chave estrangeira para a tabela base — e o campo de referência específico da sua natureza, garantindo que a integridade referencial é imposta pelo próprio motor de base de dados.

```sql
CREATE TABLE IF NOT EXISTS MovimentoFinanceiro (
    id        INT          NOT NULL AUTO_INCREMENT,
    valor     FLOAT        NOT NULL,
    data      DATETIME     NOT NULL,
    descricao VARCHAR(255),
    tipo      ENUM('Salario', 'GastoPecas', 'LucroMaoObra', 'LucroVendaPecas') NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS MovimentoFuncionario (
    id             INT NOT NULL,
    codFuncionario INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)             REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codFuncionario) REFERENCES Funcionario(id)
);

CREATE TABLE IF NOT EXISTS MovimentoReparacao (
    id           INT NOT NULL,
    codReparacao INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)           REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codReparacao) REFERENCES Reparacao(id)
);

CREATE TABLE IF NOT EXISTS MovimentoPeca (
    id       INT NOT NULL,
    codStock INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id)       REFERENCES MovimentoFinanceiro(id) ON DELETE CASCADE,
    FOREIGN KEY (codStock) REFERENCES Stock(id) ON DELETE CASCADE
);
```

Com a introdução das subclasses, cada tipo de movimento passa a ter a sua própria tabela e o seu próprio campo de referência, tipado e direcionado à entidade correta. Esta organização permite que o motor de base de dados imponha restrições de chave estrangeira específicas a cada subtipo, garantindo que um `MovimentoFuncionario` só pode referenciar funcionários existentes, um `MovimentoReparacao` só pode referenciar reparações existentes, e assim sucessivamente. Adicionalmente, a definição de ações em cascata sobre estas chaves estrangeiras assegura que a eliminação de uma entidade referenciada — apenas no caso de uma entrada de _stock_ — acarreta automaticamente a eliminação dos movimentos financeiros que dela dependiam, preservando a consistência do registo histórico sem necessidade de intervenção manual. No entanto, é relevante mencionar que tanto para um funcionário como para uma reparação, caso um deles seja apagado da base de dados, todos os movimentos que eles tenham originado continuam guardados; por outro lado, no caso do _stock_, como o `MovimentoPeca` apenas referencia o `Stock`, foi necessário ao nível do `Facade` garantir que os `MovimentosFinanceiros` fossem apagados, visto que o motor da base de dados não garantia isso.

A segunda alteração foi a introdução da classe `AnaliseFinanceira`, que funciona como um _Data Transfer Object_ (DTO) para a transmissão de dados financeiros agregados entre a camada de negócio e a camada de apresentação. Esta classe expõe atributos calculados — `receitas`, `despesas`, `saldo`, `movimentos` e `lista_tipos`, este último contendo a distribuição dos valores por categoria de movimento. O cálculo destes valores passou a ser efetuado integralmente ao nível do `SFinanceiroFacade`, libertando a camada de apresentação da responsabilidade de processar movimentos individuais para produzir indicadores agregados. Esta separação reforça o princípio de que a lógica de negócio deve residir na camada apropriada, garantindo coerência nos cálculos independentemente do consumidor da informação e simplificando significativamente o desenvolvimento da interface.

#figure(
  image("SFinanceiroDEPOISComDAOS.png", width: 100%),
  caption: [Diagrama de Classes do SFinanceiro Final]
)

==== SNotificacoes

No subsistema de notificações, a reformulação introduziu uma *hierarquia de especialização* sobre a classe `Notificacao`, seguindo a mesma lógica aplicada aos movimentos financeiros. A classe `Notificacao` foi mantida como classe base, agregando os atributos comuns a qualquer alerta gerado pelo sistema — `id`, `data_emissao`, `id_remetente`, `id_destinatario`, `data_horaTratada`, `descricao` e `estado` —, passando a ser estendida por duas subclasses especializadas: `NotificacaoOS`, que regista o `id_os` da ordem de serviço à qual o alerta diz respeito, e `NotificacaoStock`, que regista o `id_peca` da peça associada ao alerta.

A motivação para esta hierarquia é análoga à apresentada no subsistema financeiro. Na versão anterior, o contexto de cada notificação ficava implícito no conteúdo textual da `descricao`, sem qualquer ligação formal entre a notificação e a entidade que a originou. Esta abordagem era funcionalmente suficiente para apresentar o alerta ao utilizador, mas insuficiente para o modelo de dados: o motor de base de dados não tinha qualquer forma de garantir que uma notificação relativa a uma ordem de serviço referenciasse uma ordem de serviço efetivamente existente, nem de manter a coerência do registo de notificações face à eliminação das entidades originárias.

Com a introdução das subclasses, cada tipo de notificação passa a ter a sua própria tabela e o seu próprio campo de referência tipado, com restrições de chave estrangeira específicas e ações em cascata que asseguram que a eliminação de uma ordem de serviço ou de uma peça acarreta automaticamente a eliminação das notificações que a elas se referem, mantendo a consistência do registo de alertas sem necessidade de intervenção manual.

Esta organização hierárquica oferece ainda a vantagem de tornar trivial a introdução futura de novos tipos de notificações: bastará criar uma nova subclasse com o campo de referência apropriado, sem necessidade de alterar a estrutura da classe base ou a lógica de tratamento dos restantes tipos de alertas — uma aplicação direta do *princípio aberto/fechado* ao desenho do subsistema.

#figure(
  image("SNotificacoesDEPOISComDAO.png", width: 100%),
  caption: [Diagrama de Classes do SNotificacoes Final]
)

==== SOrdensServico

No subsistema de ordens de serviço, dada a sua complexidade intrínseca, as alterações introduzidas durante a reformulação foram igualmente as mais extensas. Quatro mudanças estruturais merecem destaque.

A primeira foi a introdução da classe `Registo`, que passou a agregar os dados recolhidos pela secretária no momento da abertura da ordem de serviço: `dataCriacao`, `codTrotinete`, `codCliente`, `codCriador`, `descricao` e `acessorios`. Estes atributos, na versão anterior, encontravam-se diretamente na classe `OrdemServico`. A sua extração para uma classe dedicada reforça a separação de responsabilidades dentro do subsistema, isolando os dados administrativos de registo dos dados operacionais da ordem de serviço propriamente dita e tornando explícita, ao nível do modelo, a fase do processo a que cada conjunto de informação corresponde. Os acessórios, que anteriormente eram representados como uma lista de `String`, passaram a ser representados como um único campo textual, simplificando a sua persistência sem perda de informação relevante.

A segunda alteração consistiu na promoção do atributo `codMecanico` à própria classe `OrdemServico`. Na versão anterior, o mecânico responsável pelo trabalho era registado independentemente em `Diagnostico` e em `Conserto`. Esta abordagem, embora flexível, complicava o caso comum em que o mesmo mecânico é responsável por ambas as fases. Ao colocar `codMecanico` ao nível da `OrdemServico`, esta atribuição passa a ser feita uma única vez e a aplicar-se transversalmente, simplificando o fluxo operacional sem comprometer a rastreabilidade da intervenção. No modelo relacional, este campo é definido como `NULL` permitido na tabela `OrdemServico`, uma vez que uma OS começa sem mecânico atribuído — o registo do mecânico é efetuado no momento do diagnóstico.

A terceira alteração diz respeito à representação das peças no `Diagnostico` e no `Conserto`. Na versão anterior, as peças eram modeladas através de classes intermédias (`PecasDoOrcamento` e `PecasUsadas`) que associavam uma peça ou um lote de _stock_ a uma quantidade. Esta estrutura foi substituída por estruturas do tipo _Map_: `pecasOrcamento` no `Diagnostico`, mapeando `codPeca` para quantidade, e `stocksUsados` no `Conserto`, mapeando `codStock` para quantidade. No modelo relacional, estas coleções ordenadas são representadas por tabelas de junção com um campo `ordem` inteiro que preserva a posição na lista original — a *chave primária composta* `(idEntidade, ordem)` impede valores duplicados e permite ao DAO reconstituir a lista na ordem correta através de `ORDER BY ordem`. O `ON DELETE CASCADE` nestas tabelas de junção elimina automaticamente todas as entradas quando a entidade pai é apagada. As listas `cod_reparacoes` mantêm-se como simples listas de inteiros, uma vez que não está associada a cada reparação uma quantidade — apenas a sua presença ou ausência na intervenção.

A quarta alteração foi a introdução da classe `Pagamento`, que substitui o anterior atributo simples `metodo_pagamento` da `OrdemServico`. Esta nova classe agrega o `metodo` (instância do _enum_ `Metodo_Pagamento`), a `dataPagamento`, o booleano `clienteNotificado` — que permite à secretária controlar se já entrou em contacto com o cliente para comunicar a conclusão da ordem de serviço — e a `dataNotificacao`, que regista o momento em que esse contacto foi efetuado. Esta extração para uma classe dedicada permite tratar o pagamento como uma fase autónoma do ciclo de vida da ordem de serviço, com a sua própria informação contextual, em vez de o reduzir a um simples atributo _enum_. A introdução do campo `clienteNotificado` reflete uma necessidade operacional concreta: libertar a secretária de ter de memorizar quais os clientes que já foram contactados, à semelhança do mecanismo já adotado no subsistema de notificações.

Por fim, o _enum_ `EstadoOS` foi enriquecido com o estado `ClienteNotificado`, que reflete a nova etapa do processo introduzida pela classe `Pagamento`. No modelo relacional, o campo `estado` da tabela `OrdemServico` é definido como um `ENUM` _MySQL_ que enumera explicitamente todos os estados válidos do ciclo de vida, *garantindo integridade ao nível do motor de base de dados*: qualquer tentativa de inserir ou atualizar um estado inválido é rejeitada, mesmo que a validação da camada de negócio seja contornada. Os valores do `ENUM` correspondem exatamente aos nomes da enumeração Java `EstadoOS`, o que simplifica a serialização e desserialização sem conversão adicional. 

A tabela seguinte espelha diretamente este modelo: o campo `estado` enumera todos os estados possíveis, o campo `codMecanico` é `NULL` permitido para refletir que a atribuição do mecânico ocorre apenas no momento do diagnóstico, e as chaves estrangeiras para `Trotinete`, `Cliente` e `Funcionario` formalizam as associações que na versão anterior do diagrama eram apenas referências implícitas por identificador.

```sql
CREATE TABLE IF NOT EXISTS OrdemServico (
    id           INT      NOT NULL AUTO_INCREMENT,
    descricao    TEXT,
    data_criacao DATETIME NOT NULL,
    codTrotinete INT      NOT NULL,
    codCliente   INT      NOT NULL,
    codCriador   INT      NOT NULL,
    codMecanico  INT      NULL,
    estado       ENUM(
                     'PendenteReparacao',
                     'PendenteDiagnostico',
                     'PendenteAprovacaoOrcamento',
                     'PendentePagamento',
                     'Paga',
                     'OrcamentoNaoAprovado',
                     'AguardarPecas',
                     'Eliminada',
                     'ClienteNotificado'
                 ) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codTrotinete) REFERENCES Trotinete(id),
    FOREIGN KEY (codCliente)   REFERENCES Cliente(id),
    FOREIGN KEY (codCriador)   REFERENCES Funcionario(id),
    FOREIGN KEY (codMecanico)  REFERENCES Funcionario(id)
);
```

Também é relevante mencionar que os _Maps_ presentes tanto no `Diagnostico` como no `Conserto` foram modelados da forma como estão no diagrama para manter um aspeto minimalista e mais organizado (comparativamente aos anteriores).

#figure(
  image("SOrdensServicoDEPOISComDAO.png", width: 100%),
  caption: [Diagrama de Classes do SOrdensServico Final]
)

==== SStock

No subsistema de _stock_, a reformulação introduziu várias alterações distribuídas pelas diferentes entidades que o compõem, motivadas tanto por lacunas identificadas durante a implementação como pela necessidade de suportar fluxos operacionais que a versão anterior não contemplava de forma adequada.

Na classe `Peca`, foram adicionados quatro novos atributos — `marca`, `nome`, `descricao` e `garantia` — que haviam sido omitidos na versão inicial dos diagramas por lapso da equipa de desenvolvimento. Os três primeiros completam a caracterização comercial da peça, permitindo identificá-la de forma rica perante o utilizador e suportando as funcionalidades de pesquisa do catálogo. O atributo `garantia`, do tipo `int`, representa o período em dias durante o qual a peça é coberta pela garantia do fornecedor, sendo este valor utilizado para calcular automaticamente o término da garantia de cada lote individual.

Na classe `Stock`, foi adicionado o atributo `garantia`, do tipo `LocalDate`, que regista a data de término da garantia da entrada de _stock_. Este valor é calculado no momento da chegada do lote ao armazém, somando o número de dias de garantia definido na `Peca` à `data_chegada` do lote. A representação da data efetiva de término — em vez do simples período em dias — permite consultas diretas sobre o estado da garantia de cada lote sem necessidade de cálculos repetidos a cada consulta, e permite igualmente determinar com precisão a cobertura de cada unidade física, independentemente de alterações futuras ao valor da garantia definido ao nível da peça.

Na classe `Encomenda`, a lista de entradas de _stock_ — anteriormente representada por uma associação visualmente complexa ao nível do diagrama — passou a ser representada como o atributo `codStocks` do tipo `List<Integer>`. Esta alteração é puramente representacional, motivada pelo objetivo de simplificar a leitura do diagrama, mantendo intacta a semântica da associação.

Na classe `Defeito`, foi adicionado o atributo `estadoAnterior`, instância do enumerado `EstadoStock`. Esta adição responde a uma necessidade operacional concreta identificada durante a implementação: dado que um lote de _stock_ pode encontrar-se em diferentes estados ao longo do seu ciclo de vida — `StockEmArmazem`, `StockEncomendado`, entre outros — quando esse lote é assinalado como defeituoso e transita para `StockComPossivelDefeito`, é necessário preservar a informação sobre o estado em que se encontrava previamente. Esta informação é indispensável para o caso em que a análise posterior do defeito conclui pela invalidade do reporte: o lote deverá então regressar ao estado em que se encontrava antes da deteção do alegado defeito, o que só é possível se o sistema souber inequivocamente qual era esse estado.

Por fim, no enumerado `EstadoDevolucao` foi acrescentado o valor `Enviada`, que se insere entre os estados `StockPendenteDeDevolucao` e `Devolvida`. Esta granularidade adicional permite distinguir a fase em que a devolução foi efetivamente despachada para o fornecedor da fase em que este confirmou a sua receção, oferecendo capacidade de rastreamento mais detalhada sobre o progresso de cada processo de devolução. No modelo relacional, o ciclo de vida do _stock_ é igualmente modelado através de um `ENUM` _MySQL_ no campo `estado` da tabela `Stock`, com os sete estados possíveis definidos explicitamente. O campo `data_chegada` é `NULL` para _stocks_ em estado `StockEncomendado` — peças que foram encomendadas mas ainda não chegaram ao armazém — e o campo `garantia` é calculado e preenchido no momento da chegada do lote. 

A tabela seguinte traduz estas decisões: o `ENUM` do campo `estado` enumera os sete estados do ciclo de vida, os campos `data_chegada` e `garantia` são `NULL` permitido para acomodar os lotes ainda não rececionados, e a chave estrangeira para `Peca` formaliza a associação entre cada entrada de _stock_ e a referência de peça a que pertence.

```sql
CREATE TABLE IF NOT EXISTS Stock (
    id           INT   NOT NULL AUTO_INCREMENT,
    preco_compra FLOAT NOT NULL,
    codPeca      INT   NOT NULL,
    data_chegada DATE  NULL,
    quantidade   INT   NOT NULL,
    garantia     DATE  NULL,
    estado       ENUM(
                     'StockEncomendado',
                     'StockEmArmazem',
                     'StockComPossivelDefeito',
                     'StockPendenteDeDevolucao',
                     'StockEnviadoParaFornecedor',
                     'StockDevolvidoFornecedor',
                     'StockinvalidoParaDevolucao',
                     'StockUsadoConserto'
                 ) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codPeca) REFERENCES Peca(id)
);
```

#figure(
  image("SStockDEPOISComDAO.png", width: 100%),
  caption: [Diagrama de Classes do SStock Final]
)


=== Arquitetura Final com _DAOs_ 

#figure(
  image("EcoRideCD_semOP.png",width: 100%),
  caption: [Arquitetura final da Camada de Dados]
)

=== Processo de Desenho da Camada de Persistência
O processo de desenho da camada de dados não foi uma mera transposição automática do modelo de domínio para tabelas relacionais. Exigiu decisões ponderadas sobre como representar hierarquias, gerir associações muitos-para-muitos, modelar tipos enumerados, e garantir a integridade referencial entre entidades. As secções seguintes documentam este processo, detalhando as entidades identificadas, as suas relações, as decisões de normalização tomadas, e as particularidades dos subsistemas de maior complexidade.

==== Regra Geral de Mapeamento

A cada classe _Java_ com identidade própria na camada de negócio, corresponde uma tabela no esquema relacional, com os atributos primitivos e textuais a serem diretamente mapeados para colunas do tipo adequado — `VARCHAR`, `TEXT`, `INT`, `FLOAT`, `DATE`, `DATETIME` ou `BOOLEAN`. Os identificadores internos, que na camada de negócio correspondem às chaves dos _Maps_ de cada subsistema, são representados na base de dados como colunas `id INT NOT NULL AUTO_INCREMENT`, com incremento automático gerido pelo motor _MySQL_. Esta decisão dispensa a geração de identificadores na camada de aplicação, elimina colisões em cenários de inserção concorrente e garante que cada linha possui um identificador único sem qualquer intervenção do _DAO_, diminuindo as preocupações que a equipa de desenvolvimento teve de ter conta.

As referências entre objetos _Java_ — que no modelo de negócio se expressam como associações diretas entre instâncias — são substituídas, no modelo relacional, por chaves estrangeiras que armazenam o `id` da entidade referenciada. Assim, por exemplo, a associação `Trotinete → Cliente` da camada de negócio traduz-se na coluna `cod_cliente INT` na tabela `Trotinete`, com a correspondente restrição `FOREIGN KEY (cod_cliente) REFERENCES Cliente(id)`. Os _Maps_ que na camada de negócio indexavam objetos por identificador são, portanto, substituídos por consultas _SQL_ parametrizadas nos _DAOs_, mantendo a mesma semântica de acesso direto por chave.

==== Mapeamento de _Enums_

Os tipos enumerados definidos na camada de negócio — `Cargo`, `EstadoOS`, `EstadoStock`, `TipoMovimento`, `EstadoNotificacao`, `EstadoEncomenda`, `EstadoDevolucao`, `MetodoPagamento` e `EstadoDefeitoAnterior` — são mapeados diretamente para colunas do tipo `ENUM` do _MySQL_. Esta escolha delega no motor de base de dados a responsabilidade de validar que apenas os valores declarados podem ser inseridos, adicionando uma camada de proteção independente da lógica da aplicação. Na prática, cada valor do _enum_ _Java_ corresponde a um literal no `ENUM` _MySQL_ com o mesmo nome, o que permite que os _DAOs_ convertam diretamente os valores sem necessidade de tabelas auxiliares de lookup ou mapeamentos intermédios. A utilização de `ENUM` nativo é também mais eficiente do que o equivalente em `VARCHAR` do ponto de vista de armazenamento e comparação, uma vez que o _MySQL_ armazena internamente os valores como índices numéricos.

==== Decomposição de Hierarquias de Herança

Duas hierarquias de herança presentes no modelo de negócio — `MovimentoFinanceiro` com as subclasses `MovimentoFuncionario`, `MovimentoReparacao` e `MovimentoPeca`, e `Notificacao` com as subclasses `NotificacaoOS` e `NotificacaoStock` — foram mapeadas segundo a estratégia _table-per-subclass_. A tabela da classe base (`MovimentoFinanceiro` e `Notificacao`) contém os atributos comuns a todas as especializações, enquanto cada subclasse possui a sua própria tabela com os atributos específicos e uma chave primária que é simultaneamente chave estrangeira para a tabela pai. Por exemplo, `MovimentoFuncionario` tem como chave primária `id INT`, que referencia `MovimentoFinanceiro(id)`, e adiciona a coluna `codFuncionario INT` com a respetiva restrição de integridade referencial.

Esta abordagem foi preferida à estratégia alternativa de _single-table_ — que colocaria todas as especializações numa única tabela com colunas nulas para os atributos não aplicáveis — porque permite que o motor de base de dados imponha restrições de chave estrangeira específicas a cada subtipo. Um `MovimentoFuncionario` só pode referenciar um funcionário existente, um `MovimentoReparacao` só pode referenciar uma reparação existente, e um `MovimentoPeca` só pode referenciar uma entrada de _stock_ existente — garantias que seriam impossíveis de expressar numa arquitetura de tabela única. A leitura de um movimento completo envolve uma junção entre a tabela base e a tabela do subtipo correspondente, operação que os _DAOs_ executam de forma transparente para a camada de negócio.

==== Integridade Referencial e Ações em Cascata

As restrições de chave estrangeira foram declaradas com ações de cascata diferenciadas consoante a semântica de cada relação. Nas relações de composição — onde a entidade dependente não tem existência fora do contexto da entidade pai — foi definida a ação `ON DELETE CASCADE`, garantindo que a eliminação de uma entidade pai propaga automaticamente a eliminação de todas as entidades dependentes sem necessidade de intervenção explícita pelos _DAOs_. São exemplos desta política: a eliminação de uma `OrdemServico` que elimina automaticamente o `Diagnostico`, o `Conserto`, o `Pagamento` e os `OrdemServico_Acessorio` associados; a eliminação de uma `Peca` que elimina os registos de `Stock` correspondentes; e, nas hierarquias de herança, a eliminação de um registo da tabela base que elimina o registo correspondente na tabela da subclasse.

Um caso particular digno de nota ocorre nos `MovimentoPeca`: a eliminação de uma entrada de `Stock` deve eliminar os movimentos financeiros que a referenciam, pois estes perderam o seu contexto. Contudo, esta garantia não pode ser expressa diretamente através de uma chave estrangeira `ON DELETE CASCADE` de `MovimentoPeca` para `Stock`, uma vez que a chave primária de `MovimentoPeca` referencia `MovimentoFinanceiro` e não `Stock` diretamente. A solução adotada foi tratar esta eliminação explicitamente no `SStockFacade`: antes de eliminar uma entrada de _stock_, o _facade_ invoca o _DAO_ financeiro para remover os movimentos associados, assegurando a consistência que o motor de base de dados não consegue garantir autonomamente neste caso.

==== Particularidades do Subsistema de Ordens de Serviço

O `OrdemServicoDAO` é o componente de maior complexidade da camada de dados, pelas seguintes razões. Em primeiro lugar, a leitura completa de uma ordem de serviço exige junções a múltiplas tabelas: `OrdemServico`, `Registo`, `Diagnostico`, `Diagnostico_PecaOrcamento`, `Diagnostico_Reparacao`, `Conserto`, `Conserto_PecaUsada`, `Conserto_Reparacao`, `Pagamento` e `OrdemServico_Acessorio`. O _DAO_ constrói o objeto `OrdemServico` completo a partir destas múltiplas consultas, montando as listas e os _Maps_ internos antes de devolver o objeto à camada de negócio.

Em segundo lugar, as transições de estado da ordem de serviço devem ser atómicas: a atualização do campo `estado` e as escritas nas tabelas associadas (por exemplo, a criação do registo de `Diagnostico` e das suas linhas de peças e reparações ao concluir um diagnóstico) devem ou ser ambas bem-sucedidas ou ambas revertidas, sem estados intermédios visíveis. Para garantir este comportamento, o _DAO_ encapsula estas operações em transações _JDBC_ explícitas — desativando o _autocommit_, executando todas as escritas dentro de um bloco `try`, invocando `commit()` em caso de sucesso, e `rollback()` em caso de exceção — garantindo que a base de dados permanece sempre num estado consistente, independentemente de falhas parciais durante a execução.

==== Particularidades do Subsistema de Stock

A granularidade do modelo de _stock_ — em que cada entrada física de peças em armazém corresponde a um registo individual na tabela `Stock`, em vez de uma quantidade agregada por peça — tem implicações diretas na forma como as consultas são formuladas no `StockDAO`. Quando a camada de negócio solicita a utilização de _N_ unidades de uma peça durante um conserto, o _DAO_ não atualiza um contador único: em vez disso, executa uma consulta que seleciona os registos de _stock_ disponíveis para aquela peça ordenados por `data_chegada ASC` — implementando a estratégia FIFO (_First In, First Out_) — e consome as unidades dos lotes mais antigos em primeiro lugar, atualizando o estado de cada registo para `StockUsadoConserto` à medida que as suas unidades são esgotadas.

=== Relações entre Entidades e Integridade Referencial

A integridade referencial é garantida através de chaves estrangeiras declaradas explicitamente no esquema da base de dados, com ações de cascata definidas de acordo com a semântica de cada relação.

As relações de composição (onde a entidade dependente não tem existência fora do contexto da entidade pai) são implementadas com `ON DELETE CASCADE`, garantindo que a eliminação de uma entidade pai elimina automaticamente todas as entidades dependentes. São exemplos destas relações: a eliminação de um `Funcionario` elimina o respetivo `Utilizador`; a eliminação de uma `OrdemServico` elimina o `Diagnostico`, `Conserto`, `Pagamento` e acessórios associados; e a eliminação de uma `Peca` elimina os registos de `Stock` correspondentes.

As relações de associação (onde as entidades têm existência independente) são implementadas com restrições de integridade referencial sem cascata, exigindo que a associação seja explicitamente gerida pela lógica de negócio. São exemplos: a relação entre `Trotinete` e `Cliente`, ou entre `Peca` e `Fornecedor`.

A tabela seguinte resume as principais relações entre entidades e as respetivas cardinalidades no modelo lógico:

#figure(
  caption: "Principais relações entre entidades no modelo lógico",
  kind: table,
  table(
    columns: (auto, auto, auto),
    stroke: (dash: "densely-dotted", thickness: 0.75pt),
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
    [*Entidade Origem*], [*Entidade Destino*], [*Cardinalidade*],
    [Funcionario], [Utilizador], [1 para 0..1],
    [Cliente], [Trotinete], [1 para \*],
    [Trotinete], [OrdemServico], [1 para \*],
    [Cliente], [OrdemServico], [1 para \*],
    [Funcionario], [OrdemServico (criador)], [1 para \*],
    [Funcionario], [OrdemServico (mecânico)], [1 para \*],
    [OrdemServico], [Diagnostico], [1 para 0..1],
    [OrdemServico], [Conserto], [1 para 0..1],
    [OrdemServico], [Pagamento], [1 para 0..1],
    [Diagnostico], [Diagnostico_PecaOrcamento], [1 para \*],
    [Diagnostico], [Diagnostico_Reparacao], [1 para \*],
    [Conserto], [Conserto_PecaUsada], [1 para \*],
    [Conserto], [Conserto_Reparacao], [1 para \*],
    [Peca], [Stock], [1 para \*],
    [Stock], [Defeito], [1 para 0..1],
    [Stock], [Devolucao], [1 para \*],
    [Fornecedor], [Peca], [1 para \*],
    [Fornecedor], [Encomenda], [1 para \*],
    [Encomenda], [Encomenda_EntradaStock], [1 para \*],
    [MovimentoFinanceiro], [MovimentoFuncionario], [1 para 0..1],
    [MovimentoFinanceiro], [MovimentoReparacao], [1 para 0..1],
    [MovimentoFinanceiro], [MovimentoPeca], [1 para 0..1],
    [Notificacao], [NotificacaoOS], [1 para 0..1],
    [Notificacao], [NotificacaoStock], [1 para 0..1],
  )
)


=== Normalização do Modelo Relacional

O processo de normalização foi aplicado de forma sistemática ao modelo relacional, com o objetivo de eliminar redundâncias, prevenir anomalias de inserção, atualização e eliminação, e garantir que cada atributo depende exclusivamente da chave primária da tabela em que se encontra.

*Primeira Forma Normal (1FN)*

Todas as tabelas do modelo satisfazem a primeira forma normal: cada célula contém um valor atómico (não multivalorado), cada coluna tem um nome único e um tipo de dados homogéneo, e não existem grupos de colunas repetidos. A tentação de armazenar listas de valores (como os acessórios de uma OS) numa única coluna foi evitada através da criação de tabelas auxiliares (`OrdemServico_Acessorio`), que representam cada item como uma linha independente.

*Segunda Forma Normal (2FN)*

A segunda forma normal requer que, em tabelas com chaves primárias compostas, todos os atributos não-chave dependam da totalidade da chave, e não apenas de uma parte dela. As tabelas com chaves compostas no modelo — `Conserto_PecaUsada`, `Conserto_Reparacao`, `Diagnostico_PecaOrcamento`, `Diagnostico_Reparacao` e `Encomenda_EntradaStock` — satisfazem este requisito: os seus únicos atributos não-chave (como `quantidade` em `Conserto_PecaUsada` ou `ordem` em `Encomenda_EntradaStock`) dependem funcionalmente da totalidade da chave composta.

*Terceira Forma Normal (3FN)*

A terceira forma normal requer a ausência de dependências transitivas, ou seja, nenhum atributo não-chave deve depender de outro atributo não-chave. Este requisito motivou algumas decisões de design relevantes. Por exemplo, a informação do fornecedor de uma peça não é replicada na tabela `Stock` — é sempre consultada por junção com `Peca` e depois com `Fornecedor`. Da mesma forma, os dados do cliente não são replicados na `OrdemServico`: apenas a chave estrangeira `codCliente` é armazenada, sendo os dados do cliente obtidos por junção quando necessário.

Um caso particular surge na entidade `Funcionario`, que armazena simultaneamente o `salario_bruto`, `salario_liquido` e `salario_hora`. Poder-se-ia argumentar que o salário líquido depende do salário bruto (através da aplicação de descontos fiscais e previdenciais), o que constituiria uma dependência transitiva. Contudo, a decisão de armazenar os três valores foi deliberada: os descontos aplicáveis a cada funcionário podem variar individualmente (dependendo de escalões de IRS, isenções e acordos específicos), e o cálculo do salário líquido a partir do bruto não é determinístico com base apenas nos dados armazenados no sistema. Assim, todos os três valores são tratados como atributos independentes mantidos pelo gerente.

=== Modelo Lógico da Base de Dados

O modelo lógico da base de dados apresentado de seguida constitui a representação final e normalizada da estrutura de persistência do sistema. Este modelo resulta de todas as decisões de design documentadas nas secções anteriores, servindo de base à geração do esquema físico da base de dados.

O modelo é composto por 28 tabelas, interligadas por 35 relações de chave estrangeira, e define 9 tipos enumerados que garantem a validade dos valores de estado e categoria a nível do motor de base de dados.

As tabelas podem ser agrupadas em seis domínios funcionais:

+ *Domínio de Pessoal e Autenticação*: `Funcionario`, `Utilizador`, `MovimentoFinanceiro`, `MovimentoFuncionario`;
+ *Domínio de Clientes e Equipamentos*: `Cliente`, `Trotinete`;
+ *Domínio de Ordens de Serviço*: `OrdemServico`, `Diagnostico`, `Diagnostico_PecaOrcamento`, `Diagnostico_Reparacao`, `Conserto`, `Conserto_PecaUsada`, `Conserto_Reparacao`, `Pagamento`, `OrdemServico_Acessorio`;
+ *Domínio de Stock e Peças*: `Peca`, `Stock`, `Defeito`, `Devolucao`, `MovimentoPeca`, `MovimentoReparacao`;
+ *Domínio de Aprovisionamento*: `Fornecedor`, `Encomenda`, `Encomenda_EntradaStock`;
+ *Domínio de Notificações*: `Notificacao`, `NotificacaoOS`, `NotificacaoStock`.

O modelo respeita integralmente as três primeiras formas normais, com a exceção justificada dos atributos salariais na tabela `Funcionario`, documentada na secção de normalização. Todas as chaves estrangeiras estão acompanhadas de ações de integridade referencial adequadas à semântica de cada relação, garantindo que o motor de base de dados previne ativamente a inserção de dados inconsistentes.

#figure(
  image("img-dados/modelo-logico.png"),
  caption: [Modelo lógico da base de dados do sistema]
)

== Design de Interface
=== Estrutura Geral da Interface do Sistema
Antes da implementação da camada de apresentação, foi necessário modelar a estrutura e o fluxo de navegação da interface do sistema. Para este propósito, foi esboçada a aparência de cada página Web constituinte da interface, mas antes disso, foi essencial definir o conjunto de páginas da aplicação e a forma como a navegação entre elas se processaria. Com base na análise dos requisitos levantados, construiu-se um diagrama de máquina de estados, onde cada página Web é representada como um estado e as ações dos utilizadores que conduzem ao redirecionamento entre páginas são representadas como transições de estado.

Importa salientar que a interface do sistema não está integralmente disponível para todos os funcionários. Tal como definido no REQ-001, o sistema implementa um mecanismo de controlo de acesso baseado em papéis (RBAC — _Role-Based Access Control_), pelo que cada utilizador acede apenas às páginas e funcionalidades correspondentes ao seu cargo. Nomeadamente, os perfis identificados são o Gerente, a Secretária, o Gestor de Stock e o Mecânico. O diagrama apresentado representa uma visão geral e unificada da interface, englobando todas as áreas funcionais do sistema num único modelo de navegação. As restrições de acesso por perfil são geridas ao nível da autenticação e do controlo de sessão, não estando explicitadas no diagrama para não aumentar desnecessariamente a sua complexidade visual.

#figure(
  image("img-interface/Interface.jpg"),
  caption: [Diagrama de máquina de estados da interface]
)

O ponto de entrada do diagrama corresponde ao pseudoestado inicial, representado pelo círculo negro, que origina imediatamente uma transição para a *Página de Login*. Esta decisão decorre do REQ-002, que determina que utilizadores não autenticados devem ser redirecionados para a página de autenticação, e que todos os utilizadores — independentemente do seu cargo — partilham este único ponto de entrada no sistema. Nesta página, o utilizador insere o seu identificador único e a sua palavra-passe e, caso as credenciais sejam válidas, é redirecionado para a sua *Página Inicial/Dashboard*, transição denominada "Iniciar Sessão". A Página Inicial/Dashboard constitui o hub central de toda a navegação do sistema, sendo acessível a todos os utilizadores autenticados e servindo de ponto de partida para as diversas áreas funcionais. A transição "Terminar Sessão" a partir do Dashboard redireciona o utilizador de volta para a Página de Login, em conformidade com o requisito de encerramento de sessão.

A área correspondente à gestão de clientes e ordens de serviço é acessível a partir da Página Inicial através das transições "Ir para" que conduzem, respetivamente, à *Página dos Clientes* e à *Página das Ordens de Serviço*. Estas páginas são geridas principalmente pela Secretária, em conformidade com os requisitos REQ-014, REQ-015 e REQ-018. A partir da Página dos Clientes, é possível aceder aos *Formulários sobre clientes*, que agrupam as operações de criação, edição e remoção de registos de clientes, com transições de "Abrir" e "Regressar" entre as duas páginas. De forma semelhante, a partir da Página das Ordens de Serviço é possível aceder aos *Formulários sobre OS*, que tratam a criação e edição de ordens de serviço, bem como ao *Histórico completo de OS*, que apresenta o registo histórico de todas as ordens de serviço do sistema. As transições entre estas páginas seguem o mesmo padrão de "Abrir" e "Regressar", garantindo uma navegação linear e reversível.

O catálogo de reparações e a gestão de trotinetes estão igualmente acessíveis a partir da Página Inicial. O *Catálogo de Reparações*, cujo acesso é da responsabilidade do Gerente ao abrigo do REQ-006, permite gerir as entradas da tabela de serviços disponíveis. A *Página das Trotinetes* segue o mesmo padrão estrutural — com acesso a formulários para as operações de gestão de trotinetes — e suporta as funcionalidades descritas no REQ-015. A *Página dos Fornecedores* e os seus formulários associados, geridos pelo Gestor de Stock ao abrigo do REQ-011, completam este conjunto de páginas diretamente acessíveis a partir da Página Inicial. Por fim, a *Página dos Funcionários*, com os correspondentes formulários, dá suporte às operações de gestão de recursos humanos definidas no REQ-003, sendo esta área de acesso exclusivo do Gerente.

A *Página do Financeiro* é igualmente acessível a partir da Página Inicial, através de uma transição "Ir para", e permite ao Gerente consultar o estado financeiro da empresa, conforme estabelecido no REQ-005. A *Página de Alertas*, acessível através de transição direta a partir da Página Inicial, centraliza as notificações geradas automaticamente pelo sistema em resposta a eventos como mudanças de estado de ordens de serviço, peças com quantidade mínima atingida ou peças com possível defeito, conforme descrito nos requisitos REQ-026, REQ-028 e REQ-029. Esta página é relevante tanto para a Secretária como para o Gestor de Stock, que recebem alertas distintos consoante o seu perfil.

A *Página de Peças e Stock* constitui a área de maior complexidade estrutural do diagrama, refletindo a riqueza funcional exigida ao Gestor de Stock. A esta página acede-se a partir da Página Inicial através da transição "Ir para", sendo possível regressar através da transição "Regressar". A partir dela, é possível aceder aos *Formulários sobre peças* para as operações de gestão de peças, em conformidade com o REQ-007. A página organiza-se em quatro subpáginas, entre as quais o utilizador pode transitar através de ações "Mudar para": a *Subpágina Stock*, que apresenta o inventário atual e dá acesso aos Formulários sobre entradas de _stock_ e ao Histórico completo de _stock_; a *Subpágina Defeitos*, que concentra a gestão de peças reportadas com possível defeito durante reparações, conforme o REQ-029; a *Subpágina Devoluções*, a partir da qual é possível aceder ao Histórico completo de devoluções, suportando o fluxo de devoluções ao fornecedor definido no REQ-010; e a *Subpágina Encomendas*, que gere as listas de encomenda geradas pelo sistema ao abrigo do REQ-008, com acesso ao Histórico completo de encomendas e aos Formulários sobre encomendas. A separação em subpáginas foi uma decisão de design que permite manter uma navegação clara e focada dentro de uma área funcional de elevada complexidade, evitando que o utilizador tenha de navegar para páginas externas para aceder a vistas relacionadas.

Por último, cabe mencionar que não foram representadas no diagrama as interações do utilizador que o mantêm na mesma página — como a aplicação de filtros ou a ordenação de tabelas — por forma a não aumentar desnecessariamente a complexidade visual do modelo. Também não foi representado o pseudoestado final, uma vez que é possível transitar para ele a partir de qualquer página através do fecho do navegador, o que resultaria num elevado número de transições sem relevância para a compreensão da navegação da aplicação.

=== Caracterização das páginas do sistema
Após definidas as páginas do sistema e o fluxo de navegação entre elas, procedeu-se à construção dos esboços das interfaces. Ao contrário do que acontece noutras fases de modelação, onde o Visual Paradigm foi utilizado como ferramenta principal, a construção dos esboços das interfaces foi realizada com recurso a modelos de linguagem de grande escala (LLMs), nomeadamente o Claude. Para cada página, foram elaborados prompts detalhados que descreviam os objetivos da página, as suas funcionalidades, os requisitos funcionais a que respondia e as preferências de organização visual dos componentes. Os esboços resultantes foram depois revistos e ajustados pela equipa. Esta abordagem permitiu acelerar significativamente o processo de prototipagem, mantendo ao mesmo tempo uma estreita ligação entre os requisitos levantados e a proposta de interface gerada.

As interfaces seguem uma estrutura visual consistente em todas as páginas autenticadas, composta por três áreas distintas: um cabeçalho superior com o logótipo da aplicação, o nome do sistema ("Sistema de gestão") e a identificação do utilizador com sessão iniciada; uma barra de navegação lateral esquerda com acesso às diversas secções do sistema; e uma área de conteúdo principal, à direita, onde são apresentadas as funcionalidades específicas de cada página. Esta consistência visual, alinhada com a heurística de Nielsen de coerência e uniformidade, facilita a aprendizagem e utilização do sistema por parte dos funcionários da oficina, tal como exigido pelo RNF3. De seguida, apresentam-se os esboços de cada página e a justificação das decisões tomadas com base nos requisitos.


#figure(
  image("img-interface/esb-login.png", width: 80%),
  caption: [Esboço da Página de Login]
)

A Página de Login é o ponto de entrada único no sistema para todos os utilizadores, conforme determinado pelo REQ-002. A página apresenta um design centrado e minimalista, adequado a uma página de autenticação, constituído pelo logótipo da aplicação, o nome da empresa, e dois campos de entrada textual — "Utilizador" e "Palavra-passe" — que correspondem às credenciais exigidas pelo sistema. O botão "Entrar", em destaque com a cor verde característica da identidade visual da aplicação, submete as credenciais introduzidas. Em caso de autenticação bem-sucedida, o sistema identifica o cargo do utilizador e redireciona-o para a sua Página Inicial personalizada, conforme descrito no REQ-002. Nenhum outro elemento de navegação está presente nesta página, o que reforça o seu caráter de ponto de acesso controlado.


#figure(
  image("img-interface/esb-inicial.png", width: 100%),
  caption: [Esboço da Página Inicial/Dashboard]
)

A Página Inicial/Dashboard constitui a página principal do sistema após a autenticação, funcionando como hub central de navegação. O esboço apresentado corresponde à vista do Gerente, que, ao abrigo do REQ-001, possui acesso global a todas as funcionalidades do sistema. A página apresenta uma mensagem de boas-vindas personalizada com o nome do utilizador e o seu cargo ("A operar como Gerente"), tornando a interação com o sistema mais pessoal e contextualizada. São ainda apresentados três cartões de resumo com métricas operacionais relevantes — o número de ordens de serviço abertas, o número de alertas pendentes e o total de clientes registados — que permitem ao utilizador ter uma visão imediata do estado da oficina sem necessidade de navegar para páginas específicas, cumprindo assim o requisito não funcional de minimização de cliques para operações frequentes (RNF5). Uma secção de "Acessos rápidos" disponibiliza atalhos para as operações mais comuns: criar uma nova ordem de serviço, registar um novo cliente, gerir o _stock_ e aceder ao catálogo de reparações. A barra de navegação lateral lista todas as secções disponíveis para o perfil em sessão, sendo este o mecanismo principal de navegação entre as diferentes áreas do sistema.


#figure(
  image("img-interface/esb-alertas.png", width: 100%),
  caption: [Esboço da Página de Alertas]
)

A Página de Alertas, denominada "Centro de Alertas", centraliza todas as notificações geradas automaticamente pelo sistema em resposta a eventos operacionais relevantes. O seu conteúdo é dinâmico e personalizado em função do cargo do utilizador: a Secretária recebe alertas relativos a mudanças de estado de ordens de serviço que requerem a sua intervenção (como orçamentos pendentes de aprovação pelo cliente), ao abrigo dos requisitos REQ-026 e REQ-028; o Gestor de Stock recebe alertas sobre peças com quantidade mínima atingida ou com possível defeito, conforme os requisitos REQ-028 e REQ-029. A página disponibiliza dois filtros de visualização — "Não lidas" e "Todas" — que permitem ao utilizador gerir o seu centro de notificações de forma eficiente. A tabela de alertas apresenta, para cada notificação, o seu tipo, mensagem, data e hora, e um conjunto de ações associadas. As ações disponíveis variam consoante o tipo de alerta, permitindo, por exemplo, marcar um alerta como lido ou eliminá-lo. Os botões "Marcar todas como lidas" e "Eliminar todas", disponíveis no topo da página, permitem a gestão em massa das notificações.


#figure(
  image("img-interface/esb-OS.png", width: 100%),
  caption: [Esboço da Página de Ordens de Serviço]
)

A Página de Ordens de Serviço apresenta a lista completa das ordens de serviço registadas no sistema, dando resposta ao REQ-018. O componente principal da página é uma tabela que apresenta, para cada OS, o seu número identificador, o cliente associado, a trotinete, o mecânico atribuído, o estado atual e a data de criação, campos que permitem uma consulta rápida e contextualizada de cada reparação. A página disponibiliza um conjunto de filtros — por estado, cliente, trotinete e mecânico — que correspondem diretamente às exigências do REQ-018, facilitando a localização de ordens específicas. Por omissão, todas as ordens de serviço são apresentadas, independentemente do seu estado. O botão "+ Nova OS", em destaque no canto superior direito, abre o formulário de criação de uma nova ordem de serviço, funcionalidade descrita no REQ-018 e nos casos de uso associados à Secretária. Cada linha da tabela dispõe de dois botões de ação que permitem consultar os detalhes da OS ou executar operações sobre ela, consoante o perfil do utilizador.


#figure(
  image("img-interface/esb-clintes.png", width: 100%),
  caption: [Esboço da Página de Clientes]
)

A Página de Clientes apresenta a lista de todos os clientes registados no sistema, em resposta ao REQ-014. A tabela central da página apresenta, para cada cliente, o seu nome, NIF, número de telemóvel e endereço de email, que correspondem aos campos de registo obrigatórios definidos no REQ-016. Para cada entrada da tabela estão disponíveis três botões de ação que permitem consultar, editar e eliminar o registo do cliente, funcionalidades diretamente derivadas do REQ-014. O botão "+ Novo Cliente", posicionado no canto superior direito e realçado a verde, abre o formulário de criação de um novo cliente, em conformidade com o REQ-016. A página não inclui explicitamente filtros de pesquisa no esboço apresentado, embora o REQ-014 exija a possibilidade de pesquisa por nome, identificador único ou NIF, funcionalidade que será incorporada na implementação final. Esta página é gerida principalmente pela Secretária, ainda que o Gerente, ao abrigo do REQ-001, disponha também de acesso a todas as suas funcionalidades.


#figure(
  image("img-interface/esb-trotinetes.png", width: 100%),
  caption: [Esboço da Página de Trotinetes]
)

A Página de Trotinetes apresenta todos os equipamentos registados no sistema, associados aos seus respetivos clientes, em resposta ao REQ-015. A tabela central exibe, para cada trotinete, a marca e modelo agrupados numa única coluna, o número de série, o cliente a que está associada e o tipo de motor — campos que correspondem aos dados de registo obrigatórios definidos no REQ-017. Os botões de ação disponíveis em cada linha permitem consultar, editar e eliminar o registo da trotinete, em conformidade com as operações de gestão definidas no REQ-015. O botão "+ Nova Trotinete", no canto superior direito, permite registar um novo equipamento no sistema, seguindo o fluxo de criação descrito no REQ-017. Tal como na Página de Clientes, a pesquisa e filtragem por número de série, identificador, marca ou modelo, exigidas pelo REQ-015, serão incorporadas na implementação final.


#figure(
  image("img-interface/esb-reparacoes.png", width: 100%),
  caption: [Esboço da Página do Catálogo de Reparações]
)

A Página de Reparações apresenta o catálogo de serviços de reparação disponíveis na oficina, gerido pelo Gerente ao abrigo do REQ-006. A tabela central apresenta, para cada tipo de reparação, a sua nomenclatura, descrição, preço de mão de obra e disponibilidade, campos que correspondem à estrutura de dados definida no REQ-006. A coluna de disponibilidade reflete se a entrada se encontra ativa no sistema, ou seja, se pode ser associada a novas ordens de serviço. Os botões de ação por linha permitem editar ou eliminar uma entrada do catálogo. Uma barra de pesquisa no topo da tabela permite localizar rapidamente tipos de reparação por nomenclatura. O botão "+ Nova reparação", no canto superior direito, abre o formulário de criação de uma nova entrada no catálogo, em conformidade com o REQ-006.


#figure(
  image("img-interface/esb-stock.png", width: 100%),
  caption: [Esboço da Página de Peças \& Stock]
)

A Página de Peças & Stock é a área de maior complexidade funcional do sistema, gerida pelo Gestor de Stock, e corresponde à implementação dos requisitos REQ-007, REQ-008, REQ-009, REQ-010 e REQ-011. A página organiza-se em cinco subpáginas acessíveis através de separadores horizontais — "Peças", "Stock", "Defeitos", "Devoluções" e "Encomendas" —, permitindo ao utilizador navegar entre as diferentes vistas da mesma área funcional sem sair da página. O esboço apresentado corresponde à vista "Peças", que exibe o catálogo completo de peças registadas no sistema. A tabela central apresenta, para cada peça, a sua referência, nome, marca, fornecedor, preço de venda, quantidade em _stock_, estado e prazo de garantia, campos que cobrem os dados de registo definidos no REQ-007. Os filtros de pesquisa por fornecedor e por referência ou nome permitem localizar rapidamente peças específicas, em conformidade com as exigências de filtragem do REQ-007. As restantes subpáginas seguem uma estrutura similar, adaptada aos dados e operações específicos de cada área: a subpágina "Stock" apresenta os movimentos de entrada de peças em armazém; "Defeitos" agrega as peças com possível defeito reportado durante reparações (REQ-029); "Devoluções" gere o fluxo de devoluções ao fornecedor (REQ-010); e "Encomendas" apresenta as listas de encomenda geradas pelo sistema com base nos níveis mínimos de _stock_ (REQ-008). O botão "+ Nova Peça", no canto superior direito, abre o formulário de criação de uma nova peça no sistema, em resposta ao REQ-007.


#figure(
  image("img-interface/esb-fornecedores.png", width: 100%),
  caption: [Esboço da Página de Fornecedores]
)

A Página de Fornecedores apresenta a lista de todos os parceiros e fornecedores de peças registados no sistema, em resposta ao REQ-011. A tabela central exibe, para cada fornecedor, o seu nome, número de telefone e endereço de email, que correspondem aos campos de registo definidos no REQ-012. Uma barra de pesquisa no topo da tabela permite localizar fornecedores por qualquer um dos seus campos, em conformidade com o REQ-011. Os botões de ação disponíveis por linha permitem editar ou eliminar o registo de um fornecedor. O botão "+ Novo fornecedor", realçado a verde no canto superior direito, abre o formulário de criação de um novo fornecedor, seguindo as validações descritas no REQ-012. Esta página é gerida pelo Gestor de Stock, ainda que o Gerente, ao abrigo do REQ-001, disponha igualmente de acesso a todas as suas funcionalidades.


#figure(
  image("img-interface/esb-funcionarios.png", width: 100%),
  caption: [Esboço da Página de Funcionários]
)

A Página de Funcionários apresenta a lista de todos os colaboradores da oficina registados no sistema, em resposta ao REQ-003. A tabela central exibe, para cada funcionário, o seu nome, identificador único, cargo, email, número de telemóvel, valor de remuneração por hora, salário bruto mensal e horas extraordinárias acumuladas desde o início do mês — conjunto de campos que permite ao Gerente ter uma visão imediata do estado salarial de cada colaborador, em conformidade com os requisitos REQ-003 e REQ-004. O botão "Pagar a todos" facilita o processamento salarial mensal, funcionalidade descrita no REQ-003. O botão "+ Novo Funcionário", no canto superior direito, abre o formulário de registo de um novo colaborador, que recolhe todos os dados obrigatórios definidos no REQ-004, incluindo dados pessoais, fiscais, bancários e salariais. Os botões de ação por linha permitem editar o registo de um funcionário ou registar horas extraordinárias. O acesso a esta página é exclusivo do Gerente, dado o caráter sensível dos dados apresentados, em conformidade com o REQ-001.


#figure(
  image("img-interface/esb-financeiro.png", width: 95%),
  caption: [Esboço da Página Financeiro]
)

A Página Financeiro apresenta o estado financeiro da oficina, em resposta ao REQ-005. A página divide-se em duas secções principais: um painel de resumo no topo, com quatro cartões que apresentam os valores agregados de receitas, despesas, saldo e número de movimentos no período selecionado, conferindo ao Gerente uma visão imediata da saúde financeira da empresa; e uma secção de detalhe que, do lado esquerdo, apresenta os valores desagregados por tipo de movimento — mão de obra, venda de peças, salários e compra de peças —, e do lado direito, um histórico de movimentos individual, filtrável por intervalo de datas ("Desde" / "Até") e por tipo, em conformidade com o REQ-005. A tabela de movimentos apresenta, para cada registo, a data, o tipo, a descrição e o valor, permitindo ao Gerente rastrear com detalhe cada transação financeira. O botão "Limpar filtros" permite repor rapidamente a vista para todos os movimentos registados. O acesso a esta página é exclusivo do Gerente, ao abrigo do REQ-001 e REQ-005.

== Validação da Interface
Concluída a elaboração dos esboços de todas as páginas do sistema, a equipa de desenvolvimento realizou uma reunião de validação com o gerente, João Martins, com o objetivo de apresentar e discutir as propostas de interface antes de avançar para a fase de implementação. Nesta reunião, foram percorridos os esboços de cada página, explicando as decisões de design tomadas e a forma como cada componente respondia aos requisitos previamente levantados e validados.

O gerente considerou as propostas adequadas ao contexto de utilização da oficina, reconhecendo a estrutura de navegação como intuitiva e a organização das funcionalidades como coerente com os fluxos de trabalho diários dos funcionários. Foram discutidos alguns detalhes de apresentação, nomeadamente quanto à terminologia utilizada em certos rótulos e à prioridade visual de algumas operações, tendo as sugestões apresentadas sido incorporadas nos esboços finais. No final da reunião, os esboços foram aprovados sem reservas, ficando acordado que a implementação da interface poderia avançar com base nas propostas validadas.

== Uso de _LLM_
Tal como na fase anterior, o recurso a _LLM_ foi determinante e transversal a todas as etapas do _design_ e arquitetura do software, tendo conduzido a totalidade do trabalho documentado neste capítulo.

A redação dos textos do presente capítulo foi integralmente produzida com recurso a _LLM_, que gerou as descrições arquiteturais, as justificações das decisões de _design_ e as caracterizações detalhadas de cada subsistema, entidade e atributo, mantendo ao longo de todo o documento um estilo técnico consistente e alinhado com as convenções da engenharia de _software_.

No que diz respeito à definição da arquitetura, foi o _LLM_ que conduziu o processo de identificação e delimitação dos oito subsistemas da camada de controlo, determinando quais as responsabilidades a agregar em cada um, as fronteiras entre eles e as motivações arquiteturais que justificam a separação adotada — desde o isolamento da lógica de autenticação no SAutenticacao até à necessidade de um ambiente de execução dedicado para a elevada complexidade do SOrdensServico. Foi igualmente o _LLM_ que encorajou e validou a organização final dos subsistemas, contribuindo para uma estrutura coesa e com baixo acoplamento.

Na fase de modelação da camada de dados, o _LLM_ desempenhou um papel central em duas frentes complementares. Por um lado, foi responsável por identificar as limitações estruturais dos diagramas de classes iniciais que comprometiam a sua viabilidade enquanto base para uma persistência robusta, propondo a introdução das hierarquias de especialização que permitiram separar corretamente, ao nível do modelo relacional, entidades como os movimentos financeiros e as notificações nas suas subclasses especializadas. Por outro lado, foi o _LLM_ que validou a normalização do modelo relacional resultante, confirmando a conformidade com as três primeiras formas normais, identificando as dependências funcionais relevantes e justificando as exceções deliberadas — como o caso dos atributos salariais na tabela Funcionario.

Por fim, os esboços de todas as páginas do sistema foram produzidos com recurso a _LLM_, nomeadamente o _Claude_, através de _prompts_ detalhados que descreviam os objetivos, funcionalidades e requisitos de cada página. A equipa procedeu posteriormente à revisão e refinamento dos resultados gerados, incorporando as sugestões recolhidas na reunião de validação com o gerente. A implementação efetiva da interface, realizada com recurso à ferramenta _Lovable_ e posteriormente refinada com o apoio do _Claude_, é abordada em detalhe no capítulo seguinte, onde o papel do _LLM_ nessa fase é igualmente documentado.

= Implementação e Desenvolvimento Assistido por _LLM_

== Configuração do ambiente

=== Visão geral e motivação

A EcoRide é uma aplicação distribuída composta por múltiplos serviços interdependentes, o que torna a gestão do ambiente de execução um aspeto crítico do projeto. Para resolver este desafio, foi adotado o *Docker Compose* como orquestrador de serviços, uma ferramenta que permite definir toda a infraestrutura da aplicação num único ficheiro declarativo e iniciá-la com um único comando. Esta decisão garante que o ambiente de desenvolvimento é completamente reproduzível em qualquer máquina com Docker instalado, eliminando a clássica problemática de "funciona na minha máquina" e simplificando substancialmente o processo de instalação e avaliação do sistema.

A escolha de Docker como tecnologia de containerização assenta em três pilares fundamentais. Em primeiro lugar, o *isolamento de dependências*: cada serviço encapsula as suas próprias dependências (_JDK_ 21 para o _backend_, _Node_ 22 para o build do _frontend_, _MySQL_ 8.4 para a base de dados), sem interferir com o sistema operativo anfitrião nem com outros projetos. Em segundo lugar, a *portabilidade*: as imagens _Docker_ são independentes do sistema operativo subjacente, funcionando de forma idêntica em _Linux_, _macOS_ e _Windows_. Em terceiro lugar, a *consistência entre ambientes*: a mesma configuração _Docker_ que corre localmente pode ser usada em ambiente de produção ou num servidor de integração contínua, garantindo que o comportamento do sistema é previsível.

=== Estrutura do Docker Compose
Para configurar a orquestração do _Docker_, é necessário configurar um ficheiro `docker-compose.yml`, e no caso do sistema implementado, define cinco serviços, cada um com a sua função específica no ecossistema da aplicação. A relação de dependência entre serviços é declarada explicitamente, de forma a que o _Docker Compose_ respeite a ordem de arranque correta.

```yaml
services:
  mysql:
    image: mysql:8.4
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: EcoRide
      MYSQL_USER: ecoride
      MYSQL_PASSWORD: EcoRide123!
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uecoride", "-pEcoRide123!"]
      interval: 10s
      timeout: 5s
      retries: 10
  backend:
    build: ./app
    restart: unless-stopped
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: EcoRide
      DB_USER: ecoride
      DB_PASS: EcoRide123!
      DEV_MODE: "true"
      ENCRYPTION_KEY: "gJ0uE98qhXW39ZLMVzrEM8/DlrZdGNKxnzRPvZk7nnE="
    ports:
      - "7000:7000"
    depends_on:
      mysql:
        condition: service_healthy
    healthcheck:
      test: ["CMD-SHELL", "bash -c '</dev/tcp/localhost/7000' 2>/dev/null"]
      interval: 5s
      timeout: 3s
      retries: 20
      start_period: 15s
  frontend:
    build: ./EcoRideCA
    restart: unless-stopped
    ports:
      - "3000:80"
    depends_on:
      - backend
  swagger:
    image: swaggerapi/swagger-ui
    restart: unless-stopped
    ports:
      - "8081:8080"
    environment:
      SWAGGER_JSON_URL: http://localhost:3000/api-docs 
    depends_on:
      - backend
  info:
    image: alpine
    restart: "no"
    depends_on:
      backend:
        condition: service_healthy
      frontend:
        condition: service_started
      swagger:
        condition: service_started
    command: >
      sh -c "
        echo '' &&
        echo '┌─────────────────────────────────────────┐' &&
        echo '│           EcoRide — a correr            │' &&
        echo '├─────────────────────────────────────────┤' &&
        echo '│  Interface    →  http://localhost:3000  │' &&
        echo '│  API directa  →  http://localhost:7000  │' &&
        echo '│  Swagger UI   →  http://localhost:8081  │' &&
        echo '└─────────────────────────────────────────┘' &&
        echo ''
      "

volumes:
  mysql_data:
```

O serviço `mysql` utiliza a imagem oficial _MySQL 8.4_ e persiste os dados num volume _Docker_ nomeado (`mysql_data`), garantindo que a informação não se perde entre reinicializações do contentor. As credenciais são configuradas via variáveis de ambiente, seguindo a prática recomendada de não colocar dados sensíveis diretamente no código-fonte. O *healthcheck* do _MySQL_ é um detalhe de implementação especialmente importante: recorre ao comando `mysqladmin ping` para verificar periodicamente se a base de dados está pronta para aceitar ligações. Enquanto esta verificação não for bem-sucedida, o serviço `backend` não arranca, evitando a condição de corrida (_race condition_) em que o _backend_ tenta ligar-se à base de dados antes desta estar inicializada, uma falha frequente em aplicações multi-contentor que não implementam este mecanismo.

O serviço `backend` expõe dois atributos de ambiente particularmente relevantes: a variável `ENCRYPTION_KEY` fornece a chave _AES-256_ em formato _Base64_ (exatamente 32 bytes após descodificação) utilizada para cifrar os campos sensíveis dos funcionários, conforme o REQ-012; e a variável `DEV_MODE`, quando definida como `true`, desativa a verificação de autenticação em ambiente de desenvolvimento, facilitando testes à _API_ sem necessidade de obter _tokens_. Em produção, `DEV_MODE` deve ser removida e a `ENCRYPTION_KEY` deve ser gerida por um gestor de segredos externo (como por exemplo _Docker Secrets_), nunca exposta em texto claro num ficheiro de configuração controlado por versão.

O serviço `swagger` instancia a imagem pública `swaggerapi/swagger-ui`, configurada com o _URL_ do ficheiro `openapi.yaml` servido pelo _nginx_ do _frontend_. Esta separação permite que a documentação interativa da _API_ seja explorada diretamente no browser sem necessidade de ferramentas adicionais, o que é particularmente valioso na fase de desenvolvimento e de avaliação. O projeto mantém dois ficheiros de especificação complementares: o `openapi.yaml` dentro da pasta do _frontend_, servido publicamente em `/api-docs` para o _Swagger UI_; e um `swagger.yaml` na raiz do repositório, com documentação mais detalhada orientada ao desenvolvimento, que referencia diretamente o _backend_ na porta 7000. O serviço `info` é um contentor de vida curta, baseado na imagem minimalista `alpine`, que aguarda que todos os serviços (incluindo o _Swagger UI_) estejam operacionais e depois imprime no terminal os _URLs_ de acesso ao sistema.

=== Dockerfile do _backend_ — build multi-stage

O _backend_ Java adota o padrão de *_build multi-stage_* do _Docker_, que consiste em usar múltiplas imagens de base numa única definição de _Dockerfile_, aproveitando o resultado de cada etapa sem incluir as ferramentas de construção na imagem final.

```dockerfile
# ── Etapa 1: compilação com Maven ──────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Descarrega dependências antes de copiar o código (cache de camadas Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ── Etapa 2: imagem de runtime mínima ──────────────────────────
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/ecoride-*.jar app.jar

EXPOSE 7000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

A ordem das instruções na primeira etapa é deliberada e tem impacto direto no desempenho do processo de _build_. O ficheiro `pom.xml` é copiado e as dependências são descarregadas _antes_ do código-fonte. Esta sequência aproveita o mecanismo de cache de camadas do _Docker_: enquanto o `pom.xml` não for alterado, o _Docker_ reutiliza a camada de dependências de builds anteriores sem necessidade de as descarregar novamente. Como as dependências _Maven_ mudam raramente em comparação com o código-fonte, esta otimização reduz substancialmente o tempo de _rebuild_ durante o desenvolvimento. Na segunda etapa, apenas o _JRE (Java Runtime Environment)_ é incluído na imagem final, em vez do _JDK_ completo com as ferramentas de compilação. Esta escolha reduz significativamente o tamanho da imagem resultante, pois a imagem de _runtime_ não contém compiladores, depuradores nem outras ferramentas que não são necessárias em produção.

=== Dockerfile do _frontend_ — Node, Vite e nginx

O _frontend_ segue o mesmo padrão de *_build multi-stage_*, com uma primeira etapa dedicada ao _build_ com _Node.js_ e _Vite_, e uma segunda etapa de _serving_ com _nginx_.

```dockerfile
# ── Etapa 1: build dos assets estáticos ────────────────────────
FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json .
RUN npm ci

COPY . .
RUN npm run build

# ── Etapa 2: servidor nginx minimalista ────────────────────────
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY openapi.yaml /usr/share/nginx/html/openapi.yaml

EXPOSE 80
```

A utilização de `npm ci` em vez de `npm install` é igualmente intencional: o comando `ci` (_clean install_) instala exatamente as versões registadas no `package-lock.json`, garantindo que o ambiente de _build_ é completamente determinístico e reproduzível. O _Vite_ compila e otimiza todos os ficheiros _TypeScript_, resolve as importações, elimina código não utilizado e gera os _assets_ estáticos na diretoria `dist`. Esses _assets_ são copiados para a imagem _nginx_ final, juntamente com o ficheiro de configuração do _nginx_ e o `openapi.yaml`, que é colocado na raiz do servidor _web_ para ser servido pela rota `/api-docs`.

=== Configuração do nginx — proxy reverso e SPA fallback

O _nginx_ desempenha dois papéis distintos e igualmente importantes na arquitetura do sistema: servir os ficheiros estáticos da aplicação _React_ e atuar como _proxy_ reverso para o _backend_ Java. Esta dupla função resolve elegantemente o problema de CORS (_Cross-Origin Resource Sharing_) que existiria se o _frontend_ e o _backend_ fossem servidos em origens diferentes.

```nginx
server {
    listen 80;
    root /usr/share/nginx/html;
    index index.html;

    # Proxy transparente para a API REST
    location /api/ {
        proxy_pass         http://backend:7000/api/;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
    }

    # Proxy para autenticação (rota pública)
    location /auth/ {
        proxy_pass         http://backend:7000/auth/;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
    }

    # Documentação OpenAPI — serve o YAML com o Content-Type correto
    location = /api-docs {
        alias /usr/share/nginx/html/openapi.yaml;
        add_header Content-Type "application/yaml; charset=utf-8";
        add_header Access-Control-Allow-Origin "*";
    }

    # Fallback SPA — essencial para o React Router funcionar corretamente
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

As rotas `/api/` e `/auth/` são encaminhadas transparentemente para o _backend_, usando o nome de serviço _Docker_ `backend` como _hostname_ interno — a resolução de DNS é feita automaticamente pela rede interna criada pelo _Docker Compose_. Do ponto de vista do _browser_, todas as chamadas à _API_ são feitas à mesma porta 3000, pelo que não existe qualquer problema de _CORS_, já que a política de segurança do _browser_ é satisfeita sem necessidade de configuração adicional no _backend_. A diretiva `try_files $uri $uri/ /index.html` é fundamental para o correto funcionamento do _React Router_: quando o utilizador acede diretamente a um URL como `/os/42` ou `/stock/pecas`, o _nginx_ não encontra um ficheiro estático correspondente e serve o `index.html`, deixando o _React Router_ do lado do cliente interpretar a rota e renderizar o componente adequado.

=== Inicialização automática da base de dados

Uma das preocupações de design do projeto é que o sistema deve ser completamente autossuficiente no seu arranque, sem necessitar de passos manuais de configuração da base de dados. Esta responsabilidade recai sobre a classe `DBInitializer`, executada como a primeira ação do servidor _backend_ antes de qualquer pedido _HTTP_ ser processado.

```java
public final class DBInitializer {

    private DBInitializer() {}

    public static void run() {
        runScript("schema.sql");
        runScript("seed.sql");
    }

    private static void runScript(String resource) {
        try (InputStream in = DBInitializer.class
                                           .getClassLoader()
                                           .getResourceAsStream(resource)) {
            if (in == null) return;
            String content = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            // Remove comentários SQL antes de dividir por ";"
            String cleaned = Arrays.stream(content.split("\n"))
                    .filter(line -> !line.strip().startsWith("--"))
                    .collect(Collectors.joining("\n"));

            try (Connection c = ConnectionFactory.get();
                 Statement s = c.createStatement()) {
                for (String stmt : cleaned.split(";")) {
                    String trimmed = stmt.strip();
                    if (!trimmed.isEmpty()) s.execute(trimmed);
                }
            }
        } catch (Exception e) {
            System.err.println("[DBInitializer] Aviso ao executar "
                               + resource + ": " + e.getMessage());
        }
    }
}
```

O `DBInitializer` lê os ficheiros `schema.sql` e `seed.sql` diretamente do _classpath Java_ (incluídos no _JAR_ como recursos), remove as linhas de comentário _SQL_ e executa cada instrução individualmente. A estratégia de tolerar erros silenciosamente (apenas um aviso em `stderr`) é deliberada, assim, como o _schema_ usa `CREATE TABLE IF NOT EXISTS` em todas as definições de tabela, a re-execução do _script_ em arranques subsequentes é inócua pois as tabelas já existentes são simplesmente ignoradas e os dados previamente inseridos são preservados. O `seed.sql` complementa esta abordagem verificando previamente se as tabelas estão vazias antes de inserir dados de exemplo, garantindo idempotência total do processo de inicialização.

== Implementação por módulos

=== Arquitetura em três camadas

A estrutura do código _Java_ segue rigorosamente a arquitetura em três camadas definida na fase de design do projeto, com cada camada mapeada para um pacote _Java_ distinto, em conformidade com o REQ-009 que exige perfis de utilizador diferenciados por cargo. O pacote `app.ecoRideCD` contém toda a lógica de acesso a dados, incluindo os _DAOs_, o conjunto de ligações _JDBC_, o utilitário de cifra e o inicializador da base de dados. O pacote `app.ecoRideLN` encapsula a lógica de negócio, com os `Facades` de cada subsistema, as entidades de domínio e as enumerações. O pacote `app.IEcoRideLN` representa a camada de apresentação, contendo o servidor _Javalin_, os _controllers REST_, os _DTOs_ (_Data Transfer Objects_) e o gestor de sessões.

Esta separação física em pacotes não é meramente organizacional, já que  reflete uma regra arquitetural de dependências unidirecionais, assim, a camada de apresentação pode depender da camada de negócio (através da interface `IEcoRideController`), e a camada de negócio pode depender da camada de dados (através dos _DAOs_). Pelo contrário, o inverso não é permitido: a camada de dados não conhece regras de negócio, e a camada de negócio não conhece detalhes de serialização _HTTP_. Esta disciplina garante que cada camada pode ser desenvolvida, testada e modificada de forma relativamente independente.

A classe `EcoRideController` atua como `Facade` agregador, implementando a interface `IEcoRideController` e delegando cada operação ao `Facade` do subsistema respetivo:

```java
public class EcoRideController implements IEcoRideController {

    private final ISNotificacoes sNotificacoes;
    private final ISAutenticacao sAutenticacao;
    private final ISClientes sClientes;
    private final ISFinanceiro sFinanceiro;
    private final ISFuncionarios sFuncionarios;
    private final ISOrdensServico sOrdensServico;
    private final ISStock sStock;
    private final ISReparacoes sReparacoes;

    public EcoRideController() {
        this.sNotificacoes = new SNotificacoesFacade();
        this.sAutenticacao = new SAutenticacaoFacade();
        this.sClientes = new SClientesFacade();
        this.sFinanceiro = new SFinanceiroFacade();
        this.sFuncionarios = new SFuncionariosFacade();
        this.sOrdensServico = new SOrdensServicoFacade();
        this.sStock = new SStockFacade();
        this.sReparacoes = new SReparacoesFacade();
    }

    @Override
    public Utilizador registarUtilizador(String password, int idFuncionario, Cargo cargo, String identificador) {
        return sAutenticacao.registarUtilizador(password, idFuncionario, cargo, identificador);
    }
    // ... demais métodos delegam para a facade correspondente
}
```

Os _controllers_ da camada de apresentação recebem uma instância de `IEcoRideController` por injeção no construtor, respeitando o princípio de inversão de dependências: conhecem e dependem apenas da interface, nunca da implementação concreta. Esta indireção é fundamental porque permite substituir a implementação em testes, passando uma implementação _mock_ sem alterar o código dos _controllers_.

=== Padrão _DAO_ com interface Map\<Integer, Entity>

A decisão de _design_ mais característica da camada de dados do sistema EcoRide é a implementação de cada _DAO_ como uma instância de `java.util.Map<Integer, EntidadeX>`. Esta escolha, definida durante a fase de design e documentada no capítulo anterior, concretiza-se de forma consistente em todos os _DAOs_ do projeto. A interface `Map` serve de contrato entre a camada de negócio e a camada de dados: a camada de negócio interage com os _DAOs_ utilizando os métodos familiares de qualquer `Map` _Java_ — `get`, `put`, `remove`, `values`, `containsKey` — sem ter conhecimento de _SQL_, _JDBC_ ou qualquer detalhe de persistência.

Esta abordagem tem dois benefícios fundamentais. Por um lado, cria uma *interface uniforme* para todos os _DAOs_, simplificando o código da camada de negócio. Por outro lado, permite *substituição transparente da implementação*: o mesmo contrato `Map` pode ser satisfeito por uma implementação em memória (útil em testes unitários) ou por uma implementação _JDBC_ (usada em produção), sem alterar uma única linha da camada de negócio.

Cada _DAO_ é implementado como *_Singleton_* com construtor privado, garantindo que existe apenas uma instância por _DAO_ em toda a aplicação:

```java
public class OrdemServicoDAO implements Map<Integer, OrdemServico> {

    private static OrdemServicoDAO instance;

    protected OrdemServicoDAO() {}

    public static OrdemServicoDAO getInstance() {
        if (instance == null) instance = new OrdemServicoDAO();
        return instance;
    }

    // Fragmento SQL reutilizado em múltiplas queries
    private static final String SELECT_BASE = """
            SELECT id, descricao, data_criacao, codTrotinete, codCliente,
                   codCriador, codMecanico, estado
            FROM   OrdemServico
            """;
    @Override
    public OrdemServico get(Object key) {
        if (!(key instanceof Integer id)) return null;
        try (Connection c = ConnectionFactory.get();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? buildFromRow(c, rs) : null;
            }
        } catch (SQLException e) {
            throw new EcoRideException("Erro a obter OS " + id, e);
        }
    }
    // ...
}
```

O padrão _Singleton_ é justificado pelo facto dos _DAOs_ serem objetos _stateless_ — não mantêm qualquer estado próprio entre chamadas, dado que todo o estado reside na base de dados. A _thread-safety_ das operações _JDBC_ é garantida por dois mecanismos complementares: cada operação obtém a sua própria ligação à base de dados (a partir da `ConnectionFactory`), e as operações de escrita complexas usam transações explícitas. Adicionalmente, o `ConcurrentHashMap` do `GestorSessoes` garante a _thread-safety_ da gestão de sessões.

=== Proteção de dados pessoais — cifra AES-256-GCM

O registo de funcionários inclui um conjunto extenso de dados pessoais e financeiros sensíveis — nome, data de nascimento, telemóvel, email, NISS, NIF, NUS, IBAN, morada completa e valores salariais — conforme estabelecido no REQ-012. Para salvaguardar estes dados em caso de acesso não autorizado à base de dados, o sistema implementa uma cifra simétrica autenticada sobre todos esses campos antes da sua persistência. A responsabilidade por esta cifra recai integralmente sobre a classe utilitária `CifraUtil`, localizada no pacote `app.ecoRideCD.DAOconfig`.

==== Classe CifraUtil — AES-256-GCM

O algoritmo escolhido é o AES no modo _GCM_ (_Galois/Counter Mode_) com chave de 256 bits, referenciado pelo identificador _JCA_ `AES/GCM/NoPadding`. O modo _GCM_ é preferível ao CBC (_Cipher Block Chaining_) porque, além de garantir confidencialidade, fornece simultaneamente *autenticidade* através de uma etiqueta de autenticação (_authentication tag_) de 128 bits, assim qualquer adulteração do criptograma armazenado — mesmo que de um único _bit_ — é detetada no momento da decifragem, lançando uma exceção em vez de produzir dados corrompidos silenciosamente.

```java
public class CifraUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int    IV_LEN    = 12;   // bytes (96 bits — recomendado para GCM)
    private static final int    TAG_LEN   = 128;  // bits
    private static final String ENV_KEY   = "ENCRYPTION_KEY";

    private static final SecretKey SECRET_KEY;

    static {
        String b64 = System.getenv(ENV_KEY);
        if (b64 == null || b64.isBlank())
            throw new IllegalStateException(
                "[EcoRide] Variável de ambiente " + ENV_KEY + " não definida.");
        byte[] keyBytes = Base64.getDecoder().decode(b64);
        if (keyBytes.length != 32)
            throw new IllegalStateException(
                "[EcoRide] " + ENV_KEY + " deve ter exatamente 32 bytes (256 bits) em Base64.");
        SECRET_KEY = new SecretKeySpec(keyBytes, "AES");
    }

    public static String cifrar(String plaintext) {
        if (plaintext == null) return null;
        try {
            byte[] iv = new byte[IV_LEN];
            new SecureRandom().nextBytes(iv);               // IV aleatório por operação

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LEN, iv));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

            // Concatena IV + criptograma+tag e codifica em Base64
            byte[] out = new byte[IV_LEN + encrypted.length];
            System.arraycopy(iv, 0, out, 0, IV_LEN);
            System.arraycopy(encrypted, 0, out, IV_LEN, encrypted.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cifrar dado", e);
        }
    }

    public static String decifrar(String ciphertext) {
        if (ciphertext == null) return null;
        try {
            byte[] raw  = Base64.getDecoder().decode(ciphertext);
            byte[] iv   = Arrays.copyOfRange(raw, 0, IV_LEN);
            byte[] data = Arrays.copyOfRange(raw, IV_LEN, raw.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LEN, iv));
            return new String(cipher.doFinal(data), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decifrar dado", e);
        }
    }

    // Métodos auxiliares para campos numéricos (serialização Float → String → cifra)
    public static String cifrarFloat(double value) { return cifrar(String.valueOf(value)); }
    public static double decifrarFloat(String ciphertext) {
        return Double.parseDouble(decifrar(ciphertext));
    }
}
```

Aqui, existem três decisões de implementação que merecem destaque:

- Primeiramente, a chave é lida *uma única vez* no bloco estático inicializador, desta forma, qualquer erro de configuração (variável de ambiente ausente ou chave com tamanho incorreto) é detetado imediatamente no arranque do servidor, antes de qualquer pedido ser processado, impedindo que o sistema arranque numa configuração insegura. 
- Em segundo lugar, o _IV_ (_Initialization Vector_) de 12 _bytes_ é *gerado aleatoriamente* pela `SecureRandom` em cada chamada a `cifrar` — esta aleatoriedade é essencial para o modo _GCM_, já que reutilizar o mesmo _IV_ com a mesma chave comprometeria completamente a segurança do esquema. O formato de armazenamento `Base64(IV[12] || criptograma || tag[16])` embute o _IV_ junto com o criptograma, pelo que `decifrar` consegue recuperar o _IV_ sem necessitar de um campo separado na base de dados. 
- Finalmente, os campos numéricos (salário por hora, salário líquido, salário bruto) são serializados como _string_ antes de serem cifrados, usando os métodos auxiliares `cifrarFloat` e `decifrarFloat`, porque o _GCM_ opera sobre _bytes_ arbitrários e a conversão `double → String → bytes → cifra` é completamente reversível.

==== Integração no FuncionarioDAO

O `FuncionarioDAO` é o único _DAO_ do sistema que usa a `CifraUtil`, por ser o único a persistir dados pessoais sensíveis, consoante o REQ-012. A integração ocorre em três pontos: na leitura (método `buildFromRow`), na atualização (método `put`) e na inserção (método `insert`). Em todos os casos, os campos sensíveis são sempre *decifrados ao ler* e *cifrados ao escrever*, de forma transparente para as camadas superiores.

O _schema_ _SQL_ da tabela `Funcionario` reflete esta decisão, já que todos os campos sujeitos a cifra são declarados como `VARCHAR` com largura suficiente para acomodar o criptograma codificado em _Base64_ (cujo comprimento é superior ao do texto original), com comentários _SQL_ que documentam explicitamente quais campos são cifrados:

```sql
CREATE TABLE IF NOT EXISTS Funcionario (
    id                  INT           NOT NULL AUTO_INCREMENT,
    nome                VARCHAR(200)  NOT NULL,  -- cifrado
    telemovel           VARCHAR(100)  NOT NULL,  -- cifrado
    email               VARCHAR(300)  NOT NULL,  -- cifrado
    data_nascimento     VARCHAR(100)  NULL,       -- cifrado (formato ISO: yyyy-MM-dd)
    NISS                VARCHAR(100)  NOT NULL,  -- cifrado
    NIF                 VARCHAR(100)  NOT NULL,  -- cifrado
    NUS                 VARCHAR(100)  NOT NULL,  -- cifrado
    IBAN                VARCHAR(100)  NOT NULL,  -- cifrado
    salario_hora        VARCHAR(100)  NOT NULL,  -- cifrado (float serializado)
    salario_liquido     VARCHAR(100)  NOT NULL,  -- cifrado (float serializado)
    salario_bruto       VARCHAR(100)  NOT NULL,  -- cifrado (float serializado)
    horas_extra         INT           NOT NULL DEFAULT 0,
    numero_porta        VARCHAR(100)  NOT NULL,  -- cifrado
    rua                 VARCHAR(400)  NOT NULL,  -- cifrado
    localidade          VARCHAR(200)  NOT NULL,  -- cifrado
    codigo_postal       VARCHAR(100)  NOT NULL,  -- cifrado
    PRIMARY KEY (id)
);
```

O campo `horas_extra` é o único campo numérico não cifrado, por se tratar de um dado operacional não pessoal e não financeiro. O campo `data_nascimento`, apesar de ser do tipo `DATE` no modelo de domínio _Java_, é armazenado como `VARCHAR(100)` na base de dados pois a data é serializada como _string ISO-8601_ (`"1990-05-15"`) antes de ser cifrada, e desserializada de volta para `LocalDate` após decifragem.

O método `buildFromRow` ilustra claramente o padrão de leitura com decifragem:

```java
private Funcionario buildFromRow(ResultSet rs) throws SQLException {
    String dnRaw = rs.getString("data_nascimento");
    java.time.LocalDate dn = dnRaw == null ? null
        : java.time.LocalDate.parse(CifraUtil.decifrar(dnRaw));
    return new Funcionario(
              rs.getInt("id"),
              CifraUtil.decifrar(rs.getString("nome")),
              CifraUtil.decifrar(rs.getString("telemovel")),
              CifraUtil.decifrar(rs.getString("email")),
              dn,
              CifraUtil.decifrar(rs.getString("NISS")),
              CifraUtil.decifrar(rs.getString("NIF")),
              CifraUtil.decifrar(rs.getString("NUS")),
              CifraUtil.decifrar(rs.getString("IBAN")),
              (float) CifraUtil.decifrarFloat(rs.getString("salario_hora")),
              (float) CifraUtil.decifrarFloat(rs.getString("salario_liquido")),
              (float) CifraUtil.decifrarFloat(rs.getString("salario_bruto")),
              rs.getInt("horas_extra"),
              CifraUtil.decifrar(rs.getString("numero_porta")),
              CifraUtil.decifrar(rs.getString("rua")),
              CifraUtil.decifrar(rs.getString("localidade")),
              CifraUtil.decifrar(rs.getString("codigo_postal")));
}
```

De forma análoga, o método `insert` aplica `CifraUtil.cifrar` e `CifraUtil.cifrarFloat` em cada campo sensível antes de os passar ao `PreparedStatement`:

```java
public int insert(Funcionario value) {
    String sql = """
              INSERT INTO Funcionario (nome, telemovel, email, data_nascimento, NISS, NIF,
                   NUS, IBAN, salario_hora, salario_liquido, salario_bruto, horas_extra,
                   numero_porta, rua, localidade, codigo_postal)
              VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
              """;
    try (Connection c = ConnectionFactory.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1,  CifraUtil.cifrar(value.getNome()));
        ps.setString(2,  CifraUtil.cifrar(value.getTelemovel()));
        ps.setString(3,  CifraUtil.cifrar(value.getEmail()));
        ps.setString(4,  value.getData_nascimento() == null ? null
                             : CifraUtil.cifrar(value.getData_nascimento().toString()));
        ps.setString(5,  CifraUtil.cifrar(value.getNISS()));
        ps.setString(6,  CifraUtil.cifrar(value.getNIF()));
        ps.setString(7,  CifraUtil.cifrar(value.getNUS()));
        ps.setString(8,  CifraUtil.cifrar(value.getIBAN()));
        ps.setString(9,  CifraUtil.cifrarFloat(value.getSalario_hora()));
        ps.setString(10, CifraUtil.cifrarFloat(value.getSalario_liquido()));
        ps.setString(11, CifraUtil.cifrarFloat(value.getSalario_bruto()));
        ps.setInt(12,    value.getHoras_extra());
        ps.setString(13, CifraUtil.cifrar(value.getNumero_porta()));
        ps.setString(14, CifraUtil.cifrar(value.getRua()));
        ps.setString(15, CifraUtil.cifrar(value.getLocalidade()));
        ps.setString(16, CifraUtil.cifrar(value.getCodigo_postal()));
        // ...
    }
}
```

Esta abordagem garante que, mesmo que um atacante obtenha acesso direto à base de dados, os dados pessoais dos funcionários permanecem ilegíveis sem a posse da chave `ENCRYPTION_KEY`. A cifra é transparente para todas as camadas acima do _DAO_: a facade `SFuncionariosFacade` e os controllers _REST_ trabalham sempre com objetos `Funcionario` já decifrados, nunca com criptogramas.

=== Gestão de ligações JDBC — ConnectionFactory

A classe `ConnectionFactory` centraliza toda a lógica de configuração e obtenção de ligações à base de dados, lendo os parâmetros de ligação a partir de variáveis de ambiente com valores de _fallback_ para o desenvolvimento local:

```java
public final class ConnectionFactory {

    private static final String HOST     = env("DB_HOST", "localhost");
    private static final String PORT     = env("DB_PORT", "3306");
    public  static final String DATABASE = env("DB_NAME", "EcoRide");
    public  static final String USERNAME = env("DB_USER", "ecoride");
    public  static final String PASSWORD = env("DB_PASS", "EcoRide123!");

    public static final String URL = "jdbc:mysql://"
            + HOST + ":" + PORT + "/" + DATABASE
            + "?createDatabaseIfNotExist=true"
            + "&useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }

    private ConnectionFactory() {}

    public static Connection get() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new EcoRideException("Não foi possível obter ligação à BD", e);
        }
    }
}
```

O _URL_ de ligação inclui três parâmetros de configuração notáveis. O parâmetro `'serverTimezone=UTC'` normaliza o tratamento de datas e _timestamps_ entre o servidor _MySQL_ e a _JVM_, evitando discrepâncias causadas por fusos horários diferentes. O parâmetro `'allowPublicKeyRetrieval=true'` é necessário para compatibilidade com _MySQL 8.4_ em ligações sem _SSL_, que é o caso do ambiente de desenvolvimento containerizado. Por fim, `'createDatabaseIfNotExist=true'` garante que a base de dados é criada automaticamente se não existir, embora na prática o _Docker Compose_ já a crie através das variáveis de ambiente do contentor MySQL.

Todas as ligações são obtidas e fechadas por bloco `try-with-resources` em cada operação _DAO_, garantindo a libertação de recursos mesmo em caso de exceção. Esta abordagem de _ligação por operação_ é simples e correta para o volume de dados e de utilizadores concorrentes esperados num sistema de gestão de uma oficina, embora para sistemas de maior escala se recomende a adoção de um pool de ligações.

=== Transacionalidade nas operações de escrita complexas

A entidade `OrdemServico` é a mais complexa do sistema, pois para além da tabela base `OrdemServico`, envolve até cinco tabelas dependentes — `Pagamento`, `Diagnostico`, `Conserto`, `OrdemServico_Acessorio`, e as respetivas tabelas de junção `Diagnostico_Reparacao`, `Diagnostico_PecaOrcamento`, `Conserto_Reparacao` e `Conserto_PecaUsada`. Uma operação de atualização que escreva em múltiplas tabelas sem garantias de atomicidade corre o risco de deixar a base de dados num estado inconsistente se ocorrer uma falha a meio. Para evitar este cenário, a operação `put` do `OrdemServicoDAO` é implementada com uma transação explícita:

```java
@Override
public OrdemServico put(Integer key, OrdemServico value) {
    OrdemServico prev = get(key);
    try (Connection c = ConnectionFactory.get()) {
        c.setAutoCommit(false);
        try {
            updateBase(c, key, value);
            clearChildren(c, key);
            insertAcessorios(c, key, value.getRegisto().getAcessorios());
            if (value.getDiagnostico() != null)
                insertDiagnostico(c, key, value.getDiagnostico());
            if (value.getConserto() != null)
                insertConserto(c, key, value.getConserto());
            if (value.getPagamento() != null)
                insertPagamento(c, key, value.getPagamento());
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw new EcoRideException("Erro a gravar OS " + key, e);
        } finally {
            c.setAutoCommit(true);
        }
    } catch (SQLException e) {
        throw new EcoRideException("Erro de ligação ao gravar OS " + key, e);
    }
    return prev;
}
```

A estratégia adotada para atualização é a de *delete-then-insert* (método `clearChildren` seguido de `insert*`). Em vez de calcular a diferença entre o estado anterior e o novo e executar _updates_ cirúrgicos, apagam-se todas as linhas dependentes e reinserem-se com base no estado atual do objeto _Java_. Esta abordagem elimina a complexidade de gestão de _diffs_, reduz a probabilidade de _bugs_ subtis em casos de atualização parcial, e é perfeitamente eficiente para os volumes de dados esperados (uma OS raramente tem mais de uma dezena de peças ou reparações associadas).

=== Esquema da base de dados — decisões de modelação

O _schema SQL_ foi concebido para refletir fielmente o modelo de dados definido na fase de análise, incorporando mecanismos nativos do _MySQL_ para garantir integridade e consistência. A seguir descrevem-se as decisões de modelação mais significativas.

==== Tabelas de junção com preservação de ordem

Para coleções ordenadas, como a lista de acessórios de uma OS ou os _stocks_ associados a uma encomenda, são usadas tabelas de junção com um campo `ordem` inteiro que preserva a posição na lista Java original. Esta decisão implementa o REQ-043, que exige a nomeação dos itens deixados na entrega da trotinete:

```sql
CREATE TABLE IF NOT EXISTS OrdemServico_Acessorio (
    idOS  INT          NOT NULL,
    ordem INT          NOT NULL,
    valor VARCHAR(255) NOT NULL,
    PRIMARY KEY (idOS, ordem),
    FOREIGN KEY (idOS) REFERENCES OrdemServico(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Encomenda_EntradaStock (
    idEncomenda INT NOT NULL,
    ordem       INT NOT NULL,
    codStock    INT NOT NULL,
    PRIMARY KEY (idEncomenda, ordem),
    FOREIGN KEY (idEncomenda) REFERENCES Encomenda(id) ON DELETE CASCADE,
    FOREIGN KEY (codStock)    REFERENCES Stock(id)     ON DELETE CASCADE
);
```

A chave primária composta `(idOS, ordem)` impede valores duplicados e garante que a ordem é única para cada OS. Ao ler os acessórios, o _DAO_ ordena por `ORDER BY ordem`, reconstituindo a `List<String>` Java na ordem original. O `ON DELETE CASCADE` elimina automaticamente todos os acessórios quando a OS correspondente é apagada.

==== Stock com máquina de estados própria

O _stock_ de peças modela um ciclo de vida complexo com sete estados possíveis, desde a encomenda inicial até à devolução ao fornecedor ou ao uso numa reparação, em conformidade com o REQ-018 e o REQ-024:

```sql
CREATE TABLE IF NOT EXISTS Stock (
    id           INT   NOT NULL AUTO_INCREMENT,
    preco_compra FLOAT NOT NULL,
    codPeca      INT   NOT NULL,
    data_chegada DATE  NULL,
    quantidade   INT   NOT NULL,
    garantia     DATE  NULL,
    estado       ENUM(
                     'StockEncomendado',
                     'StockEmArmazem',
                     'StockComPossivelDefeito',
                     'StockPendenteDeDevolucao',
                     'StockEnviadoParaFornecedor',
                     'StockDevolvidoFornecedor',
                     'StockinvalidoParaDevolucao',
                     'StockUsadoConserto'
                 ) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (codPeca) REFERENCES Peca(id)
);
```

O campo `data_chegada` é `NULL` para _stocks_ em estado `StockEncomendado` — ou seja, peças que foram encomendadas mas ainda não chegaram ao armazém. Este uso de `NULL` como marcador semântico é documentado diretamente no _schema_ através de comentários _SQL_, tornando a intenção explícita para qualquer desenvolvedor que consulte o _schema_ no futuro.

=== Camada de lógica de negócio — Facades e subsistemas

Cada um dos oito subsistemas da aplicação tem uma _Facade_ dedicada que encapsula as suas regras de negócio e expõe uma interface tipada à camada de apresentação. As _facades_ recebem os _DAOs_ necessários via o método `getInstance()` de cada _Singleton_, e as suas operações não têm dependências entre si — a coordenação de operações que envolvem múltiplos subsistemas é feita na `EcoRideController`.

==== Máquina de estados encapsulada na enumeração EstadoOS

A lógica de transição de estados da OS é encapsulada diretamente na enumeração `EstadoOS`, através do método `podeTransicionar` e o objetivo é que este mecanismo implemente as regras de fluxo de trabalho definidas nos REQ-031, REQ-036, REQ-038 e REQ-042.

```java
public enum EstadoOS {
    PendenteDiagnostico,
    PendenteAprovacaoOrcamento,
    PendenteReparacao,
    AguardarPecas,
    PendentePagamento,
    ClienteNotificado,
    Paga,
    OrcamentoNaoAprovado,
    Eliminada;

    public boolean podeTransicionar(EstadoOS destino) {
        return switch (this) {
            case PendenteDiagnostico        -> destino == PendenteAprovacaoOrcamento;
            case PendenteAprovacaoOrcamento -> destino == PendenteReparacao
                                           || destino == OrcamentoNaoAprovado;
            case PendenteReparacao          -> destino == PendentePagamento
                                           || destino == AguardarPecas;
            case AguardarPecas              -> destino == PendenteReparacao;
            case PendentePagamento          -> destino == ClienteNotificado;
            case ClienteNotificado          -> destino == Paga;
            default                         -> false;
        };
    }
}
```

Esta abordagem concentra num único ponto toda a lógica de transições válidas, facilitando a manutenção: qualquer alteração às regras de transição — por exemplo, permitir que uma OS no estado `PendenteReparacao` transite diretamente para `Eliminada` — requer modificar apenas este método, sem tocar nas facades ou controllers. A facade `SOrdensServicoFacade` consome este método de forma consistente em todas as operações que alteram estado:

```java
@Override
public boolean aprovarOrcamentoOS(int id) {
    OrdemServico os = ordemServicoDAO.get(id);
    if (os == null)
        throw new EcoRideException("OS " + id + " não encontrada.");
    if (os.getDiagnostico() == null)
        throw new EcoRideException("Não é possível aprovar o orçamento sem diagnóstico.");
    if (!os.getEstado().podeTransicionar(EstadoOS.PendenteReparacao))
        return false;

    Diagnostico diag = os.getDiagnostico();
    diag.setAprovado(true);
    os.setDiagnostico(diag);
    os.setEstado(EstadoOS.PendenteReparacao);
    ordemServicoDAO.put(id, os);
    return true;
}

@Override
public boolean marcarAguardarPecasOS(int id, int id_funcionario) {
    OrdemServico os = ordemServicoDAO.get(id);
    if (os == null)
        throw new EcoRideException("OS " + id + " não encontrada.");
    if (os.getCodMecanico() != id_funcionario)
        throw new EcoRideException("Não é o responsável por esta OS.");
    return alterarEstadoOS(id, EstadoOS.AguardarPecas);
}
```

==== Controlo de responsabilidade e validação de negócio

A facade implementa igualmente regras de negócio que vão além da simples validação de estado. Uma regra particularmente relevante é o controlo de responsabilidade previsto no REQ-003 e no REQ-004: apenas o mecânico atribuído a uma OS pode registar o diagnóstico, o conserto ou marcar a OS como aguardando peças. Se a OS ainda não tiver mecânico atribuído no momento do registo do diagnóstico, o mecânico que realiza a operação é automaticamente associado, garantindo que dois mecânicos não iniciam a mesma OS simultaneamente:

```java
@Override
public Diagnostico registarDiagnosticoOS(int idOS,
        Map<Integer, Integer> pecasQuantidades, List<Integer> reparacoes,
        float orcamento, String descricao, int id_funcionario) {

    if (pecasQuantidades == null || reparacoes == null
            || descricao == null || descricao.isBlank())
        throw new EcoRideException("Dados de diagnóstico incompletos ou inválidos.");

    OrdemServico os = ordemServicoDAO.get(idOS);
    if (os == null)
        throw new EcoRideException("OS " + idOS + " não encontrada.");

    // Auto-atribuição: primeiro mecânico a diagnosticar fica responsável (REQ-004)
    if (os.getCodMecanico() == null)
        os.setCodMecanico(id_funcionario);
    if (os.getCodMecanico() != id_funcionario)
        throw new EcoRideException("Não é o responsável por esta OS.");
    if (!os.getEstado().podeTransicionar(EstadoOS.PendenteAprovacaoOrcamento))
        throw new EcoRideException("Transição de estado inválida para a OS " + idOS);

    Diagnostico diag = new Diagnostico(descricao, reparacoes, pecasQuantidades, orcamento);
    os.setDiagnostico(diag);
    os.setConserto(null); // reset do conserto se existir diagnóstico anterior
    os.setEstado(EstadoOS.PendenteAprovacaoOrcamento);
    ordemServicoDAO.put(idOS, os);
    return diag;
}
```

Esta regra de negócio não poderia ser implementada corretamente na camada de apresentação sem acesso ao estado completo da OS, o que ilustra precisamente o valor da separação de camadas: a camada de apresentação invoca o método da facade passando o identificador do funcionário autenticado (retirado da sessão), e a facade verifica as regras pertinentes antes de persistir qualquer alteração.

==== Registo de pagamento com preservação de dados de notificação

O fluxo de pagamento de uma OS ilustra outro exemplo de regra de negócio não trivial: antes de registar o pagamento, o cliente tem de ter sido notificado conforme o REQ-032, e os dados dessa notificação (data e confirmação) devem ser preservados quando o pagamento é registado ao abrigo do REQ-033:

```java
@Override
public boolean registarPagamentoOS(int id_OS, Pagamento pagamento) {
    OrdemServico os = ordemServicoDAO.get(id_OS);
    if (os == null)
        throw new EcoRideException("OS " + id_OS + " não encontrada.");
    if (os.getEstado() != EstadoOS.ClienteNotificado)
        throw new EcoRideException("O cliente deve ser notificado antes do pagamento.");

    Pagamento existente = os.getPagamento();
    if (existente == null || !existente.isClienteNotificado())
        throw new EcoRideException("Registo de notificação em falta.");
    if (!os.getEstado().podeTransicionar(EstadoOS.Paga))
        return false;

    // Preserva os dados de notificação ao juntar com os dados de pagamento
    pagamento.setClienteNotificado(existente.isClienteNotificado());
    pagamento.setDataNotificacao(existente.getDataNotificacao());
    os.setPagamento(pagamento);
    os.setEstado(EstadoOS.Paga);
    ordemServicoDAO.put(id_OS, os);
    return true;
}
```

=== Camada de apresentação — Servidor Javalin e controllers REST

A classe `EcoRideAPI` constitui o ponto de entrada do servidor _backend_ e é responsável pela inicialização de todos os componentes da camada de apresentação. A escolha do *Javalin* como _framework REST_ assenta na sua leveza, na ausência de reflexão ou geração de código em tempo de execução, e na sua _API_ funcional que torna o registo de rotas explícito e legível.

```java
public static void main(String[] args) {
    DBInitializer.run();

    IEcoRideController facade        = new EcoRideController();
    GestorSessoes      gestorSessoes = new GestorSessoes();

    Javalin app = Javalin.create(config -> {
        config.showJavalinBanner = false;
        config.jsonMapper(new JavalinJackson().updateMapper(m ->
            m.registerModule(new JavaTimeModule())
             .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        ));
        config.bundledPlugins.enableCors(cors ->
            cors.addRule(it -> it.allowHost("http://localhost:8080"))
        );
    }).start(7000);

    // Middleware de autenticação aplicado a todas as rotas /api/*
    app.before("/api/*", ctx -> {
        String token = ctx.header("Authorization");
        if (token == null) throw new UnauthorizedResponse("Token em falta");
        SessaoUtilizador sessao = gestorSessoes.validar(token);
        ctx.attribute("sessao", sessao);
    });

    GlobalExceptionHandler.register(app);

    new AuthController(facade, gestorSessoes).register(app);
    new UsersController(facade).register(app);
    new ClientesController(facade).register(app);
    new OrdemServicoController(facade).register(app);
    new StockController(facade).register(app);
    new FuncionariosController(facade).register(app);
    new ReparacoesController(facade).register(app);
    new NotificacoesController(facade).register(app);
    new FinanceiroController(facade).register(app);
    new MainController(facade).register(app);
}
```

O Jackson é configurado com o módulo `JavaTimeModule` para suportar nativamente os tipos de data e hora do _Java_ 8+ (`LocalDateTime`, `LocalDate`), e com `WRITE_DATES_AS_TIMESTAMPS` desativado para usar strings ISO-8601 em vez de timestamps Unix. Esta decisão é importante para a interoperabilidade com o _frontend_ TypeScript, que consome as datas como strings legíveis e as formata para apresentação ao utilizador. O middleware `app.before("/api/*")` implementa autenticação centralizada em todas as rotas protegidas sem repetição de código em cada controller: valida o token presente no header `Authorization`, obtém a sessão correspondente e armazena-a nos atributos do contexto para uso posterior pelos handlers. Esta implementação cumpre o REQ-009, que exige que o sistema disponibilize perfis de utilizador distintos com capacidades diferenciadas por cargo.

=== Gestão de sessões e controlo de acesso

O `GestorSessoes` implementa um sistema de tokens de sessão baseado em UUIDs armazenados em memória, com verificação de expiração, em conformidade com o REQ-009:

```java
public class GestorSessoes {

    private final Map<String, SessaoUtilizador> sessoes = new ConcurrentHashMap<>();

    public String criarSessao(SessaoUtilizador sessao) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, sessao);
        return token;
    }

    public SessaoUtilizador validar(String token) throws UnauthorizedResponse {
        SessaoUtilizador s = sessoes.get(token);
        if (s == null || s.expirou()) {
            sessoes.remove(token);
            throw new UnauthorizedResponse("Sessão inválida ou expirada");
        }
        return s;
    }

    public void terminarSessao(String token) {
        sessoes.remove(token);
    }

    public static SessaoUtilizador sessao(Context ctx) throws UnauthorizedResponse {
        SessaoUtilizador s = ctx.attribute("sessao");
        if (s == null) throw new UnauthorizedResponse("Sessão não encontrada ou expirada");
        return s;
    }

    public static SessaoUtilizador verifica_cargo(Context ctx,
                                                   Cargo... cargos)
            throws ForbiddenResponse, UnauthorizedResponse {
        SessaoUtilizador s = sessao(ctx);
        if (Arrays.stream(cargos).noneMatch(c -> c == s.getCargo()))
            throw new ForbiddenResponse("Sem permissão para esta operação");
        return s;
    }
}
```

O uso de `ConcurrentHashMap` garante thread-safety sem necessidade de blocos `synchronized` explícitos: as operações `get` e `put` são atómicas, o que é suficiente para o padrão de acesso concorrente esperado. O método estático `verifica_cargo` é invocado no início de cada handler de controller para verificar simultaneamente autenticação e autorização, lançando `UnauthorizedResponse` (HTTP 401) se a sessão for inválida ou `ForbiddenResponse` (HTTP 403) se o cargo não tiver permissão para a operação.

=== Tratamento centralizado de exceções

O `GlobalExceptionHandler` mapeia cada tipo de exceção para o código _HTTP_ semanticamente correto, garantindo que todas as respostas de erro seguem o mesmo formato e que a distinção entre erros de negócio (400), de autenticação (401), de autorização (403) e erros internos (500) é sempre consistente:

```java
public class GlobalExceptionHandler {

    private GlobalExceptionHandler() {}

    public static void register(Javalin app) {

        // 400 — violação de regra de negócio (transição inválida, dados em falta, etc.)
        app.exception(EcoRideException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request", e.getMessage()))
        );

        // 400 — parâmetro numérico inválido (ex: GET /api/os/abc)
        app.exception(NumberFormatException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request",
                "Parâmetro numérico inválido: " + e.getMessage()))
        );

        // 400 — argumento ilegal passado a método da camada de negócio
        app.exception(IllegalArgumentException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request", e.getMessage()))
        );

        // 401 — token em falta, inválido ou sessão expirada
        app.exception(UnauthorizedResponse.class, (e, ctx) ->
            ctx.status(401).json(new ErroResponse(401, "Unauthorized", e.getMessage()))
        );

        // 403 — sessão válida mas cargo sem permissão para esta operação
        app.exception(ForbiddenResponse.class, (e, ctx) ->
            ctx.status(403).json(new ErroResponse(403, "Forbidden", e.getMessage()))
        );

        // 500 — erro inesperado (bug, falha de BD não antecipada, etc.)
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json(new ErroResponse(500,
                "Internal Server Error", "Erro interno do servidor"));
        });
    }

    public record ErroResponse(int status, String erro, String mensagem) {}
}
```

A `EcoRideException` é a exceção de domínio do projeto: é lançada pelas facades sempre que uma regra de negócio é violada (por exemplo, tentativa de transição de estado inválida, registo de diagnóstico sem ser o responsável, ou dados obrigatórios em falta). O facto de esta exceção ser mapeada para _HTTP_ 400 garante que o _frontend_ consegue distinguir entre erros de negócio — com mensagem legível ao utilizador — e erros internos do servidor, que não devem expor detalhes de implementação.

=== _Frontend_ — React com TypeScript

==== Estrutura e organização da aplicação

O _frontend_ é uma _Single-Page Application_ (SPA) desenvolvida com React 18, TypeScript e Vite, em conformidade com o REQ-044 que exige um "quadro de estado em tempo real" acessível na receção. A escolha de TypeScript é particularmente relevante neste contexto: os tipos definidos em `lib/types.ts` espelham as entidades do _backend_ e permitem ao compilador verificar em tempo de compilação que os dados recebidos da _API_ são usados corretamente em toda a interface. Esta verificação estática previne uma classe inteira de bugs de tempo de execução causados por acesso a campos inexistentes ou tipos incorretos.

A estrutura de ficheiros do _frontend_ reflete deliberadamente a organização por subsistemas do _backend_, facilitando a navegação e a manutenção:

```
src/
├── App.tsx                        — Router principal e providers globais
├── context/
│   └── AuthContext.tsx            — Estado de autenticação (React Context)
├── services/
│   ├── api.ts                     — Cliente HTTP genérico tipado
│   └── auth.ts                    — Operações de autenticação e sessão
├── lib/
│   ├── types.ts                   — Tipos TypeScript do domínio
│   ├── permissions.ts             — Lógica RBAC no lado do cliente
│   ├── validators.ts              — Validação de formulários
│   └── format.ts                  — Utilitários de formatação de dados
├── components/
│   ├── layout/
│   │   ├── ProtectedLayout.tsx    — Guard de rotas com RBAC
│   │   ├── AppSidebar.tsx         — Navegação lateral adaptativa
│   │   └── AppHeader.tsx          — Cabeçalho com info de sessão
│   └── ui/                        — Componentes shadcn/ui (copiados para o projeto)
└── pages/
    ├── Login.tsx
    ├── Dashboard.tsx
    ├── Funcionarios.tsx
    ├── Clientes.tsx
    ├── Trotinetes.tsx
    ├── Fornecedores.tsx
    ├── Reparacoes.tsx
    ├── Financeiro.tsx
    ├── Alertas.tsx
    ├── OS/
    │   ├── List.tsx               — Lista de OS ativas e historial
    │   ├── New.tsx                — Criação de nova OS
    │   └── Detail.tsx             — Detalhe e gestão do ciclo de vida
    └── Stock/
        ├── Pecas.tsx
        ├── Entradas.tsx
        ├── Defeitos.tsx
        ├── Devolucoes.tsx
        └── Encomendas.tsx
```

==== Routing declarativo e proteção de rotas

O `App.tsx` define toda a estrutura de navegação da aplicação usando o _React Router_ v6, com o componente `ProtectedLayout` a envolver cada grupo de rotas que requer autenticação. Este mecanismo concretiza o REQ-009 no lado do cliente, impedindo que utilizadores não autorizados acedam a áreas para as quais o seu cargo não tem permissão:

```typescript
const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner position="top-right" richColors />
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />

            <Route element={<ProtectedLayout area="os" />}>
              <Route path="/os"        element={<OSList />} />
              <Route path="/os/nova"   element={<NewOS />} />
              <Route path="/os/:id"    element={<OSDetail />} />
            </Route>

            <Route element={<ProtectedLayout area="stock" />}>
              <Route path="/stock" element={<Navigate to="/stock/pecas" replace />} />
              <Route path="/stock/pecas"      element={<StockPecas />} />
              <Route path="/stock/entradas"   element={<StockEntradas />} />
              <Route path="/stock/defeitos"   element={<StockDefeitos />} />
              <Route path="/stock/devolucoes" element={<StockDevolucoes />} />
              <Route path="/stock/encomendas" element={<StockEncomendas />} />
            </Route>

            <Route element={<ProtectedLayout area="financeiro" />}>
              <Route path="/financeiro" element={<Financeiro />} />
            </Route>

            <Route path="*" element={<NotFound />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);
```

O `ProtectedLayout` implementa o _guard_ de rotas verificando três condições em sequência: se a aplicação ainda está a carregar o estado de autenticação (mostra indicador de carregamento), se o utilizador está autenticado (redireciona para _login_ preservando a rota pretendida), e se o utilizador tem permissão para aceder à área especificada (redireciona para o _dashboard_):

```typescript
export function ProtectedLayout({ area }: ProtectedLayoutProps) {
  const { user, role, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="text-sm text-muted-foreground">A carregar…</div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (area && !canAccess(role, area)) {
    return <Navigate to="/" replace />;
  }

  return (
    <SidebarProvider>
      <div className="flex min-h-screen w-full bg-background">
        <AppSidebar />
        <SidebarInset className="flex flex-1 flex-col">
          <AppHeader />
          <main className="flex-1 p-4 md:p-6 animate-fade-in">
            <Outlet />
          </main>
        </SidebarInset>
      </div>
    </SidebarProvider>
  );
}
```

O `state={{ from: location.pathname }}` preserva a rota que o utilizador tentava aceder antes de ser redirecionado para o _login_, permitindo redirecioná-lo para o destino pretendido após autenticar-se com sucesso. O componente `Outlet` do _React Router v6_ renderiza o componente filho correspondente à rota ativa, funcionando como _slot_ de conteúdo principal da aplicação.

==== RBAC no lado do cliente

As permissões de acesso por área funcional são declaradas em `permissions.ts` como uma tabela estática imutável, espelhando fielmente as regras _RBAC_ definidas na análise de requisitos ao abrigo do REQ-009 e do REQ-010:

```typescript
export type Area =
  | "dashboard" | "funcionarios" | "financeiro" | "clientes"
  | "trotinetes" | "os" | "reparacoes" | "stock" | "fornecedores" | "alertas";

const PERMISSIONS: Record<Area, Role[]> = {
  dashboard:    ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"],
  funcionarios: ["GERENTE"],
  financeiro:   ["GERENTE"],
  clientes:     ["GERENTE", "SECRETARIA"],
  trotinetes:   ["GERENTE", "SECRETARIA"],
  os:           ["GERENTE", "SECRETARIA", "MECANICO"],
  reparacoes:   ["GERENTE"],
  stock:        ["GERENTE", "GESTOR_STOCK"],
  fornecedores: ["GERENTE", "GESTOR_STOCK"],
  alertas:      ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"],
};

export function canAccess(role: Role | undefined, area: Area): boolean {
  if (!role) return false;
  return PERMISSIONS[area].includes(role);
}
```

É importante salientar que o _RBAC_ no cliente é uma camada de conveniência, não de segurança: serve para evitar que o utilizador veja e tente aceder a funcionalidades para as quais não tem permissão, melhorando a experiência de utilização. A validação definitiva e obrigatória de autorização ocorre sempre no _backend_, através da chamada a `GestorSessoes.verifica_cargo` no início de cada _handler_. Mesmo que o código de _frontend_ seja manipulado, o _backend_ rejeita todos os pedidos não autorizados com _HTTP_ 403.

==== Cliente HTTP genérico e tipado

O módulo `api.ts` centraliza todos os pedidos _HTTP_ ao _backend_ numa única função genérica que trata consistentemente autenticação, erros e respostas sem conteúdo:

```typescript
const TOKEN_KEY = "ecoride-token";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const token = localStorage.getItem(TOKEN_KEY);
  const res = await fetch(`/api${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { "Authorization": token } : {}),
      ...init?.headers,
    },
    credentials: "include",
    ...init,
  });

  if (res.status === 401) {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem("ecoride-user");
    window.location.replace("/login");
    throw new Error("Sessão expirada. Faz login novamente.");
  }

  if (!res.ok) {
    const text = await res.text();
    let msg = text || `Erro ${res.status}`;
    try {
      const json = JSON.parse(text) as { mensagem?: string; message?: string };
      msg = json.mensagem ?? json.message ?? msg;
    } catch { /* resposta não é JSON — usa texto cru */ }
    throw new Error(msg);
  }

  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

export const api = {
  get:    <T>(path: string)                => request<T>(path),
  post:   <T>(path: string, body: unknown) => request<T>(path, {
                                               method: "POST",
                                               body: JSON.stringify(body) }),
  patch:  <T>(path: string, body: unknown) => request<T>(path, {
                                               method: "PATCH",
                                               body: JSON.stringify(body) }),
  delete: (path: string)                   => request<void>(path, {
                                               method: "DELETE" }),
};
```

A função `request` é genérica no tipo `T`, o que permite ao _TypeScript_ inferir e verificar o tipo do resultado em todos os pontos de chamada. O tratamento de _HTTP_ 401 força um _logout_ imediato e redirecionamento para o _login_, garantindo que sessões expiradas não deixam o utilizador num estado inconsistente em que as chamadas à _API_ falham silenciosamente. A extração da mensagem de erro tenta primeiro o _parse JSON_ (usando o campo `mensagem` do `ErroResponse` do _backend_) e recorre ao texto cru como _fallback_, garantindo que a mensagem de erro apresentada ao utilizador é sempre a mais informativa disponível.

==== Contexto de autenticação e persistência de sessão

O `AuthContext` disponibiliza o estado de autenticação a toda a árvore de componentes React através do padrão _Context API_, evitando a propagação de props através de múltiplos níveis de componentes intermediários:

```typescript
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser]       = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setUser(getCurrentUser());
    setLoading(false);
  }, []);

  const login = useCallback(async (identificador: string, password: string) => {
    const u = await doLogin(identificador, password);
    setUser(u);
  }, []);

  const logout = useCallback(() => {
    doLogout();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, role: user?.cargo, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth deve ser usado dentro de AuthProvider");
  return ctx;
}
```

O `useEffect` inicial lê o utilizador persistido em `localStorage` pela sessão anterior, o que permite que o estado de autenticação seja restaurado após o utilizador recarregar a página sem necessidade de novo login. O estado `loading` é crucial para prevenir _flashes_ de conteúdo durante a hidratação inicial: enquanto o estado de autenticação está a ser determinado, o `ProtectedLayout` mostra um indicador de carregamento em vez de redirecionar prematuramente para o login.

== Especificação da _API REST_

A _API REST_  do EcoRide segue as convenções _HTTP_ standard: substantivos nos paths para identificar recursos, verbos _HTTP_ para identificar operações (GET para leitura, POST para criação, PUT para substituição completa, PATCH para atualização parcial, DELETE para remoção), e códigos de status _HTTP_ semânticos. Todos os _endpoints_ protegidos exigem o header `Authorization: <token>` obtido no login. As respostas de erro seguem sempre o formato JSON estruturado:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Descrição legível do erro específico"
}
```

=== Autenticação — /auth

*POST /auth/login* — Autentica o utilizador com identificador e password, devolvendo um _token_ de sessão. Este é o único _endpoint_ público da _API_; todos os demais exigem _token_.

- *Body:* `{ "identificador": string, "password": string }`
- *200:* `{ "token": string, "cargo": string, "idFuncionario": int, "idUtilizador": int }`
- *400:* Credenciais inválidas ou utilizador não encontrado

O _token_ devolvido é um UUID v4 gerado pelo `GestorSessoes` e deve ser incluído no header `Authorization` de todos os pedidos subsequentes, em conformidade com o REQ-009. O campo `cargo` devolve o nome do cargo em português conforme definido na enumeração _Java_ (`Gerente`, `GestorStock`, `Secretaria`, `Mecanico`), e o _frontend_ mapeia-o para o tipo `Role` TypeScript internamente.

=== Clientes — /api/clientes

Estes _endpoints_ implementam o REQ-027 e o REQ-028, que exigem gestão completa de clientes com registo obrigatório de nome, email, telemóvel e NIF.

- *GET /api/clientes* — Lista todos os clientes. Cargos: Gerente, Secretaria.
- *GET /api/clientes/{id}* — Devolve um cliente pelo ID. 404 se não encontrado.
- *POST /api/clientes* — Cria um novo cliente. Body: `{ nome, email, telemovel, nif }`. Devolve 201 com o cliente criado.
- *PUT /api/clientes/{id}* — Atualiza completamente um cliente. Devolve 204.
- *DELETE /api/clientes/{id}* — Remove um cliente. Cargos: apenas Gerente.

*GET /api/clientes/{id}/trotinetes* — Lista todas as trotinetes de um cliente específico, devolvendo uma lista vazia se o cliente não tiver trotinetes registadas.

=== Trotinetes — /api/trotinetes

Estes _endpoints_ implementam o REQ-027 e o REQ-029, que exigem gestão de trotinetes com registo obrigatório de marca, modelo, número de série, tipo de motor e cliente associado.

- *GET /api/trotinetes* — Lista todas as trotinetes. Cargos: Gerente, Secretaria.
- *GET /api/trotinetes/{id}* — Devolve uma trotinete pelo ID. Cargos: Gerente, Secretaria, Mecanico.
- *GET /api/trotinetes/cliente/{id_cliente}* — Lista trotinetes por cliente.
- *POST /api/trotinetes* — Cria trotinete associada a cliente. Body: `{ cod_cliente, modelo, marca, num_serie, tipo_motor }`.
- *PUT /api/trotinetes/{id}* — Atualiza trotinete. 204 em caso de sucesso.
- *DELETE /api/trotinetes/{id}* — Remove trotinete. Cargo: Gerente.

=== Ordens de Serviço — /api/ordensservicos

Este conjunto de _endpoints_ é o mais rico da _API_, refletindo a complexidade da máquina de estados da OS. Cada transição de estado é um _endpoint PATCH_ dedicado, o que torna a _API_ expressiva e autoexplicativa.

*GET /api/ordensservicos* — Lista as OS do sistema, conforme o REQ-026. O parâmetro de query `?historico=true` inclui OS nos estados terminais (Paga e Eliminada); sem esse parâmetro, devolve apenas OS nos estados ativos (PendenteDiagnostico, PendenteAprovacaoOrcamento, PendenteReparacao, AguardarPecas, PendentePagamento, ClienteNotificado). Cargos: Gerente, Secretaria, Mecanico.

*GET /api/ordensservicos/disponiveis* — Lista OS nos estados em que podem ser atribuídas ou trabalhadas por mecânicos: PendenteDiagnostico, PendenteAprovacaoOrcamento, PendenteReparacao e AguardarPecas, em conformidade com o REQ-034. Cargos: Gerente, Mecanico.

*POST /api/ordensservicos* — Cria uma nova OS ao abrigo do REQ-030. O estado inicial é sempre `PendenteDiagnostico`. O campo `codCriador` é preenchido automaticamente a partir da sessão do utilizador autenticado. Body: `{ id_cliente, id_trotinete, descricao, acessorios: string[] }`. O campo `acessorios` implementa o REQ-043, permitindo nomear os itens deixados com a trotinete (carregador, cadeado, etc.). Devolve 201 com a OS criada. Cargos: Gerente, Secretaria, Mecanico.

*PATCH /api/ordensservicos/{id}/diagnostico* — Regista o diagnóstico de uma OS e transita o estado para `PendenteAprovacaoOrcamento`, em conformidade com os REQ-035 e REQ-036. Apenas o mecânico responsável pela OS pode executar esta operação; se a OS não tiver mecânico atribuído, o mecânico que realiza o diagnóstico fica automaticamente associado (REQ-004). Body: `{ descricao, pecasQuantidades: {codPeca: int -> quantidade: int}, reparacoes: int[], orcamento: float }`. Devolve 204. Cargos: Gerente, Mecanico.

*PATCH /api/ordensservicos/{id}/aprovarOrcamento* — Aprova o orçamento do diagnóstico, transitando a OS para `PendenteReparacao`, conforme o REQ-031. Requer que a OS tenha diagnóstico registado. Devolve 204 ou 400 se a transição for inválida. Cargos: Gerente, Secretaria.

*PATCH /api/ordensservicos/{id}/rejeitarOrcamento* — Rejeita o orçamento, transitando a OS para `OrcamentoNaoAprovado`, conforme o REQ-031. Devolve 204. Cargos: Gerente, Secretaria.

*PATCH /api/ordensservicos/{id}/conserto* — Regista o conserto realizado e transita a OS para `PendentePagamento`, ao abrigo dos REQ-037 e REQ-042. Requer diagnóstico aprovado. O campo `checklist` implementa o REQ-041 (verificação obrigatória de segurança antes de terminar a reparação). Body: `{ pecasQuantidades: {codStock: int -> quantidade: int}, reparacoes: int[], checklist: { luzes, pneus, aceleracao, travagem, visor, teste_pratico } }`. Devolve 204. Cargos: Gerente, Mecanico.

*PATCH /api/ordensservicos/{id}/aguardarPecas* — Marca a OS como aguardando peças, transitando para `AguardarPecas`, conforme o REQ-038. Apenas o mecânico responsável pode executar esta ação. Devolve 204. Cargos: Gerente, Mecanico.

*PATCH /api/ordensservicos/{id}/notificarCliente* — Regista a notificação ao cliente de que a OS está pronta para pagamento, transitando para `ClienteNotificado`, conforme o REQ-032. Devolve 204 ou 409 se o cliente já foi notificado. Cargos: Gerente, Secretaria.

*PATCH /api/ordensservicos/{id}/pagar* — Regista o pagamento da OS, transitando para o estado final `Paga`, ao abrigo do REQ-033. O cliente deve ter sido previamente notificado. Body: `{ metodo_pagamento: "NUMERARIO" | "MULTIBANCO" | "MBWAY" }`. Devolve 204 ou 409 se o cliente ainda não foi notificado. Cargos: Gerente, Secretaria.

*DELETE /api/ordensservicos/{id}* — Remove uma OS da base de dados. Devolve 204 ou 404. Cargos: Gerente, Secretaria.

=== Stock — /api/stock

O módulo de _stock_ é estruturado em cinco sub-recursos que correspondem às cinco sub-páginas da interface: peças, entradas, defeitos, devoluções e encomendas. Cada sub-recurso tem os seus próprios _endpoints_ de listagem e criação, com _endpoints_ PATCH adicionais para operações de transição de estado.

*GET /api/stock/pecas* e *POST /api/stock/pecas* — Listagem e criação de peças no catálogo, conforme o REQ-021. Cada peça tem referência, nome, descrição, preço de venda, _stock_ mínimo, fornecedor e meses de garantia. Cargos: Gerente, GestorStock.

*GET /api/stock/entradas* e *POST /api/stock/entradas* — Listagem e criação de entradas de _stock_ (lotes de peças recebidos), conforme o REQ-022. Cada entrada regista o preço de compra, quantidade, data de chegada e associa-se a uma peça do catálogo. Uma entrada de _stock_ criada diretamente transita para o estado `StockEmArmazem`. Cargos: Gerente, GestorStock.

*GET /api/stock/defeitos* e *POST /api/stock/defeitos* — Listagem e registo de defeitos, ao abrigo do REQ-025 e do REQ-040. Ao registar um defeito, o _stock_ correspondente transita para `StockComPossivelDefeito`. Mecânicos podem reportar defeitos descobertos durante reparações. Cargos: Gerente, GestorStock, Mecanico.

*GET /api/stock/devolucoes* e *POST /api/stock/devolucoes* — Listagem e criação de processos de devolução ao fornecedor, conforme o REQ-024. A devolução é criada a partir de um item de _stock_ com defeito, que transita para `StockPendenteDeDevolucao`. Os _endpoints_ PATCH `/enviar` e `/receber` gerem as transições subsequentes do processo de devolução. Cargos: Gerente, GestorStock.

*GET /api/stock/encomendas*, *POST /api/stock/encomendas*, *PATCH /api/stock/encomendas/{id}/enviar* e *PATCH /api/stock/encomendas/{id}/receber* — Gestão completa de encomendas a fornecedores, conforme o REQ-023. Uma encomenda é criada em estado `RASCUNHO`, transitando para `ENVIADA` quando enviada ao fornecedor, e para `RECEBIDA` ao ser confirmada a receção — altura em que os _stocks_ associados transitam de `StockEncomendado` para `StockEmArmazem`. Cargos: Gerente, GestorStock.

=== Funcionários e Utilizadores

*GET, POST, PUT, DELETE /api/funcionarios* e * /api/funcionarios/{id}* — CRUD completo de funcionários ao abrigo dos REQ-011 e REQ-012. A criação de um funcionário cria automaticamente o utilizador de sistema associado com o cargo selecionado. Os campos pessoais e financeiros (nome, data de nascimento, NISS, NIF, NUS, IBAN, salários, morada) são armazenados cifrados na base de dados via AES-256-GCM, sendo transparentemente decifrados antes de devolvidos na resposta. Cargos de gestão: apenas Gerente. Leitura do próprio perfil: qualquer cargo.

*PATCH /api/funcionarios/{id}/horasextra* — Regista horas extra para um funcionário, conforme o REQ-013. Body: `{ horas: int }`. Cargos: Gerente.

*PATCH /api/utilizadores/{id}/password* — Permite ao utilizador autenticado alterar a sua própria _password_, verificando a _password_ atual antes de aceitar a nova. Body: `{ currentPassword, newPassword }`. Cargos: qualquer utilizador autenticado (apenas o próprio).

=== Financeiro, Notificações, Reparações e Fornecedores

*GET /api/financeiro* — Lista os movimentos financeiros, conforme o REQ-014. Com filtragem opcional por período (`?desde=ISO_DATE&ate=ISO_DATE`), que implementa o REQ-015. Cargos: Gerente.

*POST /api/financeiro/salarios* — Regista o pagamento de salário a um funcionário, criando automaticamente o movimento financeiro do tipo `Salario`, ao abrigo do REQ-013. Cargos: Gerente.

*GET /api/notificacoes* — Devolve as notificações do utilizador autenticado, implementando o sistema de alertas previsto no REQ-002 (alertas por mudança de estado da OS) e no REQ-005 (alertas de _stock_). Os _endpoints_ PATCH `/ler` e `/tratar` atualizam o estado de cada notificação. Cargos: todos.

*GET, POST, PUT /api/reparacoes* e *PATCH /api/reparacoes/{id}/desativar* — Gestão do catálogo de reparações (nomenclatura, descrição e preço de mão de obra), conforme os REQ-016 e REQ-017. A desativação marca a reparação com `disponivel=false` sem a apagar, preservando o histórico. Cargos de gestão: Gerente.

*GET, POST, PUT, DELETE /api/fornecedores* — CRUD de fornecedores, conforme o REQ-019 e o REQ-020. Cargos: Gerente, GestorStock.

=== Documentação interativa com Swagger UI

A _API_ está integralmente documentada em dois ficheiros complementares: o `openapi.yaml` servido pelo _nginx_ em `/api-docs` (1000 linhas, no formato _OpenAPI_ 3.0), e o `swagger.yaml` na raiz do repositório (2202 linhas), mais detalhado e orientado ao desenvolvimento direto contra o _backend_ na porta 7000. O serviço `swagger` do _Docker Compose_ instancia o _Swagger UI_ apontando para o `openapi.yaml` servido pelo _frontend_, disponibilizando em `http://localhost:8081` uma interface _web_ interativa que permite explorar todos os _endpoints_, consultar _schemas_ de _request_ e _response_, e enviar pedidos reais ao _backend_. A especificação _OpenAPI_ documenta os _schemas_ de `FuncionarioRequest` e `Funcionario` com todos os campos sensíveis em texto claro — conforme o modelo de domínio — uma vez que a cifra ocorre transparentemente na camada _DAO_ e é invisível para os consumidores da _API_. Esta documentação é particularmente valiosa durante o desenvolvimento e avaliação do sistema, eliminando a necessidade de ferramentas externas como o _Postman_.

== Resultado final

=== Arranque e verificação do sistema

O sistema EcoRide é iniciado com um único comando executado na diretoria raiz do projeto:

```
docker compose up --build
```

A _flag_ `--build` garante que as imagens são recompiladas a partir do código-fonte atual, tendo em conta quaisquer alterações desde o último _build_. O _Docker Compose_ respeita automaticamente a cadeia de dependências definida no `docker-compose.yml`: o _MySQL_ inicia primeiro, o _backend_ aguarda o _healthcheck_ do _MySQL_, o _frontend_ aguarda o _backend_, e o _Swagger UI_ aguarda o _backend_. O serviço `info` é o último a correr, aguardando que o _backend_, o _frontend_ e o _Swagger UI_ estejam operacionais e depois imprimindo no terminal uma tabela com os _URLs_ de acesso.

=== Funcionalidades implementadas

A interface _web_ disponível em `http://localhost:3000` implementa todas as funcionalidades definidas nos requisitos do sistema. O acesso é controlado por cargo: a barra lateral apresenta dinamicamente apenas as secções que o utilizador autenticado tem permissão para aceder, tornando a interface intuitiva para cada perfil, em conformidade com o REQ-009.

O *Dashboard*, conforme o REQ-044, apresenta uma vista consolidada do estado operacional do sistema, com indicadores de OS em curso por estado, alertas pendentes, e métricas financeiras básicas. É acessível a todos os cargos, embora o detalhe apresentado possa variar conforme o perfil.

A secção de *Ordens de Serviço* é a mais rica da interface: permite criar novas OS selecionando cliente e trotinete (REQ-030), listar e filtrar as OS ativas e o historial completo (REQ-026), aceder ao detalhe de cada OS (com informação de diagnóstico, conserto e pagamento), e executar as transições de estado disponíveis para o cargo e estado atual. A página de detalhe apresenta apenas as ações relevantes para o estado atual, guiando o utilizador pelo fluxo de trabalho sem expor operações inaplicáveis.

A secção de *Stock* é composta por cinco sub-páginas, cada uma cobrindo uma fase do ciclo de vida do _stock_: gestão do catálogo de peças (REQ-021), registo de entradas (REQ-022), reporte de defeitos (REQ-025), criação e acompanhamento de devoluções (REQ-024), e gestão completa de encomendas a fornecedores (REQ-023) — desde a criação do rascunho até à confirmação de receção.

As secções de *Funcionários* e *Clientes* oferecem _CRUD_ completo com validação de formulários. O formulário de funcionários inclui todos os campos exigidos pelo REQ-012 (nome, morada, contacto, data de nascimento, NISS, NIF, NUS, IBAN, salário hora, salário líquido e bruto), que são transmitidos em texto claro pela _API_ e cifrados de forma transparente pelo `FuncionarioDAO` antes de serem persistidos. A criação de um funcionário cria simultaneamente o seu utilizador de sistema.

A secção *Financeiro* permite ao Gerente consultar o historial de movimentos com filtragem por período (REQ-015) e registar pagamentos de salários (REQ-013), com os cálculos de horas extra integrados no total de salário.

O sistema de *Alertas* agrega as notificações dirigidas ao utilizador autenticado (REQ-002, REQ-005), com possibilidade de as marcar como lidas ou tratadas.

=== Cobertura integral dos subsistemas

Dos oito subsistemas definidos na análise de requisitos, todos foram implementados tanto no _backend_ como no _frontend_:

- *SAutenticacao* — _Login_, _logout_, _tokens_ de sessão _UUID_, _RBAC_ em todos os _endpoints_ (REQ-009).
- *SClientes* — _CRUD_ completo de clientes e trotinetes com validação (REQ-027, REQ-028, REQ-029).
- *SFuncionarios* — _CRUD_ de funcionários com cifra _AES-256-GCM_ dos dados sensíveis, registo de horas extra, alteração de _password_ (REQ-011, REQ-012, REQ-013).
- *SOrdensServico* — Ciclo de vida completo com 9 estados, diagnóstico, conserto, pagamento, _checklist_ de segurança pós-reparação (REQ-030 a REQ-042).
- *SReparacoes* — Catálogo de serviços com preço de mão de obra e ativação/desativação (REQ-016, REQ-017).
- *SStock* — Gestão completa de peças, entradas de _stock_, defeitos, devoluções e encomendas, com máquina de estados própria (REQ-018 a REQ-025).
- *SFinanceiro* — Movimentos financeiros com herança de tabelas, filtragem por período e cálculo de salários com horas extra (REQ-014, REQ-015).
- *SNotificacoes* — Notificações tipadas (OS e Stock) com três estados (NAOLIDA, LIDA, TRATADA) e remetente/destinatário (REQ-002, REQ-005).

=== Decisões de implementação e alinhamento com o design

A implementação manteve-se fiel ao modelo arquitetural definido na fase de _design_, com adaptações pontuais justificadas pelas características tecnológicas específicas. Os _DAOs_ implementam a interface `Map<Integer, Entity>` conforme especificado, com métodos de _query_ adicionais onde a interface `Map` padrão é insuficiente (ex: `getOSsAtivas`, `getAvailableOSs`, `filtrarOSs`). As transações explícitas nas operações de escrita complexas garantem atomicidade sem necessidade de _frameworks ORM_, alinhando-se com a decisão de usar _JDBC_ diretamente. A máquina de estados encapsulada na enumeração `EstadoOS` centraliza as regras de transição num único ponto, facilitando a manutenção. O _proxy_ reverso _nginx_ elimina a necessidade de configuração _CORS_ complexa no _backend_, simplificando a arquitetura de produção. Os _builds multi-stage Docker_ otimizam o tamanho das imagens mantendo o processo de _build_ reproduzível. A cifra _AES-256-GCM_ aplicada pelo `FuncionarioDAO` protege os dados pessoais dos funcionários ao abrigo do REQ-012, de forma completamente transparente para todas as camadas superiores da arquitetura.

== Uso de _LLM_
O recurso ao _LLM_ foi transversal a todo o desenvolvimento do sistema, apoiando tanto a redação técnica como a validação das decisões de implementação. A ferramenta ajudou a manter consistência terminológica e clareza descritiva ao longo do capítulo, garantindo que cada componente e mecanismo fosse documentado com rigor técnico adequado.

Na configuração do ambiente e na definição da arquitetura, o _LLM_ contribuiu para decisões estruturais importantes, como o uso de healthchecks no _Docker Compose_ para evitar condições de corrida e a reafirmação da regra de dependências unidirecionais entre camadas. Também ajudou a identificar pontos onde a inversão de dependências, via interfaces, reforçava a modularidade do sistema.

Ao nível da modelação de dados, o _LLM_ apoiou a evolução do padrão DAO, sugerindo métodos de consulta adicionais para além do contrato `Map`, e contribuiu para decisões de desenho da base de dados, como o uso de `ENUM`, o padrão de herança para movimentos financeiros e a inclusão de campos de ordenação em tabelas de junção. Na área de segurança, justificou a adoção de _AES‑256‑GCM_ pela sua capacidade de garantir confidencialidade e integridade.

Na lógica de negócio e na definição da _API REST_, o _LLM_ ajudou a consolidar mecanismos como a máquina de estados encapsulada na enumeração, a auto‑atribuição de mecânico no primeiro diagnóstico e o uso de transações com estratégia *delete‑then‑insert* para operações complexas. Também reforçou boas práticas na _API_, como o uso de _PATCH_ para transições de estado, parametrização clara de listagens e respostas de erro uniformizadas.

No _frontend_, o _LLM_ apoiou a definição de um cliente _HTTP_ robusto, incluindo o tratamento explícito de respostas 401 e a clarificação do papel do _RBAC_ no cliente como complemento à autorização no _backend_. No conjunto, o _LLM_ funcionou como um assistente técnico contínuo, ajudando a validar decisões, antecipar problemas e manter coerência arquitetural ao longo de todo o projeto.


= Verificação, Validação e Avaliação da Qualidade do _Software_ Produzido

== Testes de Qualidade de Software

A garantia da qualidade do software é uma componente fundamental no desenvolvimento de sistemas
de informação robustos e fiáveis. A ausência de mecanismos de verificação automática aumenta
significativamente o risco de regressões — situações em que alterações ao código introduzem
erros em funcionalidades previamente corretas — e dificulta a deteção precoce de falhas de
lógica de negócio. Para mitigar estes riscos, o projeto _EcoRide_ adoptou uma estratégia de
testes automatizados, executados através da _framework_ de código aberto
*_JUnit_ 5*, amplamente utilizada no ecossistema Java para a criação e execução sistemática de
testes. A execução de todos os testes é centralizada numa classe de entrada única,
`EcoRideTestSuite`, que agrega as classes de teste em operação e serve como *ponto de
acesso central* para validação da totalidade do sistema, permitindo executar os com um único comando.

=== Testes Unitários

==== Estratégia de Isolamento: DAOs como `Map<Integer, Classe>`

A principal dificuldade na escrita de testes unitários para sistemas com persistência em base
de dados reside no facto dos testes ficarem dependentes de uma ligação externa — tornando-os
lentos, frágeis e difíceis de reproduzir. No _EcoRide_, esta dificuldade foi eliminada pela
decisão arquitetural de todos os _DAOs_ implementarem a interface padrão do _Java_
`Map<Integer, Entidade>`. Esta escolha permitiu que, em contexto de testes, cada _DAO_ real
fosse substituído por uma subclasse anónima em memória, sem qualquer alteração ao código de
produção.

Cada subclasse anónima sobrepõe todos os métodos da interface `Map` com uma implementação
baseada em `HashMap`, e define ainda os métodos de domínio específicos do _DAO_ (por exemplo,
`insert`, `getByDestinatario`, `registarComStocks`). A geração de identificadores é feita por
um `AtomicInteger` que simula o auto-incremento da base de dados. O seguinte excerto ilustra
o padrão utilizado para o `UtilizadorDAO` na classe `TesteAutenticacao`:

```java
static UtilizadorDAO daoFake() {
    return new UtilizadorDAO() {
        private final Map<Integer, Utilizador> store = new HashMap<>();
        private final AtomicInteger seq = new AtomicInteger(1);

        @Override public Utilizador get(Object k)  { return store.get(k); }
        @Override public Utilizador put(Integer k, Utilizador v) { return store.put(k, v); }
        @Override public Utilizador remove(Object k) { return store.remove(k); }

        @Override
        public int insert(Utilizador u) {
            int id = seq.getAndIncrement();
            u.setId(id);
            store.put(id, u);
            return id;
        }

        @Override
        public boolean existeIdentificador(String identificador) {
            return store.values().stream()
                    .anyMatch(u -> u.getIdentificador().equals(identificador));
        }
    };
}
```

Este _DAO_ falso é injetado na _Facade_ através de um construtor de injeção de dependência, adicionado especificamente para suporte a testes:

```java
@BeforeEach
void setUp() {
    facade = new SAutenticacaoFacade(daoFake());
}
```

Desta forma, cada teste opera sobre um estado completamente isolado e reproduzível, sem
qualquer dependência de rede, base de dados ou estado global.

==== Framework _JUnit_ 5

Os testes são escritos com a _framework_ *_JUnit_ 5* (_Jupiter_), que fornece as anotações e
mecanismos de asserção necessários para estruturar e validar os cenários de teste. As
anotações utilizadas ao longo do projeto incluem:

- `@Test` — marca um método como caso de teste individual;
- `@BeforeEach` — executa a inicialização do estado antes de cada teste, garantindo isolamento;
- `@DisplayName` — associa uma descrição legível a cada teste, visível nos relatórios;
- `@Order` — define a ordem de execução dentro de cada classe;
- `@TestMethodOrder` — aplica a ordenação definida pela anotação `@Order`.

As asserções são feitas com os métodos estáticos da classe `Assertions` do _JUnit_:
`assertEquals`, `assertNotNull`, `assertTrue`, `assertFalse`, `assertNull` e
`assertThrows`. Este último é particularmente relevante para verificar que a lógica de
negócio lança as exceções corretas perante dados inválidos. O seguinte exemplo, retirado da
classe `TesteAutenticacao`, ilustra um conjunto de testes representativo:

```java
@Test @Order(1) @DisplayName("registar: utilizador válido é criado com ID positivo")
void registar_valido() {
    Utilizador u = criarUtilizador();
    assertNotNull(u);
    assertTrue(u.getId() > 0);
    assertEquals(Cargo.Mecanico, u.getCargo());
}

@Test @Order(5) @DisplayName("registar: identificador duplicado lança exceção")
void registar_identificadorDuplicado() {
    criarUtilizador();
    assertThrows(EcoRideException.class, () ->
            facade.registarUtilizador("outrapass", 2, Cargo.Gerente, "mecanico1"));
}

@Test @Order(16) @DisplayName("atualizarPassword: password válida é alterada com sucesso")
void atualizarPassword_valida() {
    Utilizador u = criarUtilizador();
    assertTrue(facade.atualizarPalavraPasseUtilizador(u.getId(), "pass123", "nova123"));
    assertTrue(facade.autenticar(u.getId(), "nova123"));
}
```

==== Separação de Responsabilidades entre Classes de Teste

Os testes foram organizados em dez classes independentes, cada uma responsável por um
subsistema do _EcoRide_, respeitando o princípio da separação de responsabilidades. A tabela
seguinte resume a distribuição:

#figure(
  caption: "Resultados dos Testes Unitários implementados.",
  kind: table,
  table(
    columns: 3 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
      [*Classe de Teste*], [*Subsistema*], [*Testes*],
  
      [TesteValidacoes],   [Validações de dados],         [39],
      [TesteFuncionarios], [Gestão de funcionários],      [20],
      [TesteClientes],     [Clientes e trotinetes],       [18],
      [TesteStock],        [Peças, stock e fornecedores], [27],
      [TesteEncomendas],   [Encomendas],                  [10],
      [TesteOrdensServico],[Ordens de serviço],           [24],
      [TesteAutenticacao], [Autenticação e utilizadores], [20],
      [TesteReparacoes],   [Catálogo de reparações],      [17],
      [TesteNotificacoes], [Notificações],                [13],
      [TesteFinanceiro],   [Movimentos financeiros],      [19],
  )
)

Esta separação permite que, perante uma falha, o subsistema afetado seja identificado
imediatamente pelo nome da classe de teste, sem necessidade de analisar a totalidade do
_output_. Todas as classes são registadas na `EcoRideTestSuite` através da anotação
`@SelectClasses`, conforme ilustrado:

```java
@Suite
@SuiteDisplayName("EcoRide — Todos os testes")
@SelectClasses({
    TesteValidacoes.class, TesteFuncionarios.class, TesteClientes.class,
    TesteStock.class,      TesteEncomendas.class,   TesteOrdensServico.class,
    TesteAutenticacao.class, TesteReparacoes.class,
    TesteNotificacoes.class, TesteFinanceiro.class
})
public class EcoRideTestSuite {}
```

A execução da suite completa é feita com o comando `mvn test`, que invoca o _plugin_
`maven-surefire` e produz um relatório agregado. O resultado obtido durante o desenvolvimento
do projeto foi de *207 testes executados, 0 falhas, 0 erros*, em aproximadamente *1 segundo*,
confirmando a eficácia da abordagem adotada.




=== Testes de Integração

Os testes de integração foram desenvolvidos com o objetivo de verificar o ciclo de vida dos principais fluxos da lógica de negócio do sistema, nomeadamente o subsistema de Ordens de Serviço (_OS_) e o subsistema de Stock. Ao contrário dos testes unitários, que isolam cada componente individualmente, os testes de integração exercitam múltiplas camadas em conjunto — fachadas, entidades de domínio e repositórios — de forma a garantir que as interações entre subsistemas produzem os resultados esperados segundo as regras de negócio definidas.

==== Estratégia de Injeção de Dependências

A principal dificuldade na construção de testes de integração sem acesso a uma base de dados reside no facto de o _Facade_ principal, `EcoRideController`, instanciar internamente as suas próprias fachadas e _DAOs_, que estabelecem conexões _MySQL_ em tempo de execução. Para contornar esta limitação sem comprometer a _API_ pública do sistema, foi adicionado ao `EcoRideController` um construtor com visibilidade de pacote (_package-private_), que recebe como parâmetros todas as interfaces de subsistema (`ISNotificacoes`, `ISAutenticacao`, `ISClientes`, `ISFinanceiro`, `ISFuncionarios`, `ISOrdensServico`, `ISStock`, `ISReparacoes`). Uma vez que as classes de teste residem no mesmo pacote Java (`app.ecoRideLN`), este construtor é acessível sem expor dependências internas ao exterior do módulo.

Com este mecanismo, cada teste instancia o controlador com implementações em memória (_fake DAOs_) — classes anónimas que implementam as interfaces de repositório usando estruturas `HashMap`, replicando fielmente o comportamento esperado sem necessitar de infraestrutura de base de dados. Os subsistemas não relevantes para cada conjunto de testes (autenticação, notificações, clientes, financeiro) foram substituídos por _stubs_ mínimos, retornando valores neutros ou `null` quando invocados.

==== Testes de Integração — Ordens de Serviço

A classe `TesteIntegracaoOS` valida o ciclo de vida completo de uma _OS_, desde a sua criação até ao estado terminal de pagamento, exercitando o `EcoRideController` com fachadas reais (`SOrdensServicoFacade`, `SReparacoesFacade`, `SStockFacade`, `SFuncionariosFacade`) e _fake DAOs_ em memória.

As transições de estado da _OS_ são governadas pelo método `podeTransicionar` do enum `EstadoOS`, que define a máquina de estados do subsistema. Os testes validam estas transições de duas formas distintas: 

- através de fluxos de ciclo de vida completos, que invocam sequencialmente os métodos do `EcoRideController` e verificam o estado resultante com `assertEquals`; 

- e através de testes diretos à máquina de estados, que invocam `podeTransicionar` de forma exaustiva para todas as combinações de estados, usando `assertTrue` nas transições válidas e `assertFalse` nas inválidas. Estados terminais (`Paga`, `Eliminada`) são igualmente verificados para confirmar a ausência de qualquer transição possível.

A responsabilidade do mecânico é validada no `SOrdensServicoFacade` nos métodos `registarDiagnosticoOS`, `registarConsertoOS` e `marcarAguardarPecasOS`. Os testes correspondentes constroem cenários onde um mecânico diferente do responsável pela _OS_ tenta executar cada uma dessas operações, verificando com `assertThrows(EcoRideException.class, ...)` que a exceção de domínio é lançada corretamente em cada caso.

A validação do custo do conserto face ao orçamento do diagnóstico é realizada no método `registarConsertoOS` do `EcoRideController`, que agrega os preços das peças consumidas e das reparações aplicadas e compara o total com o orçamento definido no diagnóstico (com tolerância de 0,01 €). Para testar este comportamento, o _fake DAO_ de stock implementa o método `atribuirFIFO` com a lógica _FIFO_ real, ordenando os lotes por `data_chegada ASC, id ASC` e consumindo-os sequencialmente. O teste `conserto_custo_excede_orcamento_lanca_excecao` confirma o lançamento de `EcoRideException` quando o custo total ultrapassa o orçamento, enquanto `conserto_custo_igual_ao_orcamento_e_valido` verifica com `assertDoesNotThrow` que um custo exatamente igual ao limite é aceite. O teste `conserto_consome_stock_fifo_dois_lotes` valida adicionalmente que o consumo _FIFO_ distribui corretamente as quantidades entre dois lotes distintos, verificando com `assertEquals` o estado e a quantidade residual de cada lote após o conserto.

==== Testes de Integração — Stock

A classe `TesteIntegracaoStock` valida os principais fluxos de gestão de stock, exercitando o `SStockFacade` com um _fake DAO_ de stock que implementa integralmente as operações de _FIFO_, defeito e devolução.

A lógica de consumo _FIFO_ é verificada em quatro testes que cobrem cenários progressivamente mais complexos: consumo de um único lote mais antigo, esgotamento de lote com transição para o estado `StockUsadoConserto`, consumo parcial de lote com permanência em `StockEmArmazem`, e consumo sequencial de três lotes distintos. Em todos os casos, as asserções `assertEquals` verificam o estado e a quantidade residual de cada lote, enquanto `assertThrows` confirma que uma tentativa de consumo com stock insuficiente lança `EcoRideException`.

O bloqueio de stock na deteção de defeito é validado através de dois testes complementares: `defeito_bloqueia_todos_os_stocks_da_peca` verifica que todos os lotes em `StockEmArmazem` de uma determinada peça transitam para `StockComPossivelDefeito` após o registo de um defeito, usando `assertTrue` para confirmar que nenhum lote permanece disponível; `defeito_nao_afeta_stocks_de_outra_peca` garante com `assertEquals` que os lotes de peças distintas não são afetados pela mesma operação. O teste `defeito_descartado_repoe_stock_em_armazem` verifica a operação inversa, confirmando que a rejeição do defeito restaura os lotes ao estado `StockEmArmazem`.

As transições de estado das devoluções são validadas de forma integrada, dado que cada operação de devolução atualiza coordenadamente o estado do objeto `Devolucao` e do objeto `Stock` associado. O _fake DAO_ de devolução e o _fake DAO_ de stock partilham a mesma estrutura de dados em memória (`devStore`, `stockStore`), garantindo que as alterações realizadas por um são imediatamente visíveis no outro. O teste `devolucao_aceite_pelo_fornecedor_ciclo_completo` percorre a sequência `StockPendenteDeDevolucao → Enviada → Devolvida`, verificando com `assertEquals` o estado da devolução e do stock em cada etapa; `devolucao_recusada_pelo_fornecedor_ciclo_completo` percorre a sequência alternativa `StockPendenteDeDevolucao → Enviada → Invalida`, confirmando igualmente os estados intermédios e finais com `assertNotNull` e `assertEquals`.

==== Resultados

A execução da suite completa de testes `EcoRideTestSuite` produziu *232 testes aprovados*, sem falhas nem erros. Os testes de integração contribuíram com 25 novos casos de teste, distribuídos conforme apresentado na tabela seguinte.

#figure(
  caption: "Resultados dos Testes de Integração implementados.",
  kind: table,
  table(
    columns: 3 * (1fr,), 
    stroke: (dash: "densely-dotted", thickness: 0.75pt), 
    fill: (x, y) => if y == 0 { gray.lighten(50%) },
      [*Classe de Teste*], [*Subsistema*], [*N.º de Testes*],
    [`TesteIntegracaoOS`],    [Ordens de Serviço], [13],
    [`TesteIntegracaoStock`], [Stock],             [12],
  )
)


== Avaliação Geral do Sistema

Após a conclusão dos testes unitários e de integração, foi conduzida uma *avaliação funcional abrangente* do sistema, com o objetivo de verificar que a totalidade das funcionalidades implementadas se encontrava operacional num contexto de utilização real. Esta avaliação consistiu na execução manual e sistemática do programa, percorrendo os principais fluxos de negócio de ponta a ponta: criação e gestão de ordens de serviço, autenticação e controlo de acessos por cargo, gestão de clientes e trotinetes, controlo de stock com lógica _FIFO_, processamento de encomendas e devoluções, registo de reparações e diagnósticos, emissão de notificações internas e geração de movimentos financeiros.

Durante esta fase, foram verificadas igualmente as situações de erro e os fluxos alternativos definidos nos casos de uso, confirmando que o sistema responde de forma adequada a entradas inválidas, permissões insuficientes e condições de negócio não satisfeitas. Nenhuma anomalia funcional foi detetada, e o comportamento observado mostrou-se consistente com os requisitos levantados na fase de análise e com os resultados obtidos na suite de testes automatizados.

=== Validação com o Cliente

Na fase final do projeto, foi realizada uma *reunião de apresentação com o gerente da EcoRide Solutions*, João Martins, na qual foi demonstrada a solução desenvolvida na sua totalidade. A sessão percorreu os principais módulos do sistema — gestão de ordens de serviço, controlo de _stock_, gestão de clientes e trotinetes, reparações, notificações e relatórios financeiros —, ilustrando os fluxos de trabalho que substituem os processos manuais anteriormente em uso.

O gerente acompanhou a demonstração, colocou questões sobre o funcionamento de funcionalidades específicas e validou que os objetivos definidos no início do projeto tinham sido alcançados. No final da reunião, a solução recebeu a *aprovação do cliente*, confirmando que o sistema desenvolvido responde às necessidades operacionais da EcoRide Solutions e constitui uma base sólida para a modernização e crescimento da empresa.


= _Prompts_ utilizados nos _LLM_

// ============================================================
// CAPÍTULO 1 — Conceção e Engenharia de Requisitos
// ============================================================

// --- 1.1 Definição do Domínio ---

#prompt_final(
  id: "P1",
  title: "Apresentação do tema do sistema",
  prompt: [
    Considera o seguinte tema para a implementação de um sistema informático (efetua apenas uma análise breve, não inicies nenhum processo criativo): <\<Tema 3 no enunciado do trabalho prático>>
  ]
)

#v(10pt)

#prompt_final(
  id: "P2",
  title: "Definição do domínio do sistema",
  prompt: [
    Age como um engenheiro de requisitos. Define o domínio do sistema para o tema apresentado. Apresenta os seguintes tópicos:
    - Contextualização (2 a 3 parágrafos): apresenta o contexto e uma história para uma potencial empresa com pessoas reais;
    - Fundamentação (2 a 3 parágrafos): apresenta a(s) causa(s) para a implementação do sistema;
    - Objetivos a atingir;
    - Viabilidade: simula potenciais valores.
    Podes apresentar mais tópicos que aches necessários, mas o foco deverão ser os apresentados. Utiliza uma linguagem técnica e formal, adequada para um relatório académico. Evita bullet points na contextualização e fundamentação.
  ]
)

#v(10pt)

#prompt_final(
  id: "P3",
  title: "Reformulação da contextualização",
  prompt: [
    O segundo parágrafo da contextualização está semelhante à fundamentação. Neste tópico deves dar contexto à empresa e aos funcionários. Formula uma história do porquê da empresa ter sido criada, que inclua o motivo do gerente ter abandonado a sua antiga profissão. Os funcionários da empresa devem ser 2 mecânicos, 1 assistente administrativo e o gerente. Escreve dois parágrafos. Reformula apenas a contextualização, tendo em consideração os restantes tópicos já criados.
  ]
)

#v(10pt)

#prompt_final(
  id: "P4",
  title: "Reformulação da análise de viabilidade",
  prompt: [
    Na análise de viabilidade estás a exagerar nos valores apresentados. Reforma este tópico com menos referências a valores. Apresenta apenas algumas percentagens relativas a ganhos ou perdas e o valor total de custo.
  ]
)

#v(10pt)

\

#prompt_final(
  id: "P5",
  title: "Identificação dos Recursos",
  prompt: [
    Gera um texto onde são enumerados os recursos humanos e materiais. Tem em conta que os recursos humanos incluem todos os funcionários da empresa, os clientes e a equipa de desenvolvimento. Os recursos materiais incluem o hardware (servidor de base de dados e os computadores da equipa de desenvolvimento) e o software (sistema de gestão de base de dados MySQL, o Visual Paradigm para modelação UML), bem como todas as ferramentas de desenvolvimento, coordenação e teste de código em Java e React usadas pela equipa. Evita o uso de bullet points e mantém o estilo de escrita que um Engenheiro de Software teria ao redigir um relatório.
  ]
)

#v(10pt)

#prompt_final(
  id: "P6",
  title: "Enumeração da Equipa de Trabalho",
  prompt: [
    Redige um texto sobre a equipa de trabalho e menciona que pode ser dividida em duas categorias: pessoal interno (todos os funcionários da empresa) e pessoal externo — equipa de desenvolvimento (Duarte Escairo, Eduardo Fernandes, Inês Ferreira, Luís Soares e Tiago Figueiredo) e os clientes, usados para levantamento de inquéritos de opinião e validação de serviços. Explica a função de cada funcionário em não mais do que uma linha e descreve de forma concisa o que a equipa de desenvolvimento fez (levantamento de requisitos, modelação, implementação, etc.). Usa bullet points com moderação e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P7",
  title: "Plano de Execução",
  prompt: [
    Redige um texto introdutório para a secção do Plano de Execução do projeto. Menciona que, no início do projeto, foi estabelecido um plano formal com a empresa cliente, definindo as principais fases de desenvolvimento, os respetivos marcos temporais e as responsabilidades de cada etapa. Explica que o objetivo foi alinhar as expectativas da empresa com a capacidade da equipa, garantindo entregas parciais validadas ao longo do processo. Refere que o plano contempla as fases de levantamento e análise de requisitos, modelação, implementação, testes e validação, e entrega final, e que foi pensado para permitir ciclos de revisão com os stakeholders entre fases. Menciona que o diagrama de Gantt que se segue ilustra a distribuição temporal das diversas fases. Sê conciso e mantém um tom formal e académico.
  ]
)

#v(10pt)

// --- 1.2 Identificação de Stakeholders ---

#prompt_final(
  id: "P8",
  title: "Texto introdutório sobre os Stakeholders",
  prompt: [
    Redige um parágrafo breve e introdutório sobre a importância dos stakeholders no contexto do projeto. Identifica os tipos de stakeholders existentes (internos, externos e técnicos). Sê conciso e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)


\
\
\

#prompt_final(
  id: "P9",
  title: "Texto introdutório dos Stakeholders Internos",
  prompt: [
    Redige um texto introdutório sobre o que são stakeholders internos, quem eles são no contexto deste projeto (os funcionários da empresa) e qual a sua importância para o desenvolvimento. Sê breve (não mais do que 4 linhas) e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P10",
  title: "Texto introdutório dos Stakeholders Externos",
  prompt: [
    Redige um texto introdutório sobre o que são stakeholders externos, quem eles são no contexto deste projeto (clientes, fornecedores e equipa de desenvolvimento) e qual a sua importância para o desenvolvimento. Sê breve (não mais do que 4 linhas) e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P11",
  title: "Texto introdutório dos Stakeholders Técnicos",
  prompt: [
    Redige um texto introdutório sobre o que são stakeholders técnicos, quem eles são no contexto deste projeto (orientador, coordenador, programadores e analistas) e qual a sua importância para o desenvolvimento. Sê breve (não mais do que 4 linhas) e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

// --- 1.3 Eliciação de Requisitos ---

#prompt_final(
  id: "P12",
  title: "Texto introdutório da Elicitação de Requisitos",
  prompt: [
    Redige um texto introdutório sobre a elicitação de requisitos. Menciona que o domínio do sistema já foi definido e os stakeholders identificados, e explica a importância de os requisitos serem completos, claros, consistentes e corretos, para evitar retrabalho — algo recorrente e dispendioso no desenvolvimento de software.

    De seguida, cria um parágrafo para cada um dos métodos utilizados: (1) entrevistas com os membros da organização para obter um panorama geral das suas tarefas e dificuldades do dia a dia; (2) observação presencial na oficina por parte do analista Eduardo Fernandes, de forma a complementar as entrevistas com uma análise mais aprofundada de problemas que o sistema deverá corrigir; (3) user stories para modelar as principais operações que serão utilizadas após a implementação do sistema.
  ]
)

#v(10pt)

#prompt_final(
  id: "P13",
  title: "Geração da estrutura LaTeX para transcrição de entrevistas",
  prompt: [
    Gera o código LaTeX necessário para estruturar o relatório de transcrição de uma entrevista realizada no âmbito da elicitação de requisitos. O documento deve ter um cabeçalho com: ID da entrevista, data, hora de início e fim, local/meio e estado. Uma tabela de intervenientes com colunas para nome e cargo/função (entrevistador e entrevistado). Uma secção de transcrição/notas com perguntas (P:) e respostas (R:) formatadas de forma clara. Uma tabela de requisitos levantados com colunas: Nr, Descrição, Data, Fonte e Analista. Mantém um estilo profissional e uniforme para toda a documentação de entrevistas do projeto.
  ]
)

#v(10pt)

#prompt_final(
  id: "P14",
  title: "Geração de User Stories",
  prompt: [
    Com base nos requisitos já levantados e no contexto do sistema de gestão da EcoRide Solutions, gera um conjunto de user stories para as principais funcionalidades do sistema. Cada user story deve seguir a estrutura narrativa clássica — identificando quem necessita da funcionalidade, o que deve ser executado e qual o benefício esperado — e deve ser complementada por critérios de aceitação que balizam as condições de sucesso. Inclui a identificação da fonte e os requisitos que cobre. Como ponto de partida, gera a user story US-01 relativa à criação de uma ordem de serviço por parte da secretária administrativa.
  ]
)

#v(10pt)

// --- 1.4 Refinamento de Requisitos ---

#prompt_final(
  id: "P15",
  title: "Texto introdutório do Refinamento de Requisitos",
  prompt: [
    Redige um texto introdutório sobre o capítulo de Refinamento de Requisitos Ambíguos. Menciona que os requisitos já foram levantados, mas ainda descrevem um funcionamento muito geral do sistema, e que é necessário compará-los entre si e ser mais granular e específico na definição de alguns deles.
  ]
)

#v(10pt)

// --- 1.5 Especificação Detalhada ---

#prompt_final(
  id: "P16",
  title: "Texto introdutório da Reestruturação dos Requisitos Funcionais",
  prompt: [
    A equipa de desenvolvimento refinou os requisitos funcionais iniciais, mas concluiu que muitos continuavam pouco descritivos e não explicavam de forma clara o comportamento que o sistema deveria ter. Por isso, desenvolvemos um conjunto de requisitos mais detalhados, estruturados num formato consistente (com id, título, requisito do utilizador, requisitos do sistema, fonte, área e relevância). Foram criados com base na experiência da equipa, no contexto de uso do sistema e nas necessidades identificadas durante a elicitação, e validados na reunião com os stakeholders. Redige um texto introdutório para esta fase do relatório, com tom técnico e conciso, adequado a Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P17",
  title: "Validação dos Requisitos em reunião com stakeholders",
  prompt: [
    Redige um texto introdutório para a subsecção de Validação dos Requisitos. Explica que, após o refinamento, foi necessário validar todo o trabalho numa reunião formal com o gerente, a secretária e os mecânicos, para garantir que as interpretações adotadas pela equipa correspondiam às expectativas reais dos stakeholders. Menciona que, durante essa reunião, foram identificadas e clarificadas cinco questões principais que conduziram a ajustes nos requisitos: (1) separação das responsabilidades entre gerente e gestor de stock; (2) criação de um mecanismo de autenticação e controlo de acesso; (3) especificação rigorosa das validações dos campos de dados; (4) definição de níveis mínimos de stock e criação de listas de encomendas; (5) introdução de notificações automáticas. Mantém um tom formal e académico.
  ]
)

#v(10pt)

\

#prompt_final(
  id: "P18",
  title: "Texto introdutório dos Requisitos Não Funcionais",
  prompt: [
    Escreve um texto introdutório para os requisitos não funcionais. Menciona que foram criados durante a fase de refinamento dos requisitos funcionais, com base naquilo que a equipa de desenvolvimento considerou necessário para o contexto de uso do sistema. Tal como os requisitos funcionais, foram validados na reunião com os stakeholders e foram criados tendo em vista a segurança, usabilidade, confiabilidade, portabilidade e desempenho do sistema. Sê breve (não mais do que 5 linhas) e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

// --- 1.6 Use Cases ---

#prompt_final(
  id: "P19",
  title: "Texto introdutório dos Use Cases e geração em Typst",
  prompt: [
    Redige um texto introdutório sobre a criação dos use cases. Explica que foram identificados os use cases principais de criação, edição e eliminação de dados (clientes, trotinetes, ordens de serviço, fornecedores, peças e entradas no stock), e que foram criados tendo em conta os campos necessários para cada entidade, bem como os fluxos alternativos e de exceção, de acordo com os requisitos. De seguida, gera o código Typst completo para o use case "Registar Peça", incluindo: identificador, nome, ator principal, pré-condições, pós-condições, fluxo normal, fluxos alternativos e condições de exceção. Sê breve no texto introdutório (não mais do que 5 linhas) e mantém o estilo de um relatório de Engenharia de Software.
  ]
)

#v(10pt)

// --- 1.7 Diagramas UML ---

#prompt_final(
  id: "P20",
  title: "Texto introdutório do Modelo de Domínio",
  prompt: [
    Redige um texto introdutório para a secção do Modelo de Domínio. Explica que foram elaborados dois modelos complementares: um simplificado, focado nas principais entidades e relacionamentos, e um mais detalhado que expande as entidades com atributos e componentes internos. Menciona que todos os relacionamentos descritos como "gere" representam uma abstração de três tipos distintos de interação (registar, editar e remover) com as mesmas multiplicidades, e que as cardinalidades dos perfis de utilizador (Secretária, Gestor de Stock, Mecânico, Gerente) não limitam o uso concorrente do sistema. Com base no contexto do sistema e nos requisitos já especificados, identifica as entidades em falta e sugere associações e multiplicidades que não estejam suficientemente definidas. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P21",
  title: "Geração do Modelo de Domínio (UML)",
  prompt: [
    Com base no contexto do sistema de gestão da EcoRide Solutions e nos requisitos já especificados, gera o código PlantUML para o Modelo de Domínio do sistema. O modelo deve incluir duas versões: uma simplificada, com as entidades principais (Cliente, Trotinete, OrdemServico, Funcionario, Peca, Stock, Fornecedor, Reparacao, Notificacao) e os seus relacionamentos essenciais com multiplicidades; e uma versão completa, que expande cada entidade com os seus atributos mais relevantes. Os relacionamentos do tipo "gere" devem ser representados como uma única associação com a nota de que abstrai três operações (registar, editar, remover). Os perfis de utilizador (Gerente, Secretaria, GestorStock, Mecanico) devem aparecer como atores com as respetivas associações às entidades que gerem. Certifica-te que todas as multiplicidades estão corretas e coerentes com os requisitos do sistema.
  ]
)

#v(10pt)

#prompt_final(
  id: "P22",
  title: "Geração do Diagrama de Use Cases Geral (UML)",
  prompt: [
    Com base nos use cases especificados para o sistema da EcoRide Solutions, gera o código PlantUML para o Diagrama de Use Cases Geral. O diagrama deve incluir os quatro atores principais (Gerente, Secretaria, GestorStock, Mecanico) com o Gerente a ter uma relação de generalização com os restantes, refletindo as suas permissões globais. Inclui apenas os use cases transversais a vários atores ou que não se enquadram nos diagramas por ator: "Consultar Estado Financeiro" e "Fazer Autenticação". Os restantes use cases devem ser agrupados visualmente por ator de forma a não tornar o diagrama demasiado denso.
  ]
)

#v(10pt)

#prompt_final(
  id: "P23",
  title: "Geração dos Diagramas de Use Cases por Ator (UML)",
  prompt: [
    Com base nos use cases especificados, gera o código PlantUML para quatro diagramas de use cases individuais, um por cada ator secundário: Gestor de Stock, Secretária, Gerente (excluindo os de outros atores que herda) e Mecânico. Para o Gestor de Stock inclui: registar, editar e eliminar peças, entradas de stock, encomendas, fornecedores e devoluções. Para a Secretária: registar, editar e eliminar clientes, trotinetes e ordens de serviço. Para o Gerente: registar, editar e eliminar funcionários e serviços de reparação, e gerir salários e horas extra. Para o Mecânico: selecionar, diagnosticar e executar conserto de ordens de serviço, reportar defeitos em peças. Usa relações <<include>> e <<extend>> onde apropriado.
  ]
)

#v(10pt)

#prompt_final(
  id: "P24",
  title: "Texto introdutório do Diagrama de Use Cases",
  prompt: [
    Redige um texto introdutório para a secção do Diagrama de Use Cases. Explica que, tendo sido estabelecido o modelo de domínio e identificadas as principais entidades e relacionamentos, é agora possível analisar o sistema numa perspetiva comportamental. Menciona que, devido ao elevado número de casos de uso identificados, se optou por complementar o diagrama geral com diagramas adicionais por tipo de ator, para uma visualização mais focada. Refere que o Gerente possui permissões globais sobre o sistema, refletidas através de relações de generalização com os restantes atores. Descreve sucintamente as responsabilidades de cada perfil: o Gestor de Stock gere peças, stock, encomendas, fornecedores e devoluções; a Secretária gere clientes, trotinetes e ordens de serviço; o Gerente gere funcionários e serviços de reparação; o Mecânico executa e diagnostica ordens de serviço. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P25",
  title: "Texto introdutório dos Diagramas de Atividade",
  prompt: [
    Redige um texto introdutório para a secção dos Diagramas de Atividade. Explica que estes diagramas foram elaborados para complementar a especificação dos use cases, oferecendo uma representação dinâmica e sequencial dos fluxos de execução do sistema. Menciona que, ao contrário dos use cases, os diagramas de atividade permitem visualizar graficamente a ordem das ações, as condições de ramificação e as responsabilidades de cada interveniente, recorrendo a swimlanes para distinguir as ações do utilizador das ações do sistema. Refere que a seleção dos use cases a transformar em diagramas de atividade incidiu sobre os de maior complexidade comportamental — nomeadamente os que envolvem múltiplos fluxos alternativos, transições de estado com impacto noutros módulos ou interações entre atores distintos. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P26",
  title: "Geração dos Diagramas de Atividade (UML)",
  prompt: [
    Gera o código PlantUML para três diagramas de atividade, usando swimlanes para distinguir as responsabilidades de cada interveniente. Os diagramas a gerar são:

    1. Registar Ordem de Serviço — swimlanes: Secretária, Sistema. Inclui: login, seleção/criação de cliente, registo da trotinete, descrição do problema, adição de acessórios, confirmação e atribuição automática do estado "Pendente Diagnóstico".

    2. Executar Diagnóstico de Ordem de Serviço — swimlanes: Mecânico, Sistema. Inclui: seleção de uma OS disponível, bloqueio para outros mecânicos, consulta do histórico da trotinete, seleção de reparações e peças do stock, definição do orçamento, transição para "Pendente Aprovação de Orçamento".

    3. Executar Conserto de Ordem de Serviço — swimlanes: Mecânico, Secretária/Gerente, Sistema. Inclui: aprovação/rejeição do orçamento, execução do conserto, registo de peças utilizadas, checklist de segurança, transição para "Pendente Pagamento", notificação ao cliente, registo do pagamento e transição para "Paga".

    Assegura que os fluxos alternativos (ex.: rejeição de orçamento, ausência de peças) estão representados com condições de ramificação explícitas.
  ]
)

#v(10pt)

#prompt_final(
  id: "P27",
  title: "Texto para a secção Uso de LLM — Capítulo 1",
  prompt: [
    Redige o texto para a secção "Uso de LLM" do capítulo de Conceção e Engenharia de Requisitos. Descreve como o LLM foi utilizado de forma transversal a toda esta fase: (1) geração do código LaTeX para estruturar as transcrições das entrevistas; (2) organização e consolidação dos requisitos em bruto recolhidos nas entrevistas e na análise do processo operacional; (3) identificação e eliminação de redundâncias entre requisitos na fase de refinação; (4) definição do formato padronizado para a especificação detalhada dos requisitos funcionais; (5) geração da estrutura Typst e do conteúdo detalhado de cada use case, incluindo fluxos normais, alternativos e condições de exceção; (6) contribuição determinante para a construção do modelo de domínio completo, identificando entidades e relacionamentos omitidos numa primeira análise. Mantém um tom técnico e académico.
  ]
)

#v(10pt)


// ============================================================
// CAPÍTULO 2 — Arquitetura e Design do Software
// ============================================================

// --- 2.1 Definição da Arquitetura ---

#prompt_final(
  id: "P28",
  title: "Texto introdutório da Arquitetura do Sistema",
  prompt: [
    Redige um texto introdutório para a secção de Definição da Arquitetura do Sistema. Menciona os principais recursos e estratégias utilizados e as respetivas justificações:
    - Padrão arquitetural de 3 camadas (apresentação, controlo e dados) para garantir uma implementação organizada, modular, fácil de planear, corrigir e manter;
    - Criação da API de dados com Javalin, dada a sua baixa complexidade de aprendizagem e fácil integração;
    - Conetividade via JDBC e MySQL para controlo granular sobre as operações de base de dados;
    - Desenvolvimento da interface com Lovable, React e TypeScript, com refinamento posterior manual e com apoio do Claude;
    - Revisão e implementação assistida pelo LLM Claude para validar a divisão dos subsistemas e mitigar falhas estruturais;
    - Orquestração de serviços com Docker para garantir portabilidade e consistência entre ambientes;
    - Documentação da API via Swagger UI para transparência e facilidade de consumo.
    Enumera as estratégias com bullet points e mantém uma linguagem formal e académica.
  ]
)

#v(10pt)

// --- 2.2 Camada de Controlo ---

#prompt_final(
  id: "P29",
  title: "Texto introdutório dos Diagramas de Componentes",
  prompt: [
    Redige um texto introdutório para a secção dos Diagramas de Componentes da camada de controlo. Explica que, com base nos use cases previamente estabelecidos, foi necessário analisar as responsabilidades do sistema e agrupar os métodos identificados em subsistemas. Apresenta, com bullet points, os oito subsistemas identificados e as respetivas motivações:
    - SAutenticacao: isola a segurança (logins e permissões), facilitando futura substituição do mecanismo de autenticação;
    - SClientes: centraliza a manutenção dos dados de clientes e trotinetes;
    - SFinanceiro: responsável pelo registo e tratamento dos movimentos monetários;
    - SFuncionarios: garante que apenas perfis autorizados consultam dados sensíveis dos colaboradores;
    - SNotificacoes: centraliza a transmissão de alertas, permitindo futura integração com outros meios;
    - SOrdensServico: isola a elevada complexidade da lógica de execução e transição de estados das reparações;
    - SReparacoes: mantém o catálogo de serviços, sendo consultado por vários subsistemas;
    - SStock: separa a complexidade dos fluxos de inventário, expondo apenas as operações necessárias.
    Mantém uma linguagem formal e académica.
  ]
)

#v(10pt)

#prompt_final(
  id: "P30",
  title: "Geração dos Diagramas de Componentes (UML)",
  prompt: [
    Gera o código PlantUML para dois diagramas de componentes:

    1. Diagrama inicial de arquitetura em três camadas — mostra os três componentes principais (EcoRideCA para a camada de apresentação, EcoRideLN para a camada de controlo e EcoRideCD para a camada de dados) com as suas dependências unidirecionais.

    2. Diagrama expandido da camada de controlo — decompõe o componente EcoRideLN nos oito subsistemas (SAutenticacao, SClientes, SFinanceiro, SFuncionarios, SNotificacoes, SOrdensServico, SReparacoes, SStock), mostrando as interfaces expostas por cada um (ISAutenticacao, ISClientes, etc.) e as dependências entre subsistemas onde existam (ex.: SOrdensServico depende de SStock e SReparacoes; SFinanceiro é usado por SOrdensServico e SStock).

    Usa a notação UML de componentes com interfaces fornecidas e requeridas (ball-and-socket) onde aplicável.
  ]
)

#v(10pt)

#prompt_final(
  id: "P31",
  title: "Texto introdutório dos Diagramas de Classes",
  prompt: [
    Explica o processo de elaboração dos diagramas de classes, fundamentando a transposição dos requisitos técnicos e use cases para uma estrutura de modelação estática. Aborda: (1) a abordagem sistemática de tratar cada subsistema como uma unidade autónoma de design; (2) a definição de interfaces para cada Facade de forma a reduzir o acoplamento; (3) o uso de estruturas Map para armazenar entidades, minimizando a complexidade das operações mais frequentes; (4) a modelação dos atributos e tipos com base nos requisitos e regras de validação; (5) a atribuição de identificadores únicos a cada entidade. Mantém um tom formal e académico adequado a um relatório de projeto.
  ]
)

#v(10pt)

#prompt_final(
  id: "P32",
  title: "Geração dos Diagramas de Classes por Subsistema — camada de negócio (UML)",
  prompt: [
    Gera o código PlantUML para os diagramas de classes da camada de negócio, um por cada subsistema. Para cada subsistema inclui: a interface ISubsistema, a classe Facade que a implementa, as entidades do domínio com os seus atributos (tipos de dados) e métodos, e os enumerados relevantes. As associações entre classes de subsistemas diferentes devem ser representadas por referência ao identificador (ex.: codCliente: int) e não por composição direta, para refletir o baixo acoplamento entre subsistemas. Os subsistemas a gerar são: SAutenticacao (Utilizador, Cargo), SClientes (Cliente, Trotinete), SFinanceiro (MovimentoFinanceiro, TipoMovimento), SFuncionarios (Funcionario, Morada), SNotificacoes (Notificacao, TipoNotificacao), SOrdensServico (OrdemServico, Diagnostico, Conserto, EstadoOS, Pagamento), SReparacoes (Reparacao) e SStock (Peca, Stock, Defeito, Devolucao, Encomenda, Fornecedor, EstadoStock).
  ]
)

#v(10pt)

#prompt_final(
  id: "P33",
  title: "Descrição detalhada das classes por subsistema",
  prompt: [
    Para cada um dos seguintes subsistemas, descreve de forma detalhada as classes que o compõem, os seus atributos (com tipos de dados), os métodos principais e as associações com outros subsistemas. Os subsistemas são: SAutenticacao, SClientes, SFinanceiro, SFuncionarios, SNotificacoes, SOrdensServico, SReparacoes e SStock. Para cada classe, justifica as decisões de modelação com base nos requisitos e nos casos de uso do sistema de gestão da EcoRide Solutions. Mantém uma linguagem técnica e formal.
  ]
)

#v(10pt)

\
\
\

// --- 2.3 Modelo de Dados ---

#prompt_final(
  id: "P34",
  title: "Texto introdutório do Modelo de Dados (Enquadramento e DAOs)",
  prompt: [
    Redige um texto introdutório para a secção do Modelo de Dados. Explica que os diagramas de classes foram elaborados com foco exclusivo na estrutura lógica da camada de negócio, abstraindo os mecanismos de persistência. Menciona que a transição do modelo de domínio para o modelo de dados é um momento crítico no desenvolvimento, e que a equipa optou por implementar a persistência através do padrão DAO (Data Access Object) com JDBC e MySQL, dada a experiência prévia com estas tecnologias. Descreve a estrutura geral do padrão: cada DAO implementa a interface Map\<Integer, Entity> e é responsável pelas operações de leitura e escrita de uma entidade específica do domínio. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P35",
  title: "Geração dos Diagramas de Classes com DAOs (UML)",
  prompt: [
    Gera o código PlantUML para os diagramas de classes da camada de dados, um por cada subsistema. Cada diagrama deve mostrar a Facade do subsistema a delegar nas operações do(s) respetivo(s) DAO(s), cada DAO a implementar a interface Map\<Integer, Entity>, e as classes de entidade persistível com os seus atributos mapeados para colunas da base de dados. Inclui também um diagrama de componentes completo com as três camadas que evidencia como os DAOs se integram na arquitetura: a camada de apresentação (Javalin controllers) chama as Facades, as Facades chamam os DAOs, os DAOs acedem à base de dados via JDBC/ConnectionFactory. Os subsistemas a cobrir são os mesmos oito já definidos.
  ]
)

#v(10pt)

#prompt_final(
  id: "P36",
  title: "Descrição dos DAOs por subsistema",
  prompt: [
    Para cada um dos oito subsistemas (SAutenticacao, SClientes, SFinanceiro, SFuncionarios, SNotificacoes, SOrdensServico, SReparacoes e SStock), descreve os DAOs implementados, as entidades que cada um gere, os métodos de consulta adicionais para além do contrato Map (quando necessário), e as particularidades de cada implementação. Inclui também uma descrição do diagrama de componentes completo com as três camadas após a introdução dos DAOs. Mantém uma linguagem técnica e formal adequada a um relatório de Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P37",
  title: "Mudanças arquiteturais nos diagramas de classes",
  prompt: [
    Descreve as mudanças arquiteturais introduzidas nos diagramas de classes de alguns subsistemas após a fase inicial de design, motivadas pela necessidade de suportar a persistência de forma robusta. Para os subsistemas SAutenticacao, SFinanceiro, SNotificacoes, SOrdensServico e SStock, explica as limitações estruturais identificadas nos diagramas iniciais e as alterações realizadas — nomeadamente a introdução de hierarquias de especialização para separar corretamente, ao nível do modelo relacional, entidades como movimentos financeiros e notificações nas suas subclasses especializadas. Mantém um tom formal e académico.
  ]
)

#v(10pt)


\
\

#prompt_final(
  id: "P38",
  title: "Geração dos Diagramas de Classes Finais após mudanças arquiteturais (UML)",
  prompt: [
    Com base nas mudanças arquiteturais identificadas durante a fase de modelação da camada de dados, gera o código PlantUML para os diagramas de classes finais dos subsistemas que sofreram alterações: SAutenticacao, SFinanceiro, SNotificacoes, SOrdensServico e SStock. Para cada um, representa a versão final que inclui as hierarquias de especialização introduzidas — por exemplo, em SFinanceiro a decomposição de MovimentoFinanceiro em MovimentoSalario e MovimentoPeca; em SNotificacoes a separação em NotificacaoOS e NotificacaoStock; em SOrdensServico a adição explícita das tabelas de junção Diagnostico_PecaOrcamento e Conserto_PecaUsada com o campo de ordenação. Inclui também um diagrama final da arquitetura completa da camada de dados com todos os DAOs.
  ]
)

#v(10pt)


#prompt_final(
  id: "P39",
  title: "Processo de Desenho da Camada de Persistência",
  prompt: [
    Redige um texto sobre o processo de desenho da camada de persistência, abordando as seguintes regras e decisões de mapeamento: (1) regra geral de mapeamento de classes para tabelas relacionais; (2) mapeamento de enumerados como colunas ENUM no MySQL; (3) decomposição de hierarquias de herança para o modelo relacional; (4) integridade referencial e ações em cascata; (5) particularidades do subsistema de Ordens de Serviço (tabelas de junção com preservação de ordem, máquina de estados); (6) particularidades do subsistema de Stock (máquina de estados própria para os itens de stock). Para cada ponto, apresenta as decisões tomadas e as respetivas justificações técnicas. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P40",
  title: "Relações entre entidades, normalização e modelo lógico da BD",
  prompt: [
    Redige os textos introdutórios para as subsecções de Relações entre Entidades e Integridade Referencial, Normalização do Modelo Relacional e Modelo Lógico da Base de Dados. Para as relações, descreve as principais relações entre entidades e as ações de integridade referencial adequadas. Para a normalização, confirma a conformidade com as três primeiras formas normais e documenta as exceções deliberadas com justificação. Para o modelo lógico, descreve que o modelo final é composto por 28 tabelas interligadas por 35 relações de chave estrangeira, com 9 tipos enumerados, organizadas em 6 domínios funcionais: Pessoal e Autenticação, Clientes e Equipamentos, Ordens de Serviço, Stock e Peças, Aprovisionamento e Notificações. Mantém um tom formal e académico.
  ]
)

#v(10pt)

// --- 2.4 Design de Interface ---

#prompt_final(
  id: "P41",
  title: "Texto introdutório do Design de Interface e diagrama de estados",
  prompt: [
    Redige um texto introdutório para a secção de Design de Interface. Explica que, antes da implementação da camada de apresentação, foi necessário modelar a estrutura e o fluxo de navegação da interface. Menciona que foi construído um diagrama de máquina de estados onde cada página é um estado e as ações dos utilizadores que conduzem a redirecionamentos são transições. Refere que a interface não está integralmente disponível para todos os funcionários — o sistema implementa controlo de acesso baseado em cargos (RBAC), conforme definido nos requisitos. Mantém um tom formal e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P42",
  title: "Planeamento da interface da aplicação",
  prompt: [
    O meu objetivo é desenvolver o frontend da aplicação para depois o conectar ao backend e à base de dados. Relativamente à interface, quero algo simples, minimalista e user friendly, que permita realizar as tarefas descritas nos use cases. A oficina tem os seguintes cargos: gerente, gestor de stock, secretária e mecânico, com diferentes permissões de acesso. A interface deve ter uma página de Login e, após autenticação, o funcionário é redirecionado para uma Homepage com uma navigation bar que dá acesso às diferentes áreas. Com base nos use cases, planeia uma solução para a interface: identifica as áreas da navigation bar, as operações disponíveis em cada uma e a melhor forma de as expor de modo a ser intuitivo para o utilizador.
  ]
)

#v(10pt)

#prompt_final(
  id: "P43",
  title: "Geração dos esboços de páginas da interface",
  prompt: [
    Com base no plano de interface definido e nos requisitos do sistema, gera os esboços visuais (wireframes) de todas as páginas da aplicação: Login, Homepage/Dashboard, Alertas, Ordens de Serviço, Clientes, Trotinetes, Catálogo de Reparações, Peças e Stock, Fornecedores, Funcionários e Financeiro. Para cada página, descreve a estrutura visual, os componentes principais, as ações disponíveis para cada perfil de utilizador e as regras de navegação. Os esboços devem ser consistentes com o diagrama de máquina de estados definido anteriormente.
  ]
)

#v(10pt)

#prompt_final(
  id: "P44",
  title: "Validação da interface com os stakeholders",
  prompt: [
    Redige um texto para a secção de Validação da Interface. Explica que, após a produção dos esboços da interface, foi realizada uma reunião com o gerente para validar a estrutura e funcionalidades de cada página. Menciona que as sugestões recolhidas nessa reunião foram incorporadas nos esboços finais antes de se avançar para a implementação efetiva com recurso à ferramenta Lovable. Mantém um tom formal e académico, sendo breve (não mais do que 4 linhas).
  ]
)

#v(10pt)

#prompt_final(
  id: "P45",
  title: "Texto para a secção Uso de LLM — Capítulo 2",
  prompt: [
    Redige o texto para a secção "Uso de LLM" do capítulo de Arquitetura e Design do Software. Descreve como o LLM foi utilizado de forma transversal a esta fase: (1) condução do processo de identificação e delimitação dos oito subsistemas, determinando responsabilidades, fronteiras e motivações arquiteturais; (2) identificação das limitações estruturais dos diagramas de classes iniciais que comprometiam a persistência, propondo hierarquias de especialização; (3) validação da normalização do modelo relacional, confirmando conformidade com as três primeiras formas normais e justificando exceções; (4) produção dos esboços de todas as páginas do sistema através de prompts detalhados, com revisão posterior pela equipa. Redige todos os textos descritivos do capítulo e mantém um tom técnico e académico.
  ]
)

#v(10pt)


// ============================================================
// CAPÍTULO 3 — Implementação e Desenvolvimento Assistido por LLM
// ============================================================

// --- 3.1 Configuração do Ambiente ---

\

#prompt_final(
  id: "P46",
  title: "Ajuste visual ao logótipo e paleta de cores",
  prompt: [
    Substitui a imagem atual pelo logótipo da oficina e ajusta as cores da aplicação às cores do logótipo (tons verdes). Mantém o restante layout já definido.
  ]
)

#v(10pt)

#prompt_final(
  id: "P47",
  title: "Configuração do ambiente com Docker",
  prompt: [
    Redige um texto introdutório para a secção de Configuração do Ambiente. Explica que a EcoRide é uma aplicação distribuída composta por múltiplos serviços interdependentes, e que para resolver o desafio de gestão do ambiente foi adotado o Docker Compose como orquestrador. Menciona que esta decisão garante que o ambiente de desenvolvimento é completamente reproduzível em qualquer máquina com Docker instalado. Descreve os três pilares fundamentais da escolha do Docker: isolamento de dependências (JDK 21 para o backend, Node 22 para o build do frontend, MySQL 8.4 para a base de dados), portabilidade e consistência entre ambientes. Apresenta a estrutura do Docker Compose com os serviços definidos (db, backend, frontend, swagger, info) e as suas dependências. Inclui também a descrição dos Dockerfiles do backend (build multi-stage) e do frontend (Node, Vite e nginx). Mantém um tom técnico e académico.
  ]
)

#v(10pt)

// --- 3.2 Implementação por Módulos ---

#prompt_final(
  id: "P48",
  title: "Implementação da arquitetura em três camadas",
  prompt: [
    Redige um texto sobre a implementação da arquitetura em três camadas do sistema. Explica como a camada de dados (DAOs com JDBC), a camada de controlo (Facades e subsistemas) e a camada de apresentação (Javalin REST + React) foram implementadas de forma a respeitar as dependências unidirecionais definidas no design. Menciona o padrão DAO com interface Map\<Integer, Entity>, justificando a escolha por permitir acesso rápido e direto às entidades através de identificadores únicos. Mantém um tom técnico e académico adequado a um relatório de Engenharia de Software.
  ]
)

#v(10pt)

#prompt_final(
  id: "P49",
  title: "Proteção de dados pessoais com cifra AES-256-GCM",
  prompt: [
    Redige um texto sobre a implementação da proteção de dados pessoais dos funcionários através de cifra AES-256-GCM. Explica a razão da escolha (garante confidencialidade e integridade), descreve a classe CifraUtil e como a cifra é integrada de forma transparente no FuncionarioDAO — todos os campos sensíveis (nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN) são cifrados antes de serem persistidos e decifrados transparentemente ao serem lidos. Inclui os excertos de código relevantes da classe CifraUtil e da integração no DAO. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P50",
  title: "Gestão de ligações JDBC e transacionalidade",
  prompt: [
    Redige um texto sobre a gestão de ligações JDBC através da classe ConnectionFactory e sobre a transacionalidade nas operações de escrita complexas. Explica como a ConnectionFactory centraliza a criação de ligações, lendo as credenciais de variáveis de ambiente para garantir segurança. Descreve como as transações explícitas são usadas nas operações de escrita complexas (ex.: registo de conserto, criação de OS) para garantir atomicidade sem necessidade de frameworks ORM, alinhando-se com a decisão de usar JDBC diretamente. Inclui excertos de código ilustrativos. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P51",
  title: "Decisões de modelação do esquema da base de dados",
  prompt: [
    Redige um texto sobre as decisões de modelação do esquema da base de dados implementado. Aborda: (1) tabelas de junção com preservação de ordem (campo posicao nas tabelas de junção para garantir a ordem de seleção de peças e reparações); (2) a máquina de estados do stock com os estados StockEncomendado, StockEmArmazem, StockComPossivelDefeito, StockPendenteDeDevolucao, StockDevolvido e StockInvalidoParaDevolucao. Para cada decisão, inclui o código SQL correspondente e a justificação técnica. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P52",
  title: "Camada de lógica de negócio — Facades, máquina de estados e RBAC",
  prompt: [
    Redige um texto sobre a implementação da camada de lógica de negócio. Aborda: (1) a máquina de estados encapsulada na enumeração EstadoOS, que centraliza as regras de transição (PendenteDiagnostico → PendenteAprovacaoOrcamento → PendenteReparacao / OrcamentoNaoAprovado → PendentePagamento / AguardarPecas → ClienteNotificado → Paga); (2) o controlo de responsabilidade e validação de negócio nas Facades (auto-atribuição de mecânico no primeiro diagnóstico, validação de campos obrigatórios); (3) o registo de pagamento com preservação dos dados de notificação. Inclui excertos de código relevantes para cada ponto. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P53",
  title: "Camada de apresentação — Servidor Javalin e gestão de sessões",
  prompt: [
    Redige um texto sobre a implementação da camada de apresentação com o servidor Javalin e a gestão de sessões e controlo de acesso. Explica como o Javalin expõe a API REST, incluindo a configuração do servidor (CORS, before-handlers para verificação de token), o tratamento centralizado de exceções com respostas JSON uniformizadas, e a classe GestorSessoes com tokens UUID que implementa o RBAC no backend — validando cargo em cada handler. Inclui excertos de código relevantes. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P54",
  title: "Correção da lógica de atribuição de Ordens de Serviço",
  prompt: [
    Após a criação de uma nova ordem de serviço, esta deve ficar no estado "Registada" e aparecer disponível para todos os mecânicos. Os mecânicos é que selecionam autonomamente uma OS e tratam de todo o processo, desde o diagnóstico até ao conserto. Quando um mecânico seleciona uma OS, esta fica bloqueada para os restantes — nenhum outro mecânico pode escolher ou intervir nessa OS. Corrige a lógica atual em conformidade e avança depois para a implementação completa do módulo de Stock.
  ]
)

#v(10pt)

#prompt_final(
  id: "P55",
  title: "Correções ao módulo de Stock e registo de utilizadores",
  prompt: [
    Há três correções a aplicar antes de avançar:

    1. Ao adicionar uma nova entrada de stock ou fazer uma encomenda, após selecionar a peça, o preço unitário deve ser preenchido automaticamente com o preço de compra definido para essa peça, mas deve permanecer editável. Se o preço unitário for alterado, o preço de compra da peça não deve ser modificado.

    2. A opção "Reportar defeito" durante uma reparação não é relativa à trotinete — é relativa a uma peça usada na reparação. Ao clicar nessa opção, o mecânico deve poder indicar que uma determinada peça tem defeito (indicando o número de série, se aplicável). A peça é retirada do stock e adicionada a uma lista de defeitos na área de Stock, onde fica pendente de análise pelo gestor de stock para eventual devolução.

    3. Deve existir uma página de registo onde um novo funcionário insere o seu username (que tem de existir no sistema) e define a sua password (duas vezes para confirmação). Um funcionário é sempre criado pelo gerente sem password — este registo permite que o funcionário defina a sua própria.

    Após estas correções, implementa o módulo Financeiro e o Centro de Alertas.
  ]
)

#v(10pt)

#prompt_final(
  id: "P56",
  title: "Correções às Ordens de Serviço, pagamentos e acesso ao histórico",
  prompt: [
    Antes de avançar para a fase seguinte, aplica as seguintes correções:

    1. Na área das OS, substituir o botão "Notificar cliente para pagamento" por "Cliente notificado para levantamento".
    2. Remover toda a lógica de histórico — esta funcionalidade deixa de fazer parte do sistema.
    3. Corrigir a lógica de pagamento: não deve ser possível pagar uma OS mais recente se houver OS anteriores do mesmo cliente com pagamento em atraso.
    4. Durante uma reparação ou diagnóstico, o mecânico deve poder consultar (apenas para leitura) o histórico de OS anteriores da trotinete em causa.

    Após estas correções, avança para a implementação do módulo Financeiro e do Centro de Alertas.
  ]
)

#v(10pt)

#prompt_final(
  id: "P57",
  title: "Revisão da gestão de passwords e perfil de utilizador",
  prompt: [
    Quando o gerente cria um novo funcionário, deve definir de imediato o username e a password inicial. A página de registo independente deve ser removida. Em contrapartida, todos os funcionários devem poder alterar a sua própria password a partir do ícone de perfil — o processo exige a introdução da password atual seguida da nova password. Não deve ser possível a um gerente editar a password de outro funcionário.
  ]
)

#v(10pt)

#prompt_final(
  id: "P58",
  title: "Integração financeira de peças, OS, salários e horas extra",
  prompt: [
    Aplica as seguintes regras financeiras:

    1. Ao registar uma peça no stock, deve ser gerado automaticamente um movimento de despesa com valor igual ao preço unitário dessa peça.
    2. Quando uma OS for paga, as operações e as peças utilizadas devem ser registadas como receita — o valor das peças registado como receita corresponde ao preço de venda.
    3. Durante uma reparação, deve ser possível adicionar novas peças ou reparações não detetadas no diagnóstico, o que implica a aprovação de um novo orçamento.
    4. Ao registar horas extra de um funcionário, essas horas acumulam-se. Quando o gerente regista um salário, a despesa inclui o vencimento bruto mais as horas acumuladas multiplicadas pelo vencimento por hora. Após o registo, as horas extra são reiniciadas a zero.
    5. Encomendas de peças com valor superior a 70 € exigem o preenchimento do(s) número(s) de série antes de serem marcadas como recebidas.
  ]
)

#v(10pt)

#prompt_final(
  id: "P59",
  title: "Correções de filtros, campos, alertas automáticos e lógica de pagamento",
  prompt: [
    Aplica as seguintes alterações:

    - Nas OS do gerente e da secretária, substituir a pesquisa atual por filtros de estado, data, cliente e mecânico.
    - No registo de trotinetes, remover os campos Cor e Potência.
    - Uma peça tem apenas um preço de venda no catálogo; o preço de compra só é registado ao adicionar uma nova entrada de stock.
    - A ordenação das OS é sempre da mais antiga para a mais recente.
    - Remover o campo Morada dos Fornecedores e o campo Salário dos Funcionários (substituir por Vencimento à Hora e Vencimento Bruto).
    - No stock, substituir a pesquisa por filtros de: estado, fornecedor, referência, data de receção, tipo e preço.
    - Sempre que uma peça atingir a quantidade mínima ou for marcada como "Possível defeito", gerar automaticamente um alerta para o gestor de stock. Cada alerta deve ter um indicador de tratado/não tratado.
    - Na área de OS dos mecânicos, remover os estados "Aguarda pagamento" e "Paga" do filtro de estado e remover o campo de pesquisa.
    - O mecânico deve poder consultar o catálogo de peças durante diagnóstico ou reparação, com filtros por fornecedor e referência.
    - Corrigir o deadlock na lógica de pagamento: a OS mais antiga deve poder sempre ser paga, desbloqueando as seguintes.
  ]
)

#v(10pt)

#prompt_final(
  id: "P60",
  title: "Ajustes finais a clientes, trotinetes, fornecedores e funcionários",
  prompt: [
    Aplica as seguintes alterações finais:

    - Remover o campo Notas do registo e edição de clientes e trotinetes.
    - Na página das trotinetes, adicionar pesquisa por cliente; atualizar a mensagem da caixa de pesquisa para listar explicitamente as opções: marca, modelo, número de série, cliente e motor.
    - Na página dos clientes, atualizar a caixa de pesquisa para: nome, email, NIF e telefone.
    - Na gestão de reparações, adicionar visualização do campo booleano "Disponível" com caixa verde/vermelha; adicionar na criação a opção de definir disponibilidade.
    - Na gestão de fornecedores, adicionar pesquisa por telefone; remover o NIF; atualizar a caixa de pesquisa para: nome, telefone e email.
    - Remover o campo Motivo do registo de horas extra; adicionar nas ações a opção de reiniciar as horas extra a zero.
    - Alargar a caixa de pesquisa das trotinetes para que a mensagem de opções fique totalmente visível.
    - No painel lateral, ajustar o comportamento do logótipo ao recolher/expandir para que fique centrado sem sobrepor a barra de separação.
    - Limpeza do catálogo de peças: remover colunas de estado, tipo e última receção; nos filtros manter apenas Fornecedor e Referência; remover o botão "Marcar com possível defeito" da tabela.
  ]
)

#v(10pt)

// --- 3.3 API REST ---

#prompt_final(
  id: "P61",
  title: "Especificação da API REST",
  prompt: [
    Redige o texto introdutório para a secção de Especificação da API REST. Explica que a API segue as convenções HTTP standard: substantivos nos paths para identificar recursos, verbos HTTP para operações (GET, POST, PUT, PATCH, DELETE), e códigos de status HTTP semânticos. Menciona que todos os endpoints protegidos exigem o header Authorization com token obtido no login, e que as respostas de erro seguem sempre um formato JSON estruturado com campos status, erro e mensagem. Descreve brevemente os grupos de endpoints implementados: autenticação (/auth), clientes, trotinetes, ordens de serviço (o conjunto mais rico, com endpoints PATCH dedicados para cada transição de estado), stock (organizado em cinco sub-recursos), funcionários e utilizadores, financeiro, notificações, reparações e fornecedores. Refere que a API está documentada interativamente via Swagger UI, disponível em http://localhost:8081. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

// --- 3.4 Resultado Final ---

#prompt_final(
  id: "P62",
  title: "Texto sobre o Resultado Final da implementação",
  prompt: [
    Redige o texto para a secção de Resultado Final da implementação. Aborda: (1) o arranque e verificação do sistema com um único comando (docker compose up --build) e a cadeia de dependências respeitada automaticamente; (2) a cobertura integral dos oito subsistemas implementados tanto no backend como no frontend (SAutenticacao, SClientes, SFuncionarios, SOrdensServico, SReparacoes, SStock, SFinanceiro, SNotificacoes); (3) as decisões de implementação e alinhamento com o design — DAOs com interface Map\<Integer, Entity>, transações explícitas nas operações complexas, máquina de estados encapsulada na enumeração EstadoOS, proxy reverso nginx, builds multi-stage Docker, cifra AES-256-GCM transparente. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P63",
  title: "Texto para a secção Uso de LLM — Capítulo 3",
  prompt: [
    Redige o texto para a secção "Uso de LLM" do capítulo de Implementação e Desenvolvimento Assistido por LLM. Descreve como o LLM foi utilizado de forma transversal ao desenvolvimento: (1) configuração do ambiente — uso de healthchecks no Docker Compose para evitar condições de corrida, reafirmação das dependências unidirecionais entre camadas; (2) modelação de dados — sugestão de métodos de consulta adicionais para além do contrato Map, decisões de desenho da BD (ENUMs, herança para movimentos financeiros, campos de ordenação em tabelas de junção), justificação da adoção de AES-256-GCM; (3) lógica de negócio e API REST — consolidação da máquina de estados na enumeração, auto-atribuição de mecânico no primeiro diagnóstico, uso de PATCH para transições de estado, respostas de erro uniformizadas; (4) frontend — definição do cliente HTTP robusto com tratamento de 401, esclarecimento do papel do RBAC no cliente como complemento à autorização no backend. Mantém um tom técnico e académico.
  ]
)

#v(10pt)

#prompt_final(
  id: "P64",
  title: "Contextualização dos testes",
  prompt: [
    Analisando o conteudo do Facade principal.
    
    E as exigencias dadas:
    
    - Realização de testes automatizados, unitários, de integração e de sistema, geração de testes com LLM (e.g.
    - test cases, edge cases e testes baseados em requisitos),
    
    Que tipo de testes poderia implementar , nomeadamente, como é que poderia fazê-los e implementá-los e apresentá-los no relatório ? A minha ideia é ter um conjunto bem grande de testes simples e um conjunto de testes mais complexos que utilize a validação dos mais simples para seguir. Para além disso, como é que poderia acrescentá-los no sistema (tendo em conta que poderia tirar proveito dos DAO's implementarem Maps).

  ]
)

#v(10pt)

#prompt_final(
  id: "P65",
  title: "Implementação de testes unitários",
  prompt: [
    Vamos começar por implementar os testes unitários. 
    
    Cria testes para cada classe do sistema que verifique a inserção, atualização e remoção das principais entidades do sistema, nomeadamente:
    
    Para os Funcionarios, Clientes, Trotinetes, Encomendas, Defeitos, Stocks, Fornecedores, Ordens de Serviço.
    
    Deverão existir casos de lançamento de exceções para inserções inválidas de dados em Classes que existem validações do conteúdo dos dados (como o email, número de telemóvel, NIF, NUS, IBAN).
    Cria validações no final dos testes para verificar se a estrutura final dos dados é a esperada e lança para o terminal um resultado que agregue os valores de resultado dos testes, nomeadamente, quais testes correram como esperado e quais não.
    Para implementar isto, deverás criar ficheiros separados para cada classe e implementar os testes que utilizem os métodos presentes no facade.
  ]
)


#v(10pt)

#prompt_final(
  id: "P66",
  title: "Redação de texto paras os testes unitários",
  prompt: [
    Agora, preciso que me redijas a parte do relatório técnico sobre a forma como os testes unitários foram gerados, nomeadamente:
    - como aproveitaram o facto dos DAO's serem implementações de interfaces de Map<\Integer,Classe> para fazer os testes em memória, sendo rápidos e simples e fáceis de testar
    - como usamos a framework open-source para a criação e execução automatizada de testes unitários  JUnit para garantir a qualidade do software e identificar erros rapidamente.
    - como a separação dos Testes foi feita em diferentes classes para separar os testes de acordo com as suas responsabilidades
    
    Mantém uma linguagem técnica e formal e deverás redigir um texto introdutório sobre a importância da criação de testes para garantir a qualidade do software e identificar erros rapidamente. Depois, passarás a falar sobre a implementação dos testes, nomeadamente, dos testes unitários. Deverás usar uma exemplificação para mostrar como as classes de testes foram implementadas (mostrando os métodos de teste e como eles funcionam e como essas classes são construídas no TestSuite).
    
    Menciona também o uso do TestSuite na parte introdutória como ponto de acesso central para a execução de todos os testes unitários.
    
    Todo o conteúdo deverá estar escrito em typst e siglas inglesas deverão ter '\_' antes e depois para ficarem em italiano, classes deverão ter '\`' para ficarem com um estilo de letra diferente e menções importantes deverão ficar a negrito com '\*'.
  ]
)

#v(10pt)

#prompt_final(
  id: "P67",
  title: "Implementação de testes de integração",
  prompt: [
    Observando a estratégia de orquestração dos testes no (EcoTest Suite path), que orquestra testes unitarios, cria agora testes de integração que verifiquem o funcionamento do sistema como um todo. 
    
    Para verificar a lógica do sistema, consulta o Facade (path do facade principal EcoRideController.java) e os DAOS que ele usa.
    
    Os testes de integração deverão garantir o ciclo de vida dos principais fluxos da lógica de negócio, nomeadamente, da Ordem de Serviço e do Stock.
    
    No caso da OS, deverás criar testes que validem as transições de estado possíveis (presentes na função podeTransacionar) e verificar se o mecanico que esta a executar a os é o responsavel por ela e se os valores do Conserto excedem o Diagnostico.
    
    No caso do Stock, deverás verificar a lógica de consumo FIFO dele na atribuição de Stock no Conserto de uma OS, a lógica de bloqueio de Stock na deteção de Defeito nas peças do Stock, e as transições de estado para Devoluções que são aceites ou não pelo Fornecedor.
  ]
)

#v(10pt)

#prompt_final(
  id: "P68",
  title: "Redação de texto para testes de integração",
  prompt: [
    Agora, preciso que me redijas a parte do relatório técnico sobre a forma como os testes de integração foram gerados, nomeadamente:

    que o objetivo foi verificar o ciclo de vida dos principais fluxos da lógica de negócio, nomeadamente, da Ordem de Serviço e do Stock.
    No caso da OS, criar testes que validem as transições de estado possíveis (presentes na função podeTransacionar) e verificar se o mecanico que esta a executar a os é o responsavel por ela e se os valores do Conserto excedem o Diagnostico.
    No caso do Stock, verificar a lógica de consumo FIFO dele na atribuição de Stock no Conserto de uma OS, a lógica de bloqueio de Stock na deteção de Defeito nas peças do Stock, e as transições de estado para Devoluções que são aceites ou não pelo Fornecedor
    
    Mantém uma linguagem técnica e formal. Demonstra na explicação referência claras sobre a implementação que foi feita, nomeadamente as técnicas de validação dos resultados (com o recurso aos asserts do JUnit) e a transposição dos métodos mais complexos do Facade para as classes de teste, que depois de executadas, produziram os resultados esperados em relação à lógica descrita acima do ciclo de vida dos principais fluxos da lógica de negócio.
    
    Coloca também uma tabela com Classe de Teste | Subsistema | Testes no fim da explicação, explicando que os resultados são os apresentados na tabela.
    
    Todo o conteúdo deverá estar escrito em typst e siglas inglesas deverão ter '\_' antes e depois para ficarem em italiano, classes deverão ter '\`' para ficarem com um estilo de letra diferente e menções importantes deverão ficar a negrito com '\*'. Exporta o conteudo do texto para um .txt.
      ]
)



= Conclusões e Trabalho Futuro
O projeto desenvolvido no âmbito da unidade curricular de Laboratórios de Informática IV permitiu percorrer, de forma integrada e assistida por _LLM_, todas as fases do ciclo de desenvolvimento de _software_ — desde a engenharia de requisitos até à implementação e validação de um sistema funcional. O resultado final, o sistema de gestão da EcoRide Solutions, cobre integralmente os oito subsistemas identificados e encontra-se num estado operacional pronto para utilização em contexto real.

Entre os pontos fortes do trabalho, destaca-se a consistência arquitetural mantida ao longo de todas as fases. A decisão de implementar os _DAOs_ como instâncias de `Map<Integer, Entidade>` revelou-se particularmente acertada, pois permitiu não só uma integração natural do padrão de persistência na lógica de negócio, como também simplificou significativamente a escrita dos testes unitários, sem necessidade de bases de dados em memória ou _frameworks_ de _mocking_ externas. A adoção de _Docker Compose_ para orquestração dos serviços garante reprodutibilidade total do ambiente, eliminando dependências de configuração manual e tornando a avaliação e eventual implantação do sistema substancialmente mais simples. A proteção de dados sensíveis dos funcionários através de cifra _AES-256-GCM_, aplicada de forma transparente na camada de dados, representa também uma decisão de design madura, alinhada com boas práticas de segurança e com os requisitos legais associados ao tratamento de dados pessoais. Por fim, o recurso transversal ao _LLM_ demonstrou ser um multiplicador de capacidade relevante, permitindo à equipa atingir um nível de detalhe e consistência documental — nos requisitos, casos de uso, diagramas e textos técnicos — que dificilmente seria alcançável no mesmo intervalo de tempo por métodos exclusivamente manuais.

No que respeita aos pontos fracos, a gestão de sessões por _UUID_ em memória, embora funcional para o contexto da oficina, não sobrevive a um reinício do servidor, obrigando todos os utilizadores a autenticar-se novamente. Esta limitação seria inaceitável num ambiente de produção com requisitos de disponibilidade mais exigentes. A estratégia de ligação por operação ao nível do _JDBC_, adequada ao volume atual, poderá também revelar-se um estrangulamento de desempenho caso o número de utilizadores concorrentes cresça significativamente. Por fim, embora os testes unitários e de integração cubram os fluxos mais relevantes do sistema, alguns cenários de exceção mais específicos — como combinações incomuns de transições de estado encadeadas ou situações de concorrência entre mecânicos — ficaram por cobrir, deixando uma margem residual de incerteza sobre o comportamento do sistema nestas condições de fronteira.

Relativamente ao trabalho futuro, identificam-se várias direções de extensão com valor prático imediato. A introdução de um _pool_ de ligações _JDBC_ melhoraria substancialmente o desempenho em cenários de utilização concorrente. A persistência das sessões em base de dados ou num sistema de cache distribuído conferiria maior resiliência ao mecanismo de autenticação. Do ponto de vista funcional, a integração com serviços externos de notificação — por exemplo, envio de _SMS_ ou email ao cliente quando a trotinete fica pronta — eliminaria a dependência de contacto telefónico manual por parte da secretária. A geração automática de faturas em formato _PDF_, a partir dos dados já presentes no sistema, constituiria igualmente uma funcionalidade de alto valor para a empresa. Por último, a implementação de um módulo de relatórios periódicos — com métricas como taxa de reparações por mecânico, tempo médio de execução por tipo de intervenção ou análise de margens por peça — permitiria ao gerente tomar decisões estratégicas com base em dados históricos estruturados, apoiando o crescimento sustentável da EcoRide Solutions.


#heading(numbering: none)[Referências]
[1] I. Sommerville, Software Engineering, 10.ª ed. Boston: Pearson Education, 2016. ISBN: 978-0-13-394303-0. Disponível em: https://software-engineering-book.com.

[2] Câmara Municipal, Regulamento n.º 69/2007 — Regulamento Municipal de Toponímia e Numeração de Polícia. Diário da República, 2.ª Série. Portugal, 2007. Disponível em: https://diariodarepublica.pt/dr/detalhe/regulamento/69-2007-3575984.

[3] CTT — Correios de Portugal, S.A., "O que são códigos postais?", CTT Ajuda. Disponível em: https://www.ctt.pt/ajuda/empresas/enviar-correio-e-encomendas/codigos-postais/o-que-sao.

[4] ANACOM — Autoridade Nacional de Comunicações, "Numeração: Atribuição de números", ANACOM, 2024. Disponível em: https://www.anacom.pt/render.jsp?contentId=249131.

[5] Instituto da Segurança Social, I.P., "Atribuição de Número de Identificação de Segurança Social (NISS)", Segurança Social, 2024. Disponível em: https://www.cgd.pt/Site/Saldo-Positivo/protecao/Pages/niss-o-que-e-como-pedir.aspx.

[6] Assembleia da República, Decreto-Lei n.º 14/2013, de 28 de janeiro — Institui o Número de Identificação Fiscal, artigo 4.º. Diário da República, 1.ª Série, N.º 19. Portugal, 2013. Disponível em: https://diariodarepublica.pt/dr/legislacao-consolidada/decreto-lei/2013-108043706-108042237.

[7] Banco de Portugal, International Bank Account Number (IBAN). Lisboa: Banco de Portugal. Disponível em: https://www.bportugal.pt/sites/default/files/anexos/documentos-relacionados/international_bank_account_number_pt.pdf.

[8] International Organization for Standardization, "ISO 8601 — Date and time format", ISO, 2019. Disponível em: https://www.iso.org/iso-8601-date-and-time-format.html.

[9] P. Resnick, Ed., RFC 5322: Internet Message Format. Network Working Group, IETF, outubro de 2008. Disponível em: https://www.rfc-editor.org/rfc/rfc5322.html.


#heading(numbering: none)[Lista de Siglas e Acrónimos]
/ BD: Base de Dados
/ API: Application Programming Interface
/ REST: Representational State Transfer
/ SQL: Structured Query Language
/ JDBC: Java Database Connectivity
/ UML: Unified Modeling Language
/ HTTP: HyperText Transfer Protocol
/ URL: Uniform Resource Locator
/ DNS: Domain Name System
/ CORS: Cross-Origin Resource Sharing
/ SPA: Single-Page Application
/ CRUD: Create, Read, Update, Delete
/ DTO: Data Transfer Object
/ DAO: Data Access Object
/ UUID: Universally Unique Identifier
/ RBAC: Role-Based Access Control
/ JCA: Java Cryptography Architecture
/ JDK: Java Development Kit
/ JRE: Java Runtime Environment
/ FIFO: First In, First Out
/ ACID: Atomicity, Consistency, Isolation, Durability
/ ORM: Object-Relational Mapping
/ YAML: Yet Another Markup Language
/ NIF: Número de Identificação Fiscal
/ NISS: Número de Identificação de Segurança Social
/ NUS: Número de Utente de Saúde
/ IBAN: International Bank Account Number
/ RFC: Request for Comments (norma da IETF; no relatório referenciada para validação de email)
/ ISO: International Organization for Standardization
/ AES: Advanced Encryption Standard
/ GCM: Galois/Counter Mode
/ LLM: Large Language Model
/ OS: Ordem de Serviço
/ SGBDR: Sistema de Gestão de Base de Dados Relacional
/ SMS: Short Message Service
/ PDF: Portable Document Format


#heading(numbering: none)[Anexos] <anexos>

#attachment(caption: [Entrevistas feitas ao gerente, mecânico e secretária],none) <entrevistas>
#muchpdf(read("pdf/entrevista_gerente.pdf", encoding: none))
#muchpdf(read("pdf/entrevista_mecanico.pdf", encoding: none))
#muchpdf(read("pdf/entrevista_secretaria.pdf", encoding: none))

#attachment(caption: [Análise do Processo Operacional],none) <apo>

#image("pdf/pag1.png"),
#image("pdf/pag2.png"),

#attachment(caption: [Atas de reuniões sobre requisitos],none) <atas>

#image("ata-validao/val1.png")
#image("ata-validao/val2.png")

#image("pdf/nf1.png")
#image("pdf/nf2.png")
#image("pdf/nf3.png")
#image("pdf/nf4.png")

#attachment(caption: [Requisitos Funcionais pré-refinação],none) <requisitos>

#page(
  margin: (left: 1.5cm, right: 1.5cm)
)[

#table(
  columns: (0.5fr, 1.5fr, 0.5fr, 1fr, 0.6fr),
  inset: 7pt,
  fill: (x, y) => if y == 0 or y == 1{ luma(245) },
  align: (x, y) => {
    if y == 0 { return center }
    if x in (0, 2, 3, 4) { return center }
    left + horizon
  },

  table.cell(colspan: 5, fill: luma(230))[
  #set text(13pt, weight: "bold")
  Requisitos Funcionais pré-refinação
  ],
  
  [*Nr*], [*Descrição*], [*Data*], [*Fonte*], [*Analista*],
  [RF01], [O sistema deve disponibilizar ao gerente uma interface que permita visualizar o estado de todas as ordens de serviço (por diagnosticar, pendentes, em reparação, concluídas, aguardando aprovação e aguardando peças). Deve suportar filtros por estado, data, cliente e mecânico.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF02], [O sistema deve permitir registar clientes com nome, email, telemóvel, NIF e múltiplas trotinetes associadas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF03], [O sistema deve permitir registar marca, modelo, número de série, estado à entrada (notas textuais), tipo de motor e acessórios de uma trotinete.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF04], [O sistema deve permitir registar dados pessoais dos funcionários: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS e IBAN.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF05], [O sistema deve permitir registar salário base (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto), alterações salariais, horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF06], [O sistema deve permitir registar a entrada de uma trotinete com dados do cliente, dados técnicos da trotinete e descrição do problema numa ordem de serviço nova.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF07], [O sistema deverá facilitar a obtenção de novas ordens de serviço para que os mecânicos realizem diagnósticos ou reparações.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF08], [O sistema deve atribuir automaticamente o estado "Pendente Diagnóstico" a novas ordens de serviço.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF09], [O sistema deve bloquear o início da reparação até à aprovação formal do orçamento por parte do cliente.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF10], [O sistema deve impedir que duas pessoas iniciem a mesma ordem de serviço simultaneamente, atribuindo-a a um mecânico de cada vez.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF11], [O sistema deve registar automaticamente qual mecânico executou a reparação e o diagnóstico.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF12], [O sistema deverá ter uma lista de possíveis intervenções para uma ordem de serviço com orçamento aprovado, facilitando a explicação das intervenções por parte dos mecânicos.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF13], [O sistema deve permitir registar peças com referência do fornecedor, marca, nome, descrição, preço de venda único por peça, nível mínimo aceitável em _stock_, tempo de garantia e fornecedor associado.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF14], [O sistema deve permitir marcar peças como inativas para impedir a sua utilização em novas reparações ou encomendas, sem as remover do sistema, preservando o histórico de ordens de serviço onde foram usadas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF15], [O sistema deve permitir registar entradas de peças com quantidade, fornecedor, preço de compra e data de chegada, mantendo o preço de venda definido ao nível da peça.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF16], [O sistema deve permitir registar a saída de peças quando associadas a uma ordem de serviço, identificando apenas a peça utilizada e a quantidade, sem necessidade de identificar a entrada de _stock_ específica de onde a peça saiu.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF17], [O sistema deve permitir devolver peças não utilizadas ao _stock_ e remover a associação à ordem de serviço. ], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF18], [O sistema deve permitir registar referências de fornecedores e associar cada peça ao fornecedor correto.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF19], [O sistema deve armazenar nome, email e número de telemóvel de um fornecedor.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF20], [O sistema deve gerar listas de peças a encomendar com base em níveis mínimos, consumo e _stock_ atual.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF21], [O sistema deve permitir registar a deteção de defeitos em peças. Quando um defeito é reportado, o sistema deve bloquear automaticamente todas as entradas de _stock_ dessa peça, impedindo a sua utilização em novas reparações até que cada entrada seja inspecionada e o gestor decida o seu destino (devolução ao fornecedor ou retorno ao _stock_ normal).], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF22], [O sistema deve permitir registar peças devolvidas ao fornecedor, com data, motivo e estado da devolução.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF23], [O sistema deverá registar quando um cliente foi notificado sobre o término da reparação da sua trotinete.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF24], [O sistema deverá calcular o valor de um serviço baseando-se na lista de intervenções anotada pelos mecânicos e somando esses valores com os valores de venda das peças usadas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF25], [O sistema deve implementar perfis distintos (gerente, secretária, mecânico) com permissões específicas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF26], [O sistema deverá calcular os valores dos custos e lucros da oficina e mostrar essa informação na interface do gerente. Deverá ser possível acompanhar os salários, compras de peças, rendas e outras despesas e filtrar por intervalos de tempo ou tipo de despesa/lucro.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF27], [O sistema deve permitir criar ordens de serviço contendo: nome do cliente, contacto (email ou telefone), NIF (opcional), marca, modelo, número de série (quando disponível) e descrição do problema. Todos os campos devem ser estruturados e validados.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF28], [O sistema deve permitir consultar rapidamente o histórico de reparações de um cliente ou de uma trotinete, incluindo serviços anteriores, peças substituídas e problemas recorrentes.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF29], [O sistema deve permitir atualizar um orçamento já criado e não deve ser criada uma nova ordem de serviço.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF30], [O sistema deve permitir registar a aprovação do orçamento por parte de um cliente, associando-a à ordem de serviço antes de permitir que o mecânico execute a reparação.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],

  [RF31], [O sistema deve permitir consultar o estado da reparação por nome do cliente, número da ordem ou número de série da trotinete, apresentando informação atualizada.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF32], [O sistema deve disponibilizar aos mecânicos uma lista digital das ordens pendentes, ordenadas por ordem de chegada.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF33], [O sistema deve permitir ao mecânico registar digitalmente o trabalho realizado e as peças utilizadas.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF34], [O sistema deve calcular automaticamente o valor final da reparação com base nas reparações feitas, peças utilizadas e preços definidos pelo gerente.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF35], [O sistema deve permitir registar que o cliente foi contactado para informar que a trotinete está pronta para levantamento.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF36], [O sistema deve permitir registar o método de pagamento utilizado (numerário, multibanco, MBWay).], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF37], [O sistema deve permitir ao mecânico registar digitalmente o diagnóstico inicial, incluindo descrição livre do problema e seleção de avarias comuns pré‑definidas para acelerar o processo.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF38], [O sistema deve permitir ao mecânico indicar as peças necessárias para a reparação diretamente na ordem de serviço, antes do levantamento no armazém.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF39], [O sistema deve permitir ao mecânico reportar peças defeituosas instaladas, gerando automaticamente um alerta para o gerente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF40], [O sistema deve permitir ao mecânico adicionar novos problemas identificados durante a reparação, com descrição e impacto no orçamento, gerando automaticamente um alerta para a receção contactar o cliente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF41], [O sistema deve permitir ao mecânico registar os arranjos realizados na trotinete a partir de uma lista de operações previamente definida.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF42], [O sistema deve permitir ao mecânico marcar a ordem de serviço como concluída, notificando automaticamente a receção para proceder ao contacto com o cliente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF43], [O sistema deve incluir uma checklist obrigatória de verificações de segurança (luzes, pneus, aceleração, travagem, visor, teste de condução), que o mecânico deve validar antes de concluir a ordem.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF44], [O sistema deve disponibilizar ao mecânico uma lista digital das ordens pendentes, ordenadas por prioridade ou ordem de chegada.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF45], [O sistema deve permitir ao mecânico consultar rapidamente o histórico de reparações e peças substituídas de uma trotinete, para apoiar diagnósticos e evitar trabalho dobrado.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF46], [Caso uma peça não esteja disponível para ser usada por um mecânico numa reparação, ele deverá submeter uma requisição de encomenda da peça para que o gerente possa encomendá-la.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF47], [O sistema deverá ser possível nomear os itens que foram deixados na hora de entrega da trotinete, como por exemplo, carregador, cadeado e chave.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF48], [O sistema deverá ter um "Quadro de Estado em Tempo Real" acessível na receção. O mecânico deverá identificar a ordem de serviço associada e atualizar o seu estado para que a assistente tenha sempre a resposta imediatamente.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
)

#attachment(caption: [Requisitos Funcionais pós-refinação], none) <requisitos_funcionais>

#table(
  columns: (0.5fr, 1.5fr, 0.5fr, 1fr, 0.6fr),
  inset: 7pt,
  fill: (x, y) => if y == 0 or y == 1{ luma(245) },
  align: (x, y) => {
    if y == 0 { return center }
    if x in (0, 2, 3, 4) { return center }
    left + horizon
  },

  table.cell(colspan: 5, fill: luma(230))[
  #set text(13pt, weight: "bold")
  Requisitos Funcionais pós-refinação
  ],

  [*Nr*], [*Descrição*], [*Data*], [*Fonte*], [*Analista*],
  
  [RF01 <rf04>],	[O sistema deve calcular automaticamente o valor final da reparação com base na soma da reparações feitas, peças utilizadas e preços das reparações definidos pelo gerente.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],
  
  [RF02],	[Sempre que uma OS for colocada nos estados: "Pendente de aprovação de orçamento", "Pendente Pagamento" ou "A aguardar peças", deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária, que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF03],	[Deverá haver um registo de qual mecânico realizou o diagnóstico e qual realizou a reparação da OS.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF04],	[O sistema deve impedir que dois mecânicos iniciem a mesma OS simultaneamente, atribuindo-a a um mecânico de cada vez. Caso um mecânico selecione uma OS que já está a ser tratada, deverá ser impedido.], [29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF05],	[Sempre que uma peça for colocada no estado "Possível defeito" ou tiver a sua quantidade mínima atingida, deverá ser gerado automaticamente um alerta (que contém o motivo do alerta e a peça em questão) para o gestor de _stock_, que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF06],	[Quando uma peça no estado "Possível Defeito" for colocada no estado "Pendente de Devolução", a quantidade em _stock_ dessa peça deverá diminuir em 1 unidade.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF07],	[Quando uma peça no estado "Pendente de Devolução" for colocada no estado "Devolvida ao fornecedor", uma unidade dessa peça deverá ser adicionada ao _stock_ sem qualquer custo adicional.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF08],	[Quando uma peça no estado "Pendente de Devolução" for colocada no estado "Inválida para Devolução", não deverá existir qualquer consequência no _stock_ ou nos movimentos.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF09],	[O sistema deverá separar os utilizadores definindo diferentes perfis, consoante o seu cargo, disponibilizando as respetivas capacidades.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF10],	[O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de _stock_ e mecânicos podem realizar.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF11],	[O gerente deverá poder adicionar, remover e editar funcionários.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF12],	[O registo de funcionários deverá ser composto obrigatoriamente por: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN e salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF13],	[O sistema deve permitir registar horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários (somando o valor pago pelas horas extraordinárias com o salário base).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF14],	[O sistema deverá permitir ao gerente consultar o estado financeiro da empresa, apresentando os movimentos monetários realizados (pagamentos de encomendas, pagamento de salários e receção de pagamentos).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF15],	[O sistema deverá ainda disponibilizar mecanismos de filtragem por intervalo de datas e por tipo de gasto ou receita, permitindo visualizar os valores totais por categoria, como salários, gastos com peças, lucros com mão de obra e lucros provenientes da venda de peças.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF16],	[O gerente deverá poder alterar a tabela de reparações, podendo adicionar, remover ou editar novas entradas à mesma.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF17],	[Cada entrada deverá ser composta por: uma referência única, uma nomenclatura, uma descrição detalhada e um preço de execução.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF18],	[O gestor de armazém poderá consultar as peças existentes no sistema, as peças em _stock_, as peças com possível defeito em _stock_, as peças pendentes de devolução e as peças devolvidas ao fornecedor. Para isto, poderá filtrar por estado (se existe no sistema, _stock_, com possível defeito, pendente de devolução, devolvida ao fornecedor ou inválida para devolução), fornecedor, referência, data de receção (para as que estejam em _stock_), tipo ou preço.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares], 

  [RF19],	[O gestor de armazém poderá adicionar, remover e editar o _stock_ de peças existentes. Para além disso, também poderá adicionar, remover e editar fornecedores.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  
  [RF20],	[O sistema deve permitir registar fornecedores obrigatoriamente com: nome e contacto (email ou número de telefone).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF21],	[O sistema deve permitir o registo de peças obrigatoriamente com: fornecedor, referência (do fornecedor), nome, descrição, marca, preço de venda, nível mínimo aceitável em _stock_ e tempo de garantia. O preço de venda deve ser único por peça, independentemente do lote de entrada em _stock_, para garantir consistência nos valores faturados ao cliente. Deve ainda ser possível marcar peças como inativas, impedindo a sua utilização em novas reparações ou encomendas sem as remover do sistema, preservando o histórico de utilização.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF22], [O sistema deve permitir registar entradas de peças obrigatoriamente com: referência, quantidade, fornecedor, preço de compra e data de receção. O preço de venda não é definido ao nível da entrada de _stock_, sendo herdado da peça à qual a entrada está associada.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF23],	[O sistema deve gerar listas de peças a encomendar com base em níveis mínimos, consumo e _stock_ atual.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF24],	[O sistema deve permitir registar e editar peças devolvidas ao fornecedor, com data, motivo e estado da devolução ("Pendente de Devolução", "Devolvida ao Fornecedor" ou "Inválida para Devolução").],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF25],	[Quando um mecânico reportar um defeito numa peça, o sistema deverá automaticamente bloquear todas as entradas de _stock_ dessa peça, colocando-as no estado "Possível Defeito" e impedindo a sua utilização em novas reparações. Para cada entrada bloqueada, o sistema deverá preservar o estado em que se encontrava previamente, de modo a permitir a sua restauração caso a inspeção conclua que não apresenta defeito. O gestor de _stock_ deverá poder, para cada entrada no estado "Possível Defeito", decidir entre destiná-la a devolução ao fornecedor ou repô-la no estado anterior.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF26],	[A secretária deverá poder consultar todas as OS consoante os seus estados ("Pendente Diagnóstico", "Pendente Reparação", "A aguardar peças", "Pendente aprovação do orçamento", "Pendente Pagamento", "Paga"), e deverão existir filtros que permitam filtrar as OS por estado, data, cliente e mecânico.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF27],	[A secretária deverá poder adicionar, remover e editar clientes, trotinetes e OS.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],
  
  [RF28],	[O registo de clientes deverá ser composto obrigatoriamente por: nome, email, telemóvel e NIF.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF29],	[O registo de trotinetes deverá ser composto obrigatoriamente por: marca, modelo, número de série, tipo de motor e cliente.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF30],	[O sistema deverá registar a criação de uma OS obrigatoriamente com: os dados do cliente, os dados técnicos da trotinete, uma descrição do problema e, opcionalmente, por acessórios e estado à entrada (com notas textuais). Para todas as OS que forem criadas, deverá ser atribuído o estado de "Pendente Diagnóstico" e deverá ser criada uma referência única para a mesma.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF31],	[O sistema deve permitir registar a aprovação do orçamento por parte de um cliente somente em OS que estejam no estado "Pendente aprovação do orçamento", antes de permitir que o mecânico continue a reparação. Esta aprovação deverá colocar a OS num estado de "Pendente Reparação".],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF32],	[Para uma OS no estado "Pendente Pagamento" deverá ser registado quando um cliente foi notificado sobre o término da reparação da trotinete.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF33],	[O sistema deve permitir registar a efetuação do pagamento e o respetivo método utilizado numa OS no estado "Pendente Pagamento" (numerário, multibanco, MBWay). Caso sejam ambos registados, a OS deverá ficar no estado "Paga".],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF34],	[O mecânico poderá escolher de entre duas listas de OS: "Pendentes de diagnóstico" e "Pendentes de reparação".],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF35],	[Quando um mecânico selecionar uma OS "Pendente Diagnóstico", deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações que estará disponível, poderá consultar (procurando por referência ou fornecedor) e associar peças do _stock_, e também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF36],	[Quando um mecânico selecionar uma OS "Pendente Diagnóstico" (e selecionar as várias reparações e peças usadas), deverá ser atribuído automaticamente o estado de "Pendente de aprovação de orçamento" e o mecânico não poderá proceder com a sua reparação.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF37],	[Quando um mecânico selecionar uma OS no estado "Pendente de reparação", deverá poder selecionar quais as reparações que ele executou a partir da tabela de reparações e poderá consultar (procurando por referência ou  fornecedor) e associar as peças que usou. Também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF38],	[Quando um mecânico selecionar uma OS no estado "Pendente de reparação" e uma ou mais peças da lista do diagnóstico não estiver disponível no _stock_, o sistema deverá disponibilizar a opção de requisição de encomenda das peças. A OS deverá ficar em estado de "A aguardar peças", e a reparação poderá prosseguir normalmente, mas a sua terminação deverá ser impedida até que o _stock_ da peça necessário esteja disponível. Quando uma das peças estiver disponível no _stock_, a OS deverá ficar no estado "Pendente reparação".],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF39],	[O sistema deve permitir ao mecânico adicionar novos problemas identificados durante a reparação, identificando-os com recurso à tabela de reparações. Esta ação deverá colocar o OS num estado de "Pendente aprovação do orçamento".],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF40],	[Durante a realização de uma reparação, depois de adicionadas peças à lista de peças usadas na OS, o mecânico poderá reportar defeitos nas peças instaladas, atribuindo o estado "Possível defeito" à peça e removendo-a da lista de peças usadas, diminuindo o valor final da reparação. Caso não hajam mais peças iguais em _stock_, a OS deverá ficar em estado de "A aguardar peças", e a reparação poderá prosseguir normalmente, mas a sua terminação deverá ser impedida até que o _stock_ da peça necessário esteja disponível.],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF41],	[Quando um mecânico desejar terminar uma reparação, deverá verificar uma checklist obrigatória de segurança (luzes, pneus, aceleração, travagem, visor, teste de condução).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF42],	[O sistema deve permitir ao mecânico marcar a OS como concluída, ficando esta no estado "Pendente Pagamento".],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],
  
  [RF43], [NO sistema deverá ser possível nomear os itens que foram deixados na hora de entrega da trotinete, como por exemplo, carregador, cadeado e chave.], [29/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF44], [O sistema deverá ter um "Quadro de Estado em Tempo Real" acessível na receção. O mecânico deverá identificar a ordem de serviço associada e atualizar o seu estado para que a assistente tenha sempre a resposta imediatamente.], [29/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
)

#attachment(caption: [Requisitos Não Funcionais], none) <requisitos_nao_funcionais>

#table(
  columns: (0.5fr, 1.5fr, 0.5fr, 0.6fr),
  inset: 7pt,
  fill: (x, y) => if y == 0 or y == 1{ luma(245) },
  align: (x, y) => {
    if y == 0 { return center }
    if x in (0, 2, 3, 4) { return center }
    left + horizon
  },

  table.cell(colspan: 4, fill: luma(230))[
  #set text(13pt, weight: "bold")
  Requisitos Não Funcionais
  ],

  [*Nr*], [*Descrição*], [*Data*], [*Analista*],

  [RNF1], [O acesso ao sistema deve ser autenticado com credenciais individuais.], [30/2/26], [Eduardo Fernandes],

  [RNF2], [Dados sensíveis devem ser armazenados de forma encriptada.], [30/2/26],  [Eduardo Fernandes],

  [RNF3], [A interface deve ser simples e intuitiva para permitir que novos funcionários aprendam a utilizá-la rapidamente.], [30/2/26], [Eduardo Fernandes],

  [RNF4], [Os formulários devem validar automaticamente campos obrigatórios e formatos.], [30/2/26], [Eduardo Fernandes],

  [RNF5], [O sistema deve minimizar o número de cliques necessários para operações frequentes.], [30/2/26], [Eduardo Fernandes],
  
  [RNF6], [O sistema deve funcionar em qualquer navegador moderno e ser compatível com diferentes resoluções.], [30/2/26], [Eduardo Fernandes],

  [RNF7], [O sistema deve suportar todos os funcionários em simultâneo.], [30/2/26], [Eduardo Fernandes],

  [RNF8], [O sistema deve ser modular, permitindo a adição, substituição ou atualização de componentes sem impacto nas restantes funcionalidades.], [30/2/26], [Eduardo Fernandes],
)
]

#attachment(caption: [Requisitos Funcionais Estruturados], none) <requisitos_estruturados>

//  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE

#requisito(
  id: "REQ-001",
  titulo: "Controlo de Acesso e Permissões por Papel",
  requisito_utilizador: "O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar.",
  fonte: "Entrevista ID #001",
  area_sistema: "Controlo de Acesso",
  requisitos_sistema: (
    "O sistema deverá implementar um sistema de controlo de acesso baseado nos papéis dos funcionários (Gerente, Secretária, Gestor de Stock e Mecânico) .",
    "Cada papel deverá ter um conjunto específico de permissões associadas.",
    "O gerente deverá ter acesso a todas as funcionalidades do sistema, incluindo aquelas restritas à Secretária, ao Gestor de Stock e aos Mecânicos, a partir da sua página de administração principal.",
    "O acesso deve ser validado durante o processo de autenticação e controlado em cada operação realizada no sistema.",
    "O sistema deverá impedir que utilizadores sem as permissões apropriadas acedam a funcionalidades restritas."
  ),
  relevancia: "Garante a segurança e integridade do sistema, permitindo que cada utilizador aceda apenas às funcionalidades necessárias para o seu papel, enquanto o gerente mantém visibilidade e controlo total."
)

#requisito(
  id: "REQ-002",
  titulo: "Autenticação",
  requisito_utilizador: "O sistema deverá separar os utilizadores definindo diferentes perfis, consoante o seu cargo, disponibilizando as respetivas capacidades.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Controlo de Acesso",
  requisitos_sistema: (
    "Deve existir uma página de autenticação usada por todos os utilizadores.",
    "O utilizador deverá inserir o seu identificador único e a sua palavra-passe.",
    "Caso as credenciais inseridas não correspondam àquelas guardadas no sistema, o sistema deverá informar o utilizador do erro e impossibilitar a sua autenticação.",
    "Caso as credenciais correspondam àquelas guardadas no sistema, o utlizador deverá ter a sua página redirecionada para a sua página principal (consoante o seu cargo)."
  ),
  relevancia: "Garante o controlo de acesso e a separação de responsabilidades no sistema."
)

#requisito(
  id: "REQ-003",
  titulo: "Gestão de Funcionários",
  requisito_utilizador: "O gerente deverá poder adicionar, remover e editar funcionários.
O sistema deve permitir registar horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários (somando o valor pago pelas horas extraordinárias com o salário base).",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Recursos Humanos",
  requisitos_sistema: (
    "Deve existir uma página de gestão dos funcionários, com as opções de seguir para as páginas de criação, edição e remoção de funcionários. Esta página deverá ser acessível a partir da página principal de administração.",
    "Deve ser possível retornar para a página principal de administração a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão dos funcionários, estes deverão estar listados numa tabela em que cada um dos campos ocupa uma coluna distinta. Para além disso, deverá haver uma coluna com o valor total a pagar ao funcionário desde o início do mês (soma do valor do salário base bruto com a multiplicação do valor pago à hora ao funcionário com o valor das horas extraordinárias praticadas desde o início desse mês). A ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único do funcionário.",
    "O gerente deverá poder selecionar nesta tabela qual o funcionário que deseja gerir e posteriormente, deverá selecionar se deseja eliminar, editar ou adicionar horas extra a um funcionário no sistema. Deverão existir filtros que permitam a consulta de funcionários através de todos os campos de dados de um funcionário, com a exceção do seu salário.",
    "Se desejar eliminar um funcionário, depois de escolhê-lo, deverá surgir uma página com os dados do funcionário e o gerente deverá confirmar a eliminação. A partir deste ponto deverá ser impossível iniciar sessão com a conta desse funcionário.",
    "Se o gerente decidir alterar algum dado do funcionário, depois de o selecionar, deverá surgir uma página com os dados do funcionário e o gerente terá de selecionar que deseja editá-lo. Depois disto, poderá escolher qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de um funcionário.",
    "Se quiser registar horas extra, depois de o selecionar, deverá inserir o número de horas praticadas. Este valor deverá ser um valor inteiro.",
    "O sistema deverá calcular automaticamente o valor total a pagar ao funcionário desde o início do mês, somando o valor base bruto mensal com a multiplicação do valor pago à hora ao funcionário com o valor das horas extraordinárias registadas desde o início do mês.",
    "No fim do mês, o valor total a pagar deve ser reiniciado, retornando ao valor do salário base bruto."
  ),
  relevancia: "Garante o controlo fundamental sobre a base de dados de funcionários e automatiza o cálculo de remuneração e garante precisão na folha de pagamento."
)

#requisito(
  id: "REQ-004",
  titulo: "Registo de Dados de Funcionários",
  requisito_utilizador: "O registo de funcionários deverá ser composto obrigatoriamente por: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN e salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto). Dados sensíveis dos funcionários devem ser armazenados de forma encriptada na base de dados.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Recursos Humanos",
  requisitos_sistema: (
    "Na página de criação de funcionários, deverão ser fornecidos: o nome, morada (composta por número de porta, rua, localidade e código-postal), número de telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto) e palavra-passe.",
    "O nome, rua, localidade, email e palavra-passe deverão ser campos textuais não vazios e o email deverá seguir a convenção " + link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] + ".O número de porta deverá ser um número inteiro com, no máximo, 4 dígitos e poderá ser seguido por uma letra; o código postal deverá ser composto por 4 dígitos, um hífen '-' e terminado por 3 dígitos; o número de telemóvel deverá ser um número composto por 9 dígitos; o NISS, um número composto por 11 dígitos; o NIF, um número composto por 9 dígitos; o NUS, um número composto por 9 dígitos; o IBAN, composto por 2 caracteres alfabéticos, seguidos de 23 dígitos; os valores do salário deverão ser números reais positivos com 2 casas decimais.",
    "Os seguintes campos deverão ser armazenados de forma encriptada na base de dados: nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário pago por hora, salário mensal líquido, salário mensal bruto, número de porta, rua, localidade e código-postal. A encriptação deverá garantir que estes dados sensíveis não estão acessíveis em texto plano fora do contexto de operações autorizadas do sistema.",
    "Depois de validados e preenchidos todos os dados de um funcionário, quando o gerente confirmar, o sistema deverá fornecer um identificador único, informar o gerente da criação, apresentando uma página com todos os dados do novo funcionário.",
  ),
  relevancia: "Centraliza toda a informação necessária para a folha de pagamento, segurança social e conformidade legal, garantindo a proteção de dados pessoais sensíveis através de armazenamento encriptado."
)

#requisito(
  id: "REQ-005",
  titulo: "Consulta e Análise do Estado Financeiro",
  requisito_utilizador: "O sistema deverá permitir ao gerente consultar o estado financeiro da empresa, apresentando os movimentos monetários realizados (pagamentos de encomendas, pagamento de salários e receção de pagamentos).
  O sistema deverá ainda disponibilizar mecanismos de filtragem por intervalo de datas e por tipo de gasto ou receita, permitindo visualizar os valores totais por categoria, como salários, gastos com peças, lucros com mão de obra e lucros provenientes da venda de peças.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão Financeira",
  requisitos_sistema: (
    "Deve existir uma página de consulta do estado financeiro que apresente todos os movimentos financeiros realizados. Esta página deverá ser acessível a partir da página principal de administração.",
    "Deve ser possível retornar para a página principal de administração a qualquer momento a partir desta página.",
    "Na página de consulta do estado financeiro, deverão estar listados os últimos movimentos registados no sistema. Estes deverão incluir: pagamentos de encomendas, pagamento de salários e receção de pagamentos e deverão estar organizados numa tabela que apresente-os pela ordem de criação (com o mais recente em cima de todo) e deverão estar listados todos os campos de dados relativos a um movimento.",
    "O sistema deverá disponibilizar mecanismos de filtragem por intervalo de datas, permitindo ao gerente visualizar apenas os movimentos dentro de um período específico.",
    "O sistema deverá permitir a consulta  por tipo de gasto ou receita (salários, gastos com peças, lucros com mão de obra, lucros provenientes da venda de peças) e apresentá-los."
  ),
  relevancia: "Proporciona ao gerente uma visão clara da saúde financeira da empresa e facilita a tomada de decisões com base em dados financeiros precisos."
)

#requisito(
  id: "REQ-006",
  titulo: "Gestão da Tabela de Reparações",
  requisito_utilizador: "O gerente deverá poder alterar a tabela de reparações, podendo adicionar, remover ou editar novas entradas à mesma.
  Cada entrada deverá ser composta por: uma referência única, uma nomenclatura, uma descrição detalhada e um preço de execução.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Deve existir uma página de gestão da tabela de reparações, com as opções de seguir para as páginas de criação, edição e remoção de entradas. Esta página deverá ser acessível a partir da página principal de administração.",
    "Deve ser possível retornar para a página principal de administração a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão, as entradas deverão estar listadas numa tabela em que todos os seus campos de dados estão presentes em colunas distintas e ordenadas numericamente consoante o seu identificador único.",
    "O gerente deverá poder selecionar nesta tabela qual a entrada que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la. Deverão existir filtros que permitam a consulta de entradas através todos os campos de dados de uma entrada, com a exceção da sua descrição.",
    "Na página de criação de uma entrada, deverão ser fornecidos: uma nomenclatura, uma descrição detalhada e um preço de execução.",
    "A nomenclatura e descrição deverão ser campos textuais não vazios e o preço de execução deverá ser um valor real positivo com 2 casas decimais.",
    "Depois de validados e preenchidos todos os dados de uma reparação, o sistema deverá: fornecer um identificador único, informar o gestor da criação, apresentando uma página com todos os dados da nova entrada.",
    "Se o gerente quiser editar uma entrada, depois de escolhê-la, deverá surgir uma página com todos os dados da entrada, o gerente deverá selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação.",
    "Se o gerente desejar eliminar uma entrada, depois de selecioná-la, deverá surgir uma página com todos os dados da entrada e o gerente deverá confirmar a sua eliminação. A partir deste ponto deverá deixar de ser uma opção no sistema para os mecânicos e para a secretária associarem a ordens de serviço (caso o tentem fazer, deverão ser informados de que a entrada já não existe). Apesar disto, deverá manter-se no sistema."
  ),
  relevancia: "Centraliza a gestão de serviços de reparação e garante que os preços praticados estão actualizados e organizados de forma coerente."
)

//  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR  GESTOR

#requisito(
  id: "REQ-007",
  titulo: "Gestão de Stock de Peças",
  requisito_utilizador: "O gestor de armazém poderá adicionar, remover e editar o stock de peças existentes.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de peças, com as opções de seguir para as páginas de criação, edição e remoção de peças. Esta página deverá ser acessível a partir da página principal de gestão do gestor do stock.",
    "Deve ser possível retornar para a página principal de gestão do gestor a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão das peças, deverão estar listadas numa tabela as peças em que cada um dos seus campos ocupa numa coluna distinta, e, a ordenação deverá ter em conta a ordem e deverá usar o identificador único da peça.",
    "O gestor deverá poder selecionar nesta tabela qual a entrada que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la. Deverão existir filtros que permitam a procura por uma determinada peça usando todos os campos de dados de uma peça.",
    "Se o gestor de stock decidir eliminar alguma peça, depois de escolhê-la, deverá surgir uma página com os dados da peça e o gestor terá de confirmar a eliminação. A partir deste momento, qualquer tentativa de adição da peça em questão ao stock, deverá ser impedida.",
    "Se o gestor do armazém decidir alterar algum dado de uma peça, depois de selecionada, deverá surgir uma página com os dados da peça e ele deverá selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma peça."
  ),
  relevancia: "Centraliza as informações críticas sobre peças, incluindo custos e margens de lucro, essencial para a gestão financeira e de inventário."
)

#requisito(
  id: "REQ-008",
  titulo: "Registo de Dados de Peças",
  requisito_utilizador: "O sistema deve permitir o registo de peças obrigatoriamente com: fornecedor, referência (do fornecedor), nome, descrição, marca, preço de venda, nível mínimo aceitável em stock e tempo de garantia. O preço de venda deve ser único por peça, independentemente do lote de entrada em stock, para garantir consistência nos valores faturados ao cliente. Deve ainda ser possível marcar peças como inativas, impedindo a sua utilização em novas reparações ou encomendas sem as remover do sistema.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Na página de criação de peças, deverá ser fornecido obrigatoriamente o identificador do fornecedor. Caso exista um fornecedor com o identificador fornecido, deverá surgir como opção listada para o gestor selecionar. Caso contrário, o sistema deverá informar que não existe nenhum fornecedor com o identificador fornecido.",
    "No caso do fornecedor existir, deverão ser fornecidos: a referência (do fornecedor), o nome, a descrição, a marca, o preço de venda, o nível mínimo aceitável em stock e o tempo de garantia.",
    "A referência do fornecedor, o nome, a descrição e a marca deverão ser campos textuais não vazios; o preço de venda deverá ser um valor real positivo com 2 casas decimais; o nível mínimo aceitável em stock e o tempo de garantia deverão ser números inteiros não negativos.",
    "O preço de venda definido no registo da peça deverá ser único por peça e aplicar-se a todas as utilizações dessa peça em reparações, independentemente do lote em que ela tenha entrado em stock ou do preço de compra praticado pelo fornecedor em cada lote. Esta restrição visa garantir consistência nos valores faturados ao cliente.",
    "Cada peça deverá ter um campo de estado que indique se está ativa ou inativa. Por padrão, qualquer peça criada deverá estar marcada como ativa. O gestor de stock deverá poder marcar uma peça como inativa, impedindo a sua utilização em novas reparações e a sua inclusão em novas encomendas, mas preservando todas as referências históricas a essa peça em ordens de serviço, entradas de stock e devoluções já registadas.",
    "Depois de validados e preenchidos todos os dados de uma peça, o sistema deverá: fornecer um identificador único para cada peça registada, informar o gestor da criação, apresentando uma página com todos os dados da nova peça.",
  ),
  relevancia: "Centraliza a gestão de inventário e garante que as informações sobre disponibilidade de peças estão sempre actualizadas, com preços de venda consistentes por peça."
)

#requisito(
  id: "REQ-009",
  titulo: "Registo de Entradas de Peças em Stock",
  requisito_utilizador: "O sistema deve permitir registar entradas de peças obrigatoriamente com: referência, quantidade, fornecedor, preço de compra e data de receção. O preço de venda da peça não é definido ao nível da entrada de stock, sendo herdado da peça à qual a entrada está associada.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de entradas de peças em stock, com as opções de seguir para as páginas de criação, eliminação e edição de stock de peças. Esta página deverá ser acessível a partir da página principal do gestor do stock.",
    "Na página de registo, deverão estar listadas numa tabela os registos de entradas de peças em que cada um dos seus campos ocupa numa coluna distinta, e, a ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único da peça.",
    "O gestor deverá poder selecionar nesta tabela qual a entrada que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la. Deverão existir filtros que permitam a procura por uma determinada entrada de uma peça usando todos os campos de dados de uma entrada de peça.",
    "Na página de registo, deverá existir a opção de adicionar um registo novo. Quando selecionado, deverão ser fornecidos obrigatoriamente a referência do fornecedor ou o identificador único da peça no sistema. Caso exista uma peça que corresponda ao dado fornecido, deverá surgir como opção listada para o gestor selecionar. Caso contrário, o sistema deverá informar que não existe nenhuma peça com os dados fornecidos. ",
    "Caso exista a peça, depois de ser selecionada, deverá ser fornecida a quantidade a adicionar, o preço de compra e a data de receção. O preço de venda não é solicitado neste momento, uma vez que é uma propriedade da peça e não da entrada de stock.",
    "A quantidade deverá ser um número inteiro positivo, o preço de compra deverá ser um valor real positivo com 2 casas decimais e a data de receção deverá ser uma data válida no formato AAAA-MM-DD (seguindo a convenção " + link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601]+").",
    "Depois de ser efetuado o registo, uma página com os dados das peças adicionadas deverá surgir.",
    "Caso o gestor deseje eliminar uma entrada de uma peça, depois de selecioná-la, deverá surgir uma página com os dados da peça e o gestor terá de confirmar a eliminação. Após a confirmação, o registo deverá ser apagado do sistema.",
    "Se o gestor decidir editar o registo, depois de escolhê-la, uma página com os dados da peça deverá surgir e ele poderá selecionar que deseja editá-la. Depois disto, deverá escolher o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma entrada de uma peça.",
  ),
  relevancia: "Garante o registo preciso de todas as entradas de peças, incluindo informações de garantia para peças caras, essencial para rastreabilidade."
)

#requisito(
  id: "REQ-010",
  titulo: "Gestão de Devoluções de Peças ao Fornecedor",
  requisito_utilizador: "O sistema deve permitir registar e editar peças devolvidas ao fornecedor, com data, motivo e estado da devolução (\"Pendente de Devolução\", \"Devolvida ao Fornecedor\" ou \"Inválida para Devolução\").
Quando uma peça no estado \"possível defeito\" for colocada no estado \"pendente de devolução\", a quantidade em stock dessa peça deverá diminuir em 1 unidade.
Quando uma peça no estado \"Pendente de Devolução\" for colocada no estado \"Devolvida ao fornecedor\", uma unidade dessa peça deverá ser adicionada ao stock sem qualquer custo adicional.
Quando uma peça no estado \"Pendente de Devolução\" for colocada no estado \"Inválida para Devolução\", não deverá existir qualquer consequência no stock ou nos movimentos.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de devoluções de peças, com as opções de seguir para as páginas de criação, edição e eliminação de devoluções. Esta página deverá ser acessível a partir da página principal do gestor do stock.",
    "Deve ser possível retornar para a página principal de gestão do gestor de stock a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão de devoluções, por padrão, ao entrar na página, o filtro de estado deverá estar no estado 'Possível defeito', logo, todas as devoluções que estiverem no estado 'Possível defeito' deverão estar listadas numa tabela onde cada um dos seus campos ocupa uma coluna distinta e deverão estar organizadas por ordem numérica consoante o seu identificador único. ",
    "O sistema deverá disponibilizar, para a tabela, filtros de diferentes dados, nomeadamente, filtro através: dos estados possíveis ('Possível defeito', 'Pendente de devolução', 'Devolvida ao fornecedor' ou 'Inválida para devolução'), dos fornecedores (inserindo o seu identificador do sistema), das peças (inserindo a sua referência ou identifador do sistema), de um intervalo de datas ou do preço de compra de peças.",
    "O gestor poderá selecionar nesta tabela qual a devolução que deseja gerir e, posteriormente, deverá selecionar se deseja eliminá-la ou editá-la.",
    "Se quiser eliminar uma devolução, deverá surgir uma página com os dados da devolução e deverá selecionar que deseja eliminá-la. Apenas devoluções no estado \"Pendente Devolução\" poderão ser devolvidas e, caso sejam eliminadas, os gastos em peças nesse mês deverão aumentar de acordo com os valores gastos nas peças que estão na lista da devolução. O registo deverá ser apagado do sistema depois de confirmada a eliminação.",
    "Se quiser alterar uma devolução, deverá surgir uma página com os dados da devolução e deverá selecionar que deseja editá-la. Depois, terá de selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma devolução.",
    "Quando uma peça no estado 'Possível defeito' for colocada no estado 'Pendente de devolução', a quantidade em stock dessa peça deverá diminuir na quantidade especificada na devolução.",
    "Quando uma peça no estado 'Pendente de devolução' for colocada no estado 'Devolvida ao fornecedor', a quantidade especificada na devolução dessa peça deverá ser adicionada ao stock, sem custos adicionais.",
    "Nenhuma outra transição de estado deverá ser possível.",
    "Na página de criação de devoluções, deverá ser fornecido obrigatoriamente o identificador único da peça no sistema. Caso exista uma peça que corresponda ao dado fornecido, deverá surgir como opção listada para o gestor selecionar. Caso contrário, o sistema deverá informar que não existe nenhuma peça com os dados fornecidos.",
    "Caso a peça exista, depois de ser selecionada, deverão ser fornecidos obrigatoriamente a quantidade, a data da devolução, o motivo da devolução e o estado da devolução.",
    "A quantidade deverá ser um número inteiro positivo e não deverá exceder a quantidade atualmente marcada como 'Possível Defeito' em stock, a data da devolução deverá ser uma data válida no formato AAAA-MM-DD seguindo a convenção " + link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601] + ", o motivo da devolução deverá ser um campo textual não vazio e o estado atribuído deverá ser automaticamente 'Pendente de Devolução'.",
    "Depois de validados e preenchidos todos os dados de uma devolução, quando o gestor confirmar, o sistema deverá fornecer um identificador único e deverá informar o gestor da criação, apresentando uma página com todos os dados da nova devolução.",
  ),
  relevancia: "Garante o rastreamento completo de devoluções e gestão adequada do inventário, permitindo controlo automático sobre peças defeituosas ou inadequadas."
)

#requisito(
  id: "REQ-011",
  titulo: "Gestão de Fornecedores",
  requisito_utilizador: "O gestor de armazém poderá adicionar, remover e editar fornecedores.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de fornecedores, com as opções de seguir para as páginas de criação, edição e remoção de fornecedores. Esta página deverá ser acessível a partir da página principal do gestor de stock.",
    "Deve ser possível retornar para a página principal de gestão do gestor de stock a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão de fornecedores, estes deverão estar listados numa tabela em que cada um dos campos ocupa uma coluna distinta. A ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único do fornecedor.",
    "O gestor de armazém deverá poder selecionar nesta tabela qual o fornecedor que deseja gerir e posteriormente, deverá selecionar se deseja eliminar ou editar um fornecedor no sistema. Deverão existir filtros que permitam a consulta de fornecedores através de todos os campos de dados de um fornecedor.",
    "Se selecionar que deseja eliminar um fornecedor, depois de o escolher, deverá surgir uma página com os dados do fornecedor e deverá confirmar a eliminação. A partir deste momento, nenhuma peça deverá poder ser associada a este fornecedor.",
    "Se o gestor de armazém decidir alterar algum dado do fornecedor, deverá escolhê-lo e depois, deverá surgir uma página com os dados do fornecedor e o gestor terá de selecionar que deseja editá-lo. Depois disto, poderá escolher qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de um fornecedor.",
  ),
  relevancia: "Mantém um registo actualizado de fornecedores, facilitando a gestão de relações comerciais e encomendas de peças."
)

#requisito(
  id: "REQ-012",
  titulo: "Registo de Dados de Fornecedores",
  requisito_utilizador: "O sistema deve permitir registar fornecedores obrigatoriamente com: nome e contacto (email ou número de telefone).",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Na página de criação de fornecedores, deverão ser fornecidos obrigatoriamente: nome, contacto (email e/ou número de telefone).",
    "O nome deverá ser um campo textual não vazio, o contacto poderá ser um email ou um número de telefone, ou ambos. Se for email, e deverá seguir a convenção " + link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] + " mas se for número de telefone, deverá ser composto por 9 dígitos.",
    "Depois de validados e preenchidos todos os dados obrigatórios de um fornecedor, quando o gestor confirmar, o sistema deverá fornecer um identificador único para cada fornecedor registado e deverá apresentar uma página com todos os dados do novo fornecedor."
  ),
  relevancia: "Garante que todos os fornecedores têm informações de contacto válidas registados, essencial para a gestão eficiente de encomendas."
)

#requisito(
  id: "REQ-013",
  titulo: "Criação de Listas de Encomendas",
  requisito_utilizador: "O sistema deve gerar listas de peças a encomendar com base em níveis mínimos, consumo e stock atual.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de listas de encomendas, com a opção de gerar novas listas. Esta página deverá ser acessível a partir da página principal do gestor do stock.",
    "Deve ser possível retornar para a página principal de gestão do gestor de stock a qualquer momento em qualquer uma destas páginas.",
    "Na página de gestão de listas de encomendas, as listas deverão estar listadas numa tabela em que cada um dos seus campos ocupa uma coluna distinta, incluindo: identificador da lista, data de criação, fornecedor, quantidade de peças na lista e estado da lista. A ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único da lista.",
    "O gestor deverá poder selecionar nesta tabela qual a lista que deseja gerir e posteriormente, deverá selecionar se deseja editá-la ou eliminá-la. Deverão existir filtros que permitam a procura por fornecedor, por estado da lista (rascunho, enviada, recebida) e por intervalo de datas de criação.",
    "O sistema deverá analisar automaticamente o stock de todas as peças e identificar aquelas cuja quantidade em stock é inferior ao nível mínimo aceitável.",
    "O sistema deverá calcular a quantidade a encomendar de cada peça com base na diferença entre o nível mínimo aceitável e o stock atual.",
    "Quando o gestor selecionar a opção de gerar uma nova lista, o sistema deverá apresentar as peças identificadas como necessárias de encomendar, agrupadas por fornecedor.",
    "O gestor deverá poder selecionar quais as peças que deseja incluir na lista de encomenda e confirmar a criação da lista.",
    "Depois de confirmada a criação, o sistema deverá fornecer um identificador único para a lista de encomenda e deverá informar o gestor, apresentando uma página com todos os dados da nova lista, incluindo o detalhamento das peças, quantidades e fornecedores.",
    "Se o gestor desejar editar uma lista, depois de a selecionar e escolher que deseja editá-la, caso ela encontre-se no estado 'rascunho', deverá poder adicionar ou remover peças da lista. Qualquer alteração deverá seguir as mesmas validações usadas na criação.",
    "O gestor deverá poder alterar o estado de uma lista de 'rascunho' para 'enviada' quando considerar que a encomenda foi realizada junto do fornecedor.",
    "O gestor deverá poder alterar o estado de uma lista de 'enviada' para 'recebida' quando as peças forem recebidas e o sistema deverá adicionar automaticamente ao stock as peças registadas na lista.",
    "Nenhuma outra transição de estado deverá ser possível.",
    "Se o gestor quiser eliminar uma lista, depois de a escolher, deverá surgir uma página com os dados da lista e o gestor terá de confirmar a eliminação. Apenas listas no estado 'rascunho' poderão ser eliminadas.",
  ),
  relevancia: "Automatiza o processo de identificação de peças a encomendar, reduzindo o risco de ruturas de stock e otimizando a gestão de encomendas junto de fornecedores."
)

//  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA  SECRETÁRIA

#requisito(
  id: "REQ-014",
  titulo: "Gestão de Clientes",
  requisito_utilizador: "A secretária deverá poder adicionar, remover e editar clientes ...
  O sistema deve permitir consultar rapidamente o histórico de reparações de um cliente (...) através da pesquisa com o nome do cliente (...) incluindo serviços anteriores, peças substituídas e problemas identificados anteriormente.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Clientes",
  requisitos_sistema: (
    "Deve existir uma página de gestão de clientes, com as opções de seguir para as páginas de criação, edição e remoção de clientes. Esta página deverá ser acessível a partir da página principal da secretária.",
    "Na página de gestão de clientes, estes deverão estar listados numa tabela em que cada um dos seus campos ocupa uma coluna distinta. A ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único do cliente.",
    "A secretária deverá poder selecionar nesta tabela qual o cliente que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-lo ou editá-lo. Deverão existir filtros que permitam a procura por cliente usando o seu nome, identificador único ou NIF.",
    "Se selecionar desejar eliminar um cliente, depois de o escolher, deverá surgir uma página com os dados do cliente - incluindo as trotinetes a ele associadas e o todas as ordens de serviço associadas a cada uma delas, com todos os dados relevantes presentes na página - e a secretária terá de confirmar a eliminação. A partir deste momento, não deverá ser possível associar novas OS e trotinetes a este cliente.",
    "Se a secretária decidir alterar algum dado do cliente, depois de o escolher, deverá surgir uma página com os dados do cliente - incluindo as trotinetes a ele associadas e o todas as ordens de serviço associadas a cada uma delas, com todos os dados relevantes presentes na página - e ela terá de selecionar que deseja editá-lo. Depois, poderá selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de um cliente.",

  ),
  relevancia: "Centraliza a gestão de clientes, trotinetes e ordens de serviço, garantindo que toda a informação necessária para o processamento de reparações está disponível e actualizada."
)

#requisito(
  id: "REQ-015",
  titulo: "Gestão de Trotinetes",
  requisito_utilizador: "A secretária deverá poder adicionar, remover e editar (...) trotinetes ... 
  O sistema deve permitir consultar rapidamente o histórico de reparações (...) de uma trotinete, através da pesquisa com (...) o número de série da trotinete, incluindo serviços anteriores, peças substituídas e problemas identificados anteriormente.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Trotinetes",
  requisitos_sistema: (
    "Deve existir uma página de gestão de trotinetes, com as opções de seguir para as páginas de criação, edição e remoção de trotinetes. Esta página deverá ser acessível a partir da página principal da secretária.",
    "Na página de gestão de trotinetes, estas deverão estar listadas numa tabela em que cada um dos seus campos ocupa uma coluna distinta. A ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único da trotinete.",
    "A secretária deverá poder selecionar nesta tabela qual a trotinete que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la. Deverão existir filtros que permitam a procura por trotinete usando o seu número de série, o seu identificador único, a sua marca ou o seu modelo.",
    "Se desejar eliminar uma trotinete, depois de a escolher, deverá surgir uma página com os dados da trotinete - bem como todas as ordens de serviço associadas a ela, mostrando todos os dados relevantes - e a secretária terá de confirmar a eliminação. A partir deste momento, não deverá ser possível associar novas OS a esta trotinete.",
    "Se a secretária decidir que quer alterar algum dado da trotinete, depois de a selecionar, deverá surgir uma página com os dados da trotinete - bem como todas as ordens de serviço associadas a ela, mostrando todos os dados relevantes - e ela terá de selecionar que deseja editá-la. Depois, poderá escolher qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma trotinete."
    
  ),
  relevancia: "Centraliza a gestão de clientes, trotinetes e ordens de serviço, garantindo que toda a informação necessária para o processamento de reparações está disponível e actualizada."
)

#requisito(
  id: "REQ-016",
  titulo: "Registo de Dados de Clientes",
  requisito_utilizador: "O registo de clientes deverá ser composto obrigatoriamente por: nome, email, telemóvel e NIF.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Clientes",
  requisitos_sistema: (
    "Na página de criação de clientes, deverão ser fornecidos obrigatoriamente: nome, email, telemóvel e NIF.",
    "O nome deverá ser um campo textual não vazio, o email deverá seguir a convenção " + link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] + ", o telemóvel deverá ser um número composto por 9 dígitos e o NIF deverá ser um número composto por 9 dígitos.",
    "Depois de validados e preenchidos todos os dados obrigatórios de um cliente, quando a secretária confirmar, o sistema deverá fornecer um identificador único para cada cliente registado e deverá informar a secretária da criação, apresentando uma página com todos os dados do novo cliente.",
  ),
  relevancia: "Garante que todos os clientes têm informações de contacto válidas registadas, essencial para a comunicação e gestão de reparações."
)

#requisito(
  id: "REQ-017",
  titulo: "Registo de Dados de Trotinetes",
  requisito_utilizador: "O registo de trotinetes deverá ser composto obrigatoriamente por: marca, modelo, número de série, tipo de motor e cliente.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Clientes",
  requisitos_sistema: (
    "Na página de criação de trotinetes, deverão ser fornecidos obrigatoriamente: marca, modelo, número de série, tipo de motor e cliente.",
    "A marca, o modelo e o número de série deverão ser campos textuais não vazios e o tipo de motor deverá ser selecionado a partir de uma lista pré-definida de tipos de motor disponíveis no sistema.",
    "O cliente deverá ser selecionado a partir da lista de clientes já registados no sistema. Poderão ser fornecidos o identificador do cliente, o seu nome ou o seu NIF. Caso exista um cliente que corresponda ao dado fornecido, deverá surgir como opção listada para a secretária selecionar. Caso contrário, o sistema deverá informar que não existe nenhum cliente com os dados fornecidos.",
    "Depois de validados e preenchidos todos os dados de uma trotinete, quando a secretária confirmar, o sistema deverá fornecer um identificador único para cada trotinete registada e deverá informar a secretária da criação, apresentando uma página com todos os dados da nova trotinete.",
  ),
  relevancia: "Centraliza o registo de trotinetes associadas a clientes, permitindo um controlo preciso dos equipamentos sob reparação."
)

#requisito(
  id: "REQ-018",
  titulo: "Gestão de Ordens de Serviço",
  requisito_utilizador: "A secretária deverá poder consultar todas as OS consoante os seus estados (\"Pendente Diagnóstico\", \"Pendente Reparação\", \"A aguardar peças\", \"Pendente aprovação do orçamento\", \"Pendente Pagamento\", \"Paga\"), e deverão existir filtros que permitam filtrar as OS por estado, data, cliente e mecânico.
  A secretária deverá poder adicionar, remover e editar (...) ordens de serviço.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Deve existir uma página de gestão de ordens de serviço, com as opções de seguir para as páginas de criação, edição e remoção de ordens de serviço. Esta página deverá ser acessível a partir da página principal da secretária.",
    "As ordens de serviço deverão estar listadas numa tabela em que cada um dos seus campos ocupa uma coluna distinta. A ordenação deverá ter em conta a ordem numérica e deverá usar a data de criação da OS.",
    "Por padrão, ao entrar na página, todas as ordens de serviço  deverão estar listadas, independentemente do seu estado.",
    "O sistema deverá disponibilizar filtros que permitam a consulta de ordens de serviço através de: estado (\"Pendente Diagnóstico\", \"Pendente Reparação\", \"A aguardar peças\", \"Pendente aprovação do orçamento\", \"Pendente Pagamento\", \"Paga\" e \"Eliminada\"), data de criação ou intervalo de datas, cliente (inserindo o seu nome, identificador ou NIF) e mecânico atribuído (inserindo o seu nome ou identificador).",
    "A secretária deverá poder selecionar nesta tabela qual a ordem de serviço que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la.",
    "Se desejar eliminar uma ordem de serviço, depois de escolhê-la, deverá surgir uma página com os dados da ordem de serviço e a secretária terá de confirmar a eliminação. Assim, a OS deverá ficar no estado \"Eliminada\" e todos os seus dados não poderão ser usados para estatísticas da empresa.",
    "Se a secretária decidir alterar algum dado de uma ordem de serviço, deverá escolhê-la, e,depois, deverá surgir uma página com os dados da ordem de serviço e ela terá de selecionar que deseja editá-la. De seguida, deverá escolher qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma ordem de serviço.",
  ),
  relevancia: "Permite à secretária ter uma visão clara do estado de todas as ordens de serviço em processamento, facilitando a comunicação com clientes e mecânicos."
)

#requisito(
  id: "REQ-019",
  titulo: "Registo de Ordens de Serviço",
  requisito_utilizador: "O sistema deverá registar a criação de uma OS obrigatoriamente com: os dados do cliente, os dados técnicos da trotinete, uma descrição do problema e, opcionalmente, por acessórios e estado à entrada (com notas textuais).",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Deve existir uma página de criação de ordens de serviço. Esta página deverá ser acessível a partir da página principal da secretária.",
    "Na página de criação de uma ordem de serviço, deverão ser fornecidos obrigatoriamente os dados do cliente, os dados da trotinete, a descrição do problema e opcionalmente acessórios.",
    "O cliente deverá ser selecionado a partir da lista de clientes já registados no sistema. Deverão ser fornecidos o identificador do cliente, o seu nome ou o seu NIF. Caso exista um cliente que corresponda ao dado fornecido, deverá surgir como opção listada para a secretária selecionar. Caso contrário, o sistema deverá informar que não existe nenhum cliente com os dados fornecidos.",
    "A trotinete deverá ser selecionada a partir da lista de trotinetes associadas ao cliente. Caso o cliente selecionado não tenha trotinetes registadas, a secretária deverá ter a opção de registar uma nova trotinete nesse momento, sendo redirecionada para a página de criação de uma trotinete e seguindo o REQ-017.",
    "A descrição do problema deverá ser um campo textual não vazio onde a secretária descreva o problema relatado pelo cliente.",
    "Opcionalmente, a secretária deverá poder registar um ou mais acessórios associados à OS. Cada acessório deverá ser um campo textual não vazio.",
    "Depois de validados e preenchidos todos os dados obrigatórios de uma ordem de serviço, quando a secretária confirmar, o sistema deverá fornecer um identificador único para cada ordem de serviço registada e deverá informar a secretária da criação, apresentando uma página com todos os dados da nova ordem de serviço.",
    "Para além disto, deverá ser registada a data e hora da criação da OS."
    
  ),
  relevancia: "Centraliza o registo de todas as informações necessárias para o processamento de uma reparação, garantindo que mecânicos têm acesso a dados completos e precisos."
)

#requisito(
  id: "REQ-020",
  titulo: "Aprovação de Orçamento em Ordens de Serviço",
  requisito_utilizador: "O sistema deve permitir registar a aprovação do orçamento somente em OS que estejam no estado \"Pendente aprovação do orçamento\" por parte de um cliente, antes de permitir que o mecânico continue a reparação. Esta aprovação deverá colocar a OS num estado de \"Pendente Reparação\".",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Quando uma OS se encontra no estado \"Pendente aprovação do orçamento\", deverá ser possível editá-la através da página de gestão de ordens de serviço.",
    "Ao editar uma OS no estado \"Pendente aprovação do orçamento\", deverá surgir um novo campo na página de edição com a opção de \"Registar aprovação de orçamento\".",
    "A secretária deverá poder selecionar a opção para registar a aprovação do orçamento. Ao fazer isto, deverá ser solicitada uma confirmação visual antes de prosseguir.",
    "Após a confirmação da aprovação do orçamento, o estado da OS deverá ser alterado automaticamente para \"Pendente Reparação\".",
    "Caso o orçamento não seja aprovado, a OS deverá ficar no estado \"Orçamento não aprovado\" e todos os seus dados não poderão ser usados para estatísticas da empresa",
    "Nenhuma outra alteração ao estado da OS poderá ser realizado enquanto se encontrar no estado \"Pendente aprovação do orçamento\", mas poderão ser alterados os seus outros dados.",
  ),
  relevancia: "Garante que a reparação apenas prossegue após aprovação formal do cliente, evitando custos não autorizados."
)

#requisito(
  id: "REQ-021",
  titulo: "Registo de Notificação de Término de Reparação",
  requisito_utilizador: "Para uma OS no estado \"Pendente Pagamento\" deverá ser registado quando um cliente foi notificado sobre o término da reparação da trotinete.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Quando uma OS se encontra no estado \"Pendente Pagamento\", deverá ser possível editá-la através da página de gestão de ordens de serviço.",
    "Ao editar uma OS no estado \"Pendente Pagamento\", deverá surgir um novo campo na página de edição com a opção de \"Registar notificação de término\".",
    "A secretária deverá poder selecionar a opção para registar a notificação de término da reparação.",
    "Após o registo da notificação, o campo deverá exibir a data e hora em que o cliente foi notificado, de forma a permitir consulta posterior.",
    "O sistema deverá impedir que uma OS seja colocada no estado \"Paga\" se o cliente não tiver sido notificado previamente sobre o término da reparação, informando o erro.",
  ),
  relevancia: "Garante que há registo formal de quando o cliente foi contactado, importante para fins de auditoria e resolução de disputas."
)

#requisito(
  id: "REQ-022",
  titulo: "Registo de Pagamento e Conclusão de Ordens de Serviço",
  requisito_utilizador: "O sistema deve permitir registar a efetuação do pagamento e o respetivo método utilizado numa OS no estado \"Pendente Pagamento\" (numerário, multibanco, MBWay). Caso sejam ambos registados, a OS deverá ficar no estado \"Paga\".",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Quando uma OS se encontra no estado \"Pendente Pagamento\", deverá ser possível editá-la através da página de gestão de ordens de serviço.",
    "Ao editar uma OS no estado \"Pendente Pagamento\", deverá surgir uma secção na página de edição com os campos para registo de pagamento.",
    "A secretária deverá registar o método de pagamento utilizado através de um campo de seleção com as opções: \"Numerário\", \"Multibanco\" ou \"MBWay\". Apenas um método poderá ser selecionado.",
    "Se o cliente tiver sido previamente notificado sobre o término da reparação e o tiver sido registado o método de pagamento, o estado da OS deverá ser alterado automaticamente para \"Paga\".",
    "Se o método de pagamento não tiver sido selecionado o sistema deverá exibir uma mensagem de erro informando que é necessário registá-lo, ou, se o cliente não tiver sido notificado previamente, o sistema deverá exibir uma mensagem de erro informando que é necessário registar a notificação de término antes de processar o pagamento.",
    "A OS deverá passar para o estado \"Paga\" somente quando todos os requisitos forem cumpridos: cliente notificado e método de pagamento registado."
  ),
  relevancia: "Garante que a conclusão de uma reparação é registada de forma completa e auditável, com documentação clara do pagamento recebido."
)

//  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO

#requisito(
  id: "REQ-023",
  titulo: "Consulta e Gestão de Ordens de Serviço para Mecânicos",
  requisito_utilizador: "O mecânico poderá escolher de entre duas listas de OS: \"Pendentes de diagnóstico\" e \"Pendentes de reparação\".
O sistema deve impedir que dois mecânicos iniciem a mesma OS simultaneamente, atribuindo-a a um mecânico de cada vez. Caso um mecânico selecione uma OS que já está a ser tratada, deverá ser impedido.",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Deve existir uma página principal de mecânico com duas listas de ordens de serviço.",
    "A primeira lista deverá apresentar todas as OS no estado \"Pendente Diagnóstico\", organizadas numa tabela onde cada um dos seus campos ocupa uma coluna distinta.",
    "A segunda lista deverá apresentar todas as OS no estado \"Pendente Reparação\", organizadas numa tabela onde cada um dos seus campos ocupa uma coluna distinta.",
    "Ambas as listas deverão estar ordenadas pela data de criação, com as mais recentes em último lugar.",
    "O mecânico deverá poder selecionar uma OS de qualquer uma das listas para iniciar o seu trabalho.",
    "Ao selecionar uma OS, o mecânico deverá indicar que deseja executá-la, e, depois disso, deverá ser redirecionado para a página de gestão dessa OS específica.",
    "O sistema deverá impedir que múltiplas OS sejam selecionadas simultaneamente e que uma OS seja selecionada por vários mecânicos ao mesmo tempo, informando o mecânico do erro."
  ),
  relevancia: "Proporciona ao mecânico uma visão clara e organizada das tarefas disponíveis, facilitando a priorização e gestão do seu trabalho."
)

#requisito(
  id: "REQ-024",
  titulo: "Diagnóstico e Orçamentação de Ordens de Serviço",
  requisito_utilizador: "Quando um mecânico selecionar uma OS \"Pendente Diagnóstico\", deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações que estará disponível, poderá consultar (procurando por referência ou fornecedor) e associar peças do stock, e também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).
Para cada um dos passos da execução de uma OS, deverá existir um registo de qual mecânico fez. Isto é, deverá haver um registo de qual mecânico realizou o diagnóstico (...) da OS.",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Ao abrir uma OS no estado \"Pendente Diagnóstico\", deverá surgir uma página com os dados completos da OS, incluindo dados da trotinete e dados do cliente.",
    "Na página deverá estar disponível uma secção de \"Histórico de Reparações\" que apresente todas as ordens de serviço anteriores da trotinete, mostrando toda a informação disponível em cada uma delas.",
    "Na página deverá estar disponível uma secção de \"Seleção de Reparações\" onde o mecânico possa adicionar reparações necessárias.",
    "O mecânico deverá poder selecionar da tabela de reparações as entradas que pretender através de um campo de pesquisa que permita procurar por identificador ou nomenclatura.",
    "Para cada reparação selecionada, o sistema deverá apresentar: nomenclatura e preço de execução.",
    "Na página deverá estar disponível uma secção de \"Seleção de Peças\" onde o mecânico possa adicionar peças necessárias para as reparações.",
    "O mecânico deverá poder consultar o catálogo de peças disponíveis em stock através de um campo de pesquisa que permita procurar por referência do fornecedor ou identificador único da peça.",
    "Para cada peça selecionada, o sistema deverá apresentar: identificador único, referência do fornecedor, descrição, preço de venda unitário e quantidade disponível em stock.",
    "O mecânico deverá poder especificar a quantidade de cada peça utilizada. Esta quantidade deverá ser um número inteiro positivo e não poderá exceder a quantidade disponível em stock.",
    "O sistema deverá calcular automaticamente o valor total do orçamento, somando o custo das reparações selecionadas com o custo das peças (quantidade × preço unitário).",
    "O valor total do orçamento deverá ser exibido em destaque na página para consulta do mecânico.",
    "O mecânico deverá poder adicionar ou remover reparações e peças da lista até confirmar o diagnóstico.",
  ),
  relevancia: "Permite ao mecânico realizar um diagnóstico completo e bem documentado, com custos estimados claros, facilitando a aprovação pelo cliente e a execução da reparação."
)

#requisito(
  id: "REQ-026",
  titulo: "Atribuição de Estado com Base no Valor do Orçamento",
  requisito_utilizador: "Quando um mecânico selecionar uma OS \"Pendente Diagnóstico\" (e selecionar as várias reparações e peças usadas), deverá ser atribuído automaticamente o estado de \"Pendente de aprovação de orçamento\" e o mecânico não poderá proceder com a sua reparação.
Sempre que uma OS for colocada nos estados: \"Pendente de aprovação de orçamento\" (...) deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária (...), que deverá ficar guardado na sua lista de alertas. (...)",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Após o mecânico confirmar o diagnóstico, deverá ser redirecionado para a página principal.",
    "O estado da OS deverá ser alterado automaticamente para \"Pendente de aprovação de orçamento\" e deverá ser gerado um alerta que será adicionado à lista de alertas da secretária. O alerta deverá conter o identificador da OS e o estado em que ela se encontra.",
  ),
  relevancia: "Automatiza a validação de orçamentos, garantindo transparência e conformidade com políticas de aprovação de custos."
)

#requisito(
  id: "REQ-027",
  titulo: "Realização de Reparações em Ordens de Serviço",
  requisito_utilizador: "O sistema deve calcular automaticamente o valor final da reparação com base na soma das reparações feitas, peças utilizadas e preços das reparações definidos pelo gerente.
Quando um mecânico selecionar uma OS no estado \"Pendente de reparação\", deverá poder selecionar quais as reparações que ele executou a partir da tabela de reparações e poderá consultar (procurando por referência ou fornecedor) e associar as peças que usou. Também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).
Deverá haver um registo de qual mecânico realizou (...) a reparação da OS.",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Ao abrir uma OS no estado \"Pendente Reparação\", deverá surgir uma página com os dados completos da OS, incluindo os dados do diagnóstico (reparações e peças prescritas).",
    "Na página deverá estar disponível uma secção de \"Histórico de Reparações\" que apresente todas as ordens de serviço anteriores da trotinete, mostrando toda a informação disponível em cada uma delas.",
    "Na página deverá estar disponível uma secção com as reparações prescritas no diagnóstico. O mecânico deverá poder confirmar quais foram executadas marcando-as como concluídas.",
    "Na página deverá estar disponível uma secção com as peças prescritas no diagnóstico. O mecânico deverá poder confirmar quais foram utilizadas marcando-as como utilizadas.",
    "Na página deverá estar disponível uma secção de \"Seleção de Reparações\" onde o mecânico possa adicionar reparações adicionais necessárias.",
    "Para cada reparação selecionada, o sistema deverá apresentar:  nomenclatura e preço de execução.",
    "Na página deverá estar disponível uma secção de \"Seleção de Peças\" onde o mecânico possa adicionar peças adicionais necessárias para as reparações.",
    "O mecânico deverá poder consultar o catálogo de peças disponíveis em stock através de um campo de pesquisa que permita procurar por referência do fornecedor ou identificador único da peça.",
    "Para cada peça adicional utilizada, o mecânico deverá especificar a quantidade. Esta quantidade não poderá exceder a quantidade disponível em stock.",
    "Sempre que forem adicionadas peças à reparação, deverá ser retirada do stock da peça a quantidade especificada.",
    "Sempre que remover peças da lista, a quantidade no stock deverá aumentar na quantidade que o mecânico removeu.",
    "O mecânico deverá poder remover reparações ou peças da lista de utilizadas ou preescritas até confirmar a conclusão da reparação.",
    "Ao confirmar a adição de uma nova reparação ou uma nova peça, o sistema deverá calcular o novo valor total do orçamento, incluindo esta reparação ou peça adicional.",
    "Após adicionar um novo problema (reparação ou peça), o estado da OS deverá ser alterado automaticamente para \"Pendente de aprovação do orçamento\" e deverá ser gerado um alerta que será adicionado à lista de alertas da secretária quando o mecânico confirmar o término da execução da reparação. O alerta deverá conter o identificador da OS e o estado em que ela se encontra.",
    "O mecânico será informado que a reparação está pendente de aprovação do cliente para o novo orçamento mas que poderá continuar a executar reparações e a associar peças provenientes apenas do diagnóstico. Quando entender que já não pode fazer mais nenhuma reparação dentro do valor do orçamento aprovado, poderá terminar a reparação e adicionar as novas reparações e peças que achar necessárias.",
    "O sistema deverá calcular automaticamente o custo final da reparação baseado nas reparações executadas e nas peças efetivamente utilizadas.",
  ),
  relevancia: "Permite ao mecânico registar com precisão as ações realizadas, ajustando custos conforme necessário e documentando o trabalho executado."
)

#requisito(
  id: "REQ-028",
  titulo: "Gestão de Peças Indisponíveis e Requisição de Encomendas",
  requisito_utilizador: "Quando um mecânico selecionar uma OS no estado \"Pendente de reparação\" e uma ou mais peças da lista do diagnóstico não estiver disponível no stock, o sistema deverá disponibilizar a opção de requisição de encomenda das peças. A OS deverá ficar em estado de \"A aguardar peças\", e a reparação poderá prosseguir normalmente, mas a sua terminação deverá ser impedida até que o stock da peça necessário esteja disponível. Quando uma das peças estiver disponível no stock, a OS deverá ficar no estado \"Pendente reparação\".
Sempre que uma OS for colocada nos estados: (...) \"A aguardar peças\", deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) (...) para o gestor, que deverá ficar guardado na sua lista de alertas. (...)",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Ao tentar adicionar uma peça prescrita no diagnóstico durante a reparação, o sistema deverá verificar a disponibilidade em stock.",
    "Se uma peça não estiver disponível em stock, o sistema deverá alertar o mecânico indicando que a quantidade solicitada não está disponível.",
    "Para peças indisponíveis, o sistema deverá apresentar a opção de \"Requisitar Encomenda\" ao mecânico.",
    "Após registar a requisição, o estado da OS deverá ser alterado automaticamente para \"A aguardar peças\".",
    "O mecânico deverá poder selecionar esta opção e o sistema deverá adicionar um alerta à lista de alertas do gestor de stock com o identificador e referência da peça e um alerta à lista de alertas da secretária com o identificador da OS e o estado em que ela se encontra.",
    "O sistema deverá exibir uma mensagem informando que a OS está em estado de \"A aguardar peças\" e que a reparação poderá continuar com outras tarefas.",
    "Enquanto a OS está no estado \"A aguardar peças\", o mecânico poderá continuar a trabalhar noutras reparações ou outras partes da mesma OS que não dependam das peças em falta."
  ),
  relevancia: "Permite que a reparação prossiga mesmo com peças em falta, sem comprometer o progresso geral e garantindo que o sistema é atualizado automaticamente quando os recursos se tornam disponíveis."
)

#requisito(
  id: "REQ-029",
  titulo: "Gestão de Defeitos em Peças Instaladas",
  requisito_utilizador: "Durante a realização de uma reparação, depois de adicionadas peças à lista de peças usadas na OS, o mecânico poderá reportar defeitos nas peças instaladas, atribuindo o estado \"Possível defeito\" à peça e removendo-a da lista de peças usadas, diminuindo o valor final da reparação. Caso não hajam mais peças iguais em stock, a OS deverá ficar em estado de \"A aguardar peças\", e a reparação poderá prosseguir normalmente, mas a sua terminação deverá ser impedida até que o stock da peça necessário esteja disponível. Quando uma das peças estiver disponível no stock, a OS deverá ficar no estado \"Pendente reparação\".
Sempre que uma peça for colocada no estado \"Possível defeito\" ou tiver a sua quantidade mínima atingida, deverá ser gerado automaticamente um alerta (que contém o motivo do alerta e a peça em questão) para o gestor de stock, que deverá ficar guardado na sua lista de alertas. (...)",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Durante a realização de uma reparação, o mecânico deverá ter a opção de reportar um defeito numa peça já utilizada.",
    "O mecânico deverá poder selecionar a peça problemática da lista de peças utilizadas.",
    "Ao reportar um defeito, o mecânico deverá poder fornecer uma descrição do problema encontrado e o sistema deverá adicionar um alerta à lista de alertas do gestor de stock com o identificador, referência da peça e a descrição fornecida",
    "O sistema deverá registar uma entrada no sistema de gestão de devoluções, marcando o stock da peça como \"Possível defeito\".",
    "O mecânico deverá poder adicionar uma nova peça de substituição, se necessário, seguindo o processo normal de adição de peças."
  ),
  relevancia: "Garante qualidade e rastreabilidade de problemas com peças, permitindo ajustes de custos e registro automático para devoluções a fornecedores."
)

#requisito(
  id: "REQ-030",
  titulo: "Checklist de Segurança para Conclusão de Reparações",
  requisito_utilizador: "Quando um mecânico desejar terminar uma reparação, deverá verificar uma checklist obrigatória de segurança (travões, luzes, pneus, aceleração, travagem, visor, teste de condução).",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Quando o mecânico indica que deseja terminar a reparação, deverá surgir uma página com uma checklist obrigatória de segurança.",
    "A checklist deverá incluir os seguintes itens de verificação: Luzes, Pneus, Aceleração, Travagem, Visor e Teste de Condução.",
    "Para cada item da checklist, o mecânico deverá confirmar que foi verificado, marcando-o como \"Verificado\".",
    "O mecânico não poderá terminar a reparação até que todos os itens da checklist tenham sido marcados como verificados.",
    "Após a conclusão da checklist de segurança, o mecânico poderá proceder com a conclusão da reparação."
  ),
  relevancia: "Garante que todas as reparações cumprem padrões mínimos de segurança antes de serem devolvidas ao cliente, reduzindo riscos e problemas pós-reparação."
)

#requisito(
  id: "REQ-031",
  titulo: "Conclusão de Ordens de Serviço",
  requisito_utilizador: "O sistema deve permitir ao mecânico marcar a OS como concluída, ficando esta no estado \"Pendente Pagamento\".
Sempre que uma OS for colocada nos estados: (...) \"Pendente Pagamento\" (...), deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária (...), que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Após a verificação da checklist de segurança, o mecânico deverá ter a opção de \"Concluir Reparação\".",
    "Ao selecionar esta opção, o sistema deverá exibir um resumo final da OS, incluindo: dados da trotinete, reparações executadas, peças utilizadas, valor total da reparação e checklist de segurança concluída.",
    "O mecânico deverá poder confirmar a conclusão da reparação.",
    "Após a confirmação, o estado da OS deverá ser alterado automaticamente para \"Pendente Pagamento\".",
    "O sistema deverá adicionar um alerta à lista de alertas da secretária com o identificador da OS e o estado em que ela se encontra.",
  ),
  relevancia: "Marca a transição clara entre o trabalho do mecânico e o processamento administrativo e de pagamento, garantindo rastreabilidade completa da reparação."
)

#attachment(caption: [User Stories], none) <user_stories>

#user_story(
    id: "01",
    title: "Criar Ordem de Serviço",
    fonte: [],
    requisitos: ("RF01", "RF02", "RF03", "RF06"),
    criterios: (
      [A ordem de serviço deve conter dados do cliente, da trotinete e a descrição do problema.],
      [O sistema deve associar automaticamente o estado inicial 'Pendente diagnóstico' à nova ordem de serviço.],
      [A ordem de serviço deve ficar imediatamente disponível para consulta pelos mecânicos.],
    ),
    [
A secretária inicia sessão e seleciona a opção de criar uma nova ordem de serviço. O sistema apresenta um formulário para registar os dados necessários.

Se o cliente já existir no sistema, a secretária procura o cliente pelo nome, NIF ou número de cliente. Após selecionar o cliente correto, os dados são preenchidos automaticamente na ordem de serviço.

Se o cliente não existir, a secretária regista um novo cliente inserindo o nome, contacto e outros dados necessários. O sistema guarda o registo e associa-o à nova ordem de serviço.

Depois de selecionar ou criar o cliente, a secretária regista os dados da trotinete. Introduz a marca, o modelo e o número de série, quando disponível. 

De seguida, descreve o problema relatado pelo cliente. O sistema permite adicionar notas textuais sobre o comportamento da trotinete ou sintomas observados.

Se o cliente tiver deixado acessórios com a trotinete, como carregador, chave ou cadeado, a secretária regista esses itens na ordem de serviço.

Depois de inserir toda a informação necessária, a secretária confirma o registo. O sistema cria a ordem de serviço e atribui automaticamente o estado "Pendente Diagnóstico".

Após a criação, a ordem de serviço fica disponível na lista de ordens pendentes para que um mecânico possa iniciar o diagnóstico.
    ]
)

#user_story(
    id: "02",
    title: "Diagnosticar Trotinete",
    fonte: [],
    requisitos: ("RF05", "RF07", "RF08", "RF09"),
    criterios: (
      [O mecânico deve conseguir consultar a lista de ordens de serviço pendentes por diagnosticar e selecionar a próxima para diagnóstico.],
      [O sistema deve permitir registar o diagnóstico e as peças necessárias para a reparação.],
      [Caso o valor estimado exceda o orçamento aprovado, o sistema deve impedir a continuação da reparação sem nova aprovação do cliente.],
    ),
    [
O mecânico inicia sessão no sistema e consulta a lista de ordens de serviço pendentes por diagnosticar. O sistema apresenta as ordens ordenadas por ordem de chegada.

O mecânico seleciona uma OS disponível. O sistema abre a ficha da reparação e apresenta os dados do cliente, da trotinete e a descrição do problema registado na receção.

O mecânico inicia o diagnóstico técnico da trotinete. Após a verificação do equipamento, regista no sistema as conclusões do diagnóstico.

Se a causa do problema for identificada, o mecânico regista as intervenções necessárias para a reparação. Pode também indicar as peças que deverão ser substituídas.

Depois de terminar o diagnóstico, o sistema calcula automaticamente uma estimativa de custo com base nas operações registadas e nas peças selecionadas.

O sistema altera automaticamente o estado da ordem de serviço para "Pendente de aprovação de orçamento" e coloca um alerta para a secretária contactar o cliente.

Após a aprovação do orçamento, a OS fica no estado "Pendente Reparação".
    ]
)

#user_story(
    id: "03",
    title: "Gerir Alteração de Orçamento",
    criterios: (
      [O sistema deve permitir registar a aprovação ou rejeição de uma alteração de orçamento associada a uma ordem de serviço.],
      [O sistema deve manter o histórico das alterações de orçamento na OS.],
      [A reparação só pode prosseguir após a aprovação da alteração do orçamento pelo cliente.],
    ),
    [
A secretária inicia sessão e consulta a lista de ordens de serviço. O sistema apresenta as ordens no estado "Pendente aprovação do orçamento".

A secretária seleciona a ordem de serviço correspondente. O sistema apresenta a OS indicando o motivo da alteração no orçamento.

A secretária contacta o cliente através do canal disponível, como telefone, email ou outro meio de comunicação.

Se o cliente aprovar a alteração do orçamento, a secretária regista a aprovação no sistema. O sistema guarda o novo valor aprovado e atualiza o estado da ordem de serviço para "Pendente Reparação".

Se o cliente rejeitar a alteração do orçamento, a secretária regista a rejeição no sistema. O sistema mantém o valor anterior do orçamento e altera o estado da ordem de serviço para indicar que a reparação não pode prosseguir.
    ]
)

#user_story(
    id: "04",
    title: "Gerir Stock de Peças",
    criterios: (
      [O sistema deve permitir ao gestor de stock registar novas peças no sistema.],
      [O sistema deve permitir editar ou remover peças existentes.],
    ),
    [
O gestor de stock inicia sessão no sistema e acede à área de gestão de peças. O sistema apresenta uma tabela com todas as peças registadas, incluindo fornecedor, referência, preços, nível mínimo aceitável em stock e tempo de garantia.

Se o gestor pretender registar uma nova peça, seleciona a opção de criação. O sistema apresenta um formulário onde o gestor introduz o fornecedor, a referência do fornecedor, o preço de compra, o preço de venda e o nível mínimo aceitável em stock. Após confirmar, o sistema valida os dados, cria o registo da peça e apresenta um resumo da nova entrada.

Se o gestor pretender editar uma peça existente, seleciona a peça na tabela. O sistema apresenta os dados atuais e permite escolher os campos a alterar. Após guardar, o sistema atualiza a peça e regista a alteração com data, hora e utilizador.

Se o gestor pretender remover uma peça, seleciona a peça na tabela e confirma a eliminação. O sistema impede que a peça removida seja utilizada em novas operações, mas mantém o registo para histórico.

Em todos os casos, o sistema regista automaticamente a data, hora e utilizador responsável pela operação, garantindo rastreabilidade total das alterações ao stock.
    ]
)

#user_story(
    id: "05",
    title: "Consultar Estado das Ordens de Serviço",
    criterios: (
      [O sistema deve permitir consultar ordens de serviço por estado.],
      [O sistema deve apresentar listas separadas ou filtradas para diagnóstico, reparação, reparadas e pagas.],
      [A secretária deve conseguir abrir qualquer ordem de serviço para consultar os seus detalhes.],
    ),
    [

O secretária pode selecionar filtros de estado, data ou cliente e o sistema apresenta as ordens de serviço correspondentes.

Em qualquer uma das listas de estados, o sistema apresenta toda a informação de cada ordem de serviço, como o seu cliente, trotinete, estado, reparações e peças.

Após a consulta, a secretária pode regressar à lista de ordens de serviço ou aplicar um novo filtro de estado.
    ]
)



#user_story(
    id: "06",
    title: "Gerir Registo Salarial de Funcionários",
    criterios: (
      [O sistema deve permitir registar e atualizar o salário base de cada funcionário.],
      [O sistema deve permitir registar aumentos salariais e horas extraordinárias.],
      [O sistema deve calcular automaticamente o valor mensal total a pagar ao funcionário.],
    ),
    [
O gerente inicia sessão no sistema e acede à área de gestão de funcionários. O sistema apresenta a lista de funcionários registados na empresa.

O gerente seleciona um funcionário da lista. O sistema apresenta a ficha individual com os dados pessoais.

Se o gerente pretender definir ou atualizar o salário base, introduz o valor correspondente no campo de salário base. O sistema guarda o novo valor e regista a data e hora da alteração.

Se o funcionário tiver realizado horas extraordinárias, o gerente regista o número de horas realizadas desde o início do mês. O sistema associa essas horas ao mês de referência.

Após o registo das horas extraordinárias, o sistema calcula automaticamente o valor adicional a pagar com base nas regras definidas para remuneração de horas extra.

Em seguida, o sistema calcula o valor mensal total a pagar ao funcionário, somando o salário base bruto e os valores associados às horas extraordinárias.

Após a verificação, o gerente pode guardar os dados e regressar à lista de funcionários ou selecionar outro funcionário para consulta.
    ]
)

#user_story(
    id: "07",
    title: "Gerir Informação de Fornecedores",
    criterios: (
      [O sistema deve permitir registar e atualizar contactos e emails de fornecedores.],
      [O sistema deve permitir consultar rapidamente a informação de um fornecedor para apoiar encomendas.],
    ),
    [
O gerente inicia sessão no sistema e acede à área de gestão de fornecedores. O sistema apresenta a lista de fornecedores registados.

Se o gerente pretender adicionar um novo fornecedor, seleciona a opção de criar fornecedor. O sistema apresenta um formulário para registar a informação necessária.

O gerente introduz o nome do fornecedor, contactos telefónicos e/ou endereços de email. O sistema guarda os dados e cria o registo do fornecedor.

Se o fornecedor já existir, o gerente pode selecionar o registo correspondente. O sistema apresenta a ficha do fornecedor com os dados atuais.

O gerente pode atualizar os contactos ou o email caso exista alguma alteração. O sistema guarda as modificações efetuadas.

Após guardar os dados, o sistema mantém o registo atualizado e associa a informação às peças correspondentes.

Sempre que for necessário realizar uma encomenda, o gerente pode consultar rapidamente a ficha do fornecedor para verificar contactos e preços habituais.
    ]
)

#user_story(
    id: "08",
    title: "Consultar e Analisar o Estado Financeiro",
    fonte: ["Entrevista #001"],
    requisitos: ("REQ-005"),
    criterios: (
      [O sistema deve apresentar todos os movimentos financeiros registados, incluindo pagamentos de encomendas, salários e receção de pagamentos.],
      [O sistema deve permitir filtrar os movimentos por intervalo de datas.],
      [O sistema deve permitir filtrar os movimentos por tipo de gasto ou receita.],
      [O sistema deve apresentar totais agregados por categoria financeira.],
    ),
    [
O gerente inicia sessão no sistema e acede à página de consulta do estado financeiro. O sistema apresenta os movimentos financeiros mais recentes, organizados numa tabela com todos os seus campos visíveis.

O gerente seleciona um intervalo de datas para análise. O sistema filtra automaticamente os movimentos apresentados, exibindo apenas os que se encontram dentro do período definido.

De seguida, o gerente escolhe visualizar apenas um tipo específico de movimento, como salários, gastos com peças, lucros com mão de obra ou lucros provenientes da venda de peças. O sistema atualiza a tabela e apresenta os totais agregados correspondentes à categoria selecionada.

Após concluir a análise, o gerente pode regressar à página principal de administração ou continuar a explorar os dados financeiros disponíveis.
    ]
)



#user_story(
    id: "09",
    title: "Calcular Valor Final de uma Reparação",
    criterios: (
      [O sistema deve calcular o valor total de uma reparação com base nas intervenções registadas e nas peças utilizadas.],
      [O sistema deve somar automaticamente os valores das intervenções e o preço de venda das peças associadas à ordem de serviço.],
      [O sistema deve apresentar o valor final da reparação antes da faturação ou conclusão da ordem de serviço.],
    ),
    [
O mecânico inicia sessão e executa o diagnóstico e a reparação da trotinete associada a uma ordem de serviço. Durante a intervenção, regista no sistema as ações realizadas.

Para cada intervenção efetuada, o mecânico seleciona o tipo de intervenção na lista disponível. O sistema associa automaticamente o valor definido para essa intervenção.

Se forem utilizadas peças durante a reparação, o mecânico adiciona essas peças à ordem de serviço. O sistema associa automaticamente o preço de venda de cada peça.

À medida que intervenções e peças são registadas, o sistema atualiza o valor parcial da reparação.

Quando todas as intervenções e peças estiverem registadas, o sistema calcula o valor total do serviço. O cálculo corresponde à soma dos valores das intervenções realizadas e dos preços de venda das peças utilizadas.

O sistema apresenta o valor final da reparação na ordem de serviço.

Se o mecânico alterar a lista de intervenções ou remover/adicionar peças, o sistema recalcula automaticamente o valor total.

Após a conclusão da reparação, a ordem de serviço pode prosseguir para faturação e para posterior entrega ao cliente.
    ]
)

#user_story(
    id: "10",
    title: "Gerir Tabela de Reparações",
    fonte: ["Entrevista #001"],
    requisitos: ("REQ-006"),
    criterios: (
      [O sistema deve permitir ao gerente criar, editar e remover entradas da tabela de reparações.],
      [O sistema deve validar todos os campos antes de permitir a criação ou edição.],
      [O sistema deve impedir a utilização de entradas removidas em novas ordens de serviço.],
    ),
    [
O gerente inicia sessão no sistema e acede à página de gestão da tabela de reparações. O sistema apresenta uma tabela com todas as entradas existentes.

Se o gerente pretender criar uma nova entrada, seleciona a opção de criação. O sistema apresenta um formulário onde o gerente introduz a nomenclatura, a descrição detalhada e o preço de execução. Após confirmar, o sistema valida os dados e cria a entrada.

Se o gerente pretender editar uma entrada existente, seleciona a entrada na tabela. O sistema apresenta os dados atuais e permite escolher os campos a alterar. Após guardar, o sistema atualiza a entrada.

Se o gerente pretender remover uma entrada, seleciona a entrada e confirma a eliminação. O sistema impede que essa entrada seja usada em novas ordens de serviço, mas mantém o registo para histórico.

Após concluir as operações, o gerente pode regressar à página principal de administração.
    ]
)

#user_story(
    id: "11",
    title: "Registar Entradas de Peças em Stock",
    fonte: ["Entrevista #001"],
    requisitos: ("REQ-009"),
    criterios: (
      [O sistema deve permitir registar entradas de peças com todos os dados obrigatórios.],
      [O sistema deve validar automaticamente fornecedor, referência e quantidades.],
      [O sistema deve registar data, hora e utilizador em todas as operações.],
    ),
    [
O gestor de stock acede à página de registo de entradas de peças. O sistema apresenta uma tabela com todas as entradas anteriores e filtros para pesquisa.

O gestor seleciona a opção de adicionar nova entrada. O sistema solicita a referência da peça ou o identificador da peça existente. Se a peça existir, o sistema apresenta os seus dados; caso contrário, informa que não existe nenhuma peça correspondente.

O gestor introduz a quantidade, o preço de compra e o preço de venda. O sistema regista automaticamente a data de receção.

Após confirmar, o sistema valida os dados, regista a entrada e apresenta um resumo da operação.

O gestor pode consultar, editar ou eliminar entradas anteriores.
    ]
)


#user_story(
    id: "12",
    title: "Gerir Devoluções de Peças ao Fornecedor",
    fonte: ["Entrevista #001"],
    requisitos: ("REQ-010"),
    criterios: (
      [O sistema deve permitir criar, editar e eliminar registos de devoluções.],
      [O sistema deve atualizar automaticamente o stock conforme o estado da devolução.],
      [O sistema deve impedir transições de estado inválidas.],
    ),
    [
O gestor de stock acede à página de gestão de devoluções. O sistema apresenta, por padrão, as peças no estado "Possível defeito", listadas numa tabela.

O gestor seleciona uma devolução existente ou cria uma nova. Ao criar uma devolução, o sistema solicita o identificador da peça, a quantidade, a data, o motivo e regista automaticamente o estado inicial "Pendente de devolução".

Se o gestor alterar o estado para "Pendente de devolução", o sistema reduz automaticamente o stock da peça.

Se o estado for alterado para "Devolvida ao fornecedor", o sistema repõe a quantidade no stock sem custos adicionais.

Se o estado for alterado para "Inválida para devolução", o sistema aumenta os gastos com peças no valor correspondente.

O sistema impede qualquer outra transição de estado e regista sempre data, hora e utilizador responsável.

Após concluir, o gestor pode regressar à página principal de gestão de stock.
    ]
)


#user_story(
    id: "13",
    title: "Gerir Informação de Fornecedores",
    fonte: ["Entrevista #001"],
    requisitos: ("REQ-011"),
    criterios: (
      [O sistema deve permitir criar, editar e remover fornecedores.],
      [O sistema deve impedir associar peças a fornecedores removidos.],
    ),
    [
O gestor de stock acede à página de gestão de fornecedores. O sistema apresenta uma tabela com todos os fornecedores registados, incluindo nome, contactos e email.

Se o gestor pretender criar um novo fornecedor, seleciona a opção de criação e preenche o formulário com os dados necessários. O sistema valida os campos e cria o registo.

Se o gestor selecionar um fornecedor existente, o sistema apresenta a sua ficha completa. O gestor pode atualizar contactos ou email e guardar as alterações.

Se o gestor pretender remover um fornecedor, o sistema apresenta os dados e solicita confirmação. Após a remoção, o sistema impede que novas peças sejam associadas a esse fornecedor.

Todas as operações ficam registadas com data, hora e utilizador.
    ]
)

#pagebreak()

#attachment(caption: [Use Cases], none) <use_cases>

#uc_spec(
    id: "01",
    nome: "Fazer Autenticação",
    atores: "Funcionário (Gerente, Secretária, Mecânico ou Gestor de Stock)",
    pre_condicoes: "O utilizador não está autenticado e possui credenciais válidas registadas no sistema.",
    pos_condicoes: "O utilizador é autenticado e é-lhe concedido acesso às suas funcionalidades, consoante o seu cargo.",
    fluxo_normal: (
      [O utilizador introduz o seu identificador único e a sua palavra‑passe.],
      [O sistema valida as credenciais introduzidas.],
      [O sistema identifica o cargo do utilizador autenticado.],
      [O sistema concede ao utilizador acesso às funcionalidades correspondentes ao seu papel.],
    ),
    fluxos_excecao: (
      (
        condicao: "Credenciais inválidas",
        passo: "2",
        fluxo: (
          [2.1 - O sistema indica que as credenciais são inválidas.],
        )
      ),
    )
)

#uc_spec(
    id: "02",
    nome: "Registar Funcionário",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado.",
    pos_condicoes: "Um novo funcionário é registado no sistema com os campos sensíveis armazenados de forma encriptada e um identificador único é atribuído.",
    fluxo_normal: (
      [O gerente seleciona a opção de criar novo funcionário.],
      [O gerente preenche todos os dados obrigatórios: nome, morada completa (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário (valor por hora, valor mensal bruto e valor mensal líquido) e palavra-passe.],
      [O gerente confirma o registo.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o número de porta é um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra.],
      [O sistema valida que a rua é um campo textual não vazio.],
      [O sistema valida que a localidade é um campo textual não vazio.],
      [O sistema valida que o código-postal segue o formato de 4 dígitos, um hífen e 3 dígitos.],
      [O sistema valida que o telemóvel é composto por 9 dígitos.],
      [O sistema valida que o email segue o formato válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322].],
      [O sistema valida que a data de nascimento é uma data válida.],
      [O sistema valida que o NISS é composto por 11 dígitos.],
      [O sistema valida que o NIF é composto por 9 dígitos.],
      [O sistema valida que o NUS é composto por 9 dígitos.],
      [O sistema valida que o IBAN é composto por 2 caracteres alfabéticos seguidos de 23 dígitos.],
      [O sistema valida que o valor por hora é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal bruto é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal líquido é um número real positivo com 2 casas decimais.],
      [O sistema valida que a palavra-passe é um campo textual não vazio.],
      [O sistema encripta os campos sensíveis (nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário pago por hora, salário mensal líquido, salário mensal bruto, número de porta, rua, localidade e código-postal) antes do armazenamento.],
      [O sistema cria o funcionário e atribui um identificador único.],
      [O sistema confirma o registo apresentando os dados do funcionário criado.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela o registo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o gerente que o nome deve ser um campo textual não vazio e impede o registo.],
        ),
      ),
      (
        condicao: "Número de porta inválido",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o gerente que o número de porta deve ser um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra, e impede o registo.],
        ),
      ),
      (
        condicao: "Rua inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o gerente que a rua deve ser um campo textual não vazio e impede o registo.],
        ),
      ),
      (
        condicao: "Localidade inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o gerente que a localidade deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Código-postal inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o gerente que o código-postal deve seguir o formato XXXX-XXX (4 dígitos, hífen, 3 dígitos) e impede o registo.],
        )
      ),
      (
        condicao: "Telemóvel inválido",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o gerente que o telemóvel deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "Email inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o gerente que o email deve seguir o formato de endereço de correio electrónico válido (nome#"@"dominio.com) e impede o registo.],
        )
      ),
      (
        condicao: "Data de nascimento inválida",
        passo: "11",
        fluxo: (
          [11.1 - O sistema informa o gerente que a data de nascimento deve ser uma data válida e impede o registo.],
        )
      ),
      (
        condicao: "NISS inválido",
        passo: "12",
        fluxo: (
          [12.1 - O sistema informa o gerente que o NISS deve ser composto por 11 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "NIF inválido",
        passo: "13",
        fluxo: (
          [13.1 - O sistema informa o gerente que o NIF deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "NUS inválido",
        passo: "14",
        fluxo: (
          [14.1 - O sistema informa o gerente que o NUS deve ser composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "IBAN inválido",
        passo: "15",
        fluxo: (
          [15.1 - O sistema informa o gerente que o IBAN deve ser composto por 2 caracteres alfabéticos seguidos de 23 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "Valor por hora inválido",
        passo: "16",
        fluxo: (
          [16.1 - O sistema informa o gerente que o valor por hora deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Valor mensal bruto inválido",
        passo: "17",
        fluxo: (
          [17.1 - O sistema informa o gerente que o valor mensal bruto deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Valor mensal líquido inválido",
        passo: "18",
        fluxo: (
          [18.1 - O sistema informa o gerente que o valor mensal líquido deve ser um número real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Palavra-passe inválida",
        passo: "19",
        fluxo: (
          [19.1 - O sistema informa o gerente que a palavra-passe deve ser um campo textual não vazio e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "03",
    nome: "Editar Funcionário",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado e existe pelo menos um funcionário registado no sistema.",
    pos_condicoes: "Os dados do funcionário são atualizados no sistema com os novos valores fornecidos, mantendo os campos sensíveis armazenados de forma encriptada.",
    fluxo_normal: (
      [O gerente seleciona o funcionário que pretende editar.],
      [O sistema apresenta os dados atuais do funcionário.],
      [O gerente seleciona a opção de editar o funcionário.],
      [O gerente seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O gerente confirma as alterações.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o número de porta é um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra.],
      [O sistema valida que a rua é um campo textual não vazio.],
      [O sistema valida que a localidade é um campo textual não vazio.],
      [O sistema valida que o código-postal segue o formato de 4 dígitos, um hífen e 3 dígitos.],
      [O sistema valida que o telemóvel é composto por 9 dígitos.],
      [O sistema valida que o email segue o formato válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322].],
      [O sistema valida que a data de nascimento é uma data válida.],
      [O sistema valida que o NISS é composto por 11 dígitos.],
      [O sistema valida que o NIF é composto por 9 dígitos.],
      [O sistema valida que o NUS é composto por 9 dígitos.],
      [O sistema valida que o IBAN é composto por 2 caracteres alfabéticos seguidos de 23 dígitos.],
      [O sistema valida que o valor por hora é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal bruto é um número real positivo com 2 casas decimais.],
      [O sistema valida que o valor mensal líquido é um número real positivo com 2 casas decimais.],
      [O sistema valida que a palavra-passe é um campo textual não vazio.],
      [O sistema encripta os campos sensíveis alterados (entre nome, telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário pago por hora, salário mensal líquido, salário mensal bruto, número de porta, rua, localidade e código-postal) antes do armazenamento.],
      [O sistema atualiza os dados do funcionário e apresenta os dados atualizados.],
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o gerente que o nome deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Número de porta inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o gerente que o número de porta deve ser um número inteiro com no máximo 4 dígitos, podendo ser seguido por uma letra, e impede a atualização.],
        )
      ),
      (
        condicao: "Rua inválida",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o gerente que a rua deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Localidade inválida",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o gerente que a localidade deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Código-postal inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o gerente que o código-postal deve seguir o formato XXXX-XXX (4 dígitos, hífen, 3 dígitos) e impede a atualização.],
        )
      ),
      (
        condicao: "Telemóvel inválido",
        passo: "11",
        fluxo: (
          [11.1 - O sistema informa o gerente que o telemóvel deve ser composto por 9 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "Email inválido",
        passo: "12",
        fluxo: (
          [12.1 - O sistema informa o gerente que o email deve seguir o formato de endereço de correio electrónico válido (nome#"@"dominio.com) e impede a atualização.],
        )
      ),
      (
        condicao: "Data de nascimento inválida",
        passo: "13",
        fluxo: (
          [13.1 - O sistema informa o gerente que a data de nascimento deve ser uma data válida e impede a atualização.],
        )
      ),
      (
        condicao: "NISS inválido",
        passo: "14",
        fluxo: (
          [14.1 - O sistema informa o gerente que o NISS deve ser composto por 11 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "NIF inválido",
        passo: "15",
        fluxo: (
          [15.1 - O sistema informa o gerente que o NIF deve ser composto por 9 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "NUS inválido",
        passo: "16",
        fluxo: (
          [16.1 - O sistema informa o gerente que o NUS deve ser composto por 9 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "IBAN inválido",
        passo: "17",
        fluxo: (
          [17.1 - O sistema informa o gerente que o IBAN deve ser composto por 2 caracteres alfabéticos seguidos de 23 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "Valor por hora inválido",
        passo: "18",
        fluxo: (
          [18.1 - O sistema informa o gerente que o valor por hora deve ser um número real positivo com 2 casas decimais e impede a atualização.],
        )
      ),
      (
        condicao: "Valor mensal bruto inválido",
        passo: "19",
        fluxo: (
          [19.1 - O sistema informa o gerente que o valor mensal bruto deve ser um número real positivo com 2 casas decimais e impede a atualização.],
        )
      ),
      (
        condicao: "Valor mensal líquido inválido",
        passo: "20",
        fluxo: (
          [20.1 - O sistema informa o gerente que o valor mensal líquido deve ser um número real positivo com 2 casas decimais e impede a atualização.],
        )
      ),
      (
        condicao: "Palavra-passe inválida",
        passo: "21",
        fluxo: (
          [21.1 - O sistema informa o gerente que a palavra-passe deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "04",
    nome: "Remover Funcionário",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado e existe pelo menos um funcionário registado no sistema.",
    pos_condicoes: "O funcionário é removido do sistema e deixa de conseguir autenticar-se.",
    fluxo_normal: (
      [O gerente seleciona o funcionário.],
      [O sistema apresenta os dados do funcionário selecionado.],
      [O gerente seleciona a opção de eliminar o funcionário.],
      [O gerente confirma a eliminação.],
      [O sistema remove o funcionário e bloqueia o seu acesso ao sistema.],
      [O sistema confirma ao gerente que a eliminação foi bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      ),
    )
)



#uc_spec(
    id: "05",
    nome: "Registar Horas Extraordinárias",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado e existe pelo menos um funcionário registado no sistema.",
    pos_condicoes: "As horas extraordinárias são registadas e o valor total a pagar ao funcionário no mês corrente é atualizado.",
    fluxo_normal: (
      [O gerente seleciona o funcionário.],
      [O sistema apresenta os dados atuais do funcionário.],
      [O gerente seleciona a opção de registar horas extraordinárias.],
      [O gerente introduz o número de horas extraordinárias.],
      [O sistema valida que o valor introduzido é um número inteiro.],
      [O gerente confirma o registo.],
      [O sistema regista as horas extraordinárias e recalcula automaticamente o valor total a pagar ao funcionário no mês corrente.],
      [O sistema apresenta uma confirmação do registo com o valor total atualizado.]
    ),
    fluxos_excecao: (
      (
        condicao: "Valor introduzido não é um número inteiro",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o gerente que o valor introduzido é inválido e impede o registo.],
        )
      ),
      (
        condicao: "Gerente cancela o registo",
        passo: "6",
        fluxo: (
          [6.1 - O sistema cancela o processo.],
        )
      )
    )
)


#uc_spec(
    id: "06",
    nome: "Consultar Estado Financeiro",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado.",
    pos_condicoes: "Os movimentos que o gerente deseja consultar, são-lhe apresentados.",
    fluxo_normal: (
      [O gerente inicia a consulta do estado financeiro.],
      [O gerente seleciona um ou vários filtros, nomeadamente: por intervalo de datas, por tipo de gasto ou receita (salários, gastos com peças, lucros com mão de obra, lucros provenientes da venda de peças).],
      [O gerente confirma o conjunto de filtros que pretende usar.],
      [O sistema procura por movimentos que correspondam aos filtros usados e apresenta-os ao gerente.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente não confirma os filtros",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
    )
)

#uc_spec(
    id: "07",
    nome: "Registar Serviço de Reparação",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado.",
    pos_condicoes: "Uma nova entrada é adicionada ao catálogo de reparações e um identificador único é atribuído.",
    fluxo_normal: (
      [O gerente seleciona a opção de criar uma nova entrada.],
      [O gerente preenche todos os dados obrigatórios: nomenclatura, descrição detalhada e preço de execução.],
      [O gerente confirma o registo.],
      [O sistema valida que a nomenclatura é um campo textual não vazio.],
      [O sistema valida que a descrição é um campo textual não vazio.],
      [O sistema valida que o preço de execução é um valor real positivo com 2 casas decimais.],
      [O sistema cria a entrada, atribui um identificador único e apresenta os dados da nova entrada registada.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela o registo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nomenclatura inválida",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o gerente que a nomenclatura deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Descrição inválida",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o gerente que a descrição deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Preço de execução inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o gerente que o preço de execução deve ser um valor real positivo com 2 casas decimais e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "08",
    nome: "Editar Serviço de Reparação",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado e existe pelo menos uma entrada no catálogo de reparações.",
    pos_condicoes: "Os dados da entrada são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O gerente seleciona a entrada que pretende editar.],
      [O sistema apresenta os dados atuais da entrada.],
      [O gerente seleciona a opção de editar a entrada.],
      [O gerente seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O gerente confirma as alterações.],
      [O sistema valida que a nomenclatura é um campo textual não vazio.],
      [O sistema valida que a descrição é um campo textual não vazio.],
      [O sistema valida que o preço de execução é um valor real positivo com 2 casas decimais.],
      [O sistema atualiza os dados da entrada e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nomenclatura inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o gerente que a nomenclatura deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Descrição inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o gerente que a descrição deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Preço de execução inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o gerente que o preço de execução deve ser um valor real positivo com 2 casas decimais e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "09",
    nome: "Remover Serviço de Reparação",
    atores: "Gerente",
    pre_condicoes: "O gerente está autenticado e existe pelo menos uma entrada no catálogo de reparações.",
    pos_condicoes: "Deixa de ser possível associar a entrada a novas ordens de serviço.",
    fluxo_normal: (
      [O gerente seleciona a reparação que pretende eliminar.],
      [O sistema apresenta ao gerente os dados da reparação selecionada.],
      [O gerente seleciona a opção de eliminar a reparação.],
      [O gerente confirma a eliminação.],
      [O sistema apresenta uma mensagem de confirmação da eliminação bem-sucedida.],
      [O sistema impossibilita o uso da entrada em novas ordens de serviço.],
    ),
    fluxos_excecao: (
      (
        condicao: "Gerente cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      ),
    )
)


#uc_spec(
    id: "10",
    nome: "Registar Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um fornecedor registado.",
    pos_condicoes: "Uma nova peça é registada no sistema, marcada como ativa por defeito, e um identificador único é atribuído.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova peça.],
      [O funcionário introduz o identificador do fornecedor.],
      [O sistema valida a existência do fornecedor.],
      [O funcionário preenche todos os restantes dados obrigatórios: referência do fornecedor, nome, descrição, marca, preço de venda, nível mínimo aceitável em stock e tempo de garantia.],
      [O funcionário confirma o registo.],
      [O sistema valida que a referência do fornecedor é um campo textual não vazio.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que a descrição é um campo textual não vazio.],
      [O sistema valida que a marca é um campo textual não vazio.],
      [O sistema valida que o preço de venda é um valor real positivo com 2 casas decimais.],
      [O sistema valida que o nível mínimo aceitável em stock é um número inteiro não negativo.],
      [O sistema valida que o tempo de garantia é um valor inteiro positivo.],
      [O sistema cria a peça com o estado marcado como ativa e atribui-lhe um identificador único.],
      [O sistema apresenta os dados da nova peça registada.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela o registo",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Fornecedor inexistente",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existe nenhum fornecedor com o identificador fornecido.],
        )
      ),
      (
        condicao: "Referência do fornecedor inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a referência do fornecedor deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Descrição inválida",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que a descrição deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Marca inválida",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que a marca deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Preço de Venda inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o funcionário que o preço de venda deve ser um número real positivo com duas casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Nível mínimo aceitável em stock inválido",
        passo: "11",
        fluxo: (
          [11.1 - O sistema informa o funcionário que o nível mínimo aceitável em stock deve ser um número inteiro não negativo e impede o registo.],
        )
      ),
      (
        condicao: "Tempo de garantia inválido",
        passo: "12",
        fluxo: (
          [12.1 - O sistema informa o funcionário que o tempo de garantia deve ser um número inteiro positivo e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "11",
    nome: "Editar Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O utilizador está autenticado e existe pelo menos uma peça registada.",
    pos_condicoes: "Os dados da peça são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona a peça que pretende editar.],
      [O sistema apresenta os dados atuais da peça.],
      [O funcionário seleciona a opção de editar a peça.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que a referência do fornecedor é um campo textual não vazio.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que a descrição é um campo textual não vazio.],
      [O sistema valida que a marca é um campo textual não vazio.],
      [O sistema valida que o preço de venda é um valor real positivo com 2 casas decimais.],
      [O sistema valida que o nível mínimo aceitável em stock é um número inteiro não negativo.],
      [O sistema valida que o tempo de garantia é um valor inteiro positivo.],
      [O sistema atualiza os dados da peça e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Referência do fornecedor inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a referência do fornecedor deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Descrição inválida",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que a descrição deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Marca inválida",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que a marca deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Preço de Venda inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o funcionário que o preço de venda deve ser um número real positivo com duas casas decimais e impede a atualização.],
        )
      ),
      (
        condicao: "Nível mínimo aceitável em stock inválido",
        passo: "11",
        fluxo: (
          [11.1 - O sistema informa o funcionário que o nível mínimo aceitável em stock deve ser um número inteiro não negativo e impede a atualização.],
        )
      ),
      (
        condicao: "Tempo de garantia inválido",
        passo: "12",
        fluxo: (
          [12.1 - O sistema informa o funcionário que o tempo de garantia deve ser um número inteiro positivo e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "12",
    nome: "Remover Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma peça registada.",
    pos_condicoes: "A peça é removida do sistema e deixa de ser possível adicionar stock da peça removida.",
    fluxo_normal: (
      [O funcionário seleciona a peça que pretende eliminar.],
      [O sistema apresenta os dados da peça.],
      [O funcionário seleciona a opção de eliminar a peça.],
      [O funcionário confirma a eliminação.],
      [O sistema impossibilita a adição de novas entradas em stock da peça removida a partir deste momento e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      ),
    )
)

#uc_spec(
    id: "13",
    nome: "Registar Entrada de Peças em Stock",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existem peças registadas no sistema.",
    pos_condicoes: "Uma nova entrada de peça em stock é registada com identificador único numérico e a quantidade em stock é aumentada.",
    fluxo_normal: (
      [O funcionário seleciona a opção de adicionar uma nova entrada.],
      [O funcionário introduz o valor da referência do fornecedor ou o identificador único da peça.],
      [O sistema valida a existência da peça.],
      [O funcionário preenche todos os dados obrigatórios: quantidade, preço de compra e data de receção.],
      [O funcionário confirma o registo.],
      [O sistema valida que a quantidade é um número inteiro positivo.],
      [O sistema valida que o preço de compra é um valor real positivo com 2 casas decimais.],
      [O sistema valida que a data de receção é uma data válida no formato AAAA-MM-DD.],
      [O sistema cria as entradas de acordo com a quantidade especificada, atribui um identificador único numérico a cada uma e apresenta os dados das novas entradas registadas.]
    ),
    fluxos_excecao: (
      (
        condicao: "Peça inexistente",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existe nenhuma peça com os dados fornecidos.],
        )
      ),
      (
        condicao: "Funcionário cancela o registo",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a quantidade deve ser um número inteiro positivo e impede o registo.],
        )
      ),
      (
        condicao: "Preço de compra inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o preço de compra deve ser um valor real positivo com 2 casas decimais e impede o registo.],
        )
      ),
      (
        condicao: "Data de receção inválida",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que a data de receção deve ser uma data válida no formato AAAA-MM-DD e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "14",
    nome: "Editar Entrada de Stock de Peças",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma entrada de peça em stock registada.",
    pos_condicoes: "Os dados da entrada de stock são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona a entrada de peça que pretende editar.],
      [O sistema apresenta os dados atuais da entrada.],
      [O funcionário seleciona a opção de editar a entrada.],
      [O funcionário seleciona o ou os campos que pretende alterar (com a exceção da quantidade) e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que o preço de compra é um valor real positivo com 2 casas decimais.],
      [O sistema valida que a data de receção é uma data válida no formato AAAA-MM-DD.],
      [O sistema atualiza os dados da entrada de stock e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Preço de compra inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que o preço de compra deve ser um valor real positivo com 2 casas decimais e impede a atualização.],
        )
      ),
      (
        condicao: "Data de receção inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que a data de receção deve ser uma data válida no formato AAAA-MM-DD e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "15",
    nome: "Registar Devolução de Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existem peças registadas no sistema.",
    pos_condicoes: "Uma nova devolução é registada no sistema com estado 'Pendente de Devolução' e um identificador único é atribuído.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova devolução.],
      [O funcionário introduz o identificador único da peça no sistema.],
      [O sistema valida a existência da peça.],
      [O funcionário preenche todos os dados obrigatórios: quantidade, data da devolução e motivo da devolução.],
      [O funcionário confirma o registo.],
      [O sistema valida que a quantidade é um número inteiro positivo, não excede a quantidade de peças marcadas como "Possível Defeito" em stock da peça em questão.],
      [O sistema valida que a data da devolução é uma data válida no formato AAAA-MM-DD.],
      [O sistema valida que o motivo da devolução é um campo textual não vazio.],
      [O sistema cria a devolução com estado automaticamente definido como 'Pendente de Devolução' e atribui um identificador único numérico.],
      [O sistema diminui a quantidade em stock da peça consoante a quantidade especificada na devolução.],
      [O sistema apresenta os dados da nova devolução.]
    ),
    fluxos_excecao: (
      (
        condicao: "Peça inexistente",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existe nenhuma peça com o identificador fornecido.],
        )
      ),
            (
        condicao: "Funcionário cancela o registo",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade inválida",
        passo: "6",
        fluxo: (
          [6.1.1 - O sistema informa o funcionário que a quantidade deve ser um número inteiro positivo e impede o registo.],
        )
      ),
      (
        condicao: "Quantidade superior à quantidade em 'Possível Defeito'",
        passo: "6",
        fluxo: (
          [6.1.2 - O sistema informa o funcionário que a quantidade não deve exceder a quantidade marcada como 'Possível Defeito' em stock, e impede o registo.],
        )
      ),
      (
        condicao: "Data da devolução inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que a data da devolução deve ser uma data válida no formato AAAA-MM-DD e impede o registo.],
        )
      ),
      (
        condicao: "Motivo da devolução inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o motivo da devolução deve ser um campo textual não vazio e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "16",
    nome: "Editar Devolução de Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma devolução registada.",
    pos_condicoes: "Os dados da devolução são atualizados no sistema com os novos valores fornecidos e as transições de estado causam os efeitos correspondentes no stock e na gestão financeira.",
    fluxo_normal: (
      [O funcionário seleciona a devolução que pretende editar.],
      [O sistema apresenta os dados atuais da devolução.],
      [O funcionário seleciona a opção de editar a devolução.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que a quantidade é um número inteiro positivo e a diferença entre a quantidade anteriormente registada e a inserida não excede a quantidade marcada como 'Possível Defeito' em stock.],
      [O sistema valida que a data da devolução é uma data válida no formato AAAA-MM-DD.],
      [O sistema valida que o motivo da devolução é um campo textual não vazio.],
      [O sistema valida que nenhuma mudança no estado foi feita.],
      [O sistema confirma as alterações.]
    ),
    fluxos_alternativos: (
        (
        condicao: "O sistema verifica que a transição de estado é de 'Pendente de Devolução' para 'Devolvida ao Fornecedor'",
        passo: "9",
        fluxo: (
          [9.1.1 - O sistema atualiza o estado das entradas de stock devolvidas para o estado ‘Devolvida ao Fornecedor’.],
          [9.1.2 - O sistema regista a entrada de peças no stock na quantidade especificada, sem qualquer custo adicional.],
          [9.1.2 - Retorna ao passo 10 do fluxo normal.]
        )
      ),
      (
        condicao: "O sistema verifica que a transição de estado é de 'Pendente de Devolução' para 'Inválida para Devolução'",
        passo: "9",
        fluxo: (
          [9.2.1 - O sistema atualiza o estado das entradas de stock devolvidas para o estado ‘Inválida para Devolução’.],
          [9.2.2 - Retorna ao passo 10 do fluxo normal.]
        )
      ),
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a quantidade deve ser um número inteiro positivo e impede a atualização.],
        )
      ),
      (
        condicao: "Quantidade superior à quantidade em 'Possível Defeito'",
        passo: "6",
        fluxo: (
          [6.2 - O sistema informa o funcionário que a quantidade não deve exceder a soma da quantidade registada anteriormente com a quantidade marcada como 'Possível Defeito' em stock, e impede a atualização.],
        )
      ),
      (
        condicao: "Data da devolução inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que a data da devolução deve ser uma data válida no formato AAAA-MM-DD e impede a atualização.],
        )
      ),
      (
        condicao: "Motivo da devolução inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o motivo da devolução deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Transição de estado inválida",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que a transição de estado solicitada não é permitida e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "17",
    nome: "Remover Devolução de Peça",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma devolução no estado 'Pendente de Devolução' registada no sistema.",
    pos_condicoes: "A devolução é removida do sistema e os gastos em peças são aumentados consoante o valor da peça e quantidade.",
    fluxo_normal: (
      [O funcionário seleciona a devolução que pretende eliminar.],
      [O sistema apresenta os dados atuais da devolução.],
      [O funcionário seleciona a opção de eliminar a devolução.],
      [O sistema verifica que o estado da devolução é 'Pendente de Devolução'.],
      [O funcionário confirma a eliminação.],
      [O sistema remove a devolução do sistema.],
      [O sistema aumenta os gastos em peças do mês atual no valor de compra da peça multiplicado pela quantidade especificada na devolução.],
      [O sistema apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Devolução não está no estado 'Pendente de Devolução'",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o funcionário que apenas devoluções no estado 'Pendente de Devolução' podem ser eliminadas e impede a eliminação.],
        )
      ),
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "6",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      )
    )
)


#uc_spec(
    id: "18",
    nome: "Registar Fornecedor",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado.",
    pos_condicoes: "Um novo fornecedor é registado no sistema com um identificador único.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar um novo fornecedor.],
      [O funcionário preenche todos os dados obrigatórios: nome e contacto (email e/ou número de telefone).],
      [O funcionário confirma o registo.],      
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o contacto é um email válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] e/ou um número de telefone composto por 9 dígitos.],
      [O sistema cria o fornecedor, atribui um identificador único numérico e apresenta os dados do novo fornecedor.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela o registo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Contacto inválido",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o funcionário que o contacto deve ser um email válido (de acordo com a conveção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322]) e/ou um número de telefone composto por 9 dígitos, e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "19",
    nome: "Editar Fornecedor",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um fornecedor registado no sistema.",
    pos_condicoes: "Os dados do fornecedor são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona o fornecedor que pretende editar.],
      [O sistema apresenta os dados atuais do fornecedor.],
      [O funcionário seleciona a opção de editar o fornecedor.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o contacto, é um email válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] e/ou um número de telefone composto por 9 dígitos.],
      [O sistema atualiza os dados do fornecedor e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Contacto inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o contacto deve ser um email válido (de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322]) e/ou um número de telefone composto por 9 dígitos, e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "20",
    nome: "Remover Fornecedor",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um fornecedor registado no sistema.",
    pos_condicoes: "O fornecedor é removido do sistema e deixa de ser possível associar peças a este fornecedor.",
    fluxo_normal: (
        [O funcionário seleciona o fornecedor que pretende eliminar.],
        [O sistema apresenta os dados do fornecedor.],
        [O funcionário seleciona a opção de eliminar o fornecedor.],
        [O funcionário confirma a eliminação.],
        [O sistema impossibilita a associação de peças a este fornecedor e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo],
        )
      ),
    )
)

#uc_spec(
    id: "21",
    nome: "Gerar Lista de Encomendas",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existem peças com quantidade em stock inferior ao nível mínimo aceitável.",
    pos_condicoes: "Uma nova lista de encomendas é criada no sistema no estado 'Rascunho'.",
    fluxo_normal: (
      [O funcionário seleciona a opção de gerar uma nova lista.],
      [O sistema analisa automaticamente o stock de todas as peças.],
      [O sistema identifica as peças cuja quantidade em stock é inferior ao nível mínimo aceitável.],
      [O sistema calcula a quantidade a encomendar para cada peça.],
      [O sistema apresenta as peças identificadas, agrupadas por fornecedor.],
      [O funcionário seleciona fornecedor a quem vai fazer encomenda.],
      [O funcionário seleciona quais as peças que deseja incluir na lista de encomenda.],
      [O funcionário confirma as peças que deseja incluir na lista.],
      [O funcionário insere o preço de compra para cada uma das peças.],
      [O funcionário confirma a criação da lista.],
      [O sistema valida que os preços de compra são valores reais positivos com 2 casas decimais.],
      [O sistema cria a lista, atribui-lhe um identificador único, define o estado como 'Rascunho', regista a data e hora da criação e apresenta os dados da nova lista, incluindo dados das peças, quantidades, preços de compra e venda e fornecedores.],
    ),
    fluxos_excecao: (
      (
        condicao: "Nenhuma peça com stock inferior ao nível mínimo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existem peças que necessitem de encomenda no momento.],
        )
      ),
      (
        condicao: "Funcionário não seleciona nenhuma peça",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que nenhuma peça foi selecionada e impede a criação da lista.],
        )
      ),
      (
        condicao: "Funcionário cancela a criação",
        passo: "9",
        fluxo: (
          [9.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Preço de compra inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o funcionário que o preço de compra deve ser um valor real positivo com 2 casas decimais e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "22",
    nome: "Editar Lista de Encomendas",
    atores: "Gestor de Stock",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma lista de encomendas registada no sistema.",
    pos_condicoes: "Os dados da lista são atualizados com as peças adicionadas ou removidas, ou o estado da lista é alterado para o novo estado selecionado.",
    fluxo_normal: (
      [O funcionário seleciona a lista que pretende editar.],
      [O sistema apresenta os dados atuais da lista.],
      [O funcionário seleciona a opção de editar a lista.],
      [O sistema valida que a lista se encontra no estado 'Rascunho'.],
      [O funcionário adiciona ou remove peças da lista.],
      [O funcionário confirma as alterações.],
      [O sistema valida que nenhuma mudança no estado foi feita.],
      [O sistema atualiza os dados da lista e apresenta os dados atualizados.]
    ),
    fluxos_alternativos: (
      (
        condicao: "O funcionário alterou o estado da lista de 'Rascunho' para 'Enviada'",
        passo: "7",
        fluxo: (
          [7.1.1 - O sistema regista a data e hora do envio.],
          [7.1.2 - Retorna ao passo 8 do fluxo normal.],
        )
      ),
      (
        condicao: "O funcionário alterou o estado da lista de 'Enviada' para 'Recebida'",
        passo: "7",
        fluxo: (
          [7.2.1 - O sistema regista a data e hora da receção.],
          [7.2.3 - O sistema adiciona automaticamente ao stock as peças registadas na lista.],
          [7.2.4 - Retorna ao passo 8 do fluxo normal.],
        )
      ),
    ),
    fluxos_excecao: (
      (
        condicao: "Lista não se encontra no estado 'Rascunho' para edição de peças",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que apenas listas no estado 'Rascunho' podem ter as suas peças editadas.],
        )
      ),
      (
        condicao: "Transição de estado inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que a transição de estado solicitada não é permitida e impede a alteração.],
        )
      ),
      (
        condicao: "Funcionário cancela a edição",
        passo: "6",
        fluxo: (
          [6.1 - O sistema cancela o processo.],
        )
      ),
    )
)

#uc_spec(
    id: "23",
    nome: "Eliminar Lista de Encomendas",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma lista em estado 'Rascunho'.",
    pos_condicoes: "A lista de encomendas é removida do sistema.",
    fluxo_normal: (
      [O funcionário seleciona a lista que pretende eliminar.],
      [O sistema apresenta os dados da lista.],
      [O funcionário seleciona a opção de eliminar a lista.],
      [O funcionário confirma a eliminação.],
      [O sistema remove a lista e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Lista não se encontra no estado 'Rascunho'",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que apenas listas em estado 'Rascunho' podem ser eliminadas.],
        )
      ),
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      )
    )
)

#uc_spec(
    id: "24",
    nome: "Registar Cliente",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado.",
    pos_condicoes: "Um novo cliente é registado no sistema com identificador único numérico.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar um novo cliente.],
      [O funcionário preenche todos os dados obrigatórios: nome, email, telemóvel e NIF.],
      [O funcionário confirma o registo.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o email segue o formato válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322].],
      [O sistema valida que o telemóvel é um número composto por 9 dígitos.],
      [O sistema valida que o NIF é um número composto por 9 dígitos.],
      [O sistema cria o cliente, atribui um identificador único e apresenta os dados do novo cliente.],
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela o registo",
        passo: "3",
        fluxo: (
          [3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Email inválido",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o funcionário que o email deve seguir o formato de endereço de correio electrónico válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] (nome#"@"dominio.com) e impede o registo.],
        )
      ),
      (
        condicao: "Telemóvel inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que o telemóvel deve ser um número composto por 9 dígitos e impede o registo.],
        )
      ),
      (
        condicao: "NIF inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o NIF deve ser um número composto por 9 dígitos e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "25",
    nome: "Editar Cliente",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um cliente registado no sistema.",
    pos_condicoes: "Os dados do cliente são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona o cliente que pretende editar.],
      [O sistema apresenta os dados atuais do cliente, incluindo as trotinetes a ele associadas e todas as ordens de serviço associadas a cada uma delas.],
      [O funcionário seleciona a opção de editar o cliente.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que o nome é um campo textual não vazio.],
      [O sistema valida que o email segue o formato válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322].],
      [O sistema valida que o telemóvel é um número composto por 9 dígitos.],
      [O sistema valida que o NIF é um número composto por 9 dígitos.],
      [O sistema atualiza os dados do cliente e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Nome inválido",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que o nome deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Email inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o email deve seguir o formato de endereço de correio electrónico válido de acordo com a convenção #link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] (nome#"@"dominio.com) e impede a atualização.],
        )
      ),
      (
        condicao: "Telemóvel inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o telemóvel deve ser um número composto por 9 dígitos e impede a atualização.],
        )
      ),
      (
        condicao: "NIF inválido",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que o NIF deve ser um número composto por 9 dígitos e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "26",
    nome: "Remover Cliente",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um cliente registado no sistema.",
    pos_condicoes: "O cliente é removido do sistema, bem como todas as suas trotinetes associadas.",
    fluxo_normal: (
      [O funcionário seleciona o cliente que pretende eliminar.],
      [O sistema apresenta os dados do cliente, incluindo as trotinetes a ele associadas e todas as ordens de serviço associadas a cada uma delas.],
      [O funcionário seleciona a opção de eliminar o cliente.],
      [O funcionário confirma a eliminação.],
      [A partir deste momento, o sistema impossibilita a associação de novas OS e trotinetes a este cliente, e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      ),
    )
)

#uc_spec(
    id: "27",
    nome: "Registar Trotinete",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um cliente registado no sistema.",
    pos_condicoes: "Uma nova trotinete é registada no sistema com identificador único numérico.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova trotinete.],
      [O funcionário introduz o identificador único, o nome ou o NIF do cliente.],
      [O sistema valida a existência do cliente.],
      [O funcionário seleciona o cliente.],
      [O funcionário preenche todos os dados obrigatórios: marca, modelo, número de série e tipo de motor.],
      [O funcionário confirma o registo.],
      [O sistema valida que a marca é um campo textual não vazio.],
      [O sistema valida que o modelo é um campo textual não vazio.],
      [O sistema valida que o número de série é um campo textual não vazio.],
      [O sistema valida que o tipo de motor é um tipo de motor válido.],
      [O sistema cria a trotinete, atribui um identificador único e apresenta os dados da nova trotinete.]
    ),
    fluxos_excecao: (
      (
        condicao: "Cliente inexistente",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existe nenhum cliente com os dados fornecidos.],
        )
      ),
      (
        condicao: "Funcionário cancela o registo",
        passo: "6",
        fluxo: (
          [6.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Marca inválida",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que a marca deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Modelo inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o modelo deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Número de série inválido",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que o número de série deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Tipo de motor inválido",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o funcionário que o tipo de motor deve ser um tipo de motor válido.],
        )
      ),
    )
)

#uc_spec(
    id: "28",
    nome: "Editar Trotinete",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma trotinete registada no sistema.",
    pos_condicoes: "Os dados da trotinete são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona a trotinete que pretende editar.],
      [O sistema apresenta os dados atuais da trotinete, bem como todas as ordens de serviço associadas a ela.],
      [O funcionário seleciona a opção de editar a trotinete.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores (com a exceção do cliente).],
      [O funcionário confirma as alterações.],
      [O sistema valida que a marca é um campo textual não vazio.],
      [O sistema valida que o modelo é um campo textual não vazio.],
      [O sistema valida que o número de série é um campo textual não vazio.],
      [O sistema valida que o tipo de motor é um tipo de motor válido.],
      [O sistema atualiza os dados da trotinete e apresenta os dados atualizados.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a edição",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Marca inválida",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a marca deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Modelo inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o modelo deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Número de série inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o número de série deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
      (
        condicao: "Tipo de motor inválido",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que o tipo de motor deve ser um tipo de motor válido.],
        )
      ),
    )
)

#uc_spec(
    id: "29",
    nome: "Remover Trotinete",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma trotinete registada no sistema.",
    pos_condicoes: "A trotinete é removida do sistema e não é possível associar novas ordens de serviço a esta trotinete.",
    fluxo_normal: (
      [O funcionário seleciona a trotinete que pretende eliminar.],
      [O sistema apresenta os dados da trotinete, bem como todas as ordens de serviço associadas a ela.],
      [O funcionário seleciona a opção de eliminar a trotinete.],
      [O funcionário confirma a eliminação.],
      [O sistema remove a trotinete, impossibilita a associação de novas ordens de serviço a esta trotinete, e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo],
        )
      ),
    )
)

#uc_spec(
    id: "30",
    nome: "Registar Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos um cliente registado no sistema.",
    pos_condicoes: "Uma nova ordem de serviço é registada no sistema com identificador único numérico, estado 'Pendente Diagnóstico' e a data e hora de criação são registadas.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova ordem de serviço.],
      [O funcionário introduz o identificador único, nome ou NIF do cliente.],
      [O sistema valida a existência do cliente.],
      [O funcionário seleciona o cliente.],
      [O funcionário seleciona a trotinete associada ao cliente.],
      [O funcionário preenche a descrição do problema.],
      [O funcionário não regista acessórios.],
      [O funcionário confirma o registo.],
      [O sistema valida que a descrição do problema é um campo textual não vazio.],
      [O sistema cria a ordem de serviço, atribui um identificador único numérico, define o estado como 'Pendente Diagnóstico', regista a data e hora de criação e apresenta os dados da nova ordem de serviço.]
    ),
    fluxos_alternativos: (
      (
        condicao: "O cliente não tem trotinetes registadas",
        passo: "5",
        fluxo: (
          [5.1 - O funcionário seleciona a opção de registar uma nova trotinete e conclui o seu registo.],
          [5.2 - Retorna ao passo 6 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário pretende registar acessórios",
        passo: "7",
        fluxo: (
          [7.1 - O funcionário regista um ou mais acessórios como campos textuais não vazios.],
          [7.2 - O sistema valida que todos os acessórios são campos textuais não vazios],
          [7.3 - Retorna ao passo 9 do fluxo normal.]
        )
      ),
    ),
    fluxos_excecao: (
      (
        condicao: "Cliente inexistente",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que não existe nenhum cliente com os dados fornecidos.],
        )
      ),
      (
        condicao: "Descrição do problema inválida",
        passo: "10",
        fluxo: (
          [10.1 - O sistema informa o funcionário que a descrição do problema deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Acessório inválido",
        passo: "7.2",
        fluxo: (
          [7.2.1 - O sistema informa o funcionário que os acessórios devem ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Funcionário cancela o registo",
        passo: "8",
        fluxo: (
          [8.1 - O sistema cancela o processo.],
        )
      )
    )
)

#uc_spec(
    id: "31",
    nome: "Editar Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma ordem de serviço registada no sistema.",
    pos_condicoes: "Os dados da ordem de serviço são atualizados no sistema com os novos valores fornecidos.",
    fluxo_normal: (
      [O funcionário seleciona a ordem de serviço que pretende editar.],
      [O sistema apresenta os dados atuais da ordem de serviço.],
      [O funcionário seleciona a opção de editar a ordem de serviço.],
      [O sistema verifica que o estado da OS não requer um fluxo de edição especial.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário não altera acessórios.],
      [O funcionário confirma as alterações.],
      [O sistema valida que a descrição do problema é um campo textual não vazio.],
      [O sistema atualiza os dados da ordem de serviço e apresenta os dados atualizados.]
    ),
    fluxos_alternativos: (
      (
        condicao: "O funcionário pretende adicionar ou editar acessórios",
        passo: "6",
        fluxo: (
          [6.1 - O funcionário adiciona ou edita um ou mais acessórios como campos textuais não vazios.],
          [6.2 - O sistema valida que todos os acessórios são campos textuais não vazios.],
          [6.3 - Retorna ao passo 7 do fluxo normal.]
        )
      ),
      (
        condicao: "OS no estado 'Pendente aprovação do orçamento'",
        passo: "4",
        fluxo: (
          [4.1 - O funcionário não regista a aprovação do orçamento.],
          [4.2 - Retorna ao passo 8 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário regista a aprovação do orçamento",
        passo: "4.1",
        fluxo: (
          [4.1.1 - O funcionário regista a aprovação do orçamento.],
          [4.1.2 - O funcionário confirma a aprovação do orçamento.],
          [4.1.3 - O sistema altera o estado da OS para "Pendente Reparação".],
          [4.1.4 - Retorna ao passo 10 do fluxo normal.]
        )
      ),
      (
        condicao: "OS no estado 'Pendente Pagamento'",
        passo: "4",
        fluxo: (
          [4.2.1 - O funcionário não regista a notificação de término.],
          [4.2.2 - O funcionário não associa peças à OS.],
          [4.2.3 - O funcionário não regista o pagamento.],
          [4.2.4 - Retorna ao passo 8 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário regista a notificação de término",
        passo: "4.2.1",
        fluxo: (
          [4.2.1.1 - O funcionário regista a notificação de término.],
          [4.2.1.2 - O funcionário confirma a notificação de término.],
          [4.2.1.3 - O sistema regista a data e hora da notificação.],
          [4.2.1.4 - Retorna ao passo 4.2.2 dos fluxos alternativos.]
        )
      ),
      (
        condicao: "O funcionário associa ou remove peças da OS",
        passo: "4.2.2",
        fluxo: (
          [4.2.2.1 - O funcionário introduz o identificador único ou a referência do fornecedor da peça que pretende associar.],
          [4.2.2.2 - O sistema valida a existência da peça em stock.],
          [4.2.2.3 - O funcionário seleciona a peça e especifica a quantidade utilizada.],
          [4.2.2.4 - O funcionário confirma a associação.],
          [4.2.2.5 - O sistema valida que a quantidade é um número inteiro positivo e não excede a quantidade em stock.],
          [4.2.2.6 - O sistema associa as peças à OS e atualiza o valor total da OS.],
          [4.2.2.7 - Retorna ao passo 4.2.3 dos fluxos alternativos.]
        )
      ),
      (
        condicao: "O funcionário regista o pagamento",
        passo: "4.2.3",
        fluxo: (
          [4.2.3.1 - O funcionário seleciona o método de pagamento: "Numerário", "Multibanco" ou "MBWay".],
          [4.2.3.2 - O funcionário confirma o registo do pagamento.],
          [4.2.3.3 - O sistema regista o pagamento e altera o estado da OS para "Paga".],
          [4.2.3.4 - Retorna ao passo 10 do fluxo normal.]
        )
      )
    ),
    fluxos_excecao: (
      (
        condicao: "O funcionário não confirma a aprovação do orçamento",
        passo: "4.1.2",
        fluxo: (
          [4.1.2.1 - O sistema altera o estado da ordem de serviço para 'Orçamento não aprovado' e impossibilita o uso dos dados da OS para estatísticas da empresa.],
          [4.1.2.2 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário não confirma a notificação de término",
        passo: "4.2.1.2",
        fluxo: (
          [4.2.1.2.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Peça inexistente em stock",
        passo: "4.2.2.2",
        fluxo: (
          [4.2.2.2.1 - O sistema informa que não existe nenhuma peça com os dados fornecidos e cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário não confirma a associação",
        passo: "4.2.2.4",
        fluxo: (
          [4.2.2.4.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade de peça inválida",
        passo: "4.2.2.5",
        fluxo: (
          [4.2.2.5.1 - O sistema informa o funcionário que a quantidade deve ser um número inteiro positivo e não pode exceder a quantidade em stock e impede a associação.],
        )
      ),
      (
        condicao: "Notificação de término não registada",
        passo: "4.2.3.1",
        fluxo: (
          [4.2.3.1.1 - O sistema informa que é necessário registar a notificação de término antes de processar o pagamento.],
        )
      ),
      (
        condicao: "Método de pagamento não selecionado",
        passo: "4.2.3.3",
        fluxo: (
          [4.2.3.3.1 - O sistema informa que é necessário selecionar um método de pagamento e impede o registo.],
        )
      ),
      (
        condicao: "O funcionário não confirma o registo do pagamento",
        passo: "4.2.3.4",
        fluxo: (
          [4.2.3.4.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Acessório inválido",
        passo: "6.2",
        fluxo: (
          [6.2.1 - O sistema informa o funcionário que os acessórios devem ser campos textuais não vazios e impede a atualização.],
        )
      ),
      (
        condicao: "Funcionário cancela a edição",
        passo: "7",
        fluxo: (
          [7.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Descrição do problema inválida",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que a descrição do problema deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "32",
    nome: "Remover Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma ordem de serviço registada no sistema.",
    pos_condicoes: "A ordem de serviço muda para estado 'Eliminada' e não é possível usar os seus dados para estatísticas da empresa.",
    fluxo_normal: (
      [O funcionário seleciona a ordem de serviço que pretende eliminar.],
      [O sistema apresenta os dados da ordem de serviço.],
      [O funcionário seleciona a opção de eliminar a ordem de serviço.],
      [O funcionário confirma a eliminação.],
      [O sistema altera o estado da ordem de serviço para 'Eliminada', impossibilita o uso dos dados da OS para estatísticas da empresa e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a eliminação",
        passo: "4",
        fluxo: (
          [4.1 - O sistema cancela o processo.],
        )
      ),
    )
)

#uc_spec(
    id: "33",
    nome: "Consultar Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma ordem de serviço registada no sistema.",
    pos_condicoes: "Uma lista de ordens de serviço filtradas é apresentada ao funcionário.",
    fluxo_normal: (
      [O funcionário seleciona um ou vários filtros: estado, intervalo de datas, cliente e/ou funcionário.],
      [O funcionário confirma o conjunto de filtros que pretende usar.],
      [O sistema valida que o estado selecionado é um dos estados válidos: 'Pendente Diagnóstico', 'Pendente Reparação', 'A aguardar peças', 'Pendente aprovação do orçamento', 'Pendente Pagamento', 'Paga' ou 'Eliminada'.],
      [O sistema valida que o intervalo de datas segue o formato #link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601] (AAAA-MM-DD).],
      [O sistema valida que o cliente existe no sistema.],
      [O sistema valida que o funcionário existe no sistema.],
      [O sistema procura por ordens de serviço que correspondam aos filtros usados.],
      [O sistema apresenta todas as ordens de serviço filtradas, incluindo: identificador, cliente, trotinete, estado, data de criação, funcionário responsável, valor total do orçamento e se aplicável, data da última alteração.]
    ),
    fluxos_excecao: (
      (
        condicao: "Funcionário cancela a pesquisa",
        passo: "2",
        fluxo: (
          [2.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Estado inválido",
        passo: "3",
        fluxo: (
          [3.1 - O sistema informa o funcionário que o estado selecionado é inválido e impede a pesquisa.],
        )
      ),
      (
        condicao: "Intervalo de datas inválido",
        passo: "4",
        fluxo: (
          [4.1 - O sistema informa o funcionário que o intervalo de datas deve seguir o formato #link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601] (AAAA-MM-DD) e impede a pesquisa.],
        )
      ),
      (
        condicao: "Cliente inexistente",
        passo: "5",
        fluxo: (
          [5.1 - O sistema informa o funcionário que não existe nenhum cliente com os dados fornecidos e impede a pesquisa.],
        )
      ),
      (
        condicao: "Mecânico inexistente",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que não existe nenhum funcionário com os dados fornecidos e impede a pesquisa.],
        )
      ),
      (
        condicao: "Nenhuma ordem de serviço corresponde aos filtros",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que não existem ordens de serviço que correspondam aos filtros selecionados.],
        )
      ),
    )
)



// ─────────────────────────────────────────────────────────────────────────────
//  MECÂNICO
// ─────────────────────────────────────────────────────────────────────────────

#uc_spec(
    id: "34",
    nome: "Executar Diagnóstico de Ordem de Serviço",
    atores: "Mecânico e Gerente",
    pre_condicoes: "O funcionário selecionou uma OS no estado 'Pendente Diagnóstico' através do UC-37.",
    pos_condicoes: "O diagnóstico é registado com o funcionário que o realizou. A OS transita para o estado 'Pendente aprovação do orçamento'.",
    fluxo_normal: (
      [O funcionário seleciona uma das OS que estão no estado "Pendente Diagnóstico".],
      [O funcionário consulta os dados completos da OS, incluindo os dados da trotinete, do cliente e o histórico de reparações anteriores da trotinete.],
      [O funcionário adiciona e remove as reparações necessárias da tabela de reparações, pesquisando por identificador ou nomenclatura.],
      [O funcionário pesquisa, adiciona e remove peças por referência do fornecedor ou identificador único, especificando as quantidades.],
      [O funcionário confirma o diagnóstico.],
      [O sistema valida que as quantidades são números inteiros positivos e não excedem o stock disponível.],
      [O sistema calcula o valor do orçamento e altera o estado da OS para "Pendente aprovação do orçamento", regista o funcionário que realizou o diagnóstico e gera um alerta com o identificador da OS e o seu estado para a secretária],
    ),
    fluxos_excecao: (
      (
        condicao: "O funcionário cancela o diagnóstico",
        passo: "5",
        fluxo: (
          [5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade inválida ou superior ao stock disponível",
        passo: "6",
        fluxo: (
          [6.1 - O sistema informa o funcionário que a quantidade da peça deve ser um valor inteiro positivo ou excede o stock disponível e impede a adição.],
        )
      ),
    ),
)

#uc_spec(
    id: "35",
    nome: "Executar Conserto de Ordem de Serviço",
    atores: "Mecânico e Gerente",
    pre_condicoes: "O funcionário selecionou uma OS no estado 'Pendente Reparação' através do UC-37.",
    pos_condicoes: "As reparações executadas e peças utilizadas são registadas, as quantidades de peças são descontadas do stock, o custo final é calculado, o funcionário que realizou a reparação é registado e a OS transita para o estado 'Pendente Pagamento' com alerta gerado para a secretária.",
    fluxo_normal: (
      [O funcionário seleciona uma das OS que estão no estado "Pendente Reparação".],
      [O sistema valida que todas as peças prescritas no diagnóstico estão disponíveis em stock.],
      [O funcionário consulta os dados completos da OS, incluindo o histórico de reparações anteriores da trotinete e as reparações e peças prescritas no diagnóstico.],
      [O funcionário marca as reparações prescritas que executou como concluídas e as peças prescritas (e disponíveis em stock) que precisou como utilizadas.],
      [O sistema desconta do stock as quantidades das peças marcadas como utilizadas.],
      [O funcionário não pretende adicionar reparações adicionais.],
      [O funcionário não pretende adicionar peças adicionais.],
      [O funcionário não reporta defeitos em peças instaladas.],
      [O funcionário seleciona a opção de concluir a reparação.],
      [O funcionário verifica todos os itens obrigatórios da checklist de segurança: Luzes, Pneus, Aceleração, Travagem, Visor e Teste de Condução.],
      [O funcionário confirma a conclusão da checklist.],
      [O funcionário confirma a conclusão da reparação.],
      [O sistema valida que todos os itens da checklist estão marcados como verificados.],
      [O sistema calcula o custo final da reparação, altera o estado da OS para "Pendente Pagamento", regista o funcionário que realizou a reparação, gera um alerta com o identificador da OS e o seu estado para a secretária e apresenta um resumo final da OS.]
    ),
    fluxos_alternativos: (
      (
        condicao: "Uma peça prescrita no diagnóstico não está disponível em stock",
        passo: "2",
        fluxo: (
          [2.1.1 - O sistema informa o funcionário que a quantidade solicitada da peça prescrita não está disponível em stock.],
          [2.1.2 - O funcionário seleciona a opção de requisitar encomenda da peça indisponível.],
          [2.1.3 - O funcionário confirma a requisição.],
          [2.1.4 - O sistema altera o estado da OS para "A aguardar peças" e gera um alerta com o identificador e referência da peça para o do gestor de stock, gera um alerta com o identificador da OS e o seu estado para a secretária.],
          [2.1.5 - Retorna ao passo 3 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário pretende adicionar reparações adicionais",
        passo: "6",
        fluxo: (
          [6.1 - O funcionário adiciona e remove reparações adicionais da tabela de reparações, pesquisando por identificador ou nomenclatura.],
          [6.2 - O funcionário confirma as reparações adicionais.],
          [6.3 - O sistema coloca a OS no estado \"Pendente de aprovação do orçamento\" e gera um alerta novo para a secretária que contém o identificador da OS e o estado em que ela se encontra.],
          [6.4 - Retorna ao passo 7 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário pretende adicionar peças adicionais",
        passo: "7",
        fluxo: (
          [7.1 - O funcionário pesquisa, adiciona e remove peças por referência do fornecedor ou identificador único, especificando as quantidades.],
          [7.2 - O funcionário confirma as peças adicionais.],
          [7.3 - O sistema valida que as quantidades são números inteiros positivos e não excedem o stock disponível.],
          [7.4 - O sistema coloca a OS no estado \"Pendente de aprovação do orçamento\" e gera um alerta novo para a secretária que contém o identificador da OS e o estado em que ela se encontra.],
          [7.5 - Retorna ao passo 8 do fluxo normal.]
        )
      ),
      (
        condicao: "O funcionário pretende reportar um defeito numa peça instalada",
        passo: "8",
        fluxo: (
          [8.1 - O funcionário seleciona a peça defeituosa da lista de peças utilizadas.],
          [8.2 - O funcionário indica que deseja reportar um defeito na peça.],
          [8.3 - O funcionário fornece uma descrição do defeito encontrado.],
          [8.4 - O funcionário confirma o reporte do defeito.],
          [8.5 - O sistema valida que existe uma peça igual disponível em stock e adiciona-a à lista de peças usadas e remove a peça defeituosa da lista de peças utilizadas.],
          [8.6 - O sistema marca a entrada de stock onde o defeito foi detetado como "Possível Defeito", preservando o seu estado anterior para eventual restauração, e propaga automaticamente este estado a todas as outras entradas de stock da mesma peça atualmente em armazém, preservando também os respetivos estados anteriores.],
          [8.7 - O sistema cria um registo de devolução para a entrada onde o defeito foi detetado, restitui ao stock a quantidade correspondente da peça substituta e gera um alerta com o identificador, referência da peça e descrição do defeito para o gestor de stock.],
          [8.8 - Retorna ao passo 9 do fluxo normal.]
        )
      ),),
    fluxos_excecao: (
      (
        condicao: "O funcionário cancela a requisição da peça",
        passo: "2.1.3",
        fluxo: (
          [2.1.3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário cancela a adição de reparações",
        passo: "6.2",
        fluxo: (
          [6.2.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário cancela a adição das novas peças",
        passo: "7.2",
        fluxo: (
          [7.2.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Quantidade inválida ou superior ao stock disponível",
        passo: "7.3",
        fluxo: (
          [7.3.1 - O sistema informa o funcionário que a quantidade é inválida ou excede o stock disponível e impede a adição.],
        )
      ),
      (
        condicao: "O funcionário cancela o reporte do defeito",
        passo: "8.4",
        fluxo: (
          [8.4.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Não existe uma peça disponível em stock",
        passo: "8.5",
        fluxo: (
          [8.5.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário cancela a conclusão da reparação",
        passo: "12",
        fluxo: (
          [12.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Existem itens da checklist por verificar",
        passo: "13",
        fluxo: (
          [13.1 - O sistema informa o funcionário que existem itens da checklist por verificar.],
        )
      ),
    )
)

#attachment(caption: [Diagramas UML], none) <diagramas_uml>

#figure(
  image("images/modelo_dominio_simples.png", width: 130%),
  caption: [Modelo de Domínio Simplificado]
) <modelo_dominio_simples>

#figure(
  image("images/modelo_dominio_completo.jpg", width: 130%),
  caption: [Modelo de Domínio Completo]
) <modelo_dominio_completo>


#figure(
  image("Anexo10/SAutenticacaoDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SAutenticação Final ]
) <Diagrama_de_classe_do_SAutenticação_Final>

#figure(
  image("Anexo10/SClientesDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SClientes Final ]
) <Diagrama_de_classe_do_SClientes_Final>

#figure(
  image("Anexo10/SFinanceiroDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SFinanceiro Final ]
) <Diagrama_de_classe_do_SFinanceiro_Final>

#figure(
  image("Anexo10/SFuncionariosDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SFuncionarios Final ]
) <Diagrama_de_classe_do_SFuncionarios_Final>

#figure(
  image("Anexo10/SNotificacoesDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SNotificacoes Final ]
) <Diagrama_de_classe_do_SNotificacoes_Final>

#figure(
  image("Anexo10/SOrdensServicoDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SOrdensServico Final ]
) <Diagrama_de_classe_do_SOrdensServico_Final>

#figure(
  image("Anexo10/SReparacoesDAO  Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SReparacoes Final ]
) <Diagrama_de_classe_do_SReparacoes_Final>

#figure(
  image("Anexo10/SStockDAO Implementação Final Anexo10.png", width: 130%),
  caption: [Diagrama de classe do SStock Final ]
) <Diagrama_de_classe_do_SStock_Final>

#figure(
  image("Anexo10/EcoRideCD Anexo10.png", width: 130%),
  caption: [Arquitetura final da Camada de Dados]
) <Arquitetura_final_da_camada_de_dados>

