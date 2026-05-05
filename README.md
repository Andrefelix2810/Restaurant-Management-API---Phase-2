# Restaurant Management API

API REST em Spring Boot para gestão de usuários e operações de restaurantes, desenvolvida para o **Tech Challenge - Fase 1** e evoluída com entidades de operação (restaurantes, mesas, cardápio e pedidos).

## 1. Contexto do Tech Challenge (PDF)

Conforme o documento **"Postech - ADJ - Fase 1 - Tech Challenge"**, o foco principal da Fase 1 é:

- backend robusto em Spring Boot
- gerenciamento de usuários
- execução com Docker Compose
- persistência em banco relacional
- documentação e organização do projeto

## 2. Aderência ao que o PDF pediu

### 2.1 Requisitos funcionais da Fase 1 (usuários)

| Item solicitado no PDF | Status atual |
|---|---|
| Cadastro de usuário | Implementado |
| Alteração de usuário | Implementado |
| Exclusão de usuário | Implementado |
| Campos de usuário (nome, email, login, senha, data última alteração, endereço) | Implementado |
| Validação de login | **Pendente** (não há endpoint dedicado de autenticação/login neste estado) |
| Troca de senha | **Pendente** (não há endpoint dedicado de troca de senha neste estado) |

### 2.2 Entregáveis gerais da Fase 1

| Entregável do PDF | Status atual |
|---|---|
| Funcionalidade de backend | Atendido para usuários + módulos de operação |
| Qualidade/organização de código | Estrutura em camadas implementada |
| Documentação do projeto | Este README + Swagger |
| Collection de testes | Arquivo disponível em `restaurant-management-api/postman/Restaurant-API-Local.postman_collection.json` |
| Docker Compose (app + banco) | Implementado |
| Repositório de código | Aplicável ao repositório atual |

## 3. Escopo implementado hoje (estado atual real da API)

Além do escopo de usuário da Fase 1, o projeto já possui módulos da operação:

- `Restaurant` (CRUD)
- `Table` por restaurante (criação, listagem, alteração de status)
- `MenuItem` por restaurante (CRUD)
- `Order` por restaurante/mesa (abertura e consulta)
- tratamento global de exceções com payload padronizado
- documentação OpenAPI/Swagger ativa

## 4. Tecnologias

- Java 17
- Spring Boot 4.0.5
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Lombok
- springdoc-openapi (Swagger UI)
- Maven
- Docker / Docker Compose

## 5. Arquitetura

```text
src/main/java/com/restaurantsystem/restaurantmanagementapi
├── config
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
```

## 6. Regras de negócio já aplicadas

- impede duplicidade de `email` e `login` em usuário
- impede número de mesa duplicado no mesmo restaurante
- impede vincular mesa/item/pedido a restaurante diferente
- valida preço de item (`> 0`)
- status válidos de mesa: `AVAILABLE`, `OCCUPIED`, `RESERVED`, `CLEANING`
- status inicial do pedido: `OPEN`

## 7. Como executar

## Opção A - Local

Pré-requisitos:

- Java 17
- Maven
- PostgreSQL disponível

Passos:

1. Criar o banco `restaurant_db`.
2. Ajustar credenciais no `application.properties` ou variáveis de ambiente.
3. Executar:

```bash
mvn spring-boot:run
```

## Opção B - Docker Compose

```bash
docker compose up --build
```

Serviços:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

## 8. Configuração de banco (atual)

Arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=restaurant-management-api
server.port=8080

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/restaurant_db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}

spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:true}
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 9. Healthcheck e documentação

- Healthcheck: `GET /health`
- OpenAPI JSON: `GET /v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 10. Endpoints disponíveis atualmente

### 10.1 Users

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/users` | Criar usuário |
| GET | `/users` | Listar usuários |
| GET | `/users/{id}` | Buscar usuário por ID |
| PUT | `/users/{id}` | Atualizar usuário |
| DELETE | `/users/{id}` | Remover usuário |

### 10.2 Restaurants

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/restaurants` | Criar restaurante |
| GET | `/restaurants` | Listar restaurantes |
| GET | `/restaurants/{id}` | Buscar restaurante por ID |
| PUT | `/restaurants/{id}` | Atualizar restaurante |
| DELETE | `/restaurants/{id}` | Remover restaurante |

### 10.3 Tables

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/restaurants/{restaurantId}/tables` | Criar mesa |
| GET | `/restaurants/{restaurantId}/tables` | Listar mesas por restaurante |
| PATCH | `/restaurants/{restaurantId}/tables/{tableId}/status` | Alterar status da mesa |

### 10.4 Menu Items

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/restaurants/{restaurantId}/menu-items` | Criar item |
| GET | `/restaurants/{restaurantId}/menu-items` | Listar itens |
| PUT | `/restaurants/{restaurantId}/menu-items/{menuItemId}` | Atualizar item |
| DELETE | `/restaurants/{restaurantId}/menu-items/{menuItemId}` | Remover item |

### 10.5 Orders

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/restaurants/{restaurantId}/orders` | Abrir pedido |
| GET | `/restaurants/{restaurantId}/orders/{orderId}` | Consultar pedido |
| GET | `/restaurants/{restaurantId}/orders/table/{tableId}` | Listar pedidos por mesa |

## 11. Exemplos rápidos de payload

### Criar usuário

```json
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
```

### Criar restaurante

```json
{
  "name": "Restaurante A",
  "description": "Matriz",
  "phone": "11999990001"
}
```

### Criar mesa

```json
{
  "tableNumber": 1,
  "seats": 4,
  "status": "AVAILABLE"
}
```

### Criar item de cardápio

```json
{
  "name": "Burger Artesanal",
  "description": "Pao brioche, carne 180g, queijo",
  "price": 29.90,
  "available": true
}
```

### Abrir pedido

```json
{
  "tableId": 1,
  "notes": "Sem cebola"
}
```

## 12. Formato padronizado de erro

```json
{
  "timestamp": "2026-03-30T23:35:32.144021202",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant with id 999 not found",
  "path": "/restaurants/999"
}
```

## 13. Roteiro sugerido para apresentação

1. Mostrar `GET /health`.
2. Mostrar Swagger (`/swagger-ui/index.html`).
3. Criar usuário com `POST /users`.
4. Criar restaurante com `POST /restaurants`.
5. Criar mesa no restaurante.
6. Criar item de cardápio.
7. Abrir pedido e consultar por ID.
8. Forçar erro de recurso inexistente para mostrar padrão de erro.
9. Encerrar com tabela de aderência da Fase 1 (itens concluídos x pendentes).

## 14. Pontos pendentes para fechamento total da Fase 1 (segundo o PDF)

- endpoint de validação de login
- endpoint de troca de senha

Esses itens não impedem a demonstração do backend atual, mas devem ser adicionados para aderência completa ao texto da Fase 1.
