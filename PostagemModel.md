# Testes para PostagemModel

Este documento descreve os testes implementados para a classe `Postagem` no contexto de uma aplicação Quarkus.

## Estrutura do Projeto
Os testes estão localizados no arquivo [PostagemTest.java](src/test/java/br/com/doasanguepoa/postagem/model/PostagemTest.java).

## Testes Implementados

### 1. `testConstrutorEPropriedadesComDataCriacaoEDataAtualizacao`

Este teste verifica o construtor e as propriedades da classe `Postagem` quando são fornecidas datas de criação e atualização.

**Passos do Teste:**
- Criação de uma postagem com mensagem, CNPJ, data de criação e data de atualização.
- Atribuição de um ID fictício à postagem.
- Verificação se o ID não é nulo.
- Verificação se a mensagem, CNPJ, data de criação e data de atualização correspondem aos valores fornecidos.
- Verificação se as datas de criação e atualização não são nulas.

### 2. `testConstrutorEPropriedades`

Este teste verifica o construtor e as propriedades da classe `Postagem` sem fornecer datas de criação e atualização.

**Passos do Teste:**
- Criação de uma postagem com mensagem e CNPJ.
- Atribuição de um ID fictício à postagem.
- Verificação se o ID não é nulo.
- Verificação se a mensagem, CNPJ, data de criação e data de atualização não são nulas.

### 3. `testAtualizacaoDaMensagem`

Este teste verifica a atualização da mensagem de uma postagem.

**Passos do Teste:**
- Criação de uma postagem com mensagem e CNPJ.
- Atualização da mensagem da postagem.
- Verificação se a mensagem foi atualizada corretamente.

### 4. `testAtualizacaoDoTimestamp`

Este teste verifica a atualização do timestamp de uma postagem.

**Passos do Teste:**
- Criação de uma postagem com mensagem e CNPJ.
- Armazenamento do timestamp original da criação.
- Atualização do timestamp de atualização.
- Verificação se o novo timestamp é diferente do original.

### 5. `testIdNaoNulo`

Este teste verifica se o ID de uma postagem não é nulo.

**Passos do Teste:**
- Criação de uma postagem com mensagem e CNPJ.
- Atribuição de um ID fictício à postagem.
- Verificação se o ID não é nulo.

## Observações

- Os testes foram implementados usando o framework JUnit 5.
- A classe `PostagemTest` testa diversos aspectos da classe `Postagem`, incluindo construtores, propriedades e métodos de atualização.
- Os testes são independentes e podem ser executados em qualquer ordem.
