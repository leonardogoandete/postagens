package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class PostagemService {
    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    public List<DadosListagemPostagemDTO> listarTodasPostagens() {
        try {
            List<Postagem> postagens = postagemRepository.listAll();

            if (postagens.isEmpty()) {
                log.warn("Nenhuma postagem encontrada");
                return Collections.emptyList();
            }

            return postagens.stream()
                    .map(DadosListagemPostagemDTO::new)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao listar postagens", e);
            throw new WebApplicationException("Erro ao listar postagens", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Postagem buscarPostagemPorId(Long id) {
        if (id == null) {
            String mensagemErro = "Para buscar uma postagem com o ID, o ID é obrigatório";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {

            Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);
            if (optionalEntity.isEmpty()) {
                String mensagemErro = "Postagem com ID " + id + " inexistente.";
                log.info(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
            }

            return optionalEntity.get();

        } catch (Exception e) {
            log.error("Erro ao buscar postagem com id: {}", id, e);
            throw new WebApplicationException("Erro ao buscar postagem com id: " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response inserirPostagem(DadosCadastroPostagemDTO dadosCadastroPostagemDTO) {
        if (dadosCadastroPostagemDTO.mensagem() == null) {
            String mensagemErro = "Para inserir uma postagem, a mensagem é obrigatória";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {
            Postagem postagem = new Postagem(dadosCadastroPostagemDTO.mensagem());
            postagemRepository.persist(postagem);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Erro ao adicionar postagem", e);
            throw new WebApplicationException("Erro ao adicionar postagem", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Postagem editarPostagemExistente(DadosAtualizacaoPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null || postagemDTO.id() == null) {
            String mensagemErro = "Para editar uma postagem, a mensagem e o ID são obrigatórios";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(postagemDTO.id());
        if (optionalEntity.isEmpty()) {
            String mensagemErro = "Postagem com ID " + postagemDTO.id() + " inexistente.";
            log.info(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        try {
            Postagem entity = optionalEntity.get();
            entity.setMensagem(postagemDTO.mensagem());
            postagemRepository.persist(entity);
            log.info("Atualizando postagem com id: {}", postagemDTO.id());
            return entity;
        } catch (Exception e) {
            log.error("Erro ao atualizar postagem com id: {}", postagemDTO.id(), e);
            throw new WebApplicationException("Erro ao atualizar postagem com id: " + postagemDTO.id(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response excluirPostagemExistente(Long id) {
        if (id == null) {
            String mensagemErro = "Para excluir uma postagem, o ID é obrigatório";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);

        if (optionalEntity.isEmpty()) {
            String mensagemErro = "Postagem com ID " + id + " não encontrada.";
            log.info(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }

        try {
            Postagem entity = optionalEntity.get();

            log.info("Deletando postagem com id: {}", id);
            postagemRepository.delete(entity);

            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (WebApplicationException e) {
            // Você pode lidar com exceções específicas aqui, se necessário.
            throw e;
        } catch (Exception e) {
            log.error("Erro interno ao deletar postagem com id: {}", id, e);
            throw new WebApplicationException("Erro interno ao deletar postagem!", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
