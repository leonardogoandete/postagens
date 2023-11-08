package br.com.doasanguepoa.postagem.dto.postagem;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroPostagemDTO(@NotBlank String mensagem, @NotBlank String cnpj) {}
