# Restaurant Management API - Phase 2

API Spring Boot para a Fase 2 do Tech Challenge Restaurant Management. O projeto evolui a base da Sprint 1, a funcionalidade de Tipo de Usuario da Sprint 2 e, nesta Sprint 3, adiciona o Cadastro de Restaurante com dono associado a um usuario existente.

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

## Funcionalidades Implementadas

- Health check da API.
- CRUD de tipos de usuario em `/user-types`.
- CRUD de usuarios em `/users`.
- Associacao `User` muitos-para-um com `UserType`.
- CRUD de restaurantes em `/restaurants`.
- Associacao `Restaurant` muitos-para-um com `User`.
- Validacao de que o dono do restaurante existe.
- Validacao de que o dono do restaurante possui `UserType` com nome `DONO_RESTAURANTE`.
- Tratamento global para validacao, entidade nao encontrada, regra de negocio e integridade de dados.

## Sprint 2 - Tipo de Usuario

O tipo de usuario permite distinguir perfis como `CLIENTE` e `DONO_RESTAURANTE`.

Regras:

- `name` e obrigatorio.
- `name` nao pode ser vazio.
- `name` deve ter entre 2 e 80 caracteres.
- `name` deve ser unico sem diferenciar maiusculas de minusculas.
- Um tipo de usuario em uso por usuarios nao pode ser removido.

## Sprint 3 - Cadastro de Restaurante

O cadastro de restaurante permite registrar os dados operacionais do restaurante e associar um dono.

Campos:

- `name`
- `address`
- `cuisineType`
- `openingHours`
- `ownerId`

Regras:

- Todos os campos sao obrigatorios.
- `ownerId` deve referenciar um usuario existente.
- O usuario dono deve possuir tipo de usuario `DONO_RESTAURANTE`.
- Controllers retornam DTOs, nao entidades JPA.
- A resposta do dono nao expoe senha nem dados sensiveis.

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

| Metodo | Endpoint              | Descricao                                  |
|--------|-----------------------|--------------------------------------------|
| GET    | `/health`             | Health check                               |
| POST   | `/user-types`         | Criar tipo de usuario                      |
| GET    | `/user-types`         | Listar tipos de usuario                    |
| GET    | `/user-types/{id}`    | Buscar tipo de usuario por ID              |
| PUT    | `/user-types/{id}`    | Atualizar nome do tipo de usuario          |
| DELETE | `/user-types/{id}`    | Deletar tipo se nao estiver em uso         |
| POST   | `/users`              | Criar usuario com `userTypeId`             |
| GET    | `/users`              | Listar usuarios                            |
| GET    | `/users/{id}`         | Buscar usuario por ID                      |
| PUT    | `/users/{id}`         | Atualizar usuario, inclusive `userTypeId`  |
| DELETE | `/users/{id}`         | Deletar usuario                            |
| POST   | `/restaurants`        | Criar restaurante com `ownerId`            |
| GET    | `/restaurants`        | Listar restaurantes                        |
| GET    | `/restaurants/{id}`   | Buscar restaurante por ID                  |
| PUT    | `/restaurants/{id}`   | Atualizar restaurante, inclusive `ownerId` |
| DELETE | `/restaurants/{id}`   | Deletar restaurante                        |

## Exemplos - Tipos de Usuario

Criar tipo de usuario:

```http
POST /user-types
Content-Type: application/json
```

```json
{
  "name": "DONO_RESTAURANTE"
}
```

Resposta:

```json
{
  "id": 1,
  "name": "DONO_RESTAURANTE"
}
```

## Exemplos - Usuarios

Criar usuario dono de restaurante:

```http
POST /users
Content-Type: application/json
```

```json
{
  "name": "Joao Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "123456",
  "userTypeId": 1,
  "address": {
    "street": "Rua Central",
    "number": "100",
    "neighborhood": "Centro",
    "city": "Sao Paulo",
    "state": "SP",
    "zipCode": "01001000",
    "complement": "Sala 12"
  }
}
```

## Exemplos - Restaurantes

Criar restaurante:

```http
POST /restaurants
Content-Type: application/json
```

```json
{
  "name": "Restaurante Sabor Brasil",
  "address": "Rua das Flores, 123 - Sao Paulo/SP",
  "cuisineType": "Brasileira",
  "openingHours": "Segunda a sabado, das 11h as 23h",
  "ownerId": 1
}
```

Resposta:

```json
{
  "id": 1,
  "name": "Restaurante Sabor Brasil",
  "address": "Rua das Flores, 123 - Sao Paulo/SP",
  "cuisineType": "Brasileira",
  "openingHours": "Segunda a sabado, das 11h as 23h",
  "owner": {
    "id": 1,
    "name": "Joao Silva",
    "email": "joao@email.com",
    "userType": {
      "id": 1,
      "name": "DONO_RESTAURANTE"
    }
  }
}
```

Atualizar restaurante:

```http
PUT /restaurants/1
Content-Type: application/json
```

```json
{
  "name": "Restaurante Sabor Brasil Unidade Centro",
  "address": "Rua das Flores, 456 - Sao Paulo/SP",
  "cuisineType": "Brasileira",
  "openingHours": "Todos os dias, das 11h as 23h",
  "ownerId": 1
}
```

## Fluxo Minimo no Postman

1. `POST /user-types` com `DONO_RESTAURANTE`.
2. `POST /users` usando o `userTypeId` criado.
3. `POST /restaurants` usando o `ownerId` do usuario criado.
4. `GET /restaurants/{id}`.
5. `PUT /restaurants/{id}`.
6. `DELETE /restaurants/{id}`.

## Regras de Erro

- `404 Not Found`: usuario, tipo de usuario ou restaurante inexistente.
- `400 Bad Request`: campos invalidos, nome duplicado, e-mail/login duplicado, owner sem tipo `DONO_RESTAURANTE` ou tentativa de deletar tipo de usuario em uso.
- `409 Conflict`: violacao de integridade do banco.

Formato padrao:

```json
{
  "timestamp": "2026-07-12T20:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Restaurant owner must have user type DONO_RESTAURANTE",
  "path": "/restaurants"
}
```

## Banco de Dados

O projeto usa `spring.jpa.hibernate.ddl-auto=update`. O arquivo `src/main/resources/schema.sql` complementa a inicializacao das tabelas.

Modelo principal:

- `user_types`: `id`, `name`
- `users`: coluna `user_type_id`
- `restaurants`: `id`, `name`, `address`, `cuisine_type`, `opening_hours`, `owner_id`

## Postman

A collection esta em:

```text
postman/Restaurant-Management-API-Phase-2.postman_collection.json
```

Ela contem requests para health check, CRUD de tipos de usuario, CRUD de usuarios e CRUD de restaurantes.

## Testes

Executar todos os testes:

```bash
mvn clean test
```

Cobertura atual:

- `UserTypeServiceImplTest`: regras principais de tipo de usuario.
- `UserServiceImplTest`: criacao de usuario com tipo existente e erro quando `userTypeId` nao existe.
- `RestaurantServiceImplTest`: CRUD de restaurante, owner inexistente e owner com tipo invalido.
- `UserTypeControllerTest`: endpoints principais de `/user-types`.
- `RestaurantControllerTest`: endpoints principais de `/restaurants`.
