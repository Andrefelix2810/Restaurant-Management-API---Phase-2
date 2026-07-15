# Roteiro do video - Restaurant Management API

Video: `tech-challenge-fase-2-demo.mp4`

Duracao total: 5 minutos e 18 segundos.

As marcacoes de tempo abaixo sao aproximadas. A secao "Narracao" reproduz o texto utilizado na geracao da voz do video.

## Cena 1 - Abertura

Tempo aproximado: 00:00 a 00:24

### Conteudo exibido

- Restaurant Management API.
- Tech Challenge - Fase 2.
- Spring Boot, PostgreSQL, Clean Architecture e Docker.

### Narracao

Olá. Neste vídeo será apresentada a Restaurant Management API, desenvolvida para o Tech Challenge da fase dois. A aplicação permite gerenciar tipos de usuário, usuários, restaurantes e itens de cardápio. Também serão demonstradas a organização em Clean Architecture, a documentação Swagger, a execução com Docker, os testes automatizados e a cobertura de código.

## Cena 2 - Objetivo da solucao

Tempo aproximado: 00:24 a 00:47

### Conteudo exibido

- Distinguir clientes e donos de restaurante.
- Cadastrar restaurantes vinculados a um dono.
- Gerenciar itens vendidos em cada restaurante.
- Executar aplicação e PostgreSQL de forma integrada.

### Narracao

O objetivo da solução é oferecer uma base compartilhada para a gestão de restaurantes. O sistema diferencia clientes e donos de restaurante por meio dos tipos de usuário. Cada restaurante é associado a um usuário responsável e cada item do cardápio é associado a um restaurante existente. Os recursos são expostos por uma API REST e persistidos em PostgreSQL.

## Cena 3 - Clean Architecture

Tempo aproximado: 00:47 a 01:23

### Conteudo exibido

- Domain: entidades e regras de negócio.
- Application: casos de uso e ports.
- Interface Adapters: controllers, presenters e gateways.
- Frameworks and Drivers: Spring, JPA, PostgreSQL e Swagger.
- Dependências apontando para o núcleo.

### Narracao

A estrutura foi organizada conforme Clean Architecture. No centro estão as entidades de domínio, responsáveis pelas regras e invariantes. A camada de aplicação contém os casos de uso, comandos e portas. Na borda de entrada estão os controllers, DTOs e presenters. Na borda de saída estão os adaptadores, mapeadores e repositórios JPA. Spring, Hibernate, Swagger e PostgreSQL permanecem na infraestrutura. As dependências apontam para o núcleo e quatro testes ArchUnit garantem automaticamente essa regra.

## Cena 4 - Execucao com Docker

Tempo aproximado: 01:23 a 01:52

### Conteudo exibido

- Container `restaurant_management_api` na porta 8080.
- Container `restaurant_postgres` na porta 5432.
- Ambos com status `HEALTHY`.
- Comando `docker compose up --build -d`.
- Resposta do endpoint `/health`.

### Narracao

A aplicação é executada pelo Docker Compose. Um container disponibiliza a API Java na porta oitenta e oitenta e outro executa o PostgreSQL dezesseis na porta cinquenta e quatro trinta e dois. O banco possui health check próprio e a aplicação só inicia depois que ele está saudável. O endpoint de health também é utilizado para validar que a API está disponível. Durante esta gravação, os dois containers estão em execução e com status saudável.

## Cena 5 - Swagger

Tempo aproximado: 01:52 a 02:19

### Conteudo exibido

- Captura real do Swagger UI.
- Restaurant Management API - Phase 2.
- Operações GET, POST, PUT e DELETE.

### Narracao

Com a aplicação em execução, a documentação pode ser acessada pelo Swagger UI. Ela apresenta os grupos de restaurantes, tipos de usuário, usuários, itens do cardápio e o health check. Em cada grupo estão disponíveis as operações de criação, consulta, atualização e exclusão. Os contratos de entrada, os modelos de resposta e os principais códigos de erro também aparecem na especificação OpenAPI.

## Cena 6 - Tipos de usuario e usuarios

Tempo aproximado: 02:19 a 02:47

### Conteudo exibido

