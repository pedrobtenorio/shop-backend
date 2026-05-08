# Shop Backend

API REST educacional para um aplicativo de compras. O projeto simula um fluxo basico de e-commerce: cadastro e autenticacao de usuarios, gerenciamento de produtos por administradores, carrinho de compras e criacao/acompanhamento de pedidos.

O objetivo principal e estudar uma API backend com Spring Boot aplicando camadas bem separadas, autenticacao JWT, persistencia com JPA, migrations com Liquibase e validacao de entrada.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Liquibase
- MapStruct
- Lombok
- JWT com `jjwt`
- Maven

## Arquitetura

O projeto segue uma arquitetura em camadas:

- `controller`: expoe os endpoints HTTP e recebe DTOs de entrada.
- `service`: concentra regras de negocio e transacoes.
- `repository`: acesso ao banco via Spring Data JPA.
- `entity`: entidades JPA persistidas no PostgreSQL.
- `dto`: objetos usados nas requisicoes e respostas da API.
- `mapper`: conversoes entre DTOs e entidades usando MapStruct.
- `config`: configuracao de seguranca, JWT, CORS e filtros.

Essa separacao evita colocar regra de negocio no controller e mantem as entidades desacopladas do formato da API.

## Entidades

### User

Representa um usuario do sistema. Implementa `UserDetails` para integracao com Spring Security.

Campos principais:

- `id`
- `name`
- `email`
- `password`
- `address`
- `role`
- `deleted`

Papeis disponiveis:

- `USER`
- `ADMIN`

### Product

Representa um produto disponivel para compra.

Campos principais:

- `id`
- `name`
- `description`
- `price`
- `stockQuantity`
- `deleted`

### Cart

Representa o carrinho de compras de um usuario.

Relacionamentos:

- um carrinho pertence a um `User`;
- um carrinho possui varios `CartItem`.

### CartItem

Representa um produto dentro do carrinho.

Campos principais:

- `product`
- `quantity`
- `deleted`

### Order

Representa um pedido criado a partir do carrinho.

Campos principais:

- `user`
- `items`
- `status`
- `createdAt`
- `deleted`

Status disponiveis:

- `CREATED`
- `CONFIRMED`
- `CANCELED`
- `DELIVERED`

### OrderItem

Representa um item comprado dentro de um pedido.

Campos principais:

- `product`
- `quantity`
- `unitPrice`
- `deleted`

O campo `unitPrice` salva o preco do produto no momento da compra, evitando que alteracoes futuras no preco do produto modifiquem pedidos antigos.

## Funcionalidades

### Autenticacao

- Cadastro de usuario.
- Login com email e senha.
- Geracao de token JWT.
- Protecao das rotas usando `Authorization: Bearer <token>`.

Endpoints:

```http
POST /auth/register
POST /auth/login
```

### Produtos

Usuarios autenticados podem listar e consultar produtos.

Apenas usuarios `ADMIN` podem:

- criar produto;
- editar produto;
- alterar estoque;
- deletar produto logicamente.

Endpoints:

```http
GET    /products?page=0&size=10
GET    /products/{id}
POST   /products
PUT    /products/{id}
PATCH  /products/{id}/stock
DELETE /products/{id}
```

### Carrinho

Usuarios autenticados podem:

- consultar o proprio carrinho;
- adicionar produtos;
- alterar quantidade de item;
- remover item;
- limpar o carrinho.

O sistema valida estoque ao adicionar ou atualizar itens.

Endpoints:

```http
GET    /cart
POST   /cart/items
PATCH  /cart/items/{itemId}
DELETE /cart/items/{itemId}
DELETE /cart
```

### Pedidos

Usuarios autenticados podem:

- criar pedido a partir do carrinho;
- listar os proprios pedidos com paginacao;
- consultar detalhes de um pedido.

Ao criar um pedido:

- o sistema valida estoque novamente;
- copia os itens do carrinho para `OrderItem`;
- salva o preco atual em `unitPrice`;
- reduz o estoque dos produtos;
- limpa o carrinho logicamente.

Endpoints:

```http
POST /orders
GET  /orders?page=0&size=10
GET  /orders/{id}
```

## Seguranca

A seguranca usa JWT stateless:

- `JwtAuthenticationFilter` le o header `Authorization`.
- `JwtService` gera e valida tokens.
- `UserAuthService` cuida de cadastro e autenticacao.
- `SecurityConfig` define permissoes, CORS, `PasswordEncoder` e a `SecurityFilterChain`.

Regras principais:

- `/auth/**` e publico.
- `OPTIONS /**` e publico para CORS.
- Escrita em `/products` exige `ADMIN`.
- Demais rotas exigem usuario autenticado.

## Banco de Dados

O projeto usa PostgreSQL e Liquibase.

O changelog principal esta em:

```text
src/main/resources/db/changelog/db.changelog-master.yaml
```

O script inicial esta em:

```text
src/main/resources/db/changelog/changes/001-create-shop-tables.sql
```

Configuracoes esperadas:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
```

Tambem e necessario definir:

```properties
jwt.secret=${JWT_SECRET}
```

## Fluxo de Teste

Fluxo basico do MVP:

1. Criar um usuario em `/auth/register`.
2. Promover um usuario para `ADMIN` no banco, se necessario.
3. Fazer login em `/auth/login`.
4. Criar produtos com token de admin.
5. Fazer login com usuario comum.
6. Listar produtos.
7. Adicionar produtos ao carrinho.
8. Criar pedido com `POST /orders`.
9. Consultar pedidos com `GET /orders`.

Ha uma collection Postman importavel em:

```text
shop-api.postman_collection.json
```

## Observacoes de Projeto

- A delecao e logica usando o campo `deleted`.
- IDs usam `UUID`.
- DTOs de entrada usam validacao com Jakarta Validation, como `@NotBlank`, `@NotNull`, `@Positive` e `@PositiveOrZero`.
- MapStruct e usado para evitar mapeamentos manuais repetitivos.
- O `PasswordEncoder` fica no service, nao no mapper, porque criptografia de senha e regra de seguranca.
- Produtos com estoque `0` nao podem ser comprados, pois o estoque e validado no carrinho e novamente no checkout.
