
package com.projeto.ApiLivrosSpringBoot.repositorio;

import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LivroRepositorioTest {
    
    @Autowired
    TestEntityManager entity;
    
    @Autowired
    LivroRepositorio repositorio;
    
    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        
      //cenario
      String isbn = "123";
      Livro livro = new Livro("As aventuras","Fulano","123");
      entity.persist(livro);
      
      //execucao
     boolean existe = repositorio.existsByIsbn(isbn);
      
      //verificacao
       assertThat(existe).isTrue(); 
        
        
    }
    
    
    
    @Test
    @DisplayName("Deve retornar falso quando existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnNaoExists(){
        
      //cenario
      String isbn = "123";
      
      
      //execucao
     boolean existe = repositorio.existsByIsbn(isbn);
      
      //verificacao
       assertThat(existe).isFalse(); 
        
        
    }
    
    
}
