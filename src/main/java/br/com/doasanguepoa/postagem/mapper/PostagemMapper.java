package br.com.doasanguepoa.postagem.mapper;

import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface PostagemMapper {

    DadosListagemPostagemDTO toDadosListagemPostagem(Postagem postagemSalva);

    List<DadosListagemPostagemDTO> toDadosListagemPostagem(List<Postagem> postagems);

    DadosCadastroPostagemDTO toDadosCadastroPostagem(Postagem postagem);

    Postagem toPostagem(DadosListagemPostagemDTO dadosListagemPostagemDTO);
    Postagem toPostagem(DadosCadastroPostagemDTO dadosCadastroPostagemDTO);

}
