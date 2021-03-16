package com.projeto.ApiLivrosSpringBoot.dto;

import javax.validation.constraints.NotEmpty;

public class LivroDTO {

    
    private Long id;
    
    
    @NotEmpty
    private String titulo;
    
    @NotEmpty
    private String autor;
    
    @NotEmpty
    private String isbn;

    
    
    public LivroDTO(Long id, String titulo, String autor, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }

    
    
    
    
    public LivroDTO(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }

    
    
    public LivroDTO() {
    }
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    

}
