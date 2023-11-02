package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PostagemResourceUnitTest {

    @Mock
    PostagemRepository postagemRepository;

    @Mock
    PostagemResource postagemResource;

    @Test
    @Transactional
    public void listarPostagensTest() {
        List<DadosListagemPostagemDTO> postagens = new ArrayList<>(); // Crie uma lista de DadosListagemPostagemDTO fictícia

        DadosListagemPostagemDTO postagem1 = new DadosListagemPostagemDTO(1L, "Test Post 1");
        DadosListagemPostagemDTO postagem2 = new DadosListagemPostagemDTO(2L, "Test Post 2");


        postagens.add(postagem1);
        postagens.add(postagem2);

        lenient().when(postagemResource.listarPostagens()).thenReturn(postagens);
        List<DadosListagemPostagemDTO> result = postagemResource.listarPostagens();

        assertEquals(2, result.size()); // Verifique se o tamanho da lista resultante é igual a 2
    }



    @Test
    public void buscarPostagemPorIdTest() {
        Postagem postagem = new Postagem("Test Post");
        postagem.setId(1L);
        lenient().when(postagemRepository.findById(1L)).thenReturn(postagem);

        assertEquals("Test Post", postagem.getMensagem());
        assertEquals(1L, postagem.getId());
    }


    @Test
    public void adicionarPostagemTest() {
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO("Test Post");
        postagemResource.adicionarPostagem(postagemDTO);
        verify(postagemResource).adicionarPostagem(postagemDTO);
    }

    @Test
    public void adicionarPostagemComMensagemNulaTest() {
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO(null);
        try {
            postagemResource.adicionarPostagem(postagemDTO);
            verify(postagemResource).adicionarPostagem(postagemDTO);
        }catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    @Transactional
    public void atualizarPostagemTest() {
        Postagem postagem = new Postagem("Original Test Post");
        lenient().when(postagemRepository.findById(1L)).thenReturn(postagem);

        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(postagem.getId(), "Updated Test Post");
        postagem.setMensagem(postagemDTO.mensagem());
        postagemRepository.persist(postagem);

        Postagem postagemEditada = postagemRepository.findById(1L);
        assertNotNull(postagemEditada);
        assertEquals("Updated Test Post", postagemEditada.getMensagem());
    }

    @Test
    public void atualizarPostagemInexistenteTest() {
        try {
            DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(99L, "Updated Test Post");
            postagemResource.atualizarPostagem(postagemDTO);
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    @Transactional
    public void deletarPostagemTest() {
        try{
            postagemResource.deletarPostagem(1L);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), 204);
        }catch (WebApplicationException e){
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), e.getResponse().getStatus());
        }
    }



    @Test
    public void deletarPostagemInexistenteTest() {
        try {
            postagemResource.deletarPostagem(1L);
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        }
    }
}
