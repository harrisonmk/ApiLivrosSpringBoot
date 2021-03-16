package com.projeto.ApiLivrosSpringBoot.servico.implementacao;

import com.projeto.ApiLivrosSpringBoot.excecoes.BusinessException;
import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import com.projeto.ApiLivrosSpringBoot.repositorio.LivroRepositorio;
import com.projeto.ApiLivrosSpringBoot.servico.LivroServico;
import org.springframework.stereotype.Service;

@Service
public class LivroServicoImplementacao implements LivroServico {

    
    
    private LivroRepositorio repositorio;

    
    
    public LivroServicoImplementacao(LivroRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    
    
    @Override
    public Livro save(Livro livro) {

        if (repositorio.existsByIsbn(livro.getIsbn())) {
            throw new BusinessException("Isbn ja cadastrado");
        }

        return repositorio.save(livro);

    }
    
    

}
