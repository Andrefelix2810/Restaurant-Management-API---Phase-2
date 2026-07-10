# Restaurant Management API - Phase 2

API Spring Boot para a Fase 2 do Tech Challenge Restaurant Management. Nesta Sprint 1, o projeto cria a fundacao tecnica para evoluir o sistema de restaurantes, mantendo o foco em usuarios e tipos de usuario.

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
- JUnit 5 e Mockito

## Arquitetura

O projeto usa uma organizacao em camadas inspirada em Clean Architecture, mantendo uma estrutura simples para entrega academica.

```text
src/main/java/com/restaurantsystem/restaurantmanagementapi
|-- application
|   `-- service
|       `-- impl
|-- domain
|   |-- entity
|   |-- enums
|   `-- exception
|-- infrastructure
|   |-- config
|   `-- persistence
|-- mapper
`-- presentation
    |-- controller
    `-- dto
        |-- request
        `-- response
```

## Funcionalidades da Sprint 1

- Health check da API
- CRUD de tipos de usuario
- CRUD de usuarios
- Relacionamento `User` muitos-para-um com `UserType`
- Endereco como `@Embeddable`
- Validacao de dados de entrada
- Bloqueio de e-mail duplicado
- Bloqueio de login duplicado
- Tratamento global de erros
- Swagger/OpenAPI
- Testes unitarios de regras de service

Restaurant e MenuItem nao foram implementados nesta Sprint. A base esta preparada para esses dominios entrarem na Sprint 2.

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

Resposta esperada:

```json
{
  "application": "Restaurant Management API - Phase 2",
  "status": "UP"
}
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

Gerar o pacote:

```bash
mvn clean package
```

## Swagger

Com a aplicacao rodando:

```text
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

| Metodo | Endpoint           | Descricao                |
|--------|--------------------|--------------------------|
| GET    | `/health`          | Health check             |
| POST   | `/user-types`      | Criar tipo de usuario    |
| GET    | `/user-types`      | Listar tipos de usuario  |
| GET    | `/user-types/{id}` | Buscar tipo por ID       |
| PUT    | `/user-types/{id}` | Atualizar tipo           |
| DELETE | `/user-types/{id}` | Inativar tipo            |
| POST   | `/users`           | Criar usuario            |
| GET    | `/users`           | Listar usuarios          |
| GET    | `/users/{id}`      | Buscar usuario por ID    |
| PUT    | `/users/{id}`      | Atualizar usuario        |
| DELETE | `/users/{id}`      | Deletar usuario          |

## Exemplos JSON

Criar `UserType`:

```json
{
  "name": "CUSTOMER",
  "description": "Restaurant customer"
}
```

Criar `User`:

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

## Fluxo Minimo de Teste

1. `GET /health`
2. `POST /user-types`
3. `GET /user-types`
4. `POST /users` usando o `userTypeId`
5. `GET /users`

Uma collection Postman esta disponivel em:

```text
postman/Restaurant-Management-API-Phase-2.postman_collection.json
```

## Tratamento de Erros

O retorno padrao contem:

```json
{
  "timestamp": "2026-07-06T19:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already registered",
  "path": "/users"
}
```

Excecoes tratadas:

- `ResourceNotFoundException`
- `BusinessException`
- `MethodArgumentNotValidException`
- `DataIntegrityViolationException`
- `Exception`

## Testes

Executar todos os testes:

```bash
mvn test
```

Cobertura basica desta Sprint:

- Criar `UserType` com sucesso
- Impedir `UserType` duplicado
- Criar `User` com `UserType` existente
- Impedir usuario com e-mail duplicado
- Impedir usuario com login duplicado

## Proximas Sprints

- Sprint 2: dominios de restaurantes e itens de cardapio
- Sprint 3: regras operacionais de restaurante, cardapio e disponibilidade
- Sprint 4: autenticacao, autorizacao e refinamentos de seguranca
