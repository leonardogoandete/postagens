# Testes Unitários para PostagemResource

## Introdução

Este documento descreve os testes unitários implementados para a classe `PostagemResource` no contexto de uma aplicação Quarkus. Esses testes visam garantir o correto comportamento das operações relacionadas às postagens, incluindo adição, busca, atualização e exclusão.

## Estrutura do Projeto

Os testes estão localizados no arquivo [PostagemResourceUnitTest.java](src/test/java/br/com/doasanguepoa/postagem/controller/PostagemResourceUnitTest.java).

## Testes Implementados

### 1. `listarPostagensComErroTest`

Este teste verifica a listagem de postagens com uma lista fictícia de `DadosListagemPostagemDTO`.

**Passos do Teste:**
- Criação de uma lista fictícia de `DadosListagemPostagemDTO`.
- Definição de duas postagens fictícias.
TODO: Completar os passos restantes do teste.

### 2. `buscarPostagemPorIdTest`

Este teste verifica a busca de uma postagem por ID.

**Passos do Teste:**
- Criação de uma postagem fictícia.
- Configuração do comportamento do `postagemRepository` para retornar a postagem fictícia ao chamar `findById(1L)`.
- Chamada do método `findById(1L)` no `postagemRepository`.
- Verificação se a postagem retornada possui o ID esperado.
- Verificação se o método `findById` foi chamado apenas uma vez.

### 3. `adicionarPostagemTest`

Este teste verifica a adição de uma nova postagem.

**Passos do Teste:**
- Criação de um objeto `DadosCadastroPostagemDTO` representando uma nova postagem.
- Chamada do método `adicionarPostagem` no `postagemResource` com o objeto de postagem.
- Verificação se o método `adicionarPostagem` foi chamado no `postagemResource`.

### 4. `adicionarPostagemComMensagemNulaTest`

Este teste verifica a adição de uma postagem com mensagem nula.

**Passos do Teste:**
- Criação de um objeto `DadosCadastroPostagemDTO` com mensagem nula.
- Chamada do método `adicionarPostagem` no `postagemResource` com o objeto de postagem nula.
- Verificação se o método `adicionarPostagem` foi chamado no `postagemResource`.
- Captura da exceção `WebApplicationException`.
- Verificação se o código de status da resposta é 400 (Bad Request).

### 5. `atualizarPostagemTest`

Este teste verifica a atualização de uma postagem existente.

**Passos do Teste:**
- Criação de uma postagem fictícia.
- Configuração do comportamento do `postagemRepository` para retornar a postagem fictícia ao chamar `findById(1L)`.
- Criação de um objeto `DadosAtualizacaoPostagemDTO` representando a atualização da postagem.
- Chamada do método `atualizarPostagem` no `postagemResource` com o objeto de atualização.
- Verificação se a mensagem da postagem foi atualizada no repositório.

### 6. `atualizarPostagemInexistenteTest`

Este teste verifica a atualização de uma postagem inexistente.

**Passos do Teste:**
- Criação de um objeto `DadosAtualizacaoPostagemDTO` representando a atualização da postagem inexistente.
- Chamada do método `atualizarPostagem` no `postagemResource` com o objeto de atualização.
- Captura da exceção `WebApplicationException`.
- Verificação se o código de status da resposta é 404 (Not Found).

### 7. `deletarPostagemInexistenteTest`

Este teste verifica a exclusão de uma postagem inexistente.

**Passos do Teste:**
- Chamada do método `deletarPostagem` no `postagemResource` com o ID de uma postagem inexistente.
- Captura da exceção `WebApplicationException`.
- Verificação se o código de status da resposta é 404 (Not Found).

### 8. `deletarPostagemTest`

Este teste verifica a exclusão de uma postagem existente.

**Passos do Teste:**
- Criação de uma postagem fictícia.
- Configuração do comportamento do `postagemRepository` para retornar a postagem fictícia ao chamar `findById(1L)`.
- Chamada do método `deletarPostagem` no `postagemResource` com o ID da postagem.
- Verificação se o método `deletarPostagem` foi chamado no `postagemResource`.

## Observações

- A anotação `@ExtendWith(MockitoExtension.class)` é utilizada para integrar o Mockito com o JUnit 5.
- A classe `PostagemResourceUnitTest` utiliza mocks para simular o comportamento dos objetos reais durante os testes.
- Os métodos são independentes e podem ser executados em qualquer ordem.
