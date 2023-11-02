package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.List;
import java.util.Optional;

//https://www.linkedin.com/pulse/tutorial-quarkus-simplificando-o-hibernate-panache-da-silva-melo/?originalSubdomain=pt
@Slf4j
@Path("/postagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT")
public class PostagemResource {

    private final PostagemRepository postagemRepository;
    private final JsonWebToken jwt;

    PostagemResource(PostagemRepository postagemRepository, JsonWebToken jwt) {
        this.postagemRepository = postagemRepository;
        this.jwt = jwt;
    }

    @GET
    //@RolesAllowed({ "USUARIO","INSTITUICAO" })
    public List<DadosListagemPostagemDTO> listarPostagens() {
        try {
            log.info("Listando postagens");
            List<Postagem> postagens = postagemRepository.listAll();
            return postagens.stream()
                    .map(DadosListagemPostagemDTO::new)
                    .toList();
        } catch (NoResultException e) {
            log.error("Nenhuma postagem encontrada", e);
            throw new WebApplicationException("Nenhuma postagem encontrada", Response.Status.NOT_FOUND);
        } catch (Exception e) {
            log.error("Erro ao listar postagens", e);
            throw new WebApplicationException("Erro ao listar postagens", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("/{id}")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Postagem buscarPostagemPorId(@PathParam Long id) {
        if (id == null) {
            String mensagemErro = "O ID é obrigatório";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {
            log.info("Buscando postagem com id: {0}", id);
            return postagemRepository.findById(id);
        } catch (Exception e) {
            log.error("Erro ao buscar postagem com id: {0}", id, e);
            throw new WebApplicationException("Erro ao buscar postagem com id: " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public void adicionarPostagem(@Valid DadosCadastroPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null) {
            String mensagemErro = "A mensagem é obrigatória";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {
            Postagem postagem = new Postagem(postagemDTO.mensagem());
            log.info("Adicionando postagem");
            postagemRepository.persist(postagem);
        } catch (Exception e) {
            log.error("Erro ao adicionar postagem", e);
            throw new WebApplicationException("Erro ao adicionar postagem", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Postagem atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null || postagemDTO.id() == null) {
            String mensagemErro = "A mensagem e o ID são obrigatórios";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }

        try {
            Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(postagemDTO.id());
            if (optionalEntity.isEmpty()) {
                String mensagemErro = "Postagem com ID " + postagemDTO.id() + " inexistente.";
                log.info(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
            }
            Postagem entity = optionalEntity.get();
            entity.setMensagem(postagemDTO.mensagem());
            log.info("Atualizando postagem com id: {0}", postagemDTO.id());
            postagemRepository.persist(entity);
            return entity;
        } catch (WebApplicationException e) {
            // Você pode lidar com exceções específicas aqui, se necessário.
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar postagem com id: {}", postagemDTO.id(), e);
            throw new WebApplicationException("Erro ao atualizar postagem com id: " + postagemDTO.id(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"ADMIN", "INSTITUICAO"})
    public void deletarPostagem(@PathParam Long id) {
        if (id == null) {
            String mensagemErro = "O ID é obrigatório";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
        }
        try {
            Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);

            if (optionalEntity.isEmpty()) {
                String mensagemErro = "Postagem com ID " + id + " não encontrada.";
                log.info(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
            }

            Postagem entity = optionalEntity.get();

            log.info("Deletando postagem com id: {}", id);
            postagemRepository.delete(entity);

        } catch (WebApplicationException e) {
            // Você pode lidar com exceções específicas aqui, se necessário.
            throw e;
        } catch (Exception e) {
            log.error("Erro interno ao deletar postagem com id: {}", id, e);
            throw new WebApplicationException("Erro interno ao deletar postagem!", Response.Status.INTERNAL_SERVER_ERROR);
        }

    }
}