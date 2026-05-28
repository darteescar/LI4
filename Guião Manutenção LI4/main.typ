#import "resources/report.typ" as report

#show: report.styling.with(
    hasFooter: false
)

#report.index()

#import "@preview/diagraph:0.3.1" as dgraph

= Introdução


== Introdução

O presente guia destina-se a técnicos e programadores responsáveis pela manutenção,
operação e evolução do sistema *EcoRide* — uma aplicação de gestão de oficina de
trotinetes elétricas. O objetivo deste documento é fornecer uma referência completa
e estruturada para quem necessite de colocar o sistema em funcionamento, realizar
alterações ao código, resolver problemas operacionais ou estender as funcionalidades
existentes, sem necessidade de ler a totalidade do código-fonte.

O guia encontra-se organizado de forma progressiva: começa pela visão global da
arquitetura, desce até aos detalhes de configuração e base de dados, aborda os
mecanismos de segurança, explica a _API_ _REST_ e o _frontend_, descreve a estratégia
de testes, e termina com procedimentos de extensão e resolução de problemas. Cada
capítulo é autónomo mas a leitura sequencial é recomendada para quem se familiarize com o sistema
pela primeira vez.

Os principais conceitos abordados ao longo do documento incluem: a *arquitetura em
três camadas* (_CA_ → _LN_ → _CD_), a organização em *8 subsistemas* com `Facades`
independentes, a *containerização com _Docker_*, a *cifragem de dados pessoais* com
_AES_-256-_GCM_, o sistema de *autenticação por token de sessão*, a *especificação
_OpenAPI_* e os *testes unitários automatizados* com _JUnit_ 5.

== Visão Geral da Arquitetura

O EcoRide segue uma arquitetura em três camadas horizontais, claramente separadas
por pacotes Java e por responsabilidade:

- *_CA_ — Camada de Apresentação*: implementada com a _framework_ _Javalin 6_, expõe
  uma _API_ _REST_ na porta 7000 e serve o _frontend_ React através de _nginx_ na
  porta 3000. Todos os _endpoints_ estão agrupados em controllers por subsistema,
  registados em `EcoRideAPI`.

- *_LN_ — Lógica de Negócio*: organizada em 8 subsistemas, cada um com a sua
  interface e respetivo `facade` (`SAutenticacaoFacade`, `SClientesFacade`,
  `SFuncionariosFacade`, `SStockFacade`, `SOrdensServicoFacade`,
  `SReparacoesFacade`, `SNotificacoesFacade`, `SFinanceiroFacade`). O ponto de
  entrada unificado é o `EcoRideController`, que implementa `IEcoRideController` e
  agrega todos `facades` dos subssistemas.

- *_CD_ — Camada de Dados*: composta por _DAOs_ (_Data Access Objects_) que
  implementam a interface padrão `Map<Integer, Entidade>` do Java. Cada _DAO_ é um
  _singleton_ que comunica com a base de dados *_MySQL_ 8.4* via _JDBC_, através da
  classe `ConnectionFactory`.

A comunicação entre camadas é feita de forma unidirecional: a _CA_
conhece a _LN_ (através da interface `IEcoRideController`), e a _LN_ conhece a _CD_
(através das interfaces dos _DAOs_). A _CD_ não conhece nenhuma das camadas superiores.

As tecnologias utilizadas no projeto são:

#figure(
  caption: "Tecnologias usadas no projeto",
  kind: table,
    table(
    columns: (auto, auto),
    align: (left, left),
    table.header([*Componente*], [*Tecnologia*]),
    [Servidor _API_],        [Java 21 + Javalin 6],
    [Base de dados],         [MySQL 8.4],
    [_Frontend_],            [React + TypeScript + Vite + Tailwind CSS],
    [Servidor _web_],        [nginx (alpine)],
    [Containerização],       [Docker + Docker Compose],
    [Testes unitários],      [JUnit 5 + Maven Surefire 3.2.5],
  )
)

#figure(
  image("codesnapEcoRideController.png", width: 100%),
  caption: [Uso dos subsistemas em EcoRideController]
)


== Pré-requisitos e Ambiente

Antes de colocar o sistema em funcionamento, é necessário garantir que o ambiente
de execução cumpre os requisitos mínimos listados abaixo.

