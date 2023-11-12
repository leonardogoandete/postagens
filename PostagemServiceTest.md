# Testes para `PostagemService`

Este documento descreve os testes implementados para a classe `PostagemService` no contexto de uma aplicação Quarkus.

## Estrutura do Projeto

Os testes estão localizados no arquivo [PostagemServiceTest.java](src/test/java/br/com/doasanguepoa/postagem/service/PostagemServiceTest.java).

## Testes Implementados

### 1. `testListarTodasPostagensComSucesso`

Este teste verifica o método `listarTodasPostagens` quando o repositório retorna uma lista não vazia.

#### Passos do Teste

1. Configuração do comportamento do repositório para retornar uma lista com uma postagem.
2. Chamada do método `listarTodasPostagens`.
3. Verificação se a lista de postagens tem tamanho 1.

### 2. `testListarPostagemComFalhaNoRepositorioDeveLancarRuntimeException`

Este teste verifica o comportamento do método `listarTodasPostagens` quando ocorre uma falha no repositório.

#### Passos do Teste

1. Configuração do comportamento do repositório para lançar uma exceção ao listar as postagens.
2. Chamada do método `listarTodasPostagens`.
3. Verificação se uma exceção do tipo `RuntimeException` é lançada.

### 3. `testListarPostagemComListaVaziaComSucesso`

Este teste verifica o método `listarTodasPostagens` quando o repositório retorna uma lista vazia.

#### Passos do Teste

1. Configuração do comportamento do repositório para retornar uma lista vazia.
2. Chamada do método `listarTodasPostagens`.
3. Verificação se a lista de postagens está vazia.

...

### 16. `testExcluirPostagemExistenteComSucesso`

Este teste verifica o método `excluirPostagemExistente` quando a postagem é excluída com sucesso.

#### Passos do Teste

1. Configuração do ID e da postagem a ser excluída, e do comportamento do repositório.
2. Chamada do método `excluirPostagemExistente`.
3. Verificação se o método de exclusão do repositório foi chamado.

### 17. `testExcluirPostagemComIdNuloDeveLancarIllegalArgumentException`

Este teste verifica o método `excluirPostagemExistente` quando é fornecido um ID nulo.

#### Passos do Teste

1. Configuração de um ID nulo.
2. Chamada do método `excluirPostagemExistente`.
3. Verificação se uma exceção do tipo `IllegalArgumentException` é lançada.

### 18. `testExcluirPostagemInexistenteDeveLancarNotFoundException`

Este teste verifica o método `excluirPostagemExistente` quando a postagem a ser excluída não existe.

#### Passos do Teste

1. Configuração do ID e do comportamento do repositório para indicar que a postagem não existe.
2. Chamada do método `excluirPostagemExistente`.
3. Verificação se uma exceção do tipo `NotFoundException` é lançada.

## Observações

- Os testes foram implementados usando o framework JUnit 5.
- A classe `PostagemServiceTest` testa diversos métodos da classe `PostagemService`, abrangendo casos de sucesso e falhas esperadas.
- Os testes são independentes e podem ser executados em qualquer ordem.
- Certifique-se de adaptar os valores esperados conforme a lógica específica da sua aplicação.
