# Restaurant Management API

Backend desenvolvido com Spring Boot para gerenciamento de usuários de um sistema compartilhado de restaurantes.

---

## Objetivo da Fase 1

Entregar um backend robusto para gerenciamento de usuários, com foco em:

- cadastro de usuário
- atualização de usuário
- exclusão de usuário
- busca de usuários
- validação de dados
- persistência em banco relacional PostgreSQL
- execução com Docker e Docker Compose

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- Docker Compose
- Lombok

---

## Arquitetura do Projeto

O projeto foi organizado em camadas, seguindo boas práticas de separação de responsabilidades e princípios SOLID.

### Estrutura de pacotes

~~~text
src/main/java/com/restaurantsystem/restaurantmanagementapi
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── mapper
├── repository
├── service
│   └── impl
└── RestaurantManagementApiApplication.java
~~~

---

## Funcionalidades Implementadas

- Criar usuário
- Listar usuários
- Buscar usuário por ID
- Atualizar usuário
- Deletar usuário
- Validar campos obrigatórios
- Tratar usuário não encontrado
- Impedir duplicidade de email
- Impedir duplicidade de login

---

## Tipos de Usuário

- `CLIENT`
- `RESTAURANT_OWNER`

---

## Endpoints da API

| Método | Endpoint      | Descrição             |
|--------|---------------|-----------------------|
| POST   | `/users`      | Criar usuário         |
| GET    | `/users`      | Listar usuários       |
| GET    | `/users/{id}` | Buscar usuário por ID |
| PUT    | `/users/{id}` | Atualizar usuário     |
| DELETE | `/users/{id}` | Deletar usuário       |

---

## Exemplo de Requisição para Criar Usuário

~~~json
{
  "name": "Roberto Almeida",
  "email": "roberto.almeida@email.com",
  "login": "robertoalmeida",
  "password": "123456tarantula",
  "role": "CLIENT",
  "address": {
    "street": "Rua Odete",
    "number": "123",
    "neighborhood": "Vila Mariana",
    "city": "Sao Paulo",
    "state": "SP",
    "zipCode": "04444000",
    "complement": "Torre 3"
  }
}
~~~

---

## Configuração do Banco

A aplicação está configurada para usar PostgreSQL com os seguintes parâmetros:

- **Database:** `restaurant_db`
- **Username:** `postgres`
- **Password:** `postgres`
- **Porta padrão:** `5432`

### application.properties

~~~properties
spring.application.name=restaurant-management-api
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
~~~

---

## Como Executar Localmente

### Pré-requisitos

- Java 17
- Maven
- PostgreSQL

### Passos

1. Criar o banco `restaurant_db`
2. Configurar usuário e senha do PostgreSQL
3. Atualizar o `application.properties`, se necessário
4. Rodar a aplicação pela IntelliJ ou com Maven

### Executar com Maven

~~~bash
mvn spring-boot:run
~~~

---

## Como Executar com Docker Compose

~~~bash
docker compose up --build
~~~

---

## Respostas de Erro Tratadas

A API possui tratamento global de exceções para:

- usuário não encontrado
- erro de validação
- regra de negócio
- erro de duplicidade

---

## Status do Projeto

Sprint 1 praticamente concluída, contemplando backend funcional com persistência real em PostgreSQL.