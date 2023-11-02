package br.com.doasanguepoa.postagem.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

 class PostagemTest {

    @Test
     void testConstrutorEPropriedades() {
        String mensagem = "Teste de mensagem";
        Postagem postagem = new Postagem(mensagem);
        postagem.setId(1L);

        assertNotNull(postagem.getId());
        assertEquals(mensagem, postagem.getMensagem());
        assertNotNull(postagem.getCreatedAt());
        assertNotNull(postagem.getUpdateAt());
    }

    @Test
     void testAtualizacaoDaMensagem() {
        String mensagemOriginal = "Mensagem original";
        Postagem postagem = new Postagem(mensagemOriginal);

        String novaMensagem = "Nova mensagem";
        postagem.setMensagem(novaMensagem);

        assertEquals(novaMensagem, postagem.getMensagem());
    }

    @Test
     void testAtualizacaoDoTimestamp() {
        String mensagem = "Mensagem original";
        Postagem postagem = new Postagem(mensagem);
        Instant timestampOriginal = postagem.getCreatedAt();
        // Aguarde um curto período de tempo para simular uma atualização
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        postagem.setUpdateAt(Instant.now());

        Instant novoTimestamp = postagem.getUpdateAt();

        assertNotEquals(timestampOriginal, novoTimestamp);
    }

    @Test
     void testIdNaoNulo() {
        String mensagem = "Mensagem de teste";
        Postagem postagem = new Postagem(mensagem);
        postagem.setId(2L);

        assertNotNull(postagem.getId());
    }
}