- `POST /user-types`.
- `POST /users`.
- `GET /users/{id}`.
- `PUT /users/{id}`.
- `DELETE /users/{id}`.

### Narracao

O primeiro fluxo é o cadastro dos tipos de usuário. A API permite criar, listar, consultar, atualizar e remover um tipo, impedindo nomes repetidos. Depois, o usuário é criado informando nome, e-mail, login, senha, endereço e o identificador do tipo escolhido. E-mail e login são únicos. Um tipo associado a usuários não pode ser removido, evitando inconsistências de relacionamento no banco.

## Cena 7 - Restaurantes

Tempo aproximado: 02:47 a 03:12

### Conteudo exibido

- `POST /restaurants`.
- `GET /restaurants`.
- `GET /restaurants/{id}`.
- `PUT /restaurants/{id}`.
- `DELETE /restaurants/{id}`.

### Narracao

No cadastro do restaurante são informados nome, endereço, tipo de cozinha, horário de funcionamento e o usuário responsável. Antes de salvar, o caso de uso verifica se o usuário existe e se possui o tipo dono de restaurante. Um cliente comum não pode ser associado como proprietário. Além do CRUD completo, a resposta apresenta os dados essenciais do dono e seu tipo de usuário.

## Cena 8 - Itens do cardapio

Tempo aproximado: 03:12 a 03:39

### Conteudo exibido

- `POST /menu-items`.
- `GET /menu-items`.
- `GET /restaurants/{id}/menu-items`.
- `PUT /menu-items/{id}`.
- `DELETE /menu-items/{id}`.

### Narracao

Os itens do cardápio possuem nome, descrição, preço, indicador de disponibilidade apenas no restaurante, caminho da foto e restaurante relacionado. O nome e o restaurante são obrigatórios e o preço deve ser maior que zero. Além das operações tradicionais, existe uma consulta específica por restaurante. Assim, o consumidor pode obter somente os itens pertencentes ao estabelecimento selecionado.

## Cena 9 - Validacoes e erros

Tempo aproximado: 03:39 a 04:07

### Conteudo exibido

- `400 Bad Request`.
- `404 Not Found`.
- `409 Conflict`.
- `500 Internal Error`.

### Narracao

As validações existem tanto nos DTOs de entrada quanto nas entidades de domínio. Requisições inválidas retornam quatrocentos. Recursos inexistentes retornam quatrocentos e quatro. Violações de integridade retornam quatrocentos e nove. Erros inesperados retornam quinhentos sem expor detalhes internos. Todas as respostas de erro seguem o mesmo contrato, contendo data, status, categoria, mensagem e caminho solicitado.

## Cena 10 - Testes e cobertura

Tempo aproximado: 04:07 a 04:43

### Conteudo exibido

- 75 testes executados.
- Nenhuma falha ou erro.
- 91,24% de cobertura de linhas.
- Build Maven concluído com sucesso.
- Quatro regras ArchUnit aprovadas.

### Narracao

A validação automatizada é executada com Maven Verify. O projeto possui setenta e cinco testes, sem falhas, erros ou testes ignorados. Existem testes unitários dos casos de uso, testes das entidades, testes HTTP com MockMvc, testes do tratamento global de exceções e um fluxo de integração usando H dois. Esse fluxo cria tipo, usuário dono, restaurante e item, e valida as listagens. O JaCoCo registrou noventa e um vírgula vinte e quatro por cento de cobertura de linhas, acima dos oitenta por cento exigidos.

## Cena 11 - Documentacao e encerramento

Tempo aproximado: 04:43 a 05:18

### Conteudo exibido

- README com arquitetura, endpoints e instruções.
- Collection Postman com 23 requests.
- CRUDs completos.
- Swagger.
- Cobertura de 91,24%.
- Docker saudável.

### Narracao

O README apresenta a descrição do projeto, tecnologias, arquitetura, funcionalidades, endpoints e instruções para execução local, com Docker e para os testes. A collection Postman organiza o fluxo completo, armazena os identificadores criados e executa a limpeza respeitando as chaves estrangeiras. Com isso, a entrega reúne os CRUDs solicitados, documentação interativa, infraestrutura Docker, testes automatizados e separação arquitetural. Esta foi a demonstração da Restaurant Management API da fase dois.
