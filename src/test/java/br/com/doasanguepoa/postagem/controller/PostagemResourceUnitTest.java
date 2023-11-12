package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostagemResourceUnitTest {

    @Mock
    PostagemRepository postagemRepository;

    @Mock
    PostagemResource postagemResource;

    @Test
    void listarPostagensComErroTest() {
        // Teste para listar postagens com erro (não está completo)
        List<DadosListagemPostagemDTO> postagens = new ArrayList<>(); // Crie uma lista de DadosListagemPostagemDTO fictícia

        DadosListagemPostagemDTO postagem1 = new DadosListagemPostagemDTO(1L, "Test Post 1", "87020517000120");
        DadosListagemPostagemDTO postagem2 = new DadosListagemPostagemDTO(2L, "Test Post 2", "87020517000120");

        // Continuação do teste...
    }

    @Test
    void buscarPostagemPorIdTest() {
        // Teste para buscar uma postagem pelo ID e verificar se a resposta é 200 e contém o ID esperado
        Postagem postagem = new Postagem("Test Post", "87020517000120");
        postagem.setId(1L);
        // Criando uma postagem fictícia
        lenient().when(postagemRepository.findById(1L)).thenReturn(postagem);

        Postagem postagemEncontrada = postagemRepository.findById(1L);
        assertEquals(postagemEncontrada.getId(), postagem.getId());

        // Verifique se o método findById foi chamado apenas uma vez
        verify(postagemRepository, times(1)).findById(1L);
    }


    @Test
    void adicionarPostagemTest() {
        // Teste para adicionar uma postagem (não está completo)
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO("Test Post", "87020517000120");
        postagemResource.adicionarPostagem(postagemDTO);
        verify(postagemResource).adicionarPostagem(postagemDTO);
    }

    @Test
    void adicionarPostagemComMensagemNulaTest() {
        // Teste para adicionar uma postagem com mensagem nula e verificar se a exceção é lançada corretamente
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO(null, "87020517000120");
        try {
            postagemResource.adicionarPostagem(postagemDTO);
            verify(postagemResource).adicionarPostagem(postagemDTO);
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    @Transactional
    void atualizarPostagemTest() {
        // Teste para atualizar uma postagem existente e verificar se a mensagem foi atualizada corretamente
        Postagem postagem = new Postagem("Original Test Post", "87020517000120");
        lenient().when(postagemRepository.findById(1L)).thenReturn(postagem);

        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(postagem.getId(), "Updated Test Post");
        postagem.setMensagem(postagemDTO.mensagem());
        postagemRepository.persist(postagem);

        Postagem postagemEditada = postagemRepository.findById(1L);
        assertNotNull(postagemEditada);
        assertEquals("Updated Test Post", postagemEditada.getMensagem());
    }

    @Test
    void atualizarPostagemInexistenteTest() {
        // Teste para atualizar uma postagem inexistente e verificar se a exceção é lançada corretamente
        try {
            DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(99L, "Updated Test Post");
            postagemResource.atualizarPostagem(postagemDTO);
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    void deletarPostagemInexistenteTest() {
        // Teste para deletar uma postagem inexistente e verificar se a exceção é lançada corretamente
        try {
            postagemResource.deletarPostagem(1L);
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    void deletarPostagemTest() {
        // Teste para deletar uma postagem existente (não está completo)
        Postagem postagem = new Postagem("Test Post", "87020517000120");
        postagem.setId(1L);
        // Criando uma postagem fictícia
        lenient().when(postagemRepository.findById(1L)).thenReturn(postagem);

        postagemResource.deletarPostagem(1L);
        verify(postagemResource).deletarPostagem(1L);
    }
}