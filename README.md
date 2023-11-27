# Microserviço de Postagens

Microserviço responsável por manipular as postagens de instituições.<br>
Onde contem as seguintes personas:<br>
#### Instituição:
> Posta uma postagem;<br>
> Edita uma postagem;<br>
> Exclui uma postagem;<br>
> Visualiza suas postagens;<br>
> Visualiza todas as postagens.

#### Usuario:
> Visualiza todas as postagens;<br>
> Compartilha postagens;

### CI/CD Github Actions
O projeto contem uma pipe de CI/CD, conforme os passos abaixo:
#### Deploy na master: <br>
**Ao realizar deploy na master, dispara um job no actions e realiza os passos abaixo:**
 
- Configura container do Trivy para checkagem de vulnerabilidades
- Clone deste projeto
- Configuração do JDK17
- Configuração do Sonar Cloud para inspeção no código
- Build e Inspenção do Sonar
- Verificação de vulnerabilidades no projeto

#### Deploy de uma release: <br>
**Ao realizar a publicação de uma release, dispara dois jobs no actions e realiza os passos abaixo:**

- Configura container do Trivy para checkagem de vulnerabilidades
- Clone deste projeto
- Configuração do JDK17
- Configuração do Sonar Cloud para inspeção no código
- Build e Inspenção do Sonar
- Verificação de vulnerabilidades no projeto

**Ao passar com sucesso o JOB anterior, realiza os passos abaixo:**

- Baixa o repositorio com a versão lançada
- Instala o minikube e defini como registry local
- Configura o JDK17
- Gera a imagem docker com o nome leogoandete/postagens:<NUMERO DA RELEASE LANÇADA>
- Autentica no docker hub
- Envia a imagem para o docker hub


### Links extras:
- Docker: https://hub.docker.com/r/leogoandete/postagens <br>
- Heroku: https://postagens-0305647248f4.herokuapp.com/q/swagger-ui <br>
- Sonar Cloud: https://sonarcloud.io/summary/new_code?id=leonardogoandete_postagens

Obs.: No heroku ao acessar a página, pode demorar um pouco para o serviço iniciar pois o projeto fica hibernado caso não for acessado recentemente.

Exemplo docker:<br>
```
Utilização de variáveis de ambiente para uso de banco de dados.

export DB_URL=host
export DB_PORT=port
export DB_DATABASE=database
export QUARKUS_DATASOURCE_USERNAME=user
export QUARKUS_DATASOURCE_PASSWORD=password
export QUARKUS_DATASOURCE_DB_KIND=type of database

export QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://$DB_URL:$DB_PORT/$DB_DATABASE

Exemplo docker:
docker run -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://$DB_URL:$DB_PORT/$DB_DATABASE leogoandete/postagens
ou
docker run -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://127.0.0.1:5432/postagem leogoandete/postagens

```

## Dependencia de outros microserviços:
- Microserviço de login: https://github.com/LEO-SF/Service_cadastrar
- Microserviço de cadastro: https://github.com/EnzoGRodrigues/DoaSangueLogin

## Documentação dos testes

Aqui está a documentação dos testes realizados no projeto:

 - [Testes de API](PostagemResourceTest.md)
 - [Testes de Unidade](PostagemResourceUnitTest.md)
 - [Testes de Model Postagem](PostagemModel.md)
 - [Testes de API](PostagemServiceTest.md)
 - [Testes de Sistema](https://github.com/leonardogoandete/doasanguepoa/blob/main/README.md)
