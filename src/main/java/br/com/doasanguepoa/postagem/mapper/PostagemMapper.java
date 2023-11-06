package br.com.doasanguepoa.postagem.mapper;

import br.com.doasanguepoa.postagem.dto.postagem.DadosAtualizacaoPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosCadastroPostagemDTO;
import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import br.com.doasanguepoa.postagem.model.Postagem;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface PostagemMapper {

    DadosListagemPostagemDTO toDadosListagemPostagem(Postagem postagem);
    DadosAtualizacaoPostagemDTO toDadosAtualizacaoPostagem(Postagem postagem);

    List<DadosListagemPostagemDTO> toDadosListagemPostagem(List<Postagem> postagens);

    DadosCadastroPostagemDTO toDadosCadastroPostagem(Postagem postagem);

    Postagem toPostagem(DadosListagemPostagemDTO dadosListagemPostagemDTO);
    List<Postagem> toListPostagemDadosListagens(List<DadosListagemPostagemDTO> dadosListagemPostagemDTO);
    Postagem toPostagem(DadosCadastroPostagemDTO dadosCadastroPostagemDTO);

}
