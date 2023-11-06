package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostagemServiceTest {
    @InjectMocks
    private PostagemService postagemService;

    @Mock
    private PostagemRepository postagemRepository;

    @Test
    public void testListarTodasPostagensComSucesso() {
        // Arrange
        when(postagemRepository.listAll()).thenReturn(Collections.singletonList(new Postagem()));

        // Act
        List<Postagem> postagens = postagemService.listarTodasPostagens();

        // Assert
        assertEquals(1, postagens.size());
    }


    @Test
    public void testListarPostagemComFalhaNoRepositorioDeveLancarRuntimeException() {
        // Arrange
        when(postagemRepository.listAll()).thenThrow(new RuntimeException("Falha ao listar as postagens no repositório"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postagemService.listarTodasPostagens());
    }

    @Test
    public void testListarPostagemComListaVaziaComSucesso() {
        // Arrange
        when(postagemRepository.listAll()).thenReturn(Collections.emptyList());

        // Act
        List<Postagem> postagens = postagemService.listarTodasPostagens();

        // Assert
        assertEquals(Collections.emptyList(), postagens);
    }

    @Test
    public void testBuscarPostagemPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Postagem postagem = new Postagem();
        when(postagemRepository.findByIdOptional(id)).thenReturn(Optional.of(postagem));

        // Act
        Optional<Postagem> result = postagemService.buscarPostagemPorId(id);

        // Assert
        assertEquals(postagem, result.orElse(null));
    }

    @Test
    public void testBuscarPostagemPorIdComIdVazio() {
        // Arrange
        Long id = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postagemService.buscarPostagemPorId(id));
    }

    @Test
    public void testBuscarPostagemInexistenteDeveLancarNotFoundException() {
        // Arrange
        Long id = 999L;
        when(postagemRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postagemService.buscarPostagemPorId(id));
    }

    @Test
    public void testBuscarPostagemComIdFalhaNoRepositorioDeveLancarRuntimeException() {
        // Arrange
        Long id = 1L;
        when(postagemRepository.findByIdOptional(id)).thenThrow(new RuntimeException("Falha ao buscar a postagem no repositório"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postagemService.buscarPostagemPorId(id));
    }

    @Test
    public void testInserirPostagemComSucesso() {
        // Arrange
        DadosCadastroPostagemDTO postagemDTO = new DadosCadastroPostagemDTO("Mensagem");
        Postagem postagem = new Postagem(postagemDTO.mensagem());
        when(postagemRepository.isPersistent(postagem)).thenReturn(true);

        // Act
        Postagem result = postagemService.inserirPostagem(postagem);

        // Assert
        assertEquals(postagem, result);
    }

    @Test
    public void testInserirPostagemComMensagemNulaDeveLancarIllegalArgumentException() {
        // Arrange
        Postagem postagem = new Postagem();
        postagem.setMensagem(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postagemService.inserirPostagem(postagem));
    }

    @Test
    public void testInserirPostagemFalhaNoRepositorio() {
        // Arrange
        Postagem postagem = new Postagem("Mensagem");
        when(postagemRepository.isPersistent(postagem)).thenReturn(false);

        // Act & Assert
        Postagem result = postagemService.inserirPostagem(postagem);
        assertEquals(null, result);
    }

    @Test
    public void testEditarPostagemExistenteComSucesso() {
        // Arrange
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(1L,"Mensagem");
        Postagem postagem = new Postagem();
        when(postagemRepository.findByIdOptional(postagemDTO.id())).thenReturn(Optional.of(postagem));

        // Act
        Postagem result = postagemService.editarPostagemExistente(postagemDTO);

        // Assert
        assertEquals(postagem, result);
    }

    @Test
    public void testEditarPostagemComIdNuloDeveLancarIllegalArgumentException() {
        // Arrange
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(null,"Mensagem");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postagemService.editarPostagemExistente(postagemDTO));
    }

    @Test
    public void testEditarPostagemComMensagemNulaDeveLancarIllegalArgumentException() {
        // Arrange
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(1L,null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postagemService.editarPostagemExistente(postagemDTO));
    }

    @Test
    public void testEditarPostagemInexistenteDeveLancarNotFoundException() {
        // Arrange
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(999L,"Mensagem");

        when(postagemRepository.findByIdOptional(postagemDTO.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postagemService.editarPostagemExistente(postagemDTO));
    }

    @Test
    public void testExcluirPostagemExistenteComSucesso() {
        // Arrange
        Long id = 1L;
        Postagem postagem = new Postagem();
        when(postagemRepository.findByIdOptional(id)).thenReturn(Optional.of(postagem));

        // Act
        postagemService.excluirPostagemExistente(id);

        // Assert
        verify(postagemRepository).delete(postagem);
    }

    @Test
    public void testExcluirPostagemComIdNuloDeveLancarIllegalArgumentException() {
        // Arrange
        Long id = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postagemService.excluirPostagemExistente(id));
    }

    @Test
    public void testExcluirPostagemInexistenteDeveLancarNotFoundException() {
        // Arrange
        Long id = 1L;
        when(postagemRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postagemService.excluirPostagemExistente(id));
    }

}