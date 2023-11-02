package br.com.doasanguepoa.postagem.dto.postagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPostagemDTO(
        @NotNull
        Long id,
        @NotBlank
        String mensagem) {}
