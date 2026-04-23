
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
  
  [RF03], [O sistema deve permitir registar marca, modelo, número de série, estado à entrada (notas textuais ou foto), tipo de motor e acessórios de uma trotinete.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF04], [O sistema deve permitir registar dados pessoais dos funcionários: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS e IBAN.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF05], [O sistema deve permitir registar salário base (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto), alterações salariais, horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF06], [O sistema deve permitir registar a entrada de uma trotinete com dados do cliente, dados técnicos da trotinete e descrição do problema numa ordem de serviço nova. Deverá também conseguir calcular um tempo estimado para o término da reparação, para informar o cliente.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF07], [O sistema deverá facilitar a obtenção de novas ordens de serviço para que os mecânicos realizem diagnósticos ou reparações.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF08], [O sistema deve atribuir automaticamente o estado "Por diagnosticar" a novas ordens de serviço.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF09], [O sistema deve bloquear o início da reparação até existir aprovação formal do orçamento por parte do cliente registada no sistema (feita pela secretária).], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF10], [O sistema deve impedir que duas pessoas iniciem a mesma ordem de serviço simultaneamente, atribuindo-a a um mecânico de cada vez.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF11], [O sistema deve registar automaticamente qual mecânico executou cada reparação.], [26/2/26], [Entrevista ID \#001], [Luís Soares],

  [RF12], [O sistema deverá ter uma lista de possíveis intervenções para uma ordem de serviço com orçamento aprovado, facilitando a explicação das intervenções por parte dos mecânicos.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF13], [O sistema deve permitir registar entradas de peças com quantidade, fornecedor, preço de compra e venda, data e número de série (que deverá ser obrigatório se o preço de compra for superior a 70€).], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF14], [O sistema deve permitir registar automaticamente a saída de peças quando associadas a uma ordem de serviço.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF15], [O sistema deve permitir devolver peças não utilizadas ao stock e remover a associação à ordem de serviço.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF16], [O sistema deve permitir registar referências de fornecedores e associar cada peça ao fornecedor correto.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF17], [O sistema deve armazenar contactos, emails, preços habituais e prazos de entrega de fornecedores.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF18], [O sistema deve gerar listas de peças a encomendar com base em níveis mínimos, consumo e stock atual.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF19], [O sistema deve permitir registar peças devolvidas ao fornecedor, com data, motivo e estado da devolução.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF20], [O sistema deve impedir a entrega da trotinete se existirem pagamentos pendentes desse cliente.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF21], [O sistema deverá registar quando um cliente foi notificado sobre o término da reparação da sua trotinete.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF22], [O sistema deve registar quantos dias passaram desde que o cliente foi notificado e aplicar automaticamente uma taxa diária de 3€ após 7 dias (excluindo o dia de levantamento).], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF23], [O sistema deverá calcular o valor de um serviço baseando-se na lista de intervenções anotada pelos mecânicos e somando esses valores com os valores de venda das peças usadas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF24], [O sistema deve implementar perfis distintos (gerente, secretária, mecânico) com permissões específicas.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF25], [Todas as ações (entradas/saídas de stock, alterações de preços, aprovações) devem ser registadas com data, hora e utilizador.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF26], [O sistema deverá calcular os valores dos custos e lucros da oficina e mostrar essa informação na interface do gerente. Deverá ser possível acompanhar os salários, compras de peças, rendas e outras despesas e filtrar por intervalos de tempo.], [26/2/26], [Entrevista ID \#001], [Luís Soares],
  
  [RF27], [O sistema deve permitir criar ordens de serviço contendo: nome do cliente, contacto (email ou telefone), NIF (opcional), marca, modelo, número de série (quando disponível) e descrição do problema. Todos os campos devem ser estruturados e validados.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF28], [O sistema deve permitir consultar rapidamente o histórico de reparações de um cliente ou de uma trotinete, incluindo serviços anteriores, peças substituídas e problemas recorrentes.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF29], [O sistema deve permitir atualizar um orçamento já criado, mantendo histórico de alterações, motivo da alteração e valor anterior. Não deve ser criada uma nova ordem de serviço.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF30], [O sistema deve permitir registar a aprovação do cliente (via telefone, email ou outro meio), associando-a à ordem de serviço antes de permitir que o mecânico continue a reparação.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF31], [O sistema deve disponibilizar aos mecânicos uma lista digital das ordens pendentes, ordenadas por ordem de chegada.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF32], [O sistema deve permitir ao mecânico registar digitalmente o trabalho realizado e as peças utilizadas.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF33], [O sistema deve calcular automaticamente o valor final da reparação com base nas reparações feitas, peças utilizadas e preços definidos pelo gerente.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF34], [O sistema deve permitir contactar o cliente diretamente (telefone, email ou outro canal) para informar que a trotinete está pronta para levantamento.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF35], [O sistema deve permitir registar o método de pagamento utilizado (numerário, multibanco, MBWay), para controlo financeiro e reconciliação diária.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF36], [O sistema deve impedir a conclusão da entrega da trotinete caso existam pagamentos pendentes, exibindo alerta claro à secretária.], [26/2/26], [Entrevista ID \#002], [Eduardo Fernandes],
  
  [RF37], [O sistema deve permitir ao mecânico registar digitalmente o diagnóstico inicial, incluindo descrição livre do problema e seleção de avarias comuns pré‑definidas para acelerar o processo.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF38], [O sistema deve permitir ao mecânico indicar as peças necessárias para a reparação diretamente na ordem de serviço, antes do levantamento no armazém.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF39], [O sistema deve permitir que a receção associe peças a uma ordem de serviço caso o mecânico não o tenha feito no momento do levantamento, mantendo histórico de quem registou a peça e quando.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF40], [O sistema deve permitir ao mecânico reportar peças defeituosas instaladas, gerando automaticamente um alerta para o gerente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF41], [O sistema deve permitir ao mecânico adicionar novos problemas identificados durante a reparação, com descrição e impacto no orçamento, gerando automaticamente um alerta para a receção contactar o cliente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF42], [O sistema deve permitir ao mecânico registar os arranjos realizados na trotinete a partir de uma tabela de operações previamente definida.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF43], [O sistema deve permitir ao mecânico marcar a ordem de serviço como concluída, notificando automaticamente a receção para proceder ao contacto com o cliente.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF44], [O sistema deve incluir uma checklist obrigatória de verificações de segurança (travões, luzes, pneus, aceleração, travagem, visor, teste de condução), que o mecânico deve validar antes de concluir a ordem.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF45], [O sistema deve disponibilizar ao mecânico uma lista digital das ordens pendentes, ordenadas por prioridade ou ordem de chegada.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF46], [O sistema deve permitir ao mecânico consultar rapidamente o histórico de reparações e peças substituídas de uma trotinete, para apoiar diagnósticos e evitar trabalho dobrado.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF47], [Caso uma peça não esteja disponível para ser usada por um mecânico numa reparação, ele deverá submeter uma requisição de encomenda da peça para que o gerente possa encomendá-la.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],

  [RF48], [Quando uma peça que um mecânico pediu for adicionada ao stock, deverá ser automaticamente retirada do stock e adicionada à ordem de serviço que o mecânico estava a tratar. Para além disso, deverá ser obrigatoriamente a próxima ordem de serviço que o mecânico irá reparar.], [26/2/26], [Entrevista ID \#003], [Eduardo Fernandes],
  
  [RF49], [O sistema precisa de uma funcionalidade rápida na abertura da ordem de serviço para "Registo de Danos Prévios", com a opção de anexar uma ou várias fotos à ficha.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF50], [O sistema deverá ser possível nomear os itens que foram deixados na hora de entrega da trotinete, como por exemplo, carregador, cadeado e chave.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF51], [O sistema deverá ter um "Quadro de Estado em Tempo Real" acessível na receção. O mecânico deverá identificar a ordem de serviço associada e atualizar o seu estado para que a assistente tenha sempre a resposta imediatamente.], [27/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
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
  
  [RF01 <rf04>],	[O sistema deve calcular automaticamente o valor final da reparação com base na soma da reparações feitas, peças utilizadas e preços definidos pelo gerente.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF02],	[O sistema deve registar quantos dias passaram desde que o cliente foi notificado e aplicar automaticamente uma taxa diária de 3€ após 7 dias (incluindo o dia de levantamento). O valor acumulado máximo deverá ser de 90€ (correspondendo a 30 dias).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF03],	[Sempre que for criada uma nova OS, o sistema deverá conseguir calcular um tempo estimado para o término da reparação, baseado no tempo médio que uma reparação demora (fazendo o cálculo usando os dados da tabela de reparações) e o número de reparações que estão pendentes de diagnóstico e reparação.], [29/2/26],	[Entrevista ID \#001], [Luís Soares],

  [RF05],	[Sempre que uma OS for colocada nos estados: "Pendente de aprovação de orçamento", "Pendente Pagamento" ou "A aguardar peças", deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária, que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF06],	[Para cada um dos passos da execução de uma OS, deverá existir um registo de qual mecânico fez. Isto é, deverá haver um registo de qual mecânico realizou o diagnóstico e qual realizou a reparação da OS.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF07],	[O sistema deve impedir que dois mecânicos iniciem a mesma OS simultaneamente, atribuindo-a a um mecânico de cada vez. Caso um mecânico selecione uma OS que já está a ser tratada, deverá ser impedido.], [29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF08],	[Sempre que uma peça for colocada no estado "Possível defeito" ou tiver a sua quantidade mínima atingida, deverá ser gerado automaticamente um alerta (que contém o motivo do alerta e a peça em questão) para o gestor de stock, que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],
  
  [RF09],	[Todas as entradas/saídas de stock e alterações a peças ou fornecedores devem ser registadas com data, hora e utilizador.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF10],	[Quando uma peça no estado "possível defeito" for colocada no estado "pendente de devolução", a quantidade em stock dessa peça deverá diminuir em 1 unidade.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF11],	[Quando uma peça no estado "pendente de devolução" for colocada no estado "devolvida ao fornecedor", uma unidade dessa peça deverá ser adicionada ao stock, sem custos adicionais.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF12],	[Quando uma peça no estado "pendente de devolução" for colocada no estado "inválida para devolução".],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF13],	[O sistema deverá separar os utilizadores definindo diferentes perfis, consoante o seu cargo.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF15],	[O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF16],	[O gerente deverá poder adicionar, remover e editar funcionários.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF17],	[O registo de funcionários deverá ser composto obrigatoriamente por: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN e salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF18],	[O sistema deve permitir registar horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários (somando as horas extraordinárias com o salário base).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF19],	[O sistema deverá permitir ao gerente consultar o estado financeiro da empresa, apresentando os movimentos financeiros realizados (pagamentos de encomendas, pagamento de salários e receção de pagamentos).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF20],	[O sistema deverá ainda disponibilizar mecanismos de filtragem por intervalo de datas e por tipo de gasto ou receita, permitindo visualizar os valores totais por categoria, como salários, gastos com peças, lucros com mão de obra e lucros provenientes da venda de peças.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF21],	[O gerente deverá poder alterar a tabela de reparações, podendo adicionar, remover ou editar novas entradas à mesma. ],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF22],	[Cada entrada deverá ser composta por: uma referência única, uma nomenclatura, uma descrição detalhada e um preço de execução.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF23],	[O gestor de armazém poderá consultar as peças existentes no sistema, as peças em stock, as peças com possível defeito em stock, as peças pendentes de devolução e as peças devolvidas ao fornecedor. Para isto, poderá filtrar por estado (se existe no sistema, stock, com possível defeito, pendente de devolução ou devolvida ao fornecedor), fornecedor, referência, data de receção (para as que estejam em stock), tipo ou preço.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares], 

  [RF24],	[O gestor de armazém poderá adicionar, remover e editar o stock de peças existentes. Para além disso, também poderá adicionar, remover e editar fornecedores.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF25],	[O sistema deve permitir registar fornecedores obrigatoriamente com: nome e contacto (email ou número de telefone), e, opcionalmente, com prazos de entrega.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF26],	[O sistema deve permitir o registo de peças obrigatoriamente com: fornecedor, referência (do fornecedor), preço de compra, preço de venda e nível mínimo aceitável em stock.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF27],	[O sistema deve permitir registar entradas de peças obrigatoriamente com: referência, quantidade, fornecedor, preço de compra e venda, data de receção. No caso do preço de compra ser superior a 70€, deverá ser obrigatório também o registo do número de série e do tempo de garantia. Se a referência não for dada pelo fornecedor, deverá ser gerado.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF28],	[O sistema deve gerar listas de peças a encomendar com base em níveis mínimos, consumo e stock atual.],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],  

  [RF29],	[O sistema deve permitir registar e editar peças devolvidas ao fornecedor, com data, motivo e estado da devolução ("pendente de devolução", "devolvida ao fornecedor" ou "inválida para devolução").],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF30],	[A secretária deverá poder consultar todas as OS consoante os seus estados ("Pendente Diagnóstico", "Pendente Reparação", "A aguardar peças", "Pendente aprovação do orçamento", "Pendente Pagamento", "Paga"), e deverão existir filtros que permitam filtrar as OS por estado, data, cliente e mecânico.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF31],	[A secretária deverá poder adicionar, remover e editar clientes, trotinetes e OS.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],
  
  [RF32],	[O registo de clientes deverá ser composto obrigatoriamente por: nome, email, telemóvel e NIF. Poderá ser opcionalmente acompanhado pelo registo de uma ou mais trotinetes pertencentes a esse cliente.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF33],	[O registo de trotinetes deverá ser composto obrigatoriamente por: marca, modelo, número de série, tipo de motor e cliente.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF34],	[O sistema deverá registar a criação de uma OS obrigatoriamente com: os dados do cliente, os dados técnicos da trotinete, uma descrição do problema  e, opcionalmente, por acessórios e estado à entrada (com notas textuais ou até 5 fotos anexadas). Para todas as OS que forem criadas, deverá ser atribuído o estado de "Pendente Diagnóstico" e deverá ser criada uma referência única para a mesma.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF35],	[O sistema deve permitir registar a aprovação do orçamento somente em OS que estejam no estado "Pendente aprovação do orçamento" por parte de um cliente, antes de permitir que o mecânico continue a reparação. Esta aprovação deverá colocar a OS num estado de "Pendente Reparação".],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF36],	[O sistema deve permitir consultar o estado de uma qualquer OS usando o nome do cliente (para consultar todas as OS desse cliente, ordenadas por data de criação), a referência da OS ou o número de série da trotinete da OS (apresentando ordenadas por data de criação as OS dessa trotinete), apresentando informação atualizada, isto é, toda a informação relativa a cada um dos estados pelos quais a OS já passou, ordenados por ordem cronológica.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF37],	[Para uma OS no estado "Pendente Pagamento" deverá ser registado quando um cliente foi notificado sobre o término da reparação da trotinete.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF39],	[O sistema deve permitir registar a efetuação do pagamento e o respetivo método utilizado numa OS no estado "Pendente Pagamento" (numerário, multibanco, MBWay). Caso sejam ambos registados, a OS deverá ficar no estado "Paga".],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF40],	[O sistema deve impedir a conclusão de uma OS no estado "Pendente Pagamento" caso o cliente referenciado na mesma tenha outras OS no estado ""Pendente Pagamento"" que tenham tido a confirmação de término de reparação numa data anterior à primeira.],	[29/2/26],	[Entrevista ID \#002],	[Eduardo Fernandes],

  [RF41],	[O mecânico poderá escolher de entre duas listas de OS: "Pendentes de diagnóstico" ou "Pendentes de reparação", a OS que quer tratar a seguir.],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF42],	[Quando um mecânico selecionar uma OS "Pendente Diagnóstico", deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações que estará disponível, poderá consultar (procurando por referência, fornecedor ou data) e associar peças do stock, e também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF43],	[Quando um mecânico selecionar uma OS "Pendente Diagnóstico" (e selecionar as várias reparações e peças que achar necessárias),  deverá ser atribuído automaticamente o estado de "Pendente de aprovação de orçamento],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF44],	[Quando um mecânico selecionar uma OS no estado "Pendente de reparação", deverá poder selecionar quais as reparações que ele executou a partir da tabela de reparações e poderá consultar (procurando por referência, fornecedor ou data) e associar as peças que usou. Também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF45],	[Quando um mecânico selecionar uma OS no estado "Pendente de reparação" e uma ou mais peças da lista do diagnóstico não estiver disponível no stock, o sistema deverá disponibilizar a opção de requisição de encomenda das peças. A OS deverá ficar em estado de "A aguardar peças", mas a reparação poderá processeguir normalmente. Quando todas as peças estiverem disponíveis no stock, a OS deverá ficar no estado "Pendente reparação".],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF46],	[O sistema deve permitir ao mecânico adicionar novos problemas identificados durante a reparação, identificando-os com recurso à tabela de reparações. Esta ação deverá colocar o OS num estado de "Pendente aprovação do orçamento".],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF47],	[Durante a realização de uma reparação, depois de adicionadas peças à lista de peças usadas na OS, o mecânico poderá reportar defeitos nas peças instaladas, atribuindo o estado "Possível defeito" à peça e removendo-a da lista de peças usadas, diminuindo o valor final da reparação.],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF48],	[Quando um mecânico desejar terminar uma reparação, deverá verificar uma checklist obrigatória de segurança (travões, luzes, pneus, aceleração, travagem, visor, teste de condução).],	[29/2/26],	[Entrevista ID \#001],	[Luís Soares],

  [RF49],	[O sistema deve permitir ao mecânico marcar a OS como concluída, ficando esta no estado "Pendente Pagamento".],	[29/2/26],	[Entrevista ID \#003],	[Eduardo Fernandes],

  [RF50], [O sistema precisa de uma funcionalidade rápida na abertura da ordem de serviço para "Registo de Danos Prévios", com a opção de anexar uma ou várias fotos à ficha.], [29/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF51], [O sistema deverá ser possível nomear os itens que foram deixados na hora de entrega da trotinete, como por exemplo, carregador, cadeado e chave.], [29/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
  
  [RF52], [O sistema deverá ter um "Quadro de Estado em Tempo Real" acessível na receção. O mecânico deverá identificar a ordem de serviço associada e atualizar o seu estado para que a assistente tenha sempre a resposta imediatamente.], [29/2/26], [Relatório Operacional \#004], [Eduardo Fernandes],
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

  [RF8], [O sistema deve ser modular, permitindo a adição, substituição ou atualização de componentes sem impacto nas restantes funcionalidades.], [30/2/26], [Eduardo Fernandes],
)
]

#attachment(caption: [Requisitos Funcionais Estruturados], none) <requisitos_estruturados>

//  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE  GERENTE

#requisito(
  id: "REQ-001",
  titulo: "Controlo de Acesso e Permissões por Papel",
  requisito_utilizador: " O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar.",
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
  requisito_utilizador: " O sistema deverá separar os utilizadores definindo diferentes perfis, consoante o seu cargo.",
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
  O sistema deve permitir registar horas extraordinárias e calcular automaticamente o valor mensal a pagar de cada um dos funcionários (somando as horas extraordinárias com o salário base).",
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
    "O sistema deverá calcular automaticamente o valor total a pagar ao funcionário desde o início do mês, somando o valor base bruto mensal com a multiplicação do valor pago à hora ao funcionário com o valor das horas extraordinárias registadas desde o início do mês. O sistema deverá registar a data, hora, utilizador e o funcionário antes de serem registadas as horas extra.",
    "No fim do mês, o valor total a pagar deve ser reiniciado, retornando ao valor do salário base bruto."
  ),
  relevancia: "Garante o controlo fundamental sobre a base de dados de funcionários e automatiza o cálculo de remuneração e garante precisão na folha de pagamento."
)

