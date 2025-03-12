package io.github.cesar_augusto_alves_barbosa.apichavepix.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChavePixNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> handleChavePixNaoEncontradaExceptionException(ChavePixNaoEncontradaException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();

        erro.put("mensagem", ex.getMessage());
        mensagens.add(erro);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErroResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .erro("ChavePixNaoEncontrada")
                        .mensagens(mensagens)
                        .build()
        );
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> handleInvalidParametersException(HttpMessageNotReadableException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();
        Map<String, String> erro = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            erro.put("campo", invalidFormatException.getPath().get(0).getFieldName());
            erro.put("mensagem", "Valor inválido '" + invalidFormatException.getValue() +
                    "'. Os valores permitidos são: " + Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
            mensagens.add(erro);

            ErroResponseDTO response = ErroResponseDTO.builder()
                    .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .erro("Erro de validação")
                    .mensagens(mensagens)
                    .build();

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }


        if (ex.getMessage().contains("Required request body is missing")) {
            erro.put("mensagem", "O corpo da requisição (body) é obrigatório.");
            mensagens.add(erro);

            ErroResponseDTO response = ErroResponseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .erro("Erro de leitura do JSON")
                    .mensagens(mensagens)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        erro.put("mensagem", "Erro ao processar a requisição.");
        mensagens.add(erro);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErroResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .erro("Erro de leitura do JSON")
                        .mensagens(mensagens)
                        .build()
        );
    }


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
