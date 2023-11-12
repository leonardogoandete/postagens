package br.com.doasanguepoa.postagem.service;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import br.com.doasanguepoa.postagem.repository.PostagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class PostagemService {
    // Regras de negócio vão aqui
    private static final Logger logger = Logger.getLogger(PostagemService.class.getName());
    private static final String MSG_INICIAL = "Postagem com ID ";
    private static final String MSG_INEXISTENTE = " inexistente.";
    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    // Listar todas as postagens
    public List<Postagem> listarTodasPostagens() {
        List<Postagem> postagens = postagemRepository.listAll();

        if (postagens.isEmpty()) {
            logger.log(Level.WARNING, "Nenhuma postagem encontrada");
            return Collections.emptyList();
        }
        return postagens;
    }

    // Buscar postagem por ID
    public Optional<Postagem> buscarPostagemPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para buscar uma postagem.");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);
        if (optionalEntity.isEmpty()) {
            logger.log(Level.INFO, "Postagem com ID {} inexistente.", id);
            throw new NotFoundException(MSG_INICIAL + id + MSG_INEXISTENTE);
        }
        return optionalEntity;
    }

    // Inserir nova postagem
    public Postagem inserirPostagem(Postagem postagem) {
        if (postagem.getMensagem() == null) {
            throw new IllegalArgumentException("A Mensagem é obrigatória para inserir a postagem");
        }
        postagemRepository.persist(postagem);
        return postagemRepository.isPersistent(postagem) ? postagem : null;
    }

    // Editar postagem existente
    public Postagem editarPostagemExistente(DadosAtualizacaoPostagemDTO postagemDTO) {
        if (postagemDTO.mensagem() == null) {
            logger.log(Level.WARNING, "Para editar uma postagem, a mensagem é obrigatória");
            throw new IllegalArgumentException("Para editar uma postagem, a mensagem é obrigatória");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(postagemDTO.id());
        if (optionalEntity.isEmpty()) {
            String mensagemErro = MSG_INICIAL + postagemDTO.id() + MSG_INEXISTENTE;
            logger.log(Level.INFO, mensagemErro);
            throw new NotFoundException(MSG_INICIAL + postagemDTO.id() + MSG_INEXISTENTE);
        }
        Postagem entity = optionalEntity.get();
        entity.setMensagem(postagemDTO.mensagem());
        postagemRepository.persist(entity);
        logger.log(Level.INFO, "Atualizando postagem com id: {}", postagemDTO.id());
        return entity;
    }

    // Excluir postagem existente
    public void excluirPostagemExistente(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para excluir uma postagem.");
        }

        Optional<Postagem> optionalEntity = postagemRepository.findByIdOptional(id);

        if (optionalEntity.isEmpty()) {
            String msg = MSG_INICIAL.concat(id.toString()).concat(" não encontrada.");
            logger.log(Level.INFO, msg);
            throw new NotFoundException(MSG_INICIAL + id + " não encontrada.");
        }

        Postagem entity = optionalEntity.get();
        postagemRepository.delete(entity);
    }

    // Listar postagens por instituicao
    public List<Postagem> listarPostagensPorInstituicao(String cnpjInstituicao) {
        if (cnpjInstituicao == null) {
            throw new IllegalArgumentException("Para listar postagens por instituição, o CNPJ é obrigatório");
        }

        List<Postagem> postagens = postagemRepository.findByCnpj(cnpjInstituicao);

        if (postagens.isEmpty()) {
            logger.log(Level.WARNING, "Nenhuma postagem encontrada");
            return Collections.emptyList();
        }

        return postagens;
    }
}