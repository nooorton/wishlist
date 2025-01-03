# Wishlist API

Este projeto é uma API de lista de desejos (WishList), construída com **Spring Boot** e **MongoDB**. A API permite criar, atualizar, recuperar e excluir listas de desejos associadas a usuários, além de limitar o número de itens na lista a 20.

## Tecnologias Utilizadas

- **Java 17** (JDK 17)
- **Spring Boot**
- **MongoDB** (usado como banco de dados não relacional)
- **Docker** (para inicializar o MongoDB)
- **JUnit** (para testes unitarios)

## Estrutura do Projeto

O projeto contém as seguintes classes principais:

* **WishList**: Representa uma lista de desejos associada a um usuário, com uma lista de `WishItem`.
* **WishItem**: Representa um item na lista de desejos.
* **WishListController**: Controlador da API, com os endpoints REST para gerenciar as listas de desejos.
* **WishListRepository**: Interface responsável pela comunicação com o banco de dados. Extende `MongoRepository` do Spring Data MongoDB, fornecendo métodos para salvar, buscar e excluir listas de desejos.
* **WishListService**: Classe de serviço que contém a lógica de negócios para gerenciar as listas de desejos. Ela interage com o `WishListRepository` para realizar operações de CRUD (criar, ler, atualizar e excluir) nas listas de desejos.
* **WishListControllerTest**: Classe responsavel por executar os testes unitarios.
* **InvalidArgumentException**: Exceção personalizada lançada quando os dados enviados são inválidos (por exemplo, mais de 20 itens na lista).


## Execução do Projeto


### 1. **Configuração do Projeto Spring Boot**

1. Clone o repositório:

   ```bash
   git clone https://github.com/nooorton/wishlist.git
   cd wishlist-api
    ```
2. Certifique-se de ter o **Java 17**, **Docker** e o **Maven** configurados no seu sistema.
3. Para iniciar o **MongoDB** com **Docker**, execute o seguinte comando na raiz do projeto:
    ```bash
    docker-compose up
    ```
4. Execute o projeto com o **Maven**:
    ```bash
   mvn spring-boot:run
   ``` 

### 2. Acessando a API
Após iniciar o projeto, a API estará disponível em:
```bash
http://localhost:8080/api/wishlist
   ``` 


## Execução dos Testes

### 1. **Testes com Maven**

O projeto utiliza o **JUnit** para os testes. Para executar os testes da aplicação, basta usar o Maven. No terminal, execute o seguinte comando na raiz do projeto:

```bash
mvn test
```

## Endpoints da API

### 1. **GET /api/wishlist**

Recupera todas as listas de desejos.

- **Resposta**: Lista de objetos `WishList`.

#### Exemplo de resposta:
```json
[
  {
    "userId": 1,
    "items": [
      {
        "name": "Item 1",
        "description": "Descrição do Item 1"
      },
      {
        "name": "Item 2",
        "description": "Descrição do Item 2"
      }
    ]
  },
  {
    "userId": 2,
    "items": [
      {
        "name": "Item A",
        "description": "Descrição do Item A"
      }
    ]
  }
]
```

### 2. **GET /api/wishlist/{userId}**

Recupera a lista de desejos de um usuário específico pelo ID.

- **Parâmetros**:
  - `userId` (path variable): ID do usuário.

- **Resposta**: Objeto `WishList` do usuário especificado.


#### Exemplo de resposta:
- **Sucesso**:
```json
{
  "userId": 1,
  "items": [
    {
      "name": "Item 1",
      "description": "Descrição do Item 1"
    }
  ]
}
```
- **Erro**: Código de status 404 (Não encontrado).

### 3. **POST /api/wishlist**

Cria ou atualiza a lista de desejos de um usuário. A lista de itens não pode ter mais de 20 itens.

- **Corpo da Requisição**: Objeto `WishList`.

#### Exemplo de corpo da requisição:
```json
{
  "userId": 1,
  "items": [
    {
      "name": "Novo Item",
      "description": "Descrição do Novo Item"
    }
  ]
}
```

#### Exemplo da resposta:
- **Sucesso**:
```json
{
  "userId": 1,
  "items": [
    {
      "name": "Novo Item",
      "description": "Descrição do Novo Item"
    }
  ]
}
```
- **Erro**: Código de status 400 (Mais de 20 itens na lista).

### 4. **DELETE /api/wishlist/{userId}**

Exclui a lista de desejos de um usuário específico.

- **Parâmetros**:
  - `userId` (path variable): ID do usuário.

- **Resposta**: Código de status HTTP 204 (Sem conteúdo) se a lista foi excluída com sucesso, ou 404 (Não encontrado) se o usuário não tiver uma lista de desejos.

#### Exemplo de resposta:
- **Sucesso**: Código de status 204.
- **Erro**: Código de status 404 (Não encontrado).

