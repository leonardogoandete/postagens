package br.com.doasanguepoa.postagem.controller;
import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.service.PostagemService;
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
    PostagemService postagemService;

    @Inject
    JsonWebToken jwt;

    @GET
    //@RolesAllowed({ "USUARIO","INSTITUICAO" })
    public List<DadosListagemPostagemDTO> listarPostagens() {

        return postagemService.listarPostagens()
                .stream()
                .map(DadosListagemPostagemDTO::new)
                .toList();
    }


    @GET
    @Path("/{id}")
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    public Postagem buscarPostagemPorId(@PathParam Long id) {
        Postagem entity = postagemService.buscarPostagemPorId(id);
        if (entity == null) {
            String mensagemErro = "Postagem com ID '" + id +"' não encontrada.";
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        return postagemService.buscarPostagemPorId(id);
    }

    @POST
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public void adicionarPostagem(@Valid DadosCadastroPostagemDTO postagemDTO) {
        String cnpjInstituicao = jwt.getClaim("upn");
        log.info("Adicionando postagem da instituicao: {0}", cnpjInstituicao);
        Postagem postagem = new Postagem(postagemDTO.titulo(), postagemDTO.mensagem());
        postagemService.adicionarPostagem(postagem);
    }

    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public Postagem atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        Postagem entity = postagemService.buscarPostagemPorId(postagemDTO.id());
        if (entity == null) {
            String mensagemErro = "Postagem com ID '" + postagemDTO.id() + "'não encontrada.";
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        if(postagemDTO.id() != null && postagemDTO.mensagem() != null) {
            entity.setMensagem(postagemDTO.mensagem());
            postagemService.atualizarPostagemPorId(entity);
        }else {
            String mensagemErroAtualizarValor = "A mensagem é obrigatória";
            throw new WebApplicationException(mensagemErroAtualizarValor, Response.Status.BAD_REQUEST);
        }
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "ADMIN","INSTITUICAO" })
    public void deletarPostagem(@PathParam Long id) {
        Postagem entity = postagemService.buscarPostagemPorId(id);
        if (entity == null) {
            String mensagemErro = "Postagem com ID" + id + "não encontrada.";
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }
        postagemService.deletarPostagem(id);
    }
}
