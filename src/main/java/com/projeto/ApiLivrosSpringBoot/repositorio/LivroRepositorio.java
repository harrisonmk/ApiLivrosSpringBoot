
package com.projeto.ApiLivrosSpringBoot.repositorio;

import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepositorio extends JpaRepository<Livro,Long> {

    public boolean existsByIsbn(String isbn);
    
    
    
    
}
