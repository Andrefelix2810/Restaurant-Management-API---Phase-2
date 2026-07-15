# Guia da API

Este documento apresenta o contrato funcional da Restaurant Management API. Para explorar schemas e executar requisições pelo navegador, utilize o Swagger UI em `http://localhost:8080/swagger-ui/index.html`.

## Convenções

- URL base local: `http://localhost:8080`.
- Corpos de requisição e resposta utilizam JSON.
- Operações de criação retornam `201 Created`.
- Operações de consulta e atualização retornam `200 OK`.
- Exclusões bem-sucedidas retornam `204 No Content`.
- Listagens retornam um array JSON, inclusive quando não há registros.

## Endpoints

### Disponibilidade

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/health` | Verifica a disponibilidade da aplicação. |

### Tipos de usuário

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/user-types` | Cadastra um tipo permitido. |
| `GET` | `/user-types` | Lista o catálogo de tipos. |
| `GET` | `/user-types/{id}` | Busca um tipo por ID. |
| `PUT` | `/user-types/{id}` | Atualiza um tipo. |
| `DELETE` | `/user-types/{id}` | Exclui um tipo que não esteja em uso. |

O catálogo aceita apenas `CLIENTE` e `DONO_RESTAURANTE`. Em uma base vazia, ambos são criados nos IDs `1` e `2`, respectivamente. Esses registros devem ser reutilizados por todos os usuários; não é necessário criar um tipo por pessoa.

```json
{
  "name": "CLIENTE"
}
```

### Usuários

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/users` | Cria um usuário. |
| `GET` | `/users` | Lista usuários. |
| `GET` | `/users/{id}` | Busca um usuário por ID. |
| `PUT` | `/users/{id}` | Substitui os dados de um usuário. |
| `DELETE` | `/users/{id}` | Exclui um usuário. |

Payload de criação e atualização:

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "login": "maria.silva",
  "password": "123456",
  "userTypeId": 1,
  "address": {
    "street": "Rua Central",
    "number": "100",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01001000",
    "complement": "Apto. 12"
  }
}
```

E-mail e login devem ser únicos, a senha deve possuir pelo menos seis caracteres e `userTypeId` deve apontar para um tipo existente.

### Restaurantes

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/restaurants` | Cria um restaurante. |
| `GET` | `/restaurants` | Lista restaurantes. |
| `GET` | `/restaurants/{id}` | Busca um restaurante por ID. |
| `PUT` | `/restaurants/{id}` | Substitui os dados de um restaurante. |
| `DELETE` | `/restaurants/{id}` | Exclui um restaurante. |

Payload de criação e atualização:

```json
{
  "name": "Restaurante Sabor Brasil",
  "address": "Rua das Flores, 123 - São Paulo/SP",
  "cuisineType": "Brasileira",
  "openingHours": "Segunda a sábado, das 11h às 23h",
  "ownerId": 2
}
```

Todos os campos são obrigatórios. `ownerId` deve apontar para um usuário existente vinculado ao tipo `DONO_RESTAURANTE`. Um restaurante com itens de cardápio vinculados não pode ser excluído antes desses itens.

### Itens de cardápio

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/menu-items` | Cria um item de cardápio. |
| `GET` | `/menu-items` | Lista todos os itens. |
| `GET` | `/menu-items/{id}` | Busca um item por ID. |
| `PUT` | `/menu-items/{id}` | Substitui os dados de um item. |
| `DELETE` | `/menu-items/{id}` | Exclui um item. |
| `GET` | `/restaurants/{restaurantId}/menu-items` | Lista os itens de um restaurante. |

Payload de criação e atualização:

```json
{
  "name": "Feijoada",
  "description": "Feijoada completa com acompanhamentos",
  "price": 39.90,
  "availableOnlyInRestaurant": true,
  "photoPath": "/images/feijoada.jpg",
  "restaurantId": 1
}
```

`name`, `price` e `restaurantId` são obrigatórios. O preço deve ser maior que zero e o restaurante deve existir. `photoPath` armazena apenas uma referência textual; a API não realiza upload de arquivos.

## Contrato de erros

Erros seguem uma estrutura única:

```json
{
  "timestamp": "2026-07-14T20:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Restaurant owner must have user type DONO_RESTAURANTE",
  "path": "/restaurants"
}
```

| Status | Quando ocorre |
|---|---|
| `400 Bad Request` | JSON inválido, falha de validação ou violação de regra de negócio. |
| `404 Not Found` | Recurso ou rota inexistente. |
| `409 Conflict` | Violação de integridade referencial no banco. |
| `500 Internal Server Error` | Falha inesperada; detalhes internos não são expostos ao cliente. |

## Dados iniciais

Uma base vazia recebe os seguintes registros para facilitar a execução local:

| Entidade | ID | Identificação | Credencial |
|---|---:|---|---|
| Tipo de usuário | 1 | `CLIENTE` | — |
| Tipo de usuário | 2 | `DONO_RESTAURANTE` | — |
| Usuário | 1 | `cliente@exemplo.com` / `cliente` | `123456` |
| Usuário | 2 | `dono@exemplo.com` / `dono` | `123456` |

Essas credenciais são dados de demonstração e não devem ser utilizadas em produção. Restaurantes e itens de cardápio iniciam vazios.

## Postman

Importe `postman/Restaurant-Management-API-Phase-2.postman_collection.json` e confirme a variável `baseUrl` como `http://localhost:8080`. A collection contém validações automáticas e organiza a limpeza respeitando as chaves estrangeiras.
