package br.com.doasanguepoa.postagem.controller;
import br.com.doasanguepoa.postagem.dto.postagem.PostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.service.PostagemService;
import jakarta.annotation.security.PermitAll;
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

import java.util.ArrayList;
import java.util.List;

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
    @PermitAll
    public List<PostagemDTO> listarPostagens(@HeaderParam("Host") String host) {
        List<PostagemDTO> postagensDTO = new ArrayList<>();

        try{
            List<Postagem> postagens = postagemService.listarPostagens();
            for(Postagem postagem: postagens){
                postagensDTO.add(new PostagemDTO(postagem.getTitulo(),postagem.getMensagem()));
            }
            log.info("Exibindo postagens para o front {0}", host);
        }catch (Exception e){
            e.printStackTrace();
        }
        return postagensDTO;
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
    public void adicionarPostagem(@Valid PostagemDTO postagemDTO) {
        String cnpjInstituicao = jwt.getClaim("upn");
        log.info("Adicionando postagem da instituicao: {0}", cnpjInstituicao);
        Postagem postagem = new Postagem(postagemDTO.titulo(), postagemDTO.mensagem());
        postagemService.adicionarPostagem(postagem);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({ "INSTITUICAO" })
    public Postagem atualizarPostagem(@PathParam Long id, @Valid Postagem postagem) {
        Postagem entity = postagemService.buscarPostagemPorId(id);
        if (entity == null) {
            String mensagemErro = "Postagem com ID '" + id + "'não encontrada.";
            throw new WebApplicationException(mensagemErro, Response.Status.NOT_FOUND);
        }

        entity.setTitulo(postagem.getTitulo());
        entity.setMensagem(postagem.getMensagem());

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
