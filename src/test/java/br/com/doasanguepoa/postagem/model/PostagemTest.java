package br.com.doasanguepoa.postagem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostagemTest {

    @Test
    public void testConstrutorEPropriedades() {
        String mensagem = "Teste de mensagem";
        Postagem postagem = new Postagem(mensagem);
        postagem.setId(1L);

        assertNotNull(postagem.getId());
        assertEquals(mensagem, postagem.getMensagem());
        assertNotNull(postagem.getCreatedAt());
        assertNotNull(postagem.getUpdateAt());
    }
}
