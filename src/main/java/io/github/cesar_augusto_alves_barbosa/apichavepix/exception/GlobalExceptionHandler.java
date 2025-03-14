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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConsultaInvalidaException.class)
    public ResponseEntity<ErroResponseDTO> handleConsultaInvalida(ConsultaInvalidaException ex) {
        Map<String, String> erro = Map.of("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de consulta", List.of(erro)));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> erro = new HashMap<>();
        if (ex.getRequiredType() == UUID.class) {
            erro.put("campo", ex.getName());
            erro.put("mensagem", "Formato de UUID inválido. Informe um UUID válido.");
        } else {
            erro.put("campo", ex.getName());
            erro.put("mensagem", "Tipo de dado inválido. Esperado: " + ex.getRequiredType().getSimpleName());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de conversão", List.of(erro)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> erro = Map.of("mensagem", "Valor inválido informado. Verifique os valores permitidos para este campo.");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", List.of(erro)));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErroResponseDTO> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> erro = Map.of(
                "campo", ex.getPath().get(0).getFieldName(),
                "mensagem", "Valor inválido '" + ex.getValue() +
                        "'. Os valores permitidos são: " + Arrays.toString(ex.getTargetType().getEnumConstants()));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", List.of(erro)));
    }

    @ExceptionHandler(ChavePixNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> handleChavePixNaoEncontradaException(ChavePixNaoEncontradaException ex) {
        Map<String, String> erro = Map.of("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponseDTO(HttpStatus.NOT_FOUND.value(), "ChavePixNaoEncontrada", List.of(erro)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> handleInvalidParametersException(HttpMessageNotReadableException ex) {
        Map<String, String> erro = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            erro.put("campo", invalidFormatException.getPath().get(0).getFieldName());
            erro.put("mensagem", "Valor inválido '" + invalidFormatException.getValue() +
                    "'. Os valores permitidos são: " + Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
        } else if (ex.getMessage().contains("Required request body is missing")) {
            erro.put("mensagem", "O corpo da requisição (body) é obrigatório.");
        } else if (ex.getMessage().contains("Cannot deserialize value of type")) {
            erro.put("mensagem", "O formato do JSON enviado está incorreto.");
        } else {
            erro.put("mensagem", "Erro ao processar a requisição.");
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de leitura do JSON", List.of(erro)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> mensagens = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            mensagens.add(Map.of("campo", fieldError.getField(), "mensagem", fieldError.getDefaultMessage()));
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", mensagens));
    }

    @ExceptionHandler(ChavePixJaCadastradaException.class)
    public ResponseEntity<ErroResponseDTO> handleChaveJaCadastrada(ChavePixJaCadastradaException ex) {
        Map<String, String> erro = Map.of("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Chave PIX já cadastrada", List.of(erro)));
    }


    @ExceptionHandler(ChavePixInvalidaException.class)
    public ResponseEntity<ErroResponseDTO> handleChaveInvalida(ChavePixInvalidaException ex) {
        Map<String, String> erro = Map.of("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Chave PIX inválida", List.of(erro)));
    }

    @ExceptionHandler(LimiteChavesPixAtingidoException.class)
    public ResponseEntity<ErroResponseDTO> handleLimiteChavesPixAtingido(LimiteChavesPixAtingidoException ex) {
        Map<String, String> erro = Map.of("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResponseDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Limite de chaves atingido", List.of(erro)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenericException(Exception ex) {
        Map<String, String> erro = Map.of("mensagem", "Erro interno no servidor.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno", List.of(erro)));
    }
}
