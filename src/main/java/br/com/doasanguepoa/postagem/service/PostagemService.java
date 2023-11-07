package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.cliente.ICadastroServiceClient;
import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class PostagemService {
    //regras de negocio vão aqui
    private static final Logger logger = Logger.getLogger(PostagemService.class.getName());
    private static final String MSG_INICIAL = "Postagem com ID ";
    private static final String MSG_INEXISTENTE = " inexistente.";
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
            logger.log(Level.WARNING,"Nenhuma postagem encontrada");
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
            logger.log(Level.INFO,"Postagem com ID {} inexistente.", id);
            throw new NotFoundException(MSG_INICIAL + id + MSG_INEXISTENTE);
        }
        return optionalEntity;
    }

    public Postagem inserirPostagem(Postagem postagem) {
        //TO-DO pegar do JWT o CNPJ da instituicao
        if (postagem.getMensagem() == null) {
            throw new IllegalArgumentException("A Mensagem é obrigatório para inserir a postagem");
        }
        postagemRepository.persist(postagem);
        return postagemRepository.isPersistent(postagem) ? postagem : null;

    }

    public Postagem editarPostagemExistente(DadosAtualizacaoPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null) {
            logger.log(Level.WARNING, "Para editar uma postagem, a mensagem é obrigatórios");
            throw new IllegalArgumentException("Para editar uma postagem, a mensagem é obrigatórios");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(postagemDTO.id());
        if (optionalEntity.isEmpty()) {
            String mensagemErro = MSG_INICIAL + postagemDTO.id() + MSG_INEXISTENTE;
            logger.log(Level.INFO,mensagemErro);
            throw new NotFoundException(MSG_INICIAL + postagemDTO.id() + MSG_INEXISTENTE);
        }
        Postagem entity = optionalEntity.get();
        entity.setMensagem(postagemDTO.mensagem());
        postagemRepository.persist(entity);
        logger.log(Level.INFO,"Atualizando postagem com id: {}", postagemDTO.id());
        return entity;

    }

    public void excluirPostagemExistente(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para excluir uma postagem.");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);

        if (optionalEntity.isEmpty()) {
            String msg = MSG_INICIAL.concat(id.toString()).concat(" não encontrada.");
            logger.log(Level.INFO,msg);
            throw new NotFoundException(MSG_INICIAL + id + " não encontrada.");
        }

        Postagem entity = optionalEntity.get();
        postagemRepository.delete(entity);

    }

    //Listar postagem por instituicao
    public List<Postagem> listarPostagensPorInstituicao(String nomeInstituicao) {
        //TO-DO definir dono da postagem usando o CNPJ da instituicao
        String cnpjInstituicao = cadastroServiceClient.buscarInstituicaoPorNome(nomeInstituicao).cnpj();

        if (nomeInstituicao == null) {
            throw new IllegalArgumentException("Para listar postagens por instituicao, o CNPJ é obrigatório");
        }


        List<Postagem> postagens = postagemRepository.findByInstituicao(cnpjInstituicao);

        if (postagens.isEmpty()) {
            logger.log(Level.WARNING,"Nenhuma postagem encontrada");
            return Collections.emptyList();
        }

        return postagens;
    }
}
