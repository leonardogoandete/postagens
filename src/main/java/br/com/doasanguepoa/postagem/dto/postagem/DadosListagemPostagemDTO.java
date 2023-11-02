package br.com.doasanguepoa.postagem.dto.postagem;

import br.com.doasanguepoa.postagem.model.Postagem;

public record DadosListagemPostagemDTO(Long id,String mensagem) {
        public DadosListagemPostagemDTO(Postagem postagem){
                this(postagem.getId(), postagem.getMensagem());
        }
}
