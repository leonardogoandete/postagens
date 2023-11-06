//package br.com.doasanguepoa.postagem.controller;
//
//
//import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
//import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
//import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
//import br.com.doasanguepoa.postagem.model.Postagem;
//import br.com.doasanguepoa.postagem.repository.PostagemRepository;
//import br.com.doasanguepoa.postagem.service.PostagemService;
//import io.quarkus.test.InjectMock;
//import io.quarkus.test.junit.QuarkusTest;
//import io.restassured.http.ContentType;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@QuarkusTest
//public class PostagemResourceTest {
//
//    public static final String TEST_BEARER_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJ1cG4iOiI4NzAyMDUxNzAwMDEyMCIsImdyb3VwcyI6WyJJTlNUSVRVSUNBTyJdLCJleHAiOjE2OTkwNTAyOTI4NzAsImlhdCI6MTY5OTA1MDI4OSwianRpIjoiYzczYWI1N2ItOTgzMi00MDE0LTllZTgtNTlmNjMwNjZkODUxIn0.rPyS_aIR0RPoUErSjZMXIljk71TNerwXrLF_KGnbBp0S4N0kEY7n9vMIYYDEALPT_n0QcA6UfYQC6UalB_q4z1K3eO2VY0d4hSJv8aN5XQAe3My6FclsLpeIQuLJdbIvnNdd9xXfznh7QuzY4brsMliq8xpbZjwja1s_W32N99fM-gKC0QYTlnWWNgSigil6yRXY2mg2WS2Tw3ucp6E4L96qQ3Ou7muGxVGpBhYoiwAJ4HDtnBH3QN12KOoJGZgXAJs8eGFVl_IOQ5xEpuThTUsrxb2npzTzZqIM2fC0ENmBiEdFh0sbj0Mp1Bl0USWpwh1-DjDO1M1a3riEY9iPxg";
//    @InjectMock
//    PostagemRepository postagemRepository = new PostagemRepository();
//    @InjectMock
//    PostagemService postagemService= new PostagemService(postagemRepository);
//
//    PostagemResource postagemResource = new PostagemResource(postagemService);
//
//    @Test
//    void listarPostagensTest() {
//        //Deve litar todas as postagens
//        //Deve retornar 200
//        List<DadosListagemPostagemDTO> postagens = new ArrayList<>(); // Crie uma lista de DadosListagemPostagemDTO fictícia
//        postagens.add(new DadosListagemPostagemDTO(1L,"Postagem teste 1")); // Adicione um DadosListagemPostagemDTO fictício à lista
//        postagens.add(new DadosListagemPostagemDTO(2L,"Postagem teste 2")); // Adicione um DadosListagemPostagemDTO fictício à lista
//        Mockito.when(postagemService.listarTodasPostagens()).thenReturn( postagens );
//
//        List<DadosListagemPostagemDTO> result = postagemResource.listarPostagens();
//        //assertEquals(2, result.size()); // Verifique se o tamanho da lista resultante é igual a 2
//        given()
//            .when().get("/postagens")
//            .then()
//                .statusCode(200)
//                .body("$.size()", is(2));
//
//    }
//
//    @Test
//    void buscarPostagemPorIdTest() {
//        //Deve buscar uma postagem por id
//        //Deve retornar 200
//        Postagem postagem = new Postagem(1L,"Postagem teste 1", Instant.now(), Instant.now()); // Crie uma postagem fictícia
//        Mockito.when(postagemService.buscarPostagemPorId(1L)).thenReturn(postagem);
//
//        Postagem p = postagemService.buscarPostagemPorId(1L);
//
//        DadosListagemPostagemDTO dadosListagemPostagemDTO = new DadosListagemPostagemDTO(p);
//        assertEquals(1L, dadosListagemPostagemDTO.id()); // Verifique se o id da postagem resultante é igual a 1
//
//        given()
//                .header("Authorization", TEST_BEARER_TOKEN)
//            .when().get("/postagens/1")
//            .then()
//                .statusCode(200)
//                .body("id", is(1));
//    }
//
//    @Test
//    @Transactional
//    void adicionarPostagemTest() {
//        // Deve adicionar uma postagem
//        // Deve retornar 201
//        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO("Postagem teste 1");
//
//        // Configure o mock para retornar uma resposta com status 201
//        Mockito.when(postagemService.inserirPostagem(postagemDTO))
//                .thenReturn(Response.status(201).entity("Postagem criada com sucesso").build());
//
//        Response response = postagemResource.adicionarPostagem(postagemDTO);
//
//        given()
//                .header("Authorization", TEST_BEARER_TOKEN)
//                .body(postagemDTO) // Defina o corpo da solicitação com o DTO da postagem
//                .contentType(ContentType.JSON) // Defina o tipo de mídia JSON
//                .when().post("/postagens")
//                .then()
//                .statusCode(201);
//    }
//
//    @Test
//    @Transactional
//    void atualizarPostagemTest() {
//        // Deve atualizar uma postagem
//        // Deve retornar 200
//        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(2L,"Postagem teste 1");
//
//        // Configure o mock para retornar uma resposta com status 200
//        Mockito.when(postagemService.editarPostagemExistente(postagemDTO))
//                .thenReturn(new Postagem(2L,"Postagem teste 2", Instant.now(), Instant.now()));
//
//        Postagem postagem = postagemResource.atualizarPostagem(postagemDTO);
//
//        given()
//                .header("Authorization", TEST_BEARER_TOKEN)
//                .body(postagemDTO) // Defina o corpo da solicitação com o DTO da postagem
//                .contentType(ContentType.JSON) // Defina o tipo de mídia JSON
//                .when().put("/postagens")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    @Transactional
//    void deletarPostagemTest() {
//        // Deve deletar uma postagem
//        // Deve retornar 200
//        // Configure o mock para retornar uma resposta com status 200
//        Mockito.when(postagemService.excluirPostagemExistente(4L))
//                .thenReturn(Response.status(200).entity("Postagem excluída com sucesso").build());
//
//        Response response = postagemResource.deletarPostagem(4L);
//
//        given()
//                .header("Authorization", TEST_BEARER_TOKEN)
//                .when().delete("/postagens/1")
//                .then()
//                .statusCode(204);
//    }
//
//}
