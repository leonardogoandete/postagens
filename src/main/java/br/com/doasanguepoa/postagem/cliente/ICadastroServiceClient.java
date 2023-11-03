package br.com.doasanguepoa.postagem.cliente;


import br.com.doasanguepoa.postagem.model.Instituicao;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
@RegisterRestClient(baseUri = "http://localhost:8080", configKey = "cadastro-service")
public interface ICadastroServiceClient {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    Instituicao buscarInstituicaoPorId(@PathParam("id") Long id);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "USUARIO","INSTITUICAO" })
    Instituicao buscarInstituicaoPorNome(String nomeInstituicao);
}
