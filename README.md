# LI4 (Laboratórios de Informática IV) (Português)

Projeto de grupo desenvolvido no âmbito da UC de LI4. O projeto consiste no desenvolvimento do sistema de gestão para a empresa EcoRide Solutions — uma aplicação web de gestão de uma oficina de reparação de trotinetes elétricas, com suporte a ordens de serviço, stock, financeiro e notificações.
Pode consultar o [enunciado](enunciado.pdf) do projeto e o respetivos relaótios: [relatório](relatorios/relatorio.pdf), [Guia de Manutenção](relatorios/Guião%20Manutenção%20EcoRide.pdf) e [Guia de Utilização](relatorios/Guião%20Utilização%20EcoRide.pdf).

## Membros do grupo:

* [darteescar](https://github.com/darteescar)
* [luis7788](https://github.com/luis7788)
* [tiagofigueiredo7](https://github.com/tiagofigueiredo7)
* [inesferribeiro](https://github.com/inesferribeiro)

### Nota Final: ?? / 20 ⭐️

![alt text](dashboard.png)

## Dependências

Para poder correr o sistema é necessário ter o [Docker](https://docs.docker.com/get-docker/) e o [Docker Compose](https://docs.docker.com/compose/install/) instalados.

## Arranque

Na diretoria `code/`, execute:

```bash
docker compose up --build
```

Após o arranque, os seguintes serviços ficam disponíveis:

- **Interface** → http://localhost:3000
- **API directa** → http://localhost:7000
- **Swagger UI** → http://localhost:8081

### Credenciais de acesso

| Identificador | Password   | Cargo        |
|---------------|------------|--------------|
| `admin`       | `admin123` | Gerente      |

### Parar o sistema

```bash
docker compose down
```

Para parar e apagar todos os dados (reset completo):

```bash
docker compose down -v
```


# LI4 (Laboratórios de Informática IV) (English)

Group project developed within the scope of the LI4 course. The project consists of the development of a management system for the company EcoRide Solutions — a web application for managing an electric scooter repair workshop, with support for work orders, stock, financials and notifications.
You can consult the [statement](enunciado.pdf) of the project and the respective reports: [report](relatorios/relatorio.pdf), [Maintenance Guide](relatorios/Guião%20Manutenção%20EcoRide.pdf) and [User Guide](relatorios/Guião%20Utilização%20EcoRide.pdf).

## Group members:

* [darteescar](https://github.com/darteescar)
* [luis7788](https://github.com/luis7788)
* [tiagofigueiredo7](https://github.com/tiagofigueiredo7)
* [inesferribeiro](https://github.com/inesferribeiro)

### Final Grade: ?? / 20 ⭐️

## Dependencies

To run the system you need to have [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed.

## Starting the system

In the `code/` directory, run:

```bash
docker compose up --build
```

Once started, the following services are available:

- **Interface** → http://localhost:3000
- **Direct API** → http://localhost:7000
- **Swagger UI** → http://localhost:8081

### Login credentials

| Identifier | Password   | Role    |
|------------|------------|---------|
| `admin`    | `admin123` | Manager |

### Stopping the system

```bash
docker compose down
```

To stop and delete all data (full reset):

```bash
docker compose down -v
```