=== Requisitos para execução em Docker (recomendado)

- *Docker* versão 24 ou superior
- *Docker Compose* versão 2.20 ou superior

Não é necessário instalar Java, Node.js ou MySQL na máquina anfitriã pois todas as dependências são resolvidas dentro dos containers.

=== Requisitos para desenvolvimento local

- *Java 21* (_JDK_, para compilar e executar o backend diretamente)
- *Maven 3.9* (ou utilizar o _wrapper_ incluído no projeto)
- *Node.js 22* e *npm* (para o _frontend_)
- *MySQL 8.4* acessível localmente, com uma base de dados chamada `EcoRide` criada

=== Variáveis de ambiente

O sistema é configurado inteiramente através de variáveis de ambiente, definidas no
ficheiro `docker-compose.yml`. As variáveis obrigatórias para o serviço `backend` são:

#table(
  columns: (auto, auto, auto),
  align: (left, left, left),
  table.header([*Variável*], [*Valor por omissão*], [*Descrição*]),
  [`DB_HOST`],        [`mysql`],          [Nome do host da base de dados],
  [`DB_PORT`],        [`3306`],           [Porto _TCP_ do _MySQL_],
  [`DB_NAME`],        [`EcoRide`],        [Nome da base de dados],
  [`DB_USER`],        [`ecoride`],        [Utilizador _MySQL_],
  [`DB_PASS`],        [`EcoRide123!`],    [Password do utilizador _MySQL_],
  [`ENCRYPTION_KEY`], [—],                [Chave _AES_-256-_GCM_ em Base64 (obrigatória)],
  [`DEV_MODE`],       [`false`],          [Desliga autenticação se `"true"` (só desenvolvimento)],
)

*Atenção*: a variável `ENCRYPTION_KEY` é crítica. A sua ausência impede o arranque
correto do sistema, e a sua alteração após dados já inseridos torna os campos cifrados
ilegíveis. Consultar a @seccao5 para mais detalhes.

#figure(
  image("codesnapConnectionFactory.png", width: 100%),
  caption: [Classe ConnectionFactory]
)

== Arranque e Paragem do Sistema

O sistema é inteiramente gerido pelo Docker Compose, que coordena 5 serviços declarados no ficheiro `docker-compose.yml`, na raiz do projeto.

=== Primeira execução

Para construir as imagens e iniciar todos os serviços pela primeira vez, terá de se executar o seguinte comando na diretoria `code/`:

#align(center)[
```bash
docker compose up --build
```]

Na primeira execução, o Docker irá:

1. Construir a imagem do backend , compilando usando o Maven em dois estágios: primeiro, descarrega dependências e compila o _JAR_ e depois copia-o para uma imagem de runtime _JRE_ 21;
2. Construir a imagem do frontend, instalando as dependências _npm_, e executa `npm run build` com Vite, e copia os ficheiros estáticos para uma imagem nginx;
3. Iniciar o serviço `mysql`, aguardar que o `healthcheck` confirme que está
   pronto a aceitar ligações;
4. Iniciar o `backend`, que ao arrancar executa `DBInitializer.run()` — aplica
   `schema.sql` (criação das tabelas) e `seed.sql` (dados iniciais), se existirem;
5. Iniciar o `frontend` e o `swagger` após o backend estar saudável;
6. Executar o serviço `info` que é um contentor de curta duração que imprime os _URLs_ de acesso no terminal).

Após o arranque completo, os serviços ficam acessíveis nos seguintes endereços:

#figure(
  caption: "Endereços de acesso aos serviços",
  kind: table,
    table(
    columns: (120pt, 165pt),
    align: (left, left),
    table.header([*Serviço*], [*URL*]),
    [Interface _web_],     [`http://localhost:3000`],
    [_API_ direta],        [`http://localhost:7000`],
    [Swagger _UI_],        [`http://localhost:8081`],
    [Especificação _OpenAPI_], [`http://localhost:3000/api-docs`],
  )
)

=== Execuções subsequentes

Após a primeira build, basta executar:

#align(center)[
```bash
docker compose up
```]

Para reconstruir as imagens depois de alterações ao código:

#align(center)[
```bash
docker compose up --build
```]

=== Paragem

Para parar os serviços preservando os dados da base de dados:

#align(center)[
```bash
docker compose down
```]

Para parar e *eliminar completamente os dados* (volume `mysql_data`):

#align(center)[
```bash
docker compose down -v
```]

*Atenção*: o comando `down -v` é irreversível para os dados em `mysql_data`. Deverá ser usado apenas quando se pretende um ambiente completamente limpo ou se necessitar de recriar o esquema a partir do zero.

== Base de Dados

A base de dados utilizada é _MySQL_ 8.4, gerida pelo serviço `mysql` do Docker
Compose. O esquema é declarado inteiramente no ficheiro
`app/src/main/resources/schema.sql`, que é executado automaticamente no arranque
da aplicação pela classe `DBInitializer`.

=== Inicialização automática

A classe `DBInitializer` é invocada na primeira linha do método `main` de
`EcoRideAPI`. Ela lê `schema.sql` e `seed.sql` do _classpath_, remove comentários,
divide as instruções pelo delimitador `;` e executa-as sequencialmente. Dado que
todas as tabelas utilizam `CREATE TABLE IF NOT EXISTS`, o script é idempotente , já que pode ser executado múltiplas vezes sem erros nem perda de dados.

#figure(
  image("codesnapDBInitializer.png", width: 120%),
  caption: [Classe DBInitializer]
)


=== Estrutura do esquema
O esquema organiza 26 tabelas pelos 8 subsistemas. Os padrões de modelação mais
relevantes são:

- *_Class Table Inheritance_*: usado em `MovimentoFinanceiro` e `Notificacao`. Existe
  uma tabela base com os campos comuns e uma tabela filha por subtipo
  (`MovimentoFuncionario`, `MovimentoReparacao`, `MovimentoPeca`;
  `NotificacaoOS`, `NotificacaoStock`). A _fk_ das tabelas filhas aponta para a base
  com `ON DELETE CASCADE`, pelo que apagar a linha base remove automaticamente a
  linha filha.

- *Tabelas de junção para coleções*: listas como `OrdemServico_Acessorio`,
  `Diagnostico_PecaOrcamento`, `Conserto_PecaUsada` e `Encomenda_EntradaStock`
  armazenam os elementos de listas Java (`List<String>`, `Map<Integer, Integer>`)
  com uma coluna `ordem` para preservar a posição.

- *Chaves primárias compostas*: em tabelas como `Diagnostico` e `Conserto`, o `idOS`
  serve simultaneamente como _PK_ e _FK_, expressando a cardinalidade 0..1 com
  `OrdemServico`.


=== Acesso direto à base de dados

Para aceder ao _MySQL_ em modo de manutenção enquanto o sistema está a correr, pode correr:

#align(center)[
```
docker compose exec mysql mysql -uecoride -pEcoRide123! EcoRide
```]

A partir daqui, estará na _bash_ do _container_ da base de dados e poderá executar comandos _sql_ de acordo como bem necessitar.

== Cifragem de Dados Sensíveis <seccao5>

Dado que a aplicação armazena dados pessoais de funcionários (nomes, documentos de identificação, dados financeiros e de morada), foi implementada uma camada de cifragem para os campos mais sensíveis da tabela `Funcionario`.

=== Campos cifrados

Os seguintes campos são cifrados antes de serem escritos na base de dados e
decifrados aquando da leitura:

- `nome`
- `telemovel`
- `email`
- `data_nascimento`
- `NISS`
- `NIF`
- `NUS`
- `IBAN`
- `salario_hora`
- `salario_liquido`
-  `salario_bruto`
- `numero_porta`
- `rua`
- `localidade`
- `codigo_postal`

Na tabela `Funcionario` do `schema.sql`, estes campos são declarados como
`VARCHAR` com comprimento aumentado (100–400 caracteres) para acomodar o texto
cifrado em _Base64_, que é consideravelmente mais longo que o valor original.

=== Algoritmo: _AES_-256-_GCM_

A cifragem é implementada pela classe utilitária `CifraUtil`, que utiliza o algoritmo
*_AES_-256-_GCM_* (_Advanced Encryption Standard_ com modo _Galois/Counter Mode_).
Este modo garante simultaneamente *confidencialidade* e *integridade* dos dados, e, assim, qualquer adulteração do texto cifrado é detetada na decifragem.

Cada operação de cifragem gera um *_IV_ (_Initialization Vector_) aleatório* de 12 bytes, que é concatenado com o texto cifrado antes da codificação em _Base64_. Isto garante que o mesmo valor em claro produz sempre um texto cifrado diferente.

#image("codesnapCifrarEDecifrar.png")

#figure(
  image("codesnapCifrarEDecifrar.png", width: 120%),
  caption: [Métodos de cifragem e decifragem]
)

=== Gestão da chave de cifragem

A chave é fornecida ao sistema através da variável de ambiente `ENCRYPTION_KEY`,
no formato _Base64_ de 32 bytes (256 bits). Para gerar uma nova chave:

#align(center)[
```
openssl rand -base64 32
```]

*É, no entanto, relevante mencionar que* a chave de cifragem *não pode ser alterada* depois de existirem dados na base de dados, sem proceder a uma migração prévia que decifire todos os registos com a chave antiga e que os volte a cifrar com a nova. A alteração da chave sem migração tornaria todos os dados pessoais de funcionários permanentemente ilegíveis.

== Segurança e Autenticação

A segurança do sistema assenta em dois pilares: o controlo de acesso por token de sessão e a autorização por cargo. Ambos são implementados na camada _CA_, antes de qualquer lógica de negócio ser executada.

=== Middleware de autenticação

Em `EcoRideAPI`, é registado um filtro global `app.before("/api/*", ...)` que
interceta todos os pedidos para rotas protegidas. Em *modo de produção*
(`DEV_MODE=false`), o filtro:

1. Lê o cabeçalho `Authorization` do pedido;
2. Valida o token no `GestorSessoes`;
3. Se válido, injeta o objeto `SessaoUtilizador` no contexto do pedido (`ctx.attribute("sessao", sessao)`);
4. Se inválido ou ausente, devolve `HTTP 401 Unauthorized`.

Em modo de desenvolvimento (`DEV_MODE=true`), o filtro injeta automaticamente uma sessão de `Gerente` em todos os pedidos, dispensando a autenticação. Este modo nunca deve ser ativado em produção.

#image("codesnapMiddleware.png")

#figure(
  image("codesnapMiddleware.png", width: 120%),
  caption: [Middleware de autenticação]
)

=== Cargos e permissões

Existem 4 cargos no sistema, definidos no enum `Cargo`:

#figure(
  caption: "Cargos e responsabilidades",
  kind: table,
    table(
    columns: (auto, 170pt),
    align: (left, left),
    table.header([*Cargo*], [*Responsabilidades principais*]),
  [`Gerente`],      [Acesso total; gestão de funcionários, utilizadores e financeiro],
  [`GestorStock`],  [Gestão de stock, peças, fornecedores e encomendas],
  [`Secretaria`],   [Registo de clientes, trotinetes e ordens de serviço],
  [`Mecanico`],     [Diagnóstico, conserto e transições de estado das _OS_],
  )
)

Cada controller verifica o cargo da sessão injetada antes de executar operações
sensíveis, devolvendo `HTTP 403 Forbidden` em caso de acesso não autorizado.

=== Sessões

O `GestorSessoes` mantém um mapa em memória de tokens ativos. Os tokens são gerados no _endpoint_ de autenticação (`POST /auth/login`) e invalidados em (`POST /auth/logout`). 

*Nota de manutenção*: dado que as sessões são armazenadas em memória, o reinício do serviço `backend` invalida todas as sessões ativas, assim, os utilizadores terão de autenticar-se novamente.

== _API_ _REST_

O backend expõe uma _API_ _REST_ completa na porta 7000, documentada em formato *_OpenAPI_ 3* no ficheiro `EcoRideCA/openapi.yaml`. A especificação interativa está disponível em `http://localhost:8081` (Swagger _UI_) e a especificação bruta em `http://localhost:3000/api-docs`.

=== Estrutura de rotas

As rotas seguem a convenção `/<prefixo>/<recurso>`, agrupadas por subsistema:

#figure(
  caption: "Rotas disponibilizadas para interações com os subsistemas",
  kind: table,
    table(
      columns: (150pt, 180pt),
      align: (left, left),
      table.header([*Prefixo*], [*Subsistema*]),
      [`/auth/`],              [Autenticação (login/logout)],
      [`/api/clientes`],       [Clientes e trotinetes],
      [`/api/funcionarios`],   [Funcionários],
      [`/api/users`],          [Utilizadores do sistema],
      [`/api/stock`],          [Peças, stock, fornecedores, encomendas, defeitos],
      [`/api/ordens-servico`], [Ordens de serviço],
      [`/api/reparacoes`],     [Catálogo de reparações],
      [`/api/notificacoes`],   [Notificações],
      [`/api/financeiro`],     [Movimentos financeiros e análise],
    )
)

=== Tratamento de erros

Os erros de negócio são encapsulados na classe `EcoRideException` (exceção não verificada). O `GlobalExceptionHandler`, registado em `EcoRideAPI`, interceta estas exceções e devolve respostas _JSON_ estruturadas com o código _HTTP_ adequado (`400 Bad Request` para erros de validação, `404 Not Found` para recursos inexistentes, `409 Conflict` para conflitos de dados).

=== Adicionar um novo _endpoint_

Para expor uma nova operação na _API_, dever-se-á:

1. Adicionar o método à interface `IEcoRideController` e implementá-lo em `EcoRideController`;
2. Delegar na fachada do subsistema correspondente;
3. Criar ou editar o controller em `app/IEcoRideLN/controllers/<subsistema>/`;
4. Registar o controller em `EcoRideAPI.main()`;
5. Documentar o novo _endpoint_ em `openapi.yaml`.

== 8. _Frontend_

O _frontend_ é uma aplicação de página única (_SPA_) desenvolvida em React com TypeScript, construída com Vite e estilizada com Tailwind _CSS_. Em produção, é servida pelo nginx, que atua simultaneamente como servidor de ficheiros estáticos e como proxy reverso para o `backend`.

=== Configuração do nginx

O ficheiro `EcoRideCA/nginx.conf` define o comportamento do nginx:

- Rotas `/api/*` e `/auth/*` são reencaminhadas para `http://backend:7000`;
- A directiva `resolver 127.0.0.11 valid=30s ipv6=off` instrui o nginx a usar o
  _DNS_ interno do Docker, resolvendo o nome `backend` por pedido em vez de no
  arranque (evita falhas se o `backend` não estiver ainda disponível);
- Todas as restantes rotas devolvem `index.html` (necessário para o React Router
  funcionar em navegação direta).

#figure(
  image("codesnapNingx.png", width: 100%),
  caption: [Configuração do nginx]
)
  
=== Build local do _frontend_

Para compilar o _frontend_ fora do Docker poderá:

#align(center)[
```
cd code/EcoRideCA
npm ci
npm run build     # produz a pasta dist/
npm run dev       # servidor de desenvolvimento na porta 5173
```]

=== Processo de _build_ em Docker

O `Dockerfile` do _frontend_ usa um _build_ em dois estágios: o primeiro estágio usa a imagem `node:22-alpine` para compilar a aplicação; o segundo copia apenas os ficheiros estáticos resultantes para uma imagem `nginx:alpine`, sem incluir o Node.js nem as dependências de desenvolvimento na imagem final.

== Execução de Testes

A estratégia de testes do sistema baseia-se em testes unitários automatizados, executados com a _framework_ _JUnit_ 5 através do _plugin_ Maven `maven-surefire` 3.2.5. Os testes não requerem base de dados nem Docker — cada _DAO_ real é substituído por uma subclasse anónima em memória (ver @seccao92). Assim, poderam-se obter testes rápidos, simples e eficazes sem mudar a lógica de negócio ou a estrutura interna do _facade_ responsável por ela.

=== Executar os testes

Para executar a totalidade dos testes:

#align(center)[
  
```
cd code/app
mvn test
```
]

O resultado é apresentado por classe de teste, com um sumário final dos testes corridos e aqueles que: sucederam, falharam, lançaram erros ou foram ignorados.

O ponto de entrada unificado é a classe `EcoRideTestSuite`, anotada com `@Suite` e
`@SelectClasses`, que agrega todas as classes de teste.

=== Padrão de _DAO_ falso em memória <seccao92>