#requisito(
  id: "REQ-004",
  titulo: "Registo de Dados de Funcionários",
  requisito_utilizador: "O registo de funcionários deverá ser composto obrigatoriamente por: nome, morada (número de porta, rua, localidade e código-postal), telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN e salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto).",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Recursos Humanos",
  requisitos_sistema: (
    "Na página de criação de funcionários, deverão ser fornecidos: o nome, morada (composta por número de porta, rua, localidade e código-postal), número de telemóvel, email, data de nascimento, NISS, NIF, NUS, IBAN, salário (valor pago por hora e valor pago ao fim do mês, tanto líquido como bruto) e palavra-passe.",
    "O nome, rua, localidade, email e palavra-passe deverão ser campos textuais não vazios e o email deverá seguir a convenção " + link("https://www.rfc-editor.org/rfc/rfc5322.html")[RFC 5322] + ".O número de porta deverá ser um número inteiro com, no máximo, 4 dígitos e poderá ser seguido por uma letra; o código postal deverá ser composto por 4 dígitos, um hífen '-' e terminado por 3 dígitos; o número de telemóvel deverá ser um número composto por 9 dígitos; o NISS, um número composto por 11 dígitos; o NIF, um número composto por 9 dígitos; o NUS, um número composto por 9 dígitos; o IBAN, composto por 2 caracteres alfabéticos, seguidos de 23 dígitos; os valores do salário deverão ser números reais positivos com 2 casas decimais.",
    "Depois de validados e preenchidos todos os dados de um funcionário, quando o gerente confirmar, o sistema deverá fornecer um identificador único, informar o gerente da criação, apresentando uma página com todos os dados do novo funcionário.",
  ),
  relevancia: "Centraliza toda a informação necessária para a folha de pagamento, segurança social e conformidade legal."
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
    "O sistema deverá permitir a consulta  por tipo de gasto ou receita (salários, gastos com peças, lucros com mão de obra, lucros provenientes da venda de peças) e apresentá-los em gráficos de: barra, linhas, pizza, colunas ou rosca."
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
    "Depois de validados e preenchidos todos os dados de uma reparação, o sistema deverá: fornecer um identificador único, informar o gestor da criação, apresentando uma página com todos os dados da nova entrada, e registar a data e a hora seguindo a convenção "+ link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601]+ " (YYYY-MM-DD-HH:MM), o utilizador que realizou a criação e os dados da reparação criada.",
    "Se o gerente quiser editar uma entrada, depois de escolhê-la, deverá surgir uma página com todos os dados da entrada, o gerente deverá selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação. O sistema deverá registar a data e a hora seguindo a convenção "+ link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601]+ " (YYYY-MM-DD-HH:MM), o utilizador que realizou a edição e os dados da reparação antes de ser editada.",
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
    "Se o gestor de stock decidir eliminar alguma peça, depois de escolhê-la, deverá surgir uma página com os dados da peça e o gestor terá de confirmar a eliminação. A partir deste momento, qualquer tentativa de adição da peça em questão ao stock, deverá ser impedida. Para além disto, o sistema deverá registar a data e a hora seguindo a convenção "+ link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601]+ " (YYYY-MM-DD-HH:MM), o utilizador que realizou a eliminação e os dados da reparação antes de ser eliminada.",
    "Se o gestor do armazém decidir alterar algum dado de uma peça, depois de selecionada, deverá surgir uma página com os dados da peça e ele deverá selecionar qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma peça."
  ),
  relevancia: "Centraliza as informações críticas sobre peças, incluindo custos e margens de lucro, essencial para a gestão financeira e de inventário."
)

#requisito(
  id: "REQ-008",
  titulo: "Registo de Dados de Peças",
  requisito_utilizador: "O sistema deve permitir o registo de peças obrigatoriamente com: fornecedor, referência (do fornecedor), preço de compra, preço de venda e nível mínimo aceitável em stock.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Na página de criação de peças, deverá ser fornecido obrigatoriamente o identificador do fornecedor. Caso exista um fornecedor com o identificador fornecido, deverá surgir como opção listada para o gestor selecionar. Caso contrário, o sistema deverá informar que não existe nenhum fornecedor com o identificador fornecido.",
    "No caso do fornecedor existir, deverão ser fornecidos: a referência (do fornecedor) e o nível mínimo aceitável em stock.",
    "A referência do fornecedor deverá ser um campo textual não vazio e o nível mínimo aceitável em stock deverá ser um número inteiro não negativo.",
    "Depois de validados e preenchidos todos os dados de uma peça, o sistema deverá: fornecer um identificador único para cada peça registada, informar o gestor da criação, apresentando uma página com todos os dados da nova peça.",
  ),
  relevancia: "Centraliza a gestão de inventário e garante que as informações sobre disponibilidade de peças estão sempre actualizadas."
)

#requisito(
  id: "REQ-009",
  titulo: "Registo de Entradas de Peças em Stock",
  requisito_utilizador: "O sistema deve permitir registar entradas de peças obrigatoriamente com: referência, quantidade, fornecedor, preço de compra e venda, data de receção, e, quando o preço de compra for superior a 70€, número de série e tempo de garantia.",
  fonte: "Entrevista ID #001",
  area_sistema: "Gestão de Stock",
  requisitos_sistema: (
    "Deve existir uma página de gestão de entradas de peças em stock, com as opções de seguir para as páginas de criação, eliminação e edição de stock de peças. Esta página deverá ser acessível a partir da página principal do gestor do stock.",
    "Na página de registo, deverão estar listadas numa tabela os registos de entradas de peças em que cada um dos seus campos ocupa numa coluna distinta, e, a ordenação deverá ter em conta a ordem numérica e deverá usar o identificador único da peça.",
    "O gestor deverá poder selecionar nesta tabela qual a entrada que deseja gerir e posteriormente, deverá selecionar se deseja eliminá-la ou editá-la. Deverão existir filtros que permitam a procura por uma determinada entrada de uma peça usando todos os campos de dados de uma entrada de peça.",
    "Na página de registo, deverá existir a opção de adicionar um registo novo. Quando selecionado, deverão ser fornecidos obrigatoriamente a referência do fornecedor ou o identificador único da peça no sistema. Caso exista uma peça que corresponda ao dado fornecido, deverá surgir como opção listada para o gestor selecionar. Caso contrário, o sistema deverá informar que não existe nenhuma peça com os dados fornecidos. ",
    "Caso exista a peça, depois de ser selecionada, deverá ser fornecida a quantidade a adicionar, o preço de compra e o preço de venda.",
    "A quantidade deverá ser um número inteiro positivo, o preço de compra e o preço de venda deverão ser valores reais positivos com 2 casas decimais e a data de receção deverá ser uma data válida no formato AAAA-MM-DD (seguindo a convenção " + link("https://www.iso.org/iso-8601-date-and-time-format.html")[ISO 8601]+").",
    "Se o preço for superior a 70€, após preencher a quantidade, o sistema deverá apresentar um formulário para inserir o número de série e tempo de garantia para cada unidade. O número de vezes que o sistema deverá pedir o número de série  será tão grande quanto o número inserido na quantidade,",
    "O número de série deverá ser um campo textual não vazio e o tempo de garantia deverá ser registado em meses como um número inteiro positivo.",
    "Depois de ser efetuado o registo, uma página com os dados das peças adicionadas deverá surgir.",
    "Caso o gestor deseje eliminar uma entrada de uma peça, depois de selecioná-la, deverá surgir uma página com os dados da peça e o gestor terá de confirmar a eliminação. Após a confirmação, o registo deverá ser apagado do sistema.",
    "Se o gestor decidir editar o registo, depois de escolhê-la, uma página com os dados da peça deverá surgir e ele poderá selecionar que deseja editá-la. Depois disto, deverá escolher o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma entrada de uma peça.",
  ),
  relevancia: "Garante o registo preciso de todas as entradas de peças, incluindo informações de garantia para peças caras, essencial para rastreabilidade."
)

#requisito(
  id: "REQ-010",
  titulo: "Gestão de Devoluções de Peças ao Fornecedor",
  requisito_utilizador: "O sistema deve permitir registar e editar peças devolvidas ao fornecedor, com data, motivo e estado da devolução.
  Quando uma peça no estado \"possível defeito\" for colocada no estado \"pendente de devolução\", a quantidade em stock dessa peça deverá diminuir em 1 unidade.
  Quando uma peça no estado \"pendente de devolução\" for colocada no estado \"devolvida ao fornecedor\", uma unidade dessa peça deverá ser adicionada ao stock, sem custos adicionais.
  Quando uma peça no estado \"pendente de devolução\" for colocada no estado \"inválida para devolução\", os gastos com peças nesse mês deverá aumentar consoante o valor de compra da peça em questão.
  Todas as (...) saídas de stock (...) devem ser registadas com data, hora e utilizador.",
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
    "Quando uma peça no estado 'Pendente de devolução' for colocada no estado 'Inválida para devolução', deverão ser aumentados os gastos em peças no valor da peça.",
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
    "O gestor deverá poder alterar o estado de uma lista de 'enviada' para 'recebida' quando as peças forem recebidas e o sistema deverá adicionar automaticamente ao stock as peças registadas na lista. Se o preço de compra for superior a 70€ em alguma das peças, o sistema deverá apresentar um formulário para inserir o número de série e tempo de garantia para cada unidade. O número de vezes que o sistema deverá pedir o número de série  será tão grande quanto o número inserido na quantidade.",
    "O número de série deverá ser um campo textual não vazio e o tempo de garantia deverá ser registado em meses como um número inteiro positivo.",
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
    "Se a secretária decidir alterar algum dado de uma ordem de serviço, deverá escolhê-la, e ,depois, deverá surgir uma página com os dados da ordem de serviço e ela terá de selecionar que deseja editá-la. De seguida, deverá escolher qual o ou os campos que quer alterar e o ou os novos valores inseridos deverão seguir as mesmas validações usadas na criação de uma ordem de serviço.",
  ),
  relevancia: "Permite à secretária ter uma visão clara do estado de todas as ordens de serviço em processamento, facilitando a comunicação com clientes e mecânicos."
)

#requisito(
  id: "REQ-019",
  titulo: "Registo de Ordens de Serviço",
  requisito_utilizador: "O sistema deverá registar a criação de uma OS obrigatoriamente com: os dados do cliente, os dados técnicos da trotinete, uma descrição do problema e, opcionalmente, por acessórios e estado à entrada (com notas textuais ou até 5 fotos anexadas).",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Deve existir uma página de criação de ordens de serviço. Esta página deverá ser acessível a partir da página principal da secretária.",
    "Na página de criação de uma ordem de serviço, deverão ser fornecidos obrigatoriamente os dados do cliente, os dados da trotinete, a descrição do problema e opcionalmente acessórios e estado (com recurso até 5 fotografias) à entrada.",
    "O cliente deverá ser selecionado a partir da lista de clientes já registados no sistema. Deverão ser fornecidos o identificador do cliente, o seu nome ou o seu NIF. Caso exista um cliente que corresponda ao dado fornecido, deverá surgir como opção listada para a secretária selecionar. Caso contrário, o sistema deverá informar que não existe nenhum cliente com os dados fornecidos.",
    "A trotinete deverá ser selecionada a partir da lista de trotinetes associadas ao cliente. Caso o cliente selecionado não tenha trotinetes registadas, a secretária deverá ter a opção de registar uma nova trotinete nesse momento, sendo redirecionada para a página de criação de uma trotinete e seguindo o REQ-017.",
    "A descrição do problema deverá ser um campo textual não vazio onde a secretária descreva o problema relatado pelo cliente.",
    "Opcionalmente, a secretária deverá poder registar um ou mais acessórios associados à OS. Cada acessório deverá ser um campo textual não vazio.",
    "Opcionalmente, a secretária deverá poder registar o estado à entrada da trotinete, com até 5 fotos nos formatos PNG ou JPEG e com até 5MB de tamanho, anexadas. Cada foto deverá ser armazenada no sistema.",
    "Para todas as ordens de serviço criadas, o sistema deverá atribuir automaticamente o estado de \"Pendente Diagnóstico\".",
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
  requisito_utilizador: "Para uma OS no estado \"Pendente Pagamento\" deverá ser registado quando um cliente foi notificado sobre o término da reparação da trotinete.
  O sistema deve registar quantos dias passaram desde que o cliente foi notificado e aplicar automaticamente uma taxa diária de 3€ após 7 dias (excluindo o dia de levantamento). O valor acumulado máximo deverá ser de 90€ (correspondendo a 30 dias).",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Quando uma OS se encontra no estado \"Pendente Pagamento\", deverá ser possível editá-la através da página de gestão de ordens de serviço.",
    "Ao editar uma OS no estado \"Pendente Pagamento\", deverá surgir um novo campo na página de edição com a opção de \"Registar notificação de término\".",
    "A secretária deverá poder selecionar a opção para registar a notificação de término da reparação.",
    "Após o registo da notificação, o campo deverá exibir a data e hora em que o cliente foi notificado, de forma a permitir consulta posterior.",
    "O sistema deverá impedir que uma OS seja colocada no estado \"Paga\" se o cliente não tiver sido notificado previamente sobre o término da reparação, informando o erro.",
    "Se, passados 7 dias depois da notificação ter sido registada, a OS ainda encontrar-se no estado \"Pendente Pagamento\", a cada dia que passar, deverá ser registada uma taxa no valor de 3€ na OS."
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

#requisito(
  id: "REQ-023",
  titulo: "Validação de Ordem de Pagamento para Ordens de Serviço",
  requisito_utilizador: "O sistema deve impedir a conclusão de uma OS no estado \"Pendente Pagamento\" caso o cliente referenciado na mesma tenha outras OS no estado \"Pendente Pagamento\" que tenham tido a confirmação de término de reparação numa data anterior à primeira.",
  fonte: "Entrevista ID #002",
  area_sistema: "Gestão de Ordens de Serviço",
  requisitos_sistema: (
    "Quando a secretária tenta registar o pagamento de uma OS no estado \"Pendente Pagamento\", o sistema deverá verificar se existem outras OS do mesmo cliente que também estejam no estado \"Pendente Pagamento\".",
    "O sistema deverá comparar as datas de notificação de término de reparação (campo de confirmação de término) entre a OS atual e as outras OS pendentes do mesmo cliente.",
    "Se existirem outras OS pendentes do mesmo cliente com data de notificação anterior à OS atual, o sistema deverá exibir uma mensagem de aviso informando que essas OS anteriores devem ser pagas primeiro.",
    "O sistema deverá impedir o registo do pagamento se existirem OS anteriores pendentes, exibindo uma lista com as referências das OS que devem ser processadas primeiro.",
    "Apenas quando todas as OS anteriores tiverem sido pagas (estado \"Paga\"), a OS atual poderá ter o seu pagamento registado e o seu estado alterado para \"Paga\".",
    "O sistema deverá utilizar a data de notificação de término como critério de ordenação para determinar a sequência de pagamento obrigatória."
  ),
  relevancia: "Garante uma ordem justa e rastreável de processamento de pagamentos, evitando situações em que OS mais recentes são pagas antes de OS mais antigas do mesmo cliente."
)

//  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO  MECÂNICO

#requisito(
  id: "REQ-024",
  titulo: "Consulta e Gestão de Ordens de Serviço para Mecânicos",
  requisito_utilizador: "O mecânico poderá escolher de entre duas listas de OS: \"Pendentes de diagnóstico\" ou \"Pendentes de reparação\", a OS que quer tratar a seguir.
  O sistema deve impedir que dois mecânicos iniciem a mesma OS simultaneamente, atribuindo-a a um mecânico de cada vez. Caso um mecânico selecione uma OS que já está a ser tratada, deverá ser impedido.
  A interface dos mecânicos deverá mostrar apenas a sua informação pessoal, as listas de OS nos estados \"Pendente Reparação\" e \"Pendente Diagnóstico\" e, no caso do mecânico estar a tratar de alguma OS, a informação relativa a essa OS, bem como todas as opções (consoante o estado em que se encontra).",
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
  id: "REQ-025",
  titulo: "Diagnóstico e Orçamentação de Ordens de Serviço",
  requisito_utilizador: "Quando um mecânico selecionar uma OS \"Pendente Diagnóstico\", deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações que estará disponível, poderá consultar (procurando por referência, fornecedor ou data) e associar peças do stock, e também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).
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
  id: "REQ-025A",
  titulo: "Atribuição de Estado com Base no Valor do Orçamento",
  requisito_utilizador: "Quando um mecânico selecionar uma OS \"Pendente Diagnóstico\" (e selecionar as várias reparações e peças usadas), deverá ser atribuído automaticamente o estado de \"Pendente de aprovação de orçamento\" e o mecânico não poderá proceder com a sua reparação. Caso o valor não exceda, deverá ser atribuído o estado \"Pendente Reparação\".
  Sempre que uma OS for colocada nos estados: \"Pendente de aprovação de orçamento\" (...) deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária (...), que deverá ficar guardado na sua lista de alertas. (...)",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Após o mecânico confirmar o diagnóstico, depois do sistema analisar o valor total do orçamento, deverá ser redirecionado para a página principal.",
    "O estado da OS deverá ser alterado automaticamente para \"Pendente de aprovação de orçamento\" e deverá ser gerado um alerta que será adicionado à lista de alertas da secretária. O alerta deverá conter o identificador da OS e o estado em que ela se encontra.",
  ),
  relevancia: "Automatiza a validação de orçamentos, garantindo transparência e conformidade com políticas de aprovação de custos."
)

#requisito(
  id: "REQ-026",
  titulo: "Realização de Reparações em Ordens de Serviço",
  requisito_utilizador: "O sistema deve calcular automaticamente o valor final da reparação com base na soma da reparações feitas, peças utilizadas e preços definidos pelo gerente.
  Quando um mecânico selecionar uma OS no estado \"Pendente de reparação\", deverá poder selecionar quais as reparações que ele executou a partir da tabela de reparações e poderá consultar (procurando por referência, fornecedor ou data) e associar as peças que usou. Também deverá poder consultar rapidamente o histórico de reparações da trotinete (mostrando toda a informação das OS que já foram levantadas sobre ela).
  Para cada um dos passos da execução de uma OS, deverá existir um registo de qual mecânico fez. Isto é, deverá haver um registo de qual mecânico (...) realizou a reparação da OS.",
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
  id: "REQ-027",
  titulo: "Gestão de Peças Indisponíveis e Requisição de Encomendas",
  requisito_utilizador: "Quando um mecânico selecionar uma OS no estado \"Pendente de reparação\" e uma ou mais peças da lista do diagnóstico não estiver disponível no stock, o sistema deverá disponibilizar a opção de requisição de encomenda das peças. A OS deverá ficar em estado de \"A aguardar peças\", mas a reparação poderá proceseguir normalmente. Quando uma das peças estiver disponível no stock, a OS deverá ficar no estado \"Pendente reparação\".
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
    "Enquanto a OS está no estado \"A aguardar peças\", o mecânico poderá continuar a trabalhar noutras reparações ou outras partes da mesma OS que não dependam das peças em falta.",
    "Quando uma peça for recebida e disponibilizada em stock (através do sistema de gestão de stock), o sistema deverá verificar automaticamente todas as OS em estado \"A aguardar peças\" que necessitam dessa peça.",
    "Se todas as peças de uma OS estiverem agora disponíveis, o estado da OS deverá ser automaticamente alterado para \"Pendente Reparação\"."
  ),
  relevancia: "Permite que a reparação prossiga mesmo com peças em falta, sem comprometer o progresso geral e garantindo que o sistema é atualizado automaticamente quando os recursos se tornam disponíveis."
)

#requisito(
  id: "REQ-028",
  titulo: "Gestão de Defeitos em Peças Instaladas",
  requisito_utilizador: "Durante a realização de uma reparação, depois de adicionadas peças à lista de peças usadas na OS, o mecânico poderá reportar defeitos nas peças instaladas, atribuindo o estado \"Possível defeito\" à peça e removendo-a da lista de peças usadas, diminuindo o valor final da reparação.
  Sempre que uma peça for colocada no estado \"Possível defeito\" ou tiver a sua quantidade mínima atingida, deverá ser gerado automaticamente um alerta (que contém o motivo do alerta e a peça em questão) para o gestor de stock, que deverá ficar guardado na sua lista de alertas. (...)",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Durante a realização de uma reparação, o mecânico deverá ter a opção de reportar um defeito numa peça já utilizada.",
    "O mecânico deverá poder selecionar a peça problemática da lista de peças utilizadas.",
    "Ao reportar um defeito, o mecânico deverá poder fornecer uma descrição do problema encontrado e o sistema deverá adicionar um alerta à lista de alertas do gestor de stock com o identificador, referência da peça e a descrição fornecida",
    "O sistema deverá remover a peça da lista de peças utilizadas na OS.",
    "O sistema deverá recalcular automaticamente o valor total da reparação, diminuindo o custo pela peça removida.",
    "O sistema deverá registar uma entrada no sistema de gestão de devoluções, marcando o stock da peça como \"Possível defeito\".",
    "O mecânico deverá poder adicionar uma nova peça de substituição, se necessário, seguindo o processo normal de adição de peças."
  ),
  relevancia: "Garante qualidade e rastreabilidade de problemas com peças, permitindo ajustes de custos e registro automático para devoluções a fornecedores."
)

#requisito(
  id: "REQ-029",
  titulo: "Checklist de Segurança para Conclusão de Reparações",
  requisito_utilizador: "Quando um mecânico desejar terminar uma reparação, deverá verificar uma checklist obrigatória de segurança (travões, luzes, pneus, aceleração, travagem, visor, teste de condução).",
  fonte: "Entrevista ID #003",
  area_sistema: "Gestão de Reparações",
  requisitos_sistema: (
    "Quando o mecânico indica que deseja terminar a reparação, deverá surgir uma página com uma checklist obrigatória de segurança.",
    "A checklist deverá incluir os seguintes itens de verificação: Travões, Luzes, Pneus, Aceleração, Travagem, Visor e Teste de Condução.",
    "Para cada item da checklist, o mecânico deverá confirmar que foi verificado, marcando-o como \"Verificado\".",
    "O mecânico não poderá terminar a reparação até que todos os itens da checklist tenham sido marcados como verificados.",
    "Após a conclusão da checklist de segurança, o mecânico poderá proceder com a conclusão da reparação."
  ),
  relevancia: "Garante que todas as reparações cumprem padrões mínimos de segurança antes de serem devolvidas ao cliente, reduzindo riscos e problemas pós-reparação."
)

#requisito(
  id: "REQ-030",
  titulo: "Conclusão de Ordens de Serviço",
  requisito_utilizador: "O sistema deve permitir ao mecânico marcar a OS como concluída, ficando esta no estado \"Pendente Pagamento\"
  Sempre que uma OS for colocada nos estados: \"Pendente de aprovação de orçamento\", \"Pendente Pagamento\" ou \"A aguardar peças\", deverá ser gerado automaticamente um alerta (que contém a referência da OS e o estado) para a secretária ou para o gestor, que deverá ficar guardado na sua lista de alertas. Cada alerta deverá ter um campo que indique se já foi tratado ou não.",
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

Se existirem danos prévios visíveis, a secretária pode registar essas observações e anexar fotografias à ficha da ordem de serviço.

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
      [Todas as alterações devem ficar registadas com data, hora e utilizador.],
    ),
    [
O gestor de stock inicia sessão no sistema e acede à área de gestão de peças. O sistema apresenta uma tabela com todas as peças registadas, incluindo fornecedor, referência, preços e nível mínimo aceitável em stock.

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

O secretária pode selecionar filtros de estado, data, cliente ou mecânico e o sistema apresenta as ordens de serviço correspondentes.

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
      [O sistema deve disponibilizar gráficos para análise visual dos dados.],
    ),
    [
O gerente inicia sessão no sistema e acede à página de consulta do estado financeiro. O sistema apresenta os movimentos financeiros mais recentes, organizados numa tabela com todos os seus campos visíveis.

O gerente seleciona um intervalo de datas para análise. O sistema filtra automaticamente os movimentos apresentados, exibindo apenas os que se encontram dentro do período definido.

De seguida, o gerente escolhe visualizar apenas um tipo específico de movimento, como salários, gastos com peças, lucros com mão de obra ou lucros provenientes da venda de peças. O sistema atualiza a tabela e apresenta os totais agregados correspondentes à categoria selecionada.

O sistema disponibiliza ainda gráficos, permitindo ao gerente analisar visualmente a distribuição dos gastos e receitas no período escolhido.

Caso o gerente deseje analisar outro período ou outra categoria, altera os filtros aplicados e o sistema atualiza imediatamente os dados e gráficos apresentados.

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

Se o preço de compra for superior a 70€, o sistema solicita o número de série e o tempo de garantia para cada unidade adicionada.

Após confirmar, o sistema valida os dados, regista a entrada e apresenta um resumo da operação.

O gestor pode consultar, editar ou eliminar entradas anteriores, sendo que todas as alterações ficam registadas com data, hora e utilizador.
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
      [Todas as alterações devem ser registadas com data, hora e utilizador.],
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
    pos_condicoes: "Um novo funcionário é registado no sistema e um identificador único é atribuído.",
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
    pos_condicoes: "Os dados do funcionário são atualizados no sistema com os novos valores fornecidos.",
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
    pos_condicoes: "Uma nova peça é registada no sistema e um identificador único é atribuído.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova peça.],
      [O funcionário introduz o identificador do fornecedor.],
      [O sistema valida a existência do fornecedor.],
      [O funcionário preenche todos os restantes dados obrigatórios: referência do fornecedor, nível mínimo aceitável em stock e o preço de venda.],
      [O funcionário confirma o registo.],
      [O sistema valida que a referência do fornecedor é um campo textual não vazio.],
      [O sistema valida que o nível mínimo aceitável em stock é um número inteiro não negativo.],
      [O sistema valida que o preço de venda é um valor real positivo com 2 casas decimais.],
      [O sistema cria a peça e atribui-lhe um identificador único.],
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
        condicao: "Nível mínimo aceitável em stock inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o nível mínimo aceitável em stock deve ser um número inteiro não negativo e impede o registo.],
        )
      ),
      (
        condicao: "Preço de Venda inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o preço de venda deve ser um número inteiro positivo com duas casas decimais e impede o registo.],
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
      [O sistema valida que o nível mínimo aceitável em stock é um número inteiro positivo.],
      [O sistema valida que o preço de venda é um valor real positivo com 2 casas decimais.],
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
        condicao: "Nível mínimo aceitável em stock inválido",
        passo: "7",
        fluxo: (
          [7.1 - O sistema informa o funcionário que o nível mínimo aceitável em stock deve ser um número inteiro positivo e impede a atualização.],
        )
      ),
      (
        condicao: "Preço de Venda inválido",
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que o preço de venda deve ser um número inteiro positivo com duas casas decimais e impede o registo.],
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
    pos_condicoes: "Uma nova entrada de peça em stock é registada com identificador único numérico, quantidade aumenta e é registada com data, hora e utilizador.",
    fluxo_normal: (
      [O funcionário seleciona a opção de adicionar uma nova entrada.],
      [O funcionário introduz o valor da referência do fornecedor ou o identificador único da peça.],
      [O sistema valida a existência da peça.],
      [O funcionário preenche todos os dados obrigatórios: quantidade, preço de compra e data de receção.],
      [O funcionário confirma o registo.],
      [O sistema valida que a quantidade é um número inteiro positivo.],
      [O sistema valida que o preço de compra é um valor real positivo com 2 casas decimais.],
      [O sistema valida que a data de receção é uma data válida no formato AAAA-MM-DD.],
      [O sistema verifica que o preço de compra é inferior a 70€.],
      [O sistema cria as entradas de acordo com a quantidade especificada, atribui um identificador único numérico a cada uma e apresenta os dados das novas entradas registadas.]
    ),
    fluxos_alternativos: (
      (
        condicao: "O preço de compra é superior a 70€",
        passo: "9",
        fluxo: (
          [9.1 - O funcionário introduz o número de série para cada unidade da quantidade fornecida.],
          [9.2 - O funcionário introduz o tempo de garantia.],
          [9.3 - O sistema valida que o número de série é um campo textual não vazio.],
          [9.4 - O sistema valida que o tempo de garantia é um número inteiro positivo.],
          [9.5 - Retorna ao passo 10 do fluxo normal.]
        )
      ),
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
      (
        condicao: "Número de série inválido",
        passo: "9.3",
        fluxo: (
          [9.3.1 - O sistema informa o funcionário que o número de série deve ser um campo textual não vazio e impede o registo.],
        )
      ),
      (
        condicao: "Tempo de garantia inválido",
        passo: "9.4",
        fluxo: (
          [9.4.1 - O sistema informa o funcionário que o tempo de garantia deve ser um número inteiro positivo (em meses) e impede o registo.],
        )
      ),
    )
)

#uc_spec(
    id: "14",
    nome: "Editar Entrada de Stock de Peças",
    atores: "Gestor de Stock e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma entrada de peça em stock registada.",
    pos_condicoes: "Os dados da entrada de stock são atualizados no sistema com os novos valores fornecidos e é registada com data, hora e utilizador.",
    fluxo_normal: (
      [O funcionário seleciona a entrada de peça que pretende editar.],
      [O sistema apresenta os dados atuais da entrada.],
      [O funcionário seleciona a opção de editar a entrada.],
      [O funcionário seleciona o ou os campos que pretende alterar (com a exceção da quantidade) e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que o preço de compra é um valor real positivo com 2 casas decimais.],
      [O sistema valida que a data de receção é uma data válida no formato AAAA-MM-DD.],
      [O sistema atualiza os dados da entrada de stock, regista a data, hora e utilizador da edição, e apresenta os dados atualizados.]
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
    pos_condicoes: "Uma nova devolução é registada no sistema com estado 'Pendente de Devolução', identificador único é atribuído e data, hora e utilizador são registados.",
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
      [O apresenta os dados da nova devolução.]
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
    pos_condicoes: "Os dados da devolução são atualizados no sistema com os novos valores fornecidos, as transições de estado causam os efeitos correspondentes no stock e na gestão financeira, e data, hora e utilizador da edição são registados.",
    fluxo_normal: (
      [O funcionário seleciona a devolução que pretende editar.],
      [O sistema apresenta os dados atuais da devolução.],
      [O funcionário seleciona a opção de editar a devolução.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário confirma as alterações.],
      [O sistema valida que a quantidade é um número inteiro positivo e a diferença entre a quantidade anteriormente registada e a inserida não excede a quantidade marcada como 'Possível Defeito' em stock.],
      [O sistema valida que a data da devolução é uma data válida no formato AAAA-MM-DD.],
      [O sistema valida que o motivo da devolução é um campo textual não vazio.],
      [O sistema valida que nenhuma mudança no estado foi feita.]
    ),
    fluxos_alternativos: (
        (
        condicao: "O sistema verifica que a transição de estado é de 'Pendente de Devolução' para 'Devolvida ao Fornecedor'",
        passo: "9",
        fluxo: (
          [9.1.1 - O sistema verifica que a transição de estado é de 'Pendente de Devolução' para 'Devolvida ao Fornecedor'.],
          [9.1.2 O sistema atualiza o estado das entradas de stock devolvidas para o estado ‘Devolvida ao Fornecedor’],
          [9.1.3 - Retorna ao passo 10 do fluxo normal.]
        )
      ),
      (
        condicao: "O sistema verifica que a transição de estado é de 'Pendente de Devolução' para 'Inválida para Devolução'",
        passo: "9",
        fluxo: (
          [9.2.1 - O sistema aumenta os gastos em peças do mês atual no valor de compra da peça em questão.],
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
    pos_condicoes: "A devolução é removida do sistema, os gastos em peças são aumentados consoante o valor da peça e quantidade, e data, hora e utilizador da eliminação são registados.",
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
    pos_condicoes: "Um novo fornecedor é registado no sistema com identificador único e data, hora e utilizador são registados.",
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
    pos_condicoes: "Os dados do fornecedor são atualizados no sistema com os novos valores fornecidos e data, hora e utilizador da edição são registados.",
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
    pos_condicoes: "O fornecedor é removido do sistema, deixa de ser possível associar peças a este fornecedor e data, hora e utilizador da eliminação são registados.",
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
      [O sistema cria a lista, atribui-lhe um identificador único, define o estado como 'Rascunho' e apresenta os dados da nova lista, incluindo dados das peças, quantidades, preços de compra e venda e fornecedores.],
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
        passo: "8",
        fluxo: (
          [8.1.1 - Retorna ao passo 9 do fluxo normal.],
        )
      ),
      (
        condicao: "O funcionário alterou o estado da lista de 'Enviada' para 'Recebida'",
        passo: "8",
        fluxo: (
          [8.2.1 - O sistema valida que nenhuma das peças na lista tem um preço de compra superior a 70€.],
          [8.2.2 - O sistema adiciona automaticamente ao stock as peças registadas na lista.],
          [8.2.3 - Retorna ao passo 9 do fluxo normal.],
        )
      ),
      (
        condicao: "O preço de compra é superior a 70€",
        passo: "8.2.1",
        fluxo: (
          [8.2.1.1 - O funcionário introduz o número de série para cada unidade da quantidade fornecida.],
          [8.2.1.2 - O funcionário introduz o tempo de garantia.],
          [8.2.1.3 - O sistema valida que o número de série é um campo textual não vazio.],
          [8.2.1.4 - O sistema valida que o tempo de garantia é um número inteiro positivo.],
          [8.2.1.5 - O sistema adiciona automaticamente ao stock as peças registadas na lista],
          [8.2.1.6 - Retorna ao passo 9 do fluxo normal.]
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
        passo: "8",
        fluxo: (
          [8.1 - O sistema informa o funcionário que a transição de estado solicitada não é permitida e impede a alteração.],
        )
      ),
      (
        condicao: "Funcionário cancela a edição",
        passo: "6",
        fluxo: (
          [6.1 - O sistema cancela o processo.],
        )
      ),
            (
        condicao: "Número de série inválido",
        passo: "8.2.1.3",
        fluxo: (
          [8.2.1.3.1 - O sistema informa o funcionário que o número de série deve ser um campo textual não vazio e impede a alteração.],
        )
      ),
      (
        condicao: "Tempo de garantia inválido",
        passo: "8.2.1.4",
        fluxo: (
          [8.2.1.4.1 - O sistema informa o funcionário que o tempo de garantia deve ser um número inteiro positivo (em meses) e impede a alteração.],
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
    pos_condicoes: "Uma nova ordem de serviço é registada no sistema com identificador único numérico, estado 'Pendente Diagnóstico' e data, hora e utilizador são registados.",
    fluxo_normal: (
      [O funcionário seleciona a opção de criar uma nova ordem de serviço.],
      [O funcionário introduz o identificador único, nome ou NIF do cliente.],
      [O sistema valida a existência do cliente.],
      [O funcionário seleciona o cliente.],
      [O funcionário seleciona a trotinete associada ao cliente.],
      [O funcionário preenche a descrição do problema.],
      [O funcionário não regista acessórios.],
      [O funcionário não adiciona fotografias.],
      [O funcionário confirma o registo.],
      [O sistema valida que a descrição do problema é um campo textual não vazio.],
      [O sistema cria a ordem de serviço, atribui um identificador único numérico, define o estado como 'Pendente Diagnóstico', regista a data, hora e utilizador da criação, calcula o tempo estimado para a conclusão da reparação e apresenta os dados da nova ordem de serviço e o tempo estimado.]
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
      (
        condicao: "O funcionário pretende registar o estado à entrada da trotinete",
        passo: "8",
        fluxo: (
          [8.1.1 - O funcionário anexa até 5 fotografias nos formatos PNG ou JPEG, cada uma com no máximo 5MB.],
          [8.1.2 - O sistema valida o formato e tamanho de cada fotografia.],
          [8.1.3 - Retorna ao passo 9 do fluxo normal.]
        )
      )
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
        condicao: "Formato ou tamanho de fotografia inválido",
        passo: "8.1.2",
        fluxo: (
          [8.1.2.1 - O sistema informa o funcionário que uma ou mais fotografias não cumprem os requisitos (formato PNG ou JPEG com, no máximo, 5MB) e impede o registo.],
        )
      ),
      (
        condicao: "Funcionário cancela o registo",
        passo: "9",
        fluxo: (
          [9.1 - O sistema cancela o processo.],
        )
      )
    )
)

#uc_spec(
    id: "31",
    nome: "Editar Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma ordem de serviço registada no sistema.",
    pos_condicoes: "Os dados da ordem de serviço são atualizados no sistema com os novos valores fornecidos, e data, hora, utilizador e estado são registados.",
    fluxo_normal: (
      [O funcionário seleciona a ordem de serviço que pretende editar.],
      [O sistema apresenta os dados atuais da ordem de serviço.],
      [O funcionário seleciona a opção de editar a ordem de serviço.],
      [O sistema verifica que o estado da OS não requer um fluxo de edição especial.],
      [O funcionário seleciona o ou os campos que pretende alterar e introduz os novos valores.],
      [O funcionário não altera acessórios.],
      [O funcionário não altera fotografias.],
      [O funcionário confirma as alterações.],
      [O sistema valida que a descrição do problema é um campo textual não vazio.],
      [O sistema atualiza os dados da ordem de serviço, regista a data, hora, utilizador e o estado em que ficou, e apresenta os dados atualizados.]
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
        condicao: "O funcionário pretende adicionar ou editar fotos do estado à entrada",
        passo: "7",
        fluxo: (
          [7.1 - O funcionário anexa ou substitui fotografias nos formatos PNG ou JPEG, no máximo 5, cada uma com até 5MB.],
          [7.2 - O sistema valida o formato e tamanho de cada fotografia.],
          [7.3 - Retorna ao passo 8 do fluxo normal.]
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
          [4.2.1.3 - Retorna ao passo 4.2.2 dos fluxos alternativos.]
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
          [4.2.2.6 - O sistema associa as peças à OS, regista a data, hora e utilizador que associou as peças e atualiza o valor total da OS.],
          [4.2.2.7 - Retorna ao passo 4.2.3 dos fluxos alternativos.]
        )
      ),
      (
        condicao: "O funcionário regista o pagamento",
        passo: "4.2.3",
        fluxo: (
          [4.2.3.1 - O sistema valida que não existem outras OS do mesmo cliente no estado "Pendente Pagamento" com data de notificação anterior à da OS atual.],
          [4.2.3.2 - O funcionário seleciona o método de pagamento: "Numerário", "Multibanco" ou "MBWay".],
          [4.2.3.3 - O funcionário confirma o registo do pagamento.],
          [4.2.3.4 - O sistema regista o pagamento e altera o estado da OS para "Paga".],
          [4.2.3.5 - Retorna ao passo 10 do fluxo normal.]
        )
      )
    ),
    fluxos_excecao: (
      (
        condicao: "O funcionário não confirma a aprovação do orçamento",
        passo: "4.1.2",
        fluxo: (
          [4.1.2.1 - O sistema altera o estado da ordem de serviço para 'Orçamento não aprovado', regista a data, hora e utilizador e impossibilita o uso dos dados da OS para estatísticas da empresa.],
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
        condicao: "Existem OS anteriores pendentes do mesmo cliente",
        passo: "4.2.3.2",
        fluxo: (
          [4.2.3.2.1 - O sistema informa que existem OS do mesmo cliente com data de notificação anterior que devem ser pagas primeiro e apresenta a lista das respetivas referências.],
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
        condicao: "Formato ou tamanho de fotografia inválido",
        passo: "7.2",
        fluxo: (
          [7.2.1 - O sistema informa o funcionário que uma ou mais fotografias não cumprem os requisitos (formato PNG ou JPEG com no máximo 5MB) e impede a atualização.],
        )
      ),
      (
        condicao: "Funcionário cancela a edição",
        passo: "8",
        fluxo: (
          [8.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "Descrição do problema inválida",
        passo: "9",
        fluxo: (
          [9.1 - O sistema informa o funcionário que a descrição do problema deve ser um campo textual não vazio e impede a atualização.],
        )
      ),
    )
)

#uc_spec(
    id: "32",
    nome: "Remover Ordem de Serviço",
    atores: "Secretária e Gerente",
    pre_condicoes: "O funcionário está autenticado e existe pelo menos uma ordem de serviço registada no sistema.",
    pos_condicoes: "A ordem de serviço muda para estado 'Eliminada' e não é possível usar os seus dados para estatísticas da empresa. Data, hora e utilizador da eliminação são registados.",
    fluxo_normal: (
      [O funcionário seleciona a ordem de serviço que pretende eliminar.],
      [O sistema apresenta os dados da ordem de serviço.],
      [O funcionário seleciona a opção de eliminar a ordem de serviço.],
      [O funcionário confirma a eliminação.],
      [O sistema altera o estado da ordem de serviço para 'Eliminada', impossibilita o uso dos dados da OS para estatísticas da empresa, e apresenta uma mensagem de confirmação de eliminação bem-sucedida.]
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
    pos_condicoes: "O diagnóstico é registado com a data, hora e funcionário que o realizou. A OS transita para o estado 'Pendente Reparação' ou 'Pendente aprovação do orçamento' consoante o valor total do orçamento.",
    fluxo_normal: (
      [O funcionário seleciona uma das OS que estão no estado "Pendente Diagnóstico".],
      [O funcionário consulta os dados completos da OS, incluindo os dados da trotinete, do cliente e o histórico de reparações anteriores da trotinete.],
      [O funcionário adiciona e remove as reparações necessárias da tabela de reparações, pesquisando por identificador ou nomenclatura.],
      [O funcionário pesquisa, adiciona e remove peças por referência do fornecedor ou identificador único, especificando as quantidades.],
      [O funcionário confirma o diagnóstico.],
      [O sistema valida que as quantidades são números inteiros positivos e não excedem o stock disponível.],
      [O sistema calcula o valor do orçamento e altera o estado da OS para "Pendente aprovação do orçamento" e gera um alerta com o identificador da OS e o seu estado para a secretária],
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
    nome: "Executar Reparação de Ordem de Serviço",
    atores: "Mecânico e Gerente",
    pre_condicoes: "O funcionário selecionou uma OS no estado 'Pendente Reparação' através do UC-37.",
    pos_condicoes: "As reparações executadas e peças utilizadas são registadas, as quantidades de peças são descontadas do stock com registo de data, hora e utilizador, o custo final é calculado e a OS transita para o estado 'Pendente Pagamento' com alerta gerado para a lista de alertas da secretária.",
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
      [O funcionário verifica todos os itens obrigatórios da checklist de segurança: Travões, Luzes, Pneus, Aceleração, Travagem, Visor e Teste de Condução.],
      [O funcionário confirma a conclusão da checklist.],
      [O funcionário confirma a conclusão da reparação.],
      [O sistema valida que todos os itens da checklist estão marcados como verificados.],
      [O sistema calcula o custo final da reparação, altera o estado da OS para "Pendente Pagamento", gera um alerta com o identificador da OS e o seu estado para a secretária e apresenta um resumo final da OS.]
    ),
    fluxos_alternativos: (
      (
        condicao: "O funcionário pretende reportar um defeito numa peça instalada",
        passo: "8",
        fluxo: (
          [8.1 - O funcionário seleciona a peça defeituosa da lista de peças utilizadas.],
          [8.2 - O funcionário indica que deseja reportar um defeito na peça.],
          [8.3 - O funcionário fornece uma descrição do defeito encontrado.],
          [8.4 - O funcionário confirma o reporte do defeito.],
          [8.5 - O sistema valida que existe uma peça igual disponível em stock e adiciona-a à lista de peças usadas e remove a peça defeituosa da lista de peças utilizadas.],
          [8.6 - O sistema cria um registo de devolução marcando a peça defeituosa como "Possível defeito", restitui ao stock a quantidade correspondente e gera um alerta com o identificador, referência e descrição do defeito das peças para o gestor de stock.],
          [8.7 - Retorna ao passo 9 do fluxo normal.]
        )
      ),),
    fluxos_excecao: (
      (
        condicao: "Uma peça prescrita no diagnóstico não está disponível em stock",
        passo: "2",
        fluxo: (
          [2.1.1 - O sistema informa o funcionário que a quantidade solicitada da peça prescrita não está disponível em stock.],
          [2.1.2 - O funcionário seleciona a opção de requisitar encomenda da peça indisponível.],
          [2.1.3 - O funcionário confirma a requisição.],
          [2.1.4 - O sistema altera o estado da OS para "A aguardar peças" e gera um alerta com o identificador e referência da peça para o do gestor de stock, gera um alerta com o identificador da OS e o seu estado para a secretária.],
        )
      ),
      (
        condicao: "O funcionário cancela a requisição da peça",
        passo: "2.1.3",
        fluxo: (
          [2.1.3.1 - O sistema cancela o processo.],
        )
      ),
      (
        condicao: "O funcionário pretende adicionar reparações adicionais",
        passo: "6",
        fluxo: (
          [6.1 - O funcionário adiciona e remove reparações adicionais da tabela de reparações, pesquisando por identificador ou nomenclatura.],
          [6.2 - O funcionário confirma as reparações adicionais.],
          [6.3 - O sistema coloca a OS no estado \"Pendente de aprovação do orçamento\" e gera um alerta novo para a secretária que contém o identificador da OS e o estado em que ela se encontra.],
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
        condicao: "O funcionário pretende adicionar peças adicionais",
        passo: "7",
        fluxo: (
          [7.1 - O funcionário pesquisa, adiciona e remove peças por referência do fornecedor ou identificador único, especificando as quantidades.],
          [7.2 - O funcionário confirma as peças adicionais.],
          [7.3 - O sistema valida que as quantidades são números inteiros positivos e não excedem o stock disponível.],
          [7.4 - O sistema coloca a OS no estado \"Pendente de aprovação do orçamento\" e gera um alerta novo para a secretária que contém o identificador da OS e o estado em que ela se encontra.],
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