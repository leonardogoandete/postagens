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
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
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
    private PostagemService postagemService;

    public PostagemResource(PostagemService postagemService){
        this.postagemService = postagemService;
    }

    @GET
    //@RolesAllowed({ "USUARIO","INSTITUICAO" })
    public List<DadosListagemPostagemDTO> listarPostagens() {
        log.info("Buscando todas as postagens!");
        return postagemService.listarTodasPostagens();
    }


    @GET
    @Path("/{id}")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Postagem buscarPostagemPorId(@PathParam Long id) {
        log.info("Buscando postagem por ID {}", id);
        return postagemService.buscarPostagemPorId(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Response adicionarPostagem(@Valid DadosCadastroPostagemDTO postagemDTO) {
        log.info("Inserino nova postagem {}", postagemDTO);
        return postagemService.inserirPostagem(postagemDTO);
    }

    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Postagem atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        log.info("Atualizando postagem {}", postagemDTO.id());
        return postagemService.editarPostagemExistente(postagemDTO);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"ADMIN", "INSTITUICAO"})
    public Response deletarPostagem(@PathParam Long id) {
        log.info("Excluindo postagem com id {}", id);
        return postagemService.excluirPostagemExistente(id);
    }
}