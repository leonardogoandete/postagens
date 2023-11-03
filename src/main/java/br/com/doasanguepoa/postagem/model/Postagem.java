package br.com.doasanguepoa.postagem.model;

import br.com.doasanguepoa.postagem.dto.postagem.DadosListagemPostagemDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "postagens")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mensagem;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updateAt;

    public Postagem(String mensagem) {
        this.mensagem = mensagem;
        this.createdAt = Instant.now();
        this.updateAt = Instant.now();
    }
}
