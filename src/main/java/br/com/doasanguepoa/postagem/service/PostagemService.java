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
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
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
    //regras de negocio vão aqui
    @Inject
    @RestClient
    ICadastroServiceClient cadastroServiceClient;
    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    public List<Postagem> listarTodasPostagens() {

        List<Postagem> postagens = postagemRepository.listAll();

        if (postagens.isEmpty()) {
            log.warn("Nenhuma postagem encontrada");
            return Collections.emptyList();
        }
        return postagens;
    }

    public Optional<Postagem> buscarPostagemPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para buscar uma postagem.");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);
        if (optionalEntity.isEmpty()) {
            log.info("Postagem com ID " + id + " inexistente.");
            throw new NotFoundException("Postagem com ID " + id + " inexistente.");
        }
        return optionalEntity;
    }

    public Postagem inserirPostagem(Postagem postagem) {
        if (postagem.getMensagem() == null) {
            throw new IllegalArgumentException("A Mensagem é obrigatório para inserir a postagem");
        }
        postagemRepository.persist(postagem);
        return postagemRepository.isPersistent(postagem) ? postagem : null;

    }

    public Postagem editarPostagemExistente(DadosAtualizacaoPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null || postagemDTO.id() == null) {
            log.error("Para editar uma postagem, a mensagem e o ID são obrigatórios");
            throw new IllegalArgumentException("Para editar uma postagem, a mensagem e o ID são obrigatórios");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(postagemDTO.id());
        if (optionalEntity.isEmpty()) {
            String mensagemErro = "Postagem com ID " + postagemDTO.id() + " inexistente.";
            log.info(mensagemErro);
            throw new NotFoundException("Postagem com ID " + postagemDTO.id() + " inexistente.");
        }
        Postagem entity = optionalEntity.get();
        entity.setMensagem(postagemDTO.mensagem());
        postagemRepository.persist(entity);
        log.info("Atualizando postagem com id: {}", postagemDTO.id());
        return entity;

    }

    public void excluirPostagemExistente(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para excluir uma postagem.");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);

        if (optionalEntity.isEmpty()) {
            log.info("Postagem com ID " + id + " não encontrada.");
            throw new NotFoundException("Postagem com ID " + id + " não encontrada.");
        }

        Postagem entity = optionalEntity.get();
        postagemRepository.delete(entity);

    }

    //Listar postagem por instituicao
    public List<Postagem> listarPostagensPorInstituicao(String nomeInstituicao) {
        String cnpjInstituicao = cadastroServiceClient.buscarInstituicaoPorNome(nomeInstituicao).cnpj();

        if (nomeInstituicao == null) {
            throw new IllegalArgumentException("Para listar postagens por instituicao, o CNPJ é obrigatório");
        }


        List<Postagem> postagens = postagemRepository.findByInstituicao(cnpjInstituicao);

        if (postagens.isEmpty()) {
            log.warn("Nenhuma postagem encontrada");
            return Collections.emptyList();
        }

        return postagens;
    }
}