Dado que todos os _DAOs_ implementam `Map<Integer, Entidade>`, é possível criar
subclasses anónimas que sobrepõem todos os métodos com implementações baseadas em
`HashMap`, sem qualquer ligação à base de dados. O auto-incremento de _IDs_ é simulado com `AtomicInteger`.

#figure(
  image("codesnapExemploDAO.png", width: 120%),
  caption: [Exemplo de DAO usado em testes]
)

=== Adicionar testes a um subsistema existente

Para adicionar novos casos de teste deverá:

1. Abrir a classe de teste correspondente ao subsistema (ex: `Teste11.java`);
2. Adicionar um método anotado com `@Test`, `@Order(n)` e `@DisplayName("...")`;
3. Usar `assertThrows` para casos de exceção esperada e `assertEquals`/`assertTrue`
   para verificações de estado;
4. Os novos testes serão automaticamente incluídos na próxima execução de `mvn test`, não sendo necessário alterar a `EcoRideTestSuite`.

== Adição de um Novo Subsistema

Quando for necessário adicionar um subsistema completamente novo ao *EcoRide*,
deverão ser seguidos os seguintes passos, pela ordem indicada:

1. *Entidade _LN_*: criar a classe da entidade em `app/ecoRideLN/s<Nome>/`, com
   construtores, _getters_ e _setters_.

2. *_DAO_*: criar a classe `<Nome>DAO` em `app/ecoRideCD/s<Nome>/`, implementando
   `Map<Integer, <Nome>>`. O construtor deverá ser `protected` (para permitir
   subclasses em testes) e deverá implementar os métodos de domínio específicos (`insert`,
   métodos de pesquisa, etc.).

3. *Interface e Fachada _LN_*: criar `IS<Nome>` com os métodos de negócio e
   `S<Nome>Facade` que os implementa. Adicionar um construtor de injeção
   `public S<Nome>Facade(<Nome>DAO dao)` para suporte a testes.

4. *Integrar no `EcoRideController`*: adicionar o campo `IS<Nome>`, instanciá-lo
   no construtor e delegar os novos métodos na interface `IEcoRideController`.

5. *Controller _CA_*: criar `<Nome>Controller` em
   `app/IEcoRideLN/controllers/<nome>/`, com os _endpoints_ _REST_ pretendidos.
   Registar com `new <Nome>Controller(facade).register(app)` em `EcoRideAPI`.

6. *Esquema _SQL_*: adicionar as novas tabelas a `schema.sql`, no final do ficheiro,
   com `CREATE TABLE IF NOT EXISTS`. Respeitar as dependências de chaves estrangeiras.

7. *Testes*: criar `Teste<Nome>.java` em
   `app/src/test/java/app/ecoRideLN/`, seguindo o padrão dos ficheiros existentes.
   Adicionar a classe à anotação `@SelectClasses` de `EcoRideTestSuite`.

8. *Documentação _API_*: adicionar os novos _endpoints_ ao ficheiro `openapi.yaml`.

== Monitorização e Logs

Em ambiente Docker, todos os logs são acessíveis através do Docker Compose.

=== Visualizar logs em tempo real

Para seguir os logs de todos os serviços em simultâneo pode executar:
#align(center)[
```
docker compose logs -f
```
]

Para um serviço específico:

#align(center)[
```
docker compose logs -f backend
docker compose logs -f mysql
docker compose logs -f frontend
```
]

=== Mensagens de arranque relevantes

O serviço `backend` emite as seguintes mensagens durante o arranque, úteis para
diagnóstico:

- `[DBInitializer] Aviso ao executar schema.sql: ...` — indica que o script _SQL_
  falhou numa instrução. Normalmente inofensivo se o esquema já existia.
- `[EcoRide] DEV_MODE activo — autenticação desligada` — confirma que o modo de
  desenvolvimento está ativo. Não deve aparecer em produção.
- Ausência de mensagens de erro do Javalin na porta 7000 indica arranque bem
  sucedido.

=== Estado dos containers

Para verificar o estado e saúde de todos os containers pode correr:

#align(center)[
```
docker compose ps
```
]

O campo `STATUS` deverá indicar `running (healthy)` para os serviços `mysql` e
`backend`. O serviço `info` deverá aparecer como `exited (0)` após imprimir os
_URLs_ de acesso — este comportamento é esperado.
