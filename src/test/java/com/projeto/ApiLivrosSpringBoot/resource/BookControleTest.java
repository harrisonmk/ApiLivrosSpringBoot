package com.projeto.ApiLivrosSpringBoot.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.ApiLivrosSpringBoot.dto.LivroDTO;
import com.projeto.ApiLivrosSpringBoot.excecoes.BusinessException;
import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import com.projeto.ApiLivrosSpringBoot.servico.LivroServico;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControleTest {

    static String BOOK_API = "/api/livros";

    @Autowired
    MockMvc mvc;

    @MockBean
    LivroServico servico;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {

        LivroDTO dto = new LivroDTO("As Aventuras", "Harrison", "001");
        Livro savedBook = new Livro(101L, "As Aventuras", "Harrison", "001");

        BDDMockito.given(servico.save(Mockito.any(Livro.class))).willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto); //tranforma um objeto de qualquer tipo em json

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("titulo").value(dto.getTitulo()))
                .andExpect(MockMvcResultMatchers.jsonPath("autor").value(dto.getAutor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));

    }

    
    
    @Test
    @DisplayName("Deve lancar erro de validacao quando nao houver dados suficientes para criacao de livro.")
    public void createInvalidTest() throws JsonProcessingException, Exception {

        String json = new ObjectMapper().writeValueAsString(new LivroDTO()); //tranforma um objeto de qualquer tipo em json  

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));

    }

    
    
    @Test
    @DisplayName("Deve lan??ar erro ao tentar cadastrar um livro com isbn j?? utilizado por outro.")
    public void createBookWithDuplicatedIsbn() throws Exception {

        LivroDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Isbn ja cadastrado.";
        BDDMockito.given(servico.save(Mockito.any(Livro.class)))
                    .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform( request )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

    }

    /*
    @Test
    @DisplayName("Deve obter informacoes de um livro.")
    public void getBookDetailsTest() throws Exception{
        //cenario (given)
        Long id = 1l;

        Livro book  = new Livro(id,createNewBook().getTitulo(),createNewBook().getAutor(),createNewBook().getIsbn());
       
        BDDMockito.given( servico.getById(id) ).willReturn(Optional.of(book));

        //execucao (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect( jsonPath("id").value(id) )
            .andExpect( jsonPath("title").value(createNewBook().getTitulo()) )
            .andExpect( jsonPath("author").value(createNewBook().getAutor()) )
            .andExpect( jsonPath("isbn").value(createNewBook().getIsbn()) )
        ;
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o livro procurado n??o existir")
    public void bookNotFoundTest() throws Exception {

        BDDMockito.given( servico.getById(Mockito.anyLong()) ).willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception {

        BDDMockito.given(servico.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mvc.perform( request )
            .andExpect( status().isNoContent() );
    }

    @Test
    @DisplayName("Deve retornar resource not found quando n??o encontrar o livro para deletar")
    public void deleteInexistentBookTest() throws Exception {

        BDDMockito.given(servico.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {
        Long id = 1l;
        String json = new ObjectMapper().writeValueAsString(createNewBook());

        Livro updatingBook = Book.builder().id(1l).title("some title").author("some author").isbn("321").build();
        BDDMockito.given( servico.getById(id) ).willReturn( Optional.of(updatingBook) );
        Livro updatedBook = Book.builder().id(id).author("Artur").title("As aventuras").isbn("321").build();
        BDDMockito.given(servico.update(updatingBook)).willReturn(updatedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id) )
                .andExpect( jsonPath("title").value(createNewBook().getTitulo()) )
                .andExpect( jsonPath("author").value(createNewBook().getAutor()) )
                .andExpect( jsonPath("isbn").value("321") );
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente")
    public void updateInexistentBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewBook());
        BDDMockito.given( servico.getById(Mockito.anyLong()) )
                .willReturn( Optional.empty() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform( request )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve filtrar livros")
    public void findBooksTest() throws Exception{

        Long id = 1l;

        Book book = Book.builder()
                    .id(id)
                    .title(createNewBook().getTitle())
                    .author(createNewBook().getAuthor())
                    .isbn(createNewBook().getIsbn())
                    .build();

        BDDMockito.given( servico.find(Mockito.any(Livro.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Livro>( Arrays.asList(book), PageRequest.of(0,100), 1 )   );

        String queryString = String.format("?title=%s&author=%s&page=0&size=100",
                book.getTitle(), book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform( request )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("content", Matchers.hasSize(1)))
            .andExpect( jsonPath("totalElements").value(1) )
            .andExpect( jsonPath("pageable.pageSize").value(100) )
            .andExpect( jsonPath("pageable.pageNumber").value(0))
            ;
    }  */



    private LivroDTO createNewBook() {
        
        LivroDTO livroDTO = new LivroDTO("artur","As aventuras","001");
        
        return livroDTO;
    }

    

}
