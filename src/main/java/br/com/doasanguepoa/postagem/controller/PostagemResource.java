package br.com.doasanguepoa.postagem.controller;
import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import java.util.List;

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
            return postagemRepository.listAll()
                    .stream()
                    .map(DadosListagemPostagemDTO::new)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao listar postagens", e);
            throw new WebApplicationException("Erro ao listar postagens", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("/{id}")
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    public Postagem buscarPostagemPorId(@PathParam Long id) {
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
    @RolesAllowed({ "INSTITUICAO" })
    public void adicionarPostagem(@Valid DadosCadastroPostagemDTO postagemDTO) {
        try {
            if(postagemDTO.mensagem() != null) {
                Postagem postagem = new Postagem(postagemDTO.mensagem());
                log.info("Adicionando postagem");
                postagemRepository.persist(postagem);
            }else {
                String mensagemErro = "A mensagem é obrigatória";
                log.error(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Erro ao adicionar postagem", e);
            throw new WebApplicationException("Erro ao adicionar postagem", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public Postagem atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        try {
            Postagem entity = postagemRepository.findById(postagemDTO.id());
            if (entity == null) {
                String mensagemErro = "Postagem com ID" + postagemDTO.id() + "não encontrada.";
                log.info(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
            }
            entity.setMensagem(postagemDTO.mensagem());
            log.info("Atualizando postagem com id: {0}", postagemDTO.id());
            return postagemRepository.getEntityManager().merge(entity);
        } catch (Exception e) {
            log.error("Erro ao atualizar postagem com id: {0}", postagemDTO.id(), e);
            throw new WebApplicationException("Erro ao atualizar postagem com id: " + postagemDTO.id(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "ADMIN","INSTITUICAO" })
    public void deletarPostagem(@PathParam Long id) {
        try {
            Postagem entity = postagemRepository.findById(id);
            if (entity == null) {
                String mensagemErro = "Postagem com ID" + id + "não encontrada.";
                log.info(mensagemErro);
                throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
            }
            log.info("Deletando postagem com id: {0}", id);
            postagemRepository.delete(entity);
        } catch (Exception e) {
            log.error("Erro ao deletar postagem com id: {0}", id, e);
            throw new WebApplicationException("Erro ao deletar postagem com id: " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}