package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.annotations.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostagemServiceTest {

    @InjectMocks
    PostagemService postagemService;

    @Mock
    PostagemRepository postagemRepository;

    Postagem postagem;
    DadosCadastroPostagemDTO dadosCadastroPostagemDTO;
    DadosAtualizacaoPostagemDTO dadosAtualizacaoPostagemDTO;

    @BeforeEach
    void setUp() {
        postagem = new Postagem("Nova postagem!");
        postagem.setId(1L);
        dadosCadastroPostagemDTO = new DadosCadastroPostagemDTO("Nova postagem DTO!");
        dadosAtualizacaoPostagemDTO = new DadosAtualizacaoPostagemDTO(1L, "Mensagem atualizada DTO!");
    }

    @Test
    void deveListarTodasAsPostagensComSucesso() {
        // Dados de exemplo: Crie uma postagem e liste-a
        List<Postagem> postagens = Collections.singletonList(postagem);

        // Simule o comportamento do repositório ao chamar listAll()
        when(postagemRepository.listAll()).thenReturn(postagens);

        // Chame o serviço para listar as postagens
        List<DadosListagemPostagemDTO> dadosListagemPostagemDTOS = postagemService.listarTodasPostagens();

        // Verifique se a lista não está vazia e se os dados coincidem
        assertFalse(dadosListagemPostagemDTOS.isEmpty());
        assertEquals(postagens.size(), dadosListagemPostagemDTOS.size());
        assertEquals(postagem.getId(), dadosListagemPostagemDTOS.get(0).id());
        assertEquals(postagem.getMensagem(), dadosListagemPostagemDTOS.get(0).mensagem());

        // Verifique que o método listAll() foi chamado no repositório 1 vez
        verify(postagemRepository).listAll();
        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverPostagens() {
        // Simule o comportamento do repositório retornando uma lista vazia
        when(postagemRepository.listAll()).thenReturn(Collections.emptyList());

        // Chame o serviço para listar as postagens
        List<DadosListagemPostagemDTO> dadosListagemPostagemDTOS = postagemService.listarTodasPostagens();

        // Verifique se a lista está vazia
        assertTrue(dadosListagemPostagemDTOS.isEmpty());

        // Verifique que o método listAll() foi chamado no repositório
        verify(postagemRepository).listAll();

        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveLancarExceptionQuandoRepositoryFalharAoListarTodasAsPostagens() {
        // Simule o comportamento do repositório lançando uma exceção
        when(postagemRepository.listAll()).thenThrow(new RuntimeException("Erro ao listar postagens"));

        // Chame o serviço e capture a exceção
        final Exception e = assertThrows(Exception.class, () -> {
            postagemService.listarTodasPostagens();
        });

        // Verifique se a mensagem da exceção é a esperada
        assertThat(e.getMessage(), is("Erro ao listar postagens"));

        // Verifique que o método listAll() foi chamado no repositório
        verify(postagemRepository).listAll();

        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }


    @Test
    void deveBuscarPostagemPeloIdComSucesso() {
        // Usando o método when do Mockito para simular o comportamento do repositório
        when(postagemRepository.findByIdOptional(postagem.getId())).thenReturn(Optional.of(postagem)); // Use Optional.of para simular um resultado existente

        // Chame o método da classe de serviço que você deseja testar
        Postagem postagemBuscada = postagemService.buscarPostagemPorId(1L);

        // Verifique se a postagemBuscada é igual à postagem simulada
        assertEquals(postagem, postagemBuscada);

        // Verifique se o método findById(1L) foi chamado no repositório
        verify(postagemRepository).findByIdOptional(postagem.getId());

        // Certifique-se de que não haja mais interações não verificadas no repositório
        verifyNoMoreInteractions(postagemRepository);

    }

    @Test
    void deveRetornarExcessaoAoBuscarPostagemPeloIdInexistente() {
        // Configurar o comportamento do mock para retornar Optional vazio quando chamado com um ID inexistente
        when(postagemRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Chame o método da classe de serviço que você deseja testar, passando um ID inexistente
        assertThrows(WebApplicationException.class, () -> postagemService.buscarPostagemPorId(999L));

        // Verifique se o método findByIdOptional(999L) foi chamado no repositório
        verify(postagemRepository).findByIdOptional(999L);

        // Certifique-se de que não haja mais interações não verificadas no repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveRetornarExcessaoAoBuscarPostagemComIdNulo() {
        // Chame o método da classe de serviço sem fornecer um ID
        assertThrows(WebApplicationException.class, () -> postagemService.buscarPostagemPorId(null));

        // Verifique se o método lançou uma exceção WebApplicationException com status BAD_REQUEST
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> postagemService.buscarPostagemPorId(null));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        assertEquals("Para buscar uma postagem com o ID, o ID é obrigatório", e.getMessage());

        // Certificando que não houve chamadas para o repositório
        verifyNoInteractions(postagemRepository);

        // Certifique-se de que não haja mais interações não verificadas no repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveLancarExceptionQuandoRepositoryFalharAoBuscarPorId() {
        // Simule o comportamento do repositório lançando uma exceção
        when(postagemRepository.findByIdOptional(999L)).thenThrow(new RuntimeException("Erro ao buscar postagem com id: " + 999L));

        // Chame o serviço e capture a exceção
        final Exception e = assertThrows(Exception.class, () -> {
            postagemService.buscarPostagemPorId(999L);
        });

        // Verifique se a mensagem da exceção é a esperada
        assertThat(e.getMessage(), is("Erro ao buscar postagem com id: " + 999));

        // Verifique que o método listAll() foi chamado no repositório
        verify(postagemRepository).findByIdOptional(999L);

        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }
//###############################################3

    @Test
    void deveInserirPostagemComSucesso() {
        // Crie uma entidade Postagem com base nos dados do DTO
        Postagem p = new Postagem(dadosCadastroPostagemDTO.mensagem());

        // Chame o método para inserir a postagem
        Response response = postagemService.inserirPostagem(dadosCadastroPostagemDTO);

        // Verifique se a resposta tem o status NO_CONTENT (201)
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Verifique que o método persist() foi chamado no repositório com a entidade Postagem
        verify(postagemRepository).persist(ArgumentMatchers.any(Postagem.class));

        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveLancarExcessaoAoInserirPostagemSemMensagem() {
        // Dados de exemplo: Crie um DTO sem mensagem
        DadosCadastroPostagemDTO dadosCadastroPostagemDTO = new DadosCadastroPostagemDTO(null);

        // Capture a exceção ao chamar o método para inserir a postagem
        final WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.inserirPostagem(dadosCadastroPostagemDTO);
        });

        // Verifique se a mensagem da exceção é a esperada
        assertThat(e.getResponse().getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        assertThat(e.getMessage(), is("Para inserir uma postagem, a mensagem é obrigatória"));

        // Certifique-se de que não haja interações com o repositório
        verifyNoInteractions(postagemRepository);
    }

    @Test
    void deveLancarExceptionQuandoRepositoryFalharAoInserirPostagem() {

        // Configure o comportamento do postagemRepository para lançar uma exceção ao chamar o método persist
        doThrow(new RuntimeException("Erro ao persistir")).when(postagemRepository).persist(ArgumentMatchers.any(Postagem.class));

        // Capture a exceção ao chamar o método para inserir a postagem
        final WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.inserirPostagem(dadosCadastroPostagemDTO);
        });

        // Verifique se a mensagem da exceção é a esperada
        assertThat(e.getResponse().getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat(e.getMessage(), is("Erro ao adicionar postagem"));

        // Verifique que o método persist() foi chamado no repositório com a entidade Postagem
        verify(postagemRepository).persist(ArgumentMatchers.any(Postagem.class));

        // Certifique-se de que não haja mais interações não verificadas com o repositório
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveAtualizarPostagemExistenteComSucesso() {
        // Entidade de exemplo
        when(postagemRepository.findByIdOptional(1L)).thenReturn(Optional.of(postagem));

        // Chame o método para editar a postagem
        Postagem postagemEditada = postagemService.editarPostagemExistente(dadosAtualizacaoPostagemDTO);

        // Verifique se a postagem foi editada corretamente
        assertEquals(dadosAtualizacaoPostagemDTO.mensagem(), postagemEditada.getMensagem());
        assertEquals(dadosAtualizacaoPostagemDTO.id(), postagemEditada.getId());
        assertEquals(dadosAtualizacaoPostagemDTO.id(), postagem.getId());
        assertNotEquals(dadosCadastroPostagemDTO.mensagem(), postagem.getMensagem());


        // Verifique que o método persist() foi chamado no repositório com a entidade correta
        verify(postagemRepository).persist(postagem);
    }

    @Test
    void deveLancarExcessaoQuandoAtualizarMensagemEstiverComMensagemNula() {
        // Entidade de exemplo
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(1L, null);

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.editarPostagemExistente(postagemDTO);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        assertThat(e.getMessage(), is("Para editar uma postagem, a mensagem e o ID são obrigatórios"));

        // Certifique-se de que o método persist() não tenha sido chamado no repositório
        verify(postagemRepository, never()).persist(ArgumentMatchers.any(Postagem.class));
    }

    @Test
    void deveLancarExcessaoQuandoAtualizarMensagemEstiverComIdNulo() {
        // Entidade de exemplo
        DadosAtualizacaoPostagemDTO postagemDTO = new DadosAtualizacaoPostagemDTO(null, "Nova Mensagem");

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.editarPostagemExistente(postagemDTO);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        assertThat(e.getMessage(), is("Para editar uma postagem, a mensagem e o ID são obrigatórios"));

        // Certifique-se de que o método persist() não tenha sido chamado no repositório
        verify(postagemRepository, never()).persist(ArgumentMatchers.any(Postagem.class));
    }

    @Test
    void deveLancarExcessaoQuandoPostagemNaoExistir() {
        // Simule que a postagem não foi encontrada com base no valor do postagemDTO.id()
        when(postagemRepository.findByIdOptional(dadosAtualizacaoPostagemDTO.id())).thenReturn(Optional.empty());

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.editarPostagemExistente(dadosAtualizacaoPostagemDTO);
        });

        // Verifique se a exceção tem o status NOT_FOUND esperado
        assertThat(e.getResponse().getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        // Certifique-se de que o método persist() não tenha sido chamado no repositório
        verify(postagemRepository, never()).persist(ArgumentMatchers.any(Postagem.class));
    }


    @Test
    void deveLancarExcessaoQuandoRepositorioFalharAoAtualizarPostagem() {

        // Simule o repositório retornando uma postagem válida ao buscar por ID
        when(postagemRepository.findByIdOptional(1L)).thenReturn(Optional.of(postagem));

        // Simule o lançamento de uma exceção ao persistir uma postagem
        doThrow(new RuntimeException("Erro ao atualizar postagem com id: 1")).when(postagemRepository).persist(ArgumentMatchers.any(Postagem.class));

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.editarPostagemExistente(dadosAtualizacaoPostagemDTO);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat(e.getMessage(), is("Erro ao atualizar postagem com id: 1"));

        // Certifique-se de que o método persist() foi chamado no repositório
        verify(postagemRepository).findByIdOptional(1L);
        verify(postagemRepository).persist(ArgumentMatchers.any(Postagem.class));
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveDeletarPostagemComSucesso() {
        // Simule o repositório retornando uma postagem válida ao buscar por ID
        when(postagemRepository.findByIdOptional(1L)).thenReturn(Optional.of(postagem));

        // Simule a exclusão da postagem
        doNothing().when(postagemRepository).delete(postagem);

        // Chame o método para excluir a postagem
        postagemService.excluirPostagemExistente(1L);

        // Verifique se o método delete() foi chamado com a postagem correta
        verify(postagemRepository).delete(postagem);

        // Certifique-se de que nenhum outro método tenha sido chamado no repositório
        verifyNoMoreInteractions(postagemRepository);

    }

    @Test
    void deveLancarExcessaoQuandoDeletarPostagemEstiverComIdNulo() {

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.excluirPostagemExistente(null);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
        assertThat(e.getMessage(), is("Para excluir uma postagem, o ID é obrigatório"));

        // Certifique-se de que o método persist() não tenha sido chamado no repositório
        verify(postagemRepository, never()).delete(ArgumentMatchers.any(Postagem.class));
    }

    @Test
    void deveLancarExcessaoAoDeletarQuandoPostagemNaoExistir() {
        // Simule que a postagem não foi encontrada com base no valor do postagemDTO.id()
        when(postagemRepository.findByIdOptional(dadosAtualizacaoPostagemDTO.id())).thenReturn(Optional.empty());

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.excluirPostagemExistente(1L);
        });

        // Verifique se a exceção tem o status NOT_FOUND esperado
        assertThat(e.getResponse().getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        // Certifique-se de que o método delete() não tenha sido chamado no repositório
        verify(postagemRepository, never()).delete(ArgumentMatchers.any(Postagem.class));
    }

    @Test
    void deveLancarExcessaoQuandoRepositorioFalharAoDeletarPostagem() {

        // Simule o repositório retornando uma postagem válida ao buscar por ID
        when(postagemRepository.findByIdOptional(1L)).thenReturn(Optional.of(postagem));

        // Simule o lançamento de uma exceção ao persistir uma postagem
        doThrow(new RuntimeException("Erro interno ao deletar postagem!")).when(postagemRepository).delete(ArgumentMatchers.any(Postagem.class));

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.excluirPostagemExistente(1L);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat(e.getMessage(), is("Erro interno ao deletar postagem!"));


        // Certifique-se de que o método delete() não tenha sido chamado no repositório
        verify(postagemRepository).findByIdOptional(1L);
        verifyNoMoreInteractions(postagemRepository);
    }

    @Test
    void deveLancarExcessaoQuandoRepositorioFalharAoListarPostagens() {

        // Simule o repositório retornando uma postagem válida ao buscar por ID
        when(postagemRepository.listAll()).thenThrow(new RuntimeException("Erro interno ao listar postagens!"));

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.listarTodasPostagens();
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat(e.getMessage(), is("Erro ao listar postagens"));
    }

    @Test
    void deveLancarExcessaoQuandoRepositorioFalharAoBuscarPostagemPorId() {

        // Simule o repositório retornando uma postagem válida ao buscar por ID
        when(postagemRepository.findByIdOptional(1L)).thenThrow(new RuntimeException("Erro interno ao buscar postagem por id!"));

        // Chame o método para editar a postagem
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            postagemService.buscarPostagemPorId(1L);
        });

        // Verifique se a exceção tem a mensagem e o status esperados
        assertThat(e.getResponse().getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        assertThat(e.getMessage(), is("Erro ao buscar postagem com id: 1"));
    }

}
