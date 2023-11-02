package br.com.doasanguepoa.postagem.dto.postagem;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroPostagemDTO(
        @NotBlank // o NotNull está dentro do NotBlank
        String mensagem) {
}
