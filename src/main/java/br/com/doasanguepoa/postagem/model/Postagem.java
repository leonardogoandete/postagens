package br.com.doasanguepoa.postagem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "postagens")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;
    private String mensagem;

    public Postagem(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }
}
