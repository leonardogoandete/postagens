package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.cliente.ICadastroServiceClient;
import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
@Transactional
public class PostagemService {
    @Inject
    @RestClient
    ICadastroServiceClient cadastroServiceClient;
    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    public List<Postagem> listarTodasPostagens() {
        try {
            List<Postagem> postagens = postagemRepository.listAll();

            if (postagens.isEmpty()) {
                log.warn("Nenhuma postagem encontrada");
                return Collections.emptyList();
            }

            return postagens;
        } catch (Exception e) {
            log.error("Erro ao listar postagens", e);
            throw new WebApplicationException("Erro ao listar postagens", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Postagem> buscarPostagemPorId(Long id) {
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

            return optionalEntity;

        } catch (Exception e) {
            log.error("Erro ao buscar postagem com id: {}", id, e);
            throw new WebApplicationException("Erro ao buscar postagem com id: " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Postagem inserirPostagem(Postagem postagem) {
        postagemRepository.persist(postagem);
        if (postagemRepository.isPersistent(postagem)) return postagem;
        else return null;
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

    public void excluirPostagemExistente(Long id) {
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
            postagemRepository.delete(entity);
        } catch (WebApplicationException e) {
            // Você pode lidar com exceções específicas aqui, se necessário.
            throw e;
        } catch (Exception e) {
            log.error("Erro interno ao deletar postagem com id: {}", id, e);
            throw new WebApplicationException("Erro interno ao deletar postagem!", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //Listar postagem por instituicao
    public List<Postagem> listarPostagensPorInstituicao(String nomeInstituicao){
        String cnpjInstituicao = cadastroServiceClient.buscarInstituicaoPorNome(nomeInstituicao).cnpj();

        if (cnpjInstituicao == null) {
            String mensagemErro = "Para listar postagens por instituicao, o CNPJ é obrigatório";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {

            List<Postagem> postagens = postagemRepository.findByInstituicao(cnpjInstituicao);

            if (postagens.isEmpty()) {
                log.warn("Nenhuma postagem encontrada");
                return Collections.emptyList();
            }

            return postagens;
        } catch (Exception e) {
            log.error("Erro ao listar postagens", e);
            throw new WebApplicationException("Erro ao listar postagens por instituição", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
