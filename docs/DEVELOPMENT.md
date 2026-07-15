# Guia de desenvolvimento

## Pré-requisitos

- JDK 17 ou superior.
- Docker e Docker Compose, para o fluxo recomendado.
- PostgreSQL 16, caso a aplicação seja executada sem Docker.

Não é necessário instalar Maven: o repositório inclui o Maven Wrapper.

## Execução com Docker

Suba a API e o PostgreSQL:

```bash
docker compose up --build -d
docker compose ps
```

Acompanhe os logs da aplicação:

```bash
docker compose logs -f app
```

Encerre os containers preservando o banco:

```bash
docker compose down
```

Para reiniciar o banco do zero, remova também o volume. Esse comando apaga todos os dados locais desse ambiente:

```bash
docker compose down -v
```

## Execução local

Com um PostgreSQL disponível, execute:

```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux ou macOS
./mvnw spring-boot:run
```

Configuração padrão:

| Propriedade | Valor padrão | Variável de ambiente |
|---|---|---|
| URL JDBC | `jdbc:postgresql://localhost:5432/restaurant_db` | `SPRING_DATASOURCE_URL` |
| Usuário | `postgres` | `SPRING_DATASOURCE_USERNAME` |
| Senha | `postgres` | `SPRING_DATASOURCE_PASSWORD` |
| Estratégia DDL | `update` | `SPRING_JPA_HIBERNATE_DDL_AUTO` |
| Exibição de SQL | `true` | `SPRING_JPA_SHOW_SQL` |

Os valores padrão são adequados apenas ao desenvolvimento. Utilize segredos e configurações externas em ambientes compartilhados ou produtivos.

## Testes e qualidade

Execute a verificação completa antes de enviar alterações:

```bash
# Windows
./mvnw.cmd clean verify

# Linux ou macOS
./mvnw clean verify
```

A suíte contém:

- testes unitários dos casos de uso e entidades;
- testes HTTP com MockMvc;
- teste de integração do fluxo principal com H2;
- testes de arquitetura com ArchUnit;
- medição de cobertura com JaCoCo.

O build falha se a cobertura de linhas considerada pelo JaCoCo ficar abaixo de 80%. O relatório HTML é criado em `target/site/jacoco/index.html`.

## Banco de dados

O arquivo `src/main/resources/schema.sql` é executado após a inicialização do Hibernate. Ele mantém o catálogo de tipos, trata compatibilidade com dados legados, cria índices e inclui os dados de demonstração quando a tabela de usuários está vazia.

Como o script contém comandos específicos do PostgreSQL, alterações devem ser validadas tanto com Docker Compose quanto com o perfil de teste, que usa configuração própria em `src/test/resources/application-test.properties`.

## Documentação da API

Com a aplicação ativa:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Health check: `http://localhost:8080/health`

Ao alterar endpoints ou DTOs, atualize as anotações OpenAPI, o [guia da API](API.md) e a collection Postman.

## Checklist de alteração

1. Preserve a direção das dependências entre as camadas.
2. Cubra a regra alterada no nível apropriado.
3. Execute `clean verify` e confirme o limite de cobertura.
4. Valide o contrato no Swagger UI quando houver mudança HTTP.
5. Atualize documentação e collection Postman quando o comportamento público mudar.
