package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostagemServiceTest {

    @InjectMocks
    private PostagemService postagemService;

    @Mock
    private PostagemRepository postagemRepository;

    @Test
    void testListarTodasPostagensComSucesso() {
        // Teste para listar todas as postagens com sucesso
        when(postagemRepository.listAll()).thenReturn(Collections.singletonList(new Postagem()));
        List<Postagem> postagens = postagemService.listarTodasPostagens();
        assertEquals(1, postagens.size());
    }

    @Test
    void testListarPostagemComFalhaNoRepositorioDeveLancarRuntimeException() {
        // Teste para listar postagens com falha no repositório, deve lançar RuntimeException
        when(postagemRepository.listAll()).thenThrow(new RuntimeException("Falha ao listar as postagens no repositório"));
        assertThrows(RuntimeException.class, () -> postagemService.listarTodasPostagens());
    }

    @Test
    void testListarPostagemComListaVaziaComSucesso() {
        // Teste para listar postagens com lista vazia
        when(postagemRepository.listAll()).thenReturn(Collections.emptyList());
        List<Postagem> postagens = postagemService.listarTodasPostagens();
        assertEquals(Collections.emptyList(), postagens);
    }


    @Test
    void testBuscarPostagemPorIdComSucesso() {
        // Teste para buscar postagem por ID com sucesso
        Long id = 1L;
        Postagem postagem = new Postagem();
        when(postagemRepository.findByIdOptional(id)).thenReturn(Optional.of(postagem));
        Optional<Postagem> result = postagemService.buscarPostagemPorId(id);
        assertEquals(postagem, result.orElse(null));
    }

    @Test
    void testBuscarPostagemPorIdComIdVazio() {
        // Teste para buscar postagem com ID vazio, deve lançar IllegalArgumentException
        Long id = null;
        assertThrows(IllegalArgumentException.class, () -> postagemService.buscarPostagemPorId(id));
    }



    @Test
    void testInserirPostagemComMensagemNulaDeveLancarIllegalArgumentException() {
        // Teste para inserir postagem com mensagem nula, deve lançar IllegalArgumentException
        Postagem postagem = new Postagem();
        postagem.setMensagem(null);
        assertThrows(IllegalArgumentException.class, () -> postagemService.inserirPostagem(postagem));
    }



    @Test
    void testEditarPostagemComIdNuloDeveLancarNotFoundException() {
        // Teste para editar postagem com ID nulo, deve lançar NotFoundException
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(null, "Mensagem");
        assertThrows(NotFoundException.class, () -> postagemService.editarPostagemExistente(postagemDTO));
    }



    @Test
    void testExcluirPostagemComIdNuloDeveLancarIllegalArgumentException() {
        // Teste para excluir postagem com ID nulo, deve lançar IllegalArgumentException
        Long id = null;
        assertThrows(IllegalArgumentException.class, () -> postagemService.excluirPostagemExistente(id));
    }
    
}
