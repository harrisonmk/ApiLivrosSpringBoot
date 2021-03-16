package com.projeto.ApiLivrosSpringBoot.service;

import com.projeto.ApiLivrosSpringBoot.excecoes.BusinessException;
import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import com.projeto.ApiLivrosSpringBoot.repositorio.LivroRepositorio;
import com.projeto.ApiLivrosSpringBoot.servico.LivroServico;
import com.projeto.ApiLivrosSpringBoot.servico.implementacao.LivroServicoImplementacao;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    LivroServico servico;

    @MockBean
    LivroRepositorio repositorio;

    @BeforeEach
    public void setUp() {
        this.servico = new LivroServicoImplementacao(repositorio);

    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {

        //Cenario
        Livro livro = new Livro("As Aventuras", "Fulano", "123");

        Livro livro1 = new Livro(11L, "As Aventuras", "Fulano", "123");
        
        Mockito.when(repositorio.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repositorio.save(livro)).thenReturn(livro1);

        //execucao
        Livro savedBook = servico.save(livro);

        //Verificacao
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitulo()).isEqualTo("As Aventuras");
        Assertions.assertThat(savedBook.getAutor()).isEqualTo("Fulano");
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");

    }

    @Test
    @DisplayName("Deve lancar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedIsbn() {
        
       //cenario
      Livro livro = createValidBook();
      Mockito.when(repositorio.existsByIsbn(Mockito.anyString())).thenReturn(true);
      
        //Execucao
        Throwable catchThrowable = Assertions.catchThrowable(() -> servico.save(livro)); 
        
        //Verificacoes
        assertThat(catchThrowable).isInstanceOf(BusinessException.class).hasMessage("Isbn ja cadastrado");
        
        
        Mockito.verify(repositorio,Mockito.never()).save(livro);

    }
    
    public Livro createValidBook(){
        
      Livro livro = new Livro("Fulano","As aventuras","123");
      
      return livro;
        
    }

}
