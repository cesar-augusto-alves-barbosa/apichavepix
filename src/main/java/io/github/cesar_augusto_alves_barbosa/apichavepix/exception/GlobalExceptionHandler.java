package io.github.cesar_augusto_alves_barbosa.apichavepix.exception;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> erroCampo = new HashMap<>();
            erroCampo.put("campo", fieldError.getField());
            erroCampo.put("mensagem", fieldError.getDefaultMessage());
            mensagens.add(erroCampo);
        }

        ErroResponseDTO erro = ErroResponseDTO.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .erro("Erro de validação")
                .mensagens(mensagens)
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    @ExceptionHandler(ChavePixJaCadastradaException.class)
    public ResponseEntity<ErroResponseDTO> handleChaveJaCadastrada(ChavePixJaCadastradaException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", ex.getMessage());
        mensagens.add(erro);

        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .erro("Chave PIX já cadastrada")
                .mensagens(mensagens)
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ChavePixInvalidaException.class)
    public ResponseEntity<ErroResponseDTO> handleChaveInvalida(ChavePixInvalidaException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", ex.getMessage());
        mensagens.add(erro);

        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .erro("Chave PIX inválida")
                .mensagens(mensagens)
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(LimiteChavesPixAtingidoException.class)
    public ResponseEntity<ErroResponseDTO> handleLimiteChavesPixAtingido(LimiteChavesPixAtingidoException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", ex.getMessage());
        mensagens.add(erro);

        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Limite de chaves atingido")
                .mensagens(mensagens)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenericException(Exception ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", "Erro interno no servidor.");
        mensagens.add(erro);

        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Erro interno")
                .mensagens(mensagens)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
