package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class PostagemResourceUnitTest {

    @Mock
    private PostagemRepository postagemRepository;
    @Mock
    private JsonWebToken jwt;

    private PostagemResource postagemResource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postagemResource = new PostagemResource(postagemRepository, jwt);
    }

    @Test
    public void listarPostagensTest() {
        List<Postagem> postagens = new ArrayList<>(); // Crie uma lista de postagens fictícia

        // Configure o comportamento esperado do postagemRepository.listAll()
        when(postagemRepository.listAll()).thenReturn(postagens);

        List<DadosListagemPostagemDTO> result = postagemResource.listarPostagens();

        assertEquals(0, result.size()); // Verifique se a lista resultante está vazia
    }

    @Test
    public void buscarPostagemPorIdTest() {
        Long postId = 1L;
        Postagem postagem = new Postagem("Test Post");
        when(postagemRepository.findById(postId)).thenReturn(postagem);

        Postagem result = postagemResource.buscarPostagemPorId(postId);

        assertEquals(postagem, result);
    }

    @Test
    public void adicionarPostagemTest() {
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO("Test Post");

        postagemResource.adicionarPostagem(postagemDTO);

        verify(postagemRepository).persist(any(Postagem.class));
    }


    @Test
    public void adicionarPostagemComMensagemNulaTest() {
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO(null);

        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> postagemResource.adicionarPostagem(postagemDTO)
        );

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), exception.getResponse().getStatus());
    }

    @Test
    public void atualizarPostagemTest() {
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(1L, "Updated Test Post");
        Postagem postagem = new Postagem("Original Test Post");
        when(postagemRepository.findById(postagemDTO.id())).thenReturn(postagem);

        Postagem result = postagemResource.atualizarPostagem(postagemDTO);
        assertEquals(postagemDTO.mensagem(), result.getMensagem());
    }

    @Test
    public void atualizarPostagemInexistenteTest() {
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(99L, "Updated Test Post");
        lenient().when(postagemRepository.findById(postagemDTO.id())).thenReturn(null);

        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> postagemResource.atualizarPostagem(postagemDTO)
        );

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
    }

    @Test
    public void deletarPostagemTest() {
        Long postId = 1L;
        postagemResource.deletarPostagem(postId);
        verify(postagemRepository, times(1)).delete(any());
    }

    @Test
    public void deletarPostagemInexistenteTest() {
        Long postId = 1L;
        lenient().when(postagemRepository.findById(postId)).thenReturn(null);

        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> postagemResource.deletarPostagem(postId)
        );

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
    }
}
