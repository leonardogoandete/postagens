package br.com.doasanguepoa.postagem.dto.postagem;

import br.com.doasanguepoa.postagem.model.Postagem;
import lombok.Getter;
import lombok.Setter;


public record DadosListagemPostagemDTO(Long id,String mensagem) {
        public DadosListagemPostagemDTO(Postagem postagem){
                this(postagem.getId(), postagem.getMensagem());
        }
}
