package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
class PostagemResourceTest {

    @Test
    void testListarPostagens() {
        // Teste para verificar se a listagem de postagens retorna o código 200, é JSON e tem um tamanho de 3
        Response response = given()
                .when()
                .get("/postagens");

        response.then()
                .statusCode(200);

        response.then()
                .contentType(ContentType.JSON);

        response.then()
                .body("$.", hasSize(3)); // Verifica se a resposta tem um tamanho de 3

        response.then()
                .log()
                .all();
    }

    @Test
    void testAdicionarPostagem(){
        // Teste para adicionar uma nova postagem e verificar se a resposta é 201
        DadosCadastroPostagemDTO dadosPostagem = new DadosCadastroPostagemDTO("Minha nova postagem","87020517000120");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(dadosPostagem)
                .when()
                .post("/postagens");

        response.then()
                .statusCode(201);

        response.then()
                .body("mensagem", Matchers.equalTo("Minha nova postagem"))
                .body("cnpj", Matchers.equalTo("87020517000120"));

        response.then()
                .log()
                .all();
    }

    @Test
    void testBuscarPostagemPorId() {
        // Teste para buscar uma postagem pelo ID e verificar se a resposta é 200 e contém o ID esperado
        long postId = 2; // ID da postagem a ser buscada

        Response response = given()
                .pathParam("id", postId)
                .when()
                .get("/postagens/{id}");

        response.then()
                .statusCode(200);

        response.then()
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(2)); // Verifica se o ID na resposta é igual a 2

        response.then()
                .log()
                .all();
    }

    @Test
    void testAtualizarPostagem() {
        // Teste para atualizar uma postagem existente e verificar se a resposta é 200 e contém a mensagem atualizada
        DadosAtualizacaoPostagemDTO dadosAtualizacaoPostagemDTO = new DadosAtualizacaoPostagemDTO(2L,"Nova mensagem para a postagem 2");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(dadosAtualizacaoPostagemDTO)
                .when()
                .put("/postagens");

        response.then()
                .statusCode(200);

        response.then()
                .body("mensagem", Matchers.equalTo("Nova mensagem para a postagem 2")); // Certifique-se de que a chave "mensagem" corresponda à estrutura da resposta real

        response.then()
                .log()
                .all();
    }

    @Test
    void testDeletarPostagem() {
        // Teste para deletar uma postagem pelo ID e verificar se a resposta é 204 (sem conteúdo)
        long postId = 4;

        Response response = given()
                .pathParam("id", postId)
                .when()
                .delete("/postagens/{id}");

        response.then()
                .statusCode(204);

        response.then()
                .log()
                .all();
    }
}
