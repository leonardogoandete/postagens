package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.mapper.PostagemMapper;

import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;

import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.service.PostagemService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.Collections;
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
public class PostagemResource{
    private final PostagemService postagemService;

    private final PostagemMapper postagemMapper;

    public PostagemResource(PostagemService postagemService, PostagemMapper postagemMapper){
        this.postagemService = postagemService;
        this.postagemMapper = postagemMapper;
    }

    @GET
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    public Response listarPostagens() {
        log.info("Buscando todas as postagens!");
        List<DadosListagemPostagemDTO> postagens;
        postagens = postagemMapper.toDadosListagemPostagem(postagemService.listarTodasPostagens());
        return Response.status(Response.Status.OK).entity(postagens).build();
    }

    @POST
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Response adicionarPostagem(@Valid DadosCadastroPostagemDTO dadosCadastroPostagemDTO) {
        log.info("Inserino nova postagem {}", dadosCadastroPostagemDTO);
        Postagem postagem = postagemMapper.toPostagem(dadosCadastroPostagemDTO);
        Postagem postagemSalva = postagemService.inserirPostagem(postagem);
        DadosListagemPostagemDTO dadosListagemPostagemDTO = postagemMapper.toDadosListagemPostagem(postagemSalva);
        return Response.status(Response.Status.CREATED).entity(dadosListagemPostagemDTO).build();
    }

    //#############################3


    @GET
    @Path("/{id}")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Response buscarPostagemPorId(@PathParam Long id) {
        log.info("Buscando postagem por ID {}", id);
        return postagemService.buscarPostagemPorId(id)
                .map(postagemMapper::toDadosListagemPostagem)
                .map(dadosListagemPostagemDTO -> Response.status(Response.Status.OK).entity(dadosListagemPostagemDTO))
                .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    @Path("/instituicao/{nomeInstituicao}")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Response buscarPostagemPorNomeInstituicao(@PathParam String nomeInstituicao) {
        log.info("Buscando postagem por nome da instituição {}", nomeInstituicao);

        List<DadosListagemPostagemDTO> postagens;
        postagens = Optional.ofNullable(postagemService.listarPostagensPorInstituicao(nomeInstituicao))
                .map(list -> list.stream()
                        .map(postagemMapper::toDadosListagemPostagem)
                        .toList())
                .orElse(Collections.emptyList());
        return Response.status(Response.Status.OK).entity(postagens).build();
    }



    @PUT
    //@Path("/{id}")
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Response atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        log.info("Atualizando postagem {}", postagemDTO.id());
        Postagem postagem = postagemService.editarPostagemExistente(postagemDTO);
        return Response.status(Response.Status.OK).entity(postagemMapper.toDadosListagemPostagem(postagem)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"ADMIN", "INSTITUICAO"})
    public Response deletarPostagem(@PathParam Long id) {
        log.info("Excluindo postagem com id {}", id);
        postagemService.excluirPostagemExistente(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}