package br.com.doasanguepoa.postagem.repository;

import br.com.doasanguepoa.postagem.model.Postagem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PostagemRepository implements PanacheRepository<Postagem> {
    public List<Postagem> findByCnpj(String cnpj){
        return find("cnpj",cnpj).list();
    }
}
