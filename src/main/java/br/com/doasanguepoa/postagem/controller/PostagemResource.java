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

    @Inject
    PostagemRepository postagemRepository;

    @Inject
    JsonWebToken jwt;

    @GET
    //@RolesAllowed({ "USUARIO","INSTITUICAO" })
    public List<DadosListagemPostagemDTO> listarPostagens() {
        log.info("Listando postagens");
        return postagemRepository.listAll()
                .stream()
                .map(DadosListagemPostagemDTO::new)
                .toList();
    }


    @GET
    @Path("/{id}")
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    public Postagem buscarPostagemPorId(@PathParam Long id) {
        Postagem entity = postagemRepository.findById(id);
        if (entity == null) {
            String mensagemErro = "Postagem com ID '" + id +"' não encontrada.";
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        log.info("Buscando postagem por id: {0}", id);
        return entity;
    }

    @POST
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public void adicionarPostagem(@Valid DadosCadastroPostagemDTO postagemDTO) {
        String cnpjInstituicao = jwt.getClaim("upn");
        Postagem postagem = new Postagem(postagemDTO.mensagem());
        log.info("Adicionando postagem da instituicao: {0}", cnpjInstituicao);
        postagemRepository.persist(postagem);
    }

    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public Postagem atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        Postagem entity = postagemRepository.findById(postagemDTO.id());
        if (entity == null) {
            String mensagemErro = "Postagem com ID '" + postagemDTO.id() + "'não encontrada.";
            log.error(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        if(postagemDTO.id() != null && postagemDTO.mensagem() != null) {
            entity.setMensagem(postagemDTO.mensagem());
            log.info("Atualizando postagem com id: {0}", postagemDTO.id());
            postagemRepository.persist(entity);
        }else {
            String mensagemErroAtualizarValor = "A mensagem é obrigatória";
            log.error(mensagemErroAtualizarValor);
            throw new WebApplicationException(mensagemErroAtualizarValor, Response.Status.BAD_REQUEST);
        }
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "ADMIN","INSTITUICAO" })
    public void deletarPostagem(@PathParam Long id) {
        Postagem entity = postagemRepository.findById(id);
        if (entity == null) {
            String mensagemErro = "Postagem com ID" + id + "não encontrada.";
            log.info(mensagemErro);
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        log.info("Deletando postagem com id: {0}", id);
        postagemRepository.deleteById(id);
    }
}