# Testes de API para PostagemResource

## Introdução

Este documento descreve os testes de API implementados usando RestAssured para a classe `PostagemResource` em uma aplicação Quarkus.

## Estrutura do Projeto

Os testes estão localizados no arquivo [PostagemResourceTest.java](src/test/java/br/com/doasanguepoa/postagem/controller/PostagemResourceTest.java).

## Testes Implementados

### 1. `testListarPostagens`

Este teste verifica a listagem de postagens.

**Método:** GET  
**Endpoint:** /postagens  
**Passos do Teste:**
- Envio de uma requisição GET para /postagens.
- Verificação do código de status da resposta (esperado: 200).
- Verificação do tipo de conteúdo da resposta (esperado: JSON).
- Verificação do tamanho da lista de postagens na resposta (esperado: 3).
- Registro completo da resposta.

### 2. `testAdicionarPostagem`

Este teste verifica a adição de uma nova postagem.

**Método:** POST  
**Endpoint:** /postagens  
**Passos do Teste:**
- Criação de um objeto `DadosCadastroPostagemDTO` representando uma nova postagem.
- Envio de uma requisição POST para /postagens com o objeto de postagem no corpo.
- Verificação do código de status da resposta (esperado: 201).
- Verificação do conteúdo da resposta, garantindo que a mensagem e o CNPJ correspondem ao esperado.
- Registro completo da resposta.

### 3. `testBuscarPostagemPorId`

Este teste verifica a busca de uma postagem por ID.

**Método:** GET  
**Endpoint:** /postagens/{id}  
**Passos do Teste:**
- Definição do ID da postagem a ser buscada.
- Envio de uma requisição GET para /postagens/{id}.
- Verificação do código de status da resposta (esperado: 200).
- Verificação do tipo de conteúdo da resposta (esperado: JSON).
- Verificação do ID da postagem na resposta (esperado: 2).
- Registro completo da resposta.

### 4. `testAtualizarPostagem`

Este teste verifica a atualização de uma postagem existente.

**Método:** PUT  
**Endpoint:** /postagens  
**Passos do Teste:**
- Criação de um objeto `DadosAtualizacaoPostagemDTO` representando a atualização da postagem.
- Envio de uma requisição PUT para /postagens com o objeto de atualização no corpo.
- Verificação do código de status da resposta (esperado: 200).
- Verificação da mensagem atualizada na resposta.
- Registro completo da resposta.

### 5. `testDeletarPostagem`

Este teste verifica a exclusão de uma postagem.

**Método:** DELETE  
**Endpoint:** /postagens/{id}  
**Passos do Teste:**
- Definição do ID da postagem a ser deletada.
- Envio de uma requisição DELETE para /postagens/{id}.
- Verificação do código de status da resposta (esperado: 204).
- Registro completo da resposta.

## Observações

- A anotação `@QuarkusTest` indica que esses testes são testes de integração Quarkus.
- A anotação `@TestSecurity(authorizationEnabled = false)` desativa a autorização durante os testes.
- A biblioteca RestAssured é utilizada para realizar as requisições HTTP e as verificações.
- Os testes são independentes e podem ser executados em qualquer ordem.
- Este conjunto de testes fornece uma cobertura básica para as operações da classe `PostagemResource`.
