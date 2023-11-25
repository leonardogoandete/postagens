package br.com.doasanguepoa.postagem.controller;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.mapper.PostagemMapper;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.service.PostagemService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
import java.util.logging.Level;
import java.util.logging.Logger;

// Link útil: https://www.linkedin.com/pulse/tutorial-quarkus-simplificando-o-hibernate-panache-da-silva-melo/?originalSubdomain=pt

// Recurso para manipulação de postagens
@Path("/postagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(scheme = "Bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class PostagemResource {
    private static final Logger logger = Logger.getLogger(PostagemResource.class.getName());
    private final PostagemService postagemService;
    private final JsonWebToken jwt;
    private final PostagemMapper postagemMapper;

    // Injeção de dependências nos parâmetros do construtor
    public PostagemResource(PostagemService postagemService, PostagemMapper postagemMapper, JsonWebToken jwt) {
        this.postagemService = postagemService;
        this.postagemMapper = postagemMapper;
        this.jwt = jwt;
    }

    // Listar todas as postagens (requer roles de USUARIO ou INSTITUICAO)
    @GET
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    public Response listarPostagens() {
        logger.info("Buscando todas as postagens!");
        String cnpj = jwt.getClaim("upn");  // Recuperar CNPJ do token
        logger.info(cnpj);
        List<DadosListagemPostagemDTO> postagens;
        postagens = postagemMapper.toDadosListagemPostagem(postagemService.listarTodasPostagens());
        return Response.status(Response.Status.OK).entity(postagens).build();
    }

    // Adicionar nova postagem (requer role de INSTITUICAO)
    @POST
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Response adicionarPostagem(@Valid DadosCadastroPostagemDTO dadosCadastroPostagemDTO) {
        logger.log(Level.INFO,"Inserindo nova postagem {}", dadosCadastroPostagemDTO);
        Postagem postagem = postagemMapper.toPostagem(dadosCadastroPostagemDTO);
        Postagem postagemSalva = postagemService.inserirPostagem(postagem);
        DadosListagemPostagemDTO dadosListagemPostagemDTO = postagemMapper.toDadosListagemPostagem(postagemSalva);
        return Response.status(Response.Status.CREATED).entity(dadosListagemPostagemDTO).build();
    }

    // Buscar postagem por ID (requer roles de USUARIO ou INSTITUICAO)
    @GET
    @Path("/{id}")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Response buscarPostagemPorId(@PathParam Long id) {
        logger.log(Level.INFO,"Buscando postagem por ID {}", id);
        return postagemService.buscarPostagemPorId(id)
                .map(postagemMapper::toDadosListagemPostagem)
                .map(dadosListagemPostagemDTO -> Response.status(Response.Status.OK).entity(dadosListagemPostagemDTO))
                .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    // Buscar postagem por nome da instituição (requer roles de USUARIO ou INSTITUICAO)
    @GET
    @Path("/instituicao/")
    @RolesAllowed({"USUARIO", "INSTITUICAO"})
    public Response buscarPostagemPorNomeInstituicao() {
        String cnpjInstituicao = jwt.getClaim("upn");
        logger.log(Level.INFO,"Buscando postagem com CNPJ da instituição {0}", cnpjInstituicao);
        List<DadosListagemPostagemDTO> postagens;
        postagens = Optional.ofNullable(postagemService.listarPostagensPorInstituicao(cnpjInstituicao))
                .map(list -> list.stream()
                        .map(postagemMapper::toDadosListagemPostagem)
                        .toList())
                .orElse(Collections.emptyList());
        return Response.status(Response.Status.OK).entity(postagens).build();
    }

    // Atualizar postagem (requer role de INSTITUICAO)
    @PUT
    @Transactional
    @RolesAllowed({"INSTITUICAO"})
    public Response atualizarPostagem(@Valid DadosAtualizacaoPostagemDTO postagemDTO) {
        logger.log(Level.INFO,"Atualizando postagem {}", postagemDTO.id());
        Postagem postagem = postagemService.editarPostagemExistente(postagemDTO);
        return Response.status(Response.Status.OK).entity(postagemMapper.toDadosListagemPostagem(postagem)).build();
    }

    // Deletar postagem por ID (requer roles de ADMIN ou INSTITUICAO)
    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"ADMIN", "INSTITUICAO"})
    public Response deletarPostagem(@PathParam Long id) {
        logger.log(Level.INFO,"Excluindo postagem com id {}", id);
        postagemService.excluirPostagemExistente(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}