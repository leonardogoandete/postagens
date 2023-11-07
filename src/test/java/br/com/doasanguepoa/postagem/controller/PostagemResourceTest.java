package br.com.doasanguepoa.postagem.controller;


import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.mapper.PostagemMapper;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import br.com.doasanguepoa.postagem.service.PostagemService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.equalTo;
import static io.restassured.RestAssured.given;
import static io.smallrye.common.constraint.Assert.assertFalse;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Slf4j
@QuarkusTest
public class PostagemResourceTest {

    @Test
    public void testListarPostagens() {
        ValidatableResponse response =
                given()
                .when()
                .get("/postagens")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", Matchers.is(3));

        log.info("Teste de listagem de postagens"+ response.log().body());

    }

    @Test
    public void testBuscarPostagemPorId() {
        long postId = 2; // ID da postagem a ser buscada

        ValidatableResponse response = given()
                .pathParam("id", postId)
                .when()
                .get("/postagens/{id}")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", Matchers.equalTo(0)); // Verifique se o ID da postagem corresponde ao esperado



    }
}
