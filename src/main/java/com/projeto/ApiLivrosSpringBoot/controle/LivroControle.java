package com.projeto.ApiLivrosSpringBoot.controle;

import com.projeto.ApiLivrosSpringBoot.dto.LivroDTO;
import com.projeto.ApiLivrosSpringBoot.excecoes.ApiErros;
import com.projeto.ApiLivrosSpringBoot.excecoes.BusinessException;
import com.projeto.ApiLivrosSpringBoot.modelo.Livro;
import com.projeto.ApiLivrosSpringBoot.servico.LivroServico;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livros")
public class LivroControle {

    private LivroServico servico;
    private ModelMapper modelMapper;

    
    public LivroControle(LivroServico servico, ModelMapper mapper) {
        this.servico = servico;
        this.modelMapper = mapper;
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroDTO create(@RequestBody @Valid LivroDTO dto) {

        Livro livro = modelMapper.map(dto, Livro.class);

        livro = servico.save(livro);

        return modelMapper.map(livro, LivroDTO.class);

    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();

        return new ApiErros(result);

    }

    @ExceptionHandler(BusinessException.class)
    public ApiErros handleBusinessException(BusinessException ex) {
        
     return new ApiErros(ex);   
        

    }

}
