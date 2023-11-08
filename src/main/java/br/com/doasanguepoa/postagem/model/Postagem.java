package br.com.doasanguepoa.postagem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@Table(name = "postagens")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private String cnpj;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updateAt;

    public Postagem(){}
    public Postagem(String mensagem, String cnpj) {
        this.cnpj = cnpj;
        this.mensagem = mensagem;
        this.createdAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public Postagem(String mensagem, String cnpj, Instant createdAt, Instant updateAt) {
        this.cnpj = cnpj;
        this.mensagem = mensagem;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }
}
