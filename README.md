# Restaurant Management API - Phase 2

API Spring Boot para a Fase 2 do Tech Challenge Restaurant Management. Esta sprint implementa o cadastro e gerenciamento de Tipos de Usuario e permite associar cada usuario existente a um tipo, como `CLIENTE` ou `DONO_RESTAURANTE`.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Lombok
- Springdoc OpenAPI / Swagger
- Maven
- Docker e Docker Compose
- JUnit 5, Mockito e MockMvc

## Arquitetura

O projeto usa organizacao em camadas, mantendo controllers, DTOs, services, repositories, mappers, entidades e exceptions separados.

```text
src/main/java/com/restaurantsystem/restaurantmanagementapi
|-- application/service
|-- domain/entity
|-- domain/exception
|-- infrastructure/persistence
|-- mapper
`-- presentation
    |-- controller
    `-- dto
        |-- request
        `-- response
```

## Sprint 2 - Tipo de Usuario

Funcionalidades implementadas:

- CRUD REST de tipos de usuario em `/user-types`.
- Campo obrigatorio `name`, validado como nao vazio e entre 2 e 80 caracteres.
- Nome unico, validado sem diferenciar maiusculas de minusculas.
- Associacao `User` muitos-para-um com `UserType`.
- Criacao e atualizacao de usuario com `userTypeId`.
- Resposta de usuario contendo o tipo associado em `userType`.
- Bloqueio de exclusao de tipo de usuario quando houver usuarios associados.
- Tratamento global para validacao, entidade nao encontrada e erros de negocio.

## Como Rodar com Docker

```bash
docker compose up --build
```

Servicos expostos:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`
- Banco: `restaurant_db`
- Usuario: `postgres`
- Senha: `postgres`

Health check:

```bash
curl http://localhost:8080/health
```

## Como Rodar Localmente

Suba um PostgreSQL local ou use apenas o servico de banco do Docker Compose. A aplicacao usa estes valores padrao:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Executar a aplicacao:

```bash
mvn spring-boot:run
```

Executar os testes:

```bash
mvn clean test
```

## Swagger

Com a aplicacao rodando:

```text
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

| Metodo | Endpoint           | Descricao                                 |
|--------|--------------------|-------------------------------------------|
| GET    | `/health`          | Health check                              |
| POST   | `/user-types`      | Criar tipo de usuario                     |
| GET    | `/user-types`      | Listar tipos de usuario                   |
| GET    | `/user-types/{id}` | Buscar tipo de usuario por ID             |
| PUT    | `/user-types/{id}` | Atualizar nome do tipo de usuario         |
| DELETE | `/user-types/{id}` | Deletar tipo se nao estiver em uso        |
| POST   | `/users`           | Criar usuario com `userTypeId`            |
| GET    | `/users`           | Listar usuarios                           |
| GET    | `/users/{id}`      | Buscar usuario por ID                     |
| PUT    | `/users/{id}`      | Atualizar usuario, inclusive `userTypeId` |
| DELETE | `/users/{id}`      | Deletar usuario                           |

## Exemplos

Criar tipo de usuario:

```http
POST /user-types
Content-Type: application/json
```

```json
{
  "name": "CLIENTE"
}
```

Resposta:

```json
{
  "id": 1,
  "name": "CLIENTE"
}
```

Atualizar tipo de usuario:

```http
PUT /user-types/1
Content-Type: application/json
```

```json
{
  "name": "DONO_RESTAURANTE"
}
```

Criar usuario associado a um tipo:

```http
POST /users
Content-Type: application/json
```

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "login": "mariasilva",
  "password": "123456",
  "userTypeId": 1,
  "address": {
    "street": "Rua Central",
    "number": "100",
    "neighborhood": "Centro",
    "city": "Sao Paulo",
    "state": "SP",
    "zipCode": "01001000",
    "complement": "Apto 12"
  }
}
```

Resposta de usuario:

```json
{
  "id": 1,
  "name": "Maria Silva",
  "email": "maria@email.com",
  "login": "mariasilva",
  "lastModifiedDate": "2026-07-12T20:00:00",
  "userType": {
    "id": 1,
    "name": "CLIENTE"
  },
  "address": {
    "street": "Rua Central",
    "number": "100",
    "neighborhood": "Centro",
    "city": "Sao Paulo",
    "state": "SP",
    "zipCode": "01001000",
    "complement": "Apto 12"
  }
}
```

## Regras de Erro

- `404 Not Found`: tipo de usuario ou usuario inexistente.
- `400 Bad Request`: nome invalido, nome duplicado, e-mail/login duplicado ou tentativa de deletar tipo em uso.
- `409 Conflict`: violacao de integridade do banco.

Formato padrao:

```json
{
  "timestamp": "2026-07-12T20:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User type already registered",
  "path": "/user-types"
}
```

## Banco de Dados

O projeto usa `spring.jpa.hibernate.ddl-auto=update`. O arquivo `src/main/resources/schema.sql` complementa a inicializacao criando `user_types` quando necessario e adicionando `user_type_id` em `users`.

Modelo principal:

- `user_types`: `id`, `name`
- `users`: coluna `user_type_id` associada ao tipo de usuario

## Postman

A collection esta em:

```text
postman/Restaurant-Management-API-Phase-2.postman_collection.json
```

Ela contem requests para health check, CRUD de tipos de usuario e criacao/atualizacao de usuario com `userTypeId`.

## Testes

Cobertura adicionada:

- `UserTypeServiceImplTest`: criacao, duplicidade, listagem, busca por ID, atualizacao, nao encontrado, exclusao e bloqueio de exclusao em uso.
- `UserServiceImplTest`: criacao de usuario com `UserType` existente e erro quando `userTypeId` nao existe.
- `UserTypeControllerTest`: endpoints principais de `/user-types` com MockMvc.
