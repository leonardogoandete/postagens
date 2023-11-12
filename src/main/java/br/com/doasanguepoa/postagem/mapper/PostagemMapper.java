package br.com.doasanguepoa.postagem.mapper;

import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface PostagemMapper {

    // Converte uma entidade Postagem para um DTO de listagem
    DadosListagemPostagemDTO toDadosListagemPostagem(Postagem postagem);

    // Converte uma lista de entidades Postagem para uma lista de DTOs de listagem
    List<DadosListagemPostagemDTO> toDadosListagemPostagem(List<Postagem> postagens);

    // Converte um DTO de cadastro para uma entidade Postagem
    Postagem toPostagem(DadosCadastroPostagemDTO dadosCadastroPostagemDTO);
}
