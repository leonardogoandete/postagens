package br.com.doasanguepoa.postagem.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PostagemTest {

    @Test
    void testConstrutorEPropriedadesComDataCriacaoEDataAtualizacao() {
        String mensagem = "Teste de mensagem";
        String cnpj = "87020517000120";
        Instant createdAt = Instant.now();
        Instant updateAt = Instant.now().plusSeconds(200);
        Postagem postagem = new Postagem(mensagem,cnpj,createdAt,updateAt);
        postagem.setId(1L);

        assertNotNull(postagem.getId());
        assertEquals(mensagem, postagem.getMensagem());
        //assertEquals(cnpj, postagem.getCnpj());
        assertEquals(createdAt, postagem.getCreatedAt());
        assertEquals(updateAt, postagem.getUpdateAt());
        assertNotNull(postagem.getCreatedAt());
        assertNotNull(postagem.getUpdateAt());
    }

    @Test
    void testConstrutorEPropriedades() {
        String mensagem = "Teste de mensagem";
        String cnpj = "87020517000120";
        Postagem postagem = new Postagem(mensagem,cnpj);
        postagem.setId(1L);

        assertNotNull(postagem.getId());
        assertEquals(mensagem, postagem.getMensagem());
        assertNotNull(postagem.getCreatedAt());
        assertNotNull(postagem.getUpdateAt());
    }

    @Test
    void testAtualizacaoDaMensagem() {
        String mensagemOriginal = "Mensagem original";
        String cnpj = "87020517000120";
        Postagem postagem = new Postagem(mensagemOriginal,cnpj);

        String novaMensagem = "Nova mensagem";
        postagem.setMensagem(novaMensagem);

        assertEquals(novaMensagem, postagem.getMensagem());
    }

    @Test
    void testAtualizacaoDoTimestamp() {
        String mensagem = "Mensagem original";
        String cnpj = "87020517000120";
        Postagem postagem = new Postagem(mensagem,cnpj);
        Instant timestampOriginal = postagem.getCreatedAt();

        postagem.setUpdateAt(Instant.now());

        Instant novoTimestamp = postagem.getUpdateAt();

        assertNotEquals(timestampOriginal, novoTimestamp);
    }

    @Test
    void testIdNaoNulo() {
        String mensagem = "Mensagem de teste";
        String cnpj = "87020517000120";
        Postagem postagem = new Postagem(mensagem,cnpj);
        postagem.setId(2L);

        assertNotNull(postagem.getId());
    }
}
