package io.github.cesar_augusto_alves_barbosa.apichavepix.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.ErroResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornarErroConsultaInvalida() {
        ConsultaInvalidaException ex = new ConsultaInvalidaException("Erro de consulta");
        ResponseEntity<ErroResponseDTO> response = handler.handleConsultaInvalida(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Erro de consulta", response.getBody().mensagens().get(0).get("mensagem"));
    }

    @Test
    void deveRetornarErroDeConversao() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("campoTeste");
        when(ex.getRequiredType()).thenAnswer(invocation -> (Class) String.class);

        ResponseEntity<ErroResponseDTO> response = handler.handleTypeMismatchException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Tipo de dado inválido"));
    }

    @Test
    void deveRetornarErroDeValidacao() {
        IllegalArgumentException ex = new IllegalArgumentException("Valor inválido");
        ResponseEntity<ErroResponseDTO> response = handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Valor inválido"));
    }

    @Test
    void deveRetornarErroFormatoInvalido() {
        InvalidFormatException ex = mock(InvalidFormatException.class);

        com.fasterxml.jackson.databind.JsonMappingException.Reference referenceMock =
                mock(com.fasterxml.jackson.databind.JsonMappingException.Reference.class);

        when(referenceMock.getFieldName()).thenReturn("campoTeste"); // Nome do campo simulado
        when(ex.getPath()).thenReturn(java.util.List.of(referenceMock));
        when(ex.getValue()).thenReturn("valorIncorreto");
        when(ex.getTargetType()).thenReturn((Class) String.class);

        ResponseEntity<ErroResponseDTO> response = handler.handleInvalidFormatException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().mensagens().isEmpty());
        assertTrue(response.getBody().mensagens().get(0).containsKey("campo"));
        assertEquals("campoTeste", response.getBody().mensagens().get(0).get("campo"));
    }

    @Test
    void deveRetornarErroParametrosInvalidos() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Required request body is missing");
        ResponseEntity<ErroResponseDTO> response = handler.handleInvalidParametersException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("O corpo da requisição (body) é obrigatório."));
    }

    @Test
    void deveRetornarErroChaveNaoEncontrada() {
        ChavePixNaoEncontradaException ex = new ChavePixNaoEncontradaException("Chave não encontrada");
        ResponseEntity<ErroResponseDTO> response = handler.handleChavePixNaoEncontradaException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Chave não encontrada"));
    }

    @Test
    void deveRetornarErroGenerico() {
        Exception ex = new Exception("Erro interno");
        ResponseEntity<ErroResponseDTO> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Erro interno no servidor"));
    }

    @Test
    void deveRetornarErroDeLimiteChavesAtingido() {
        LimiteChavesPixAtingidoException ex = new LimiteChavesPixAtingidoException("Limite de chaves atingido");
        ResponseEntity<ErroResponseDTO> response = handler.handleLimiteChavesPixAtingido(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Limite de chaves atingido"));
    }

    @Test
    void deveRetornarErroFormatoUUIDInvalido() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        when(ex.getName()).thenReturn("campoTeste");
        when(ex.getRequiredType()).thenReturn((Class) UUID.class);
        when(ex.getValue()).thenReturn("12345");

        ResponseEntity<ErroResponseDTO> response = handler.handleTypeMismatchException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Formato de UUID inválido"));
    }

    @Test
    void deveRetornarErroDeLeituraJsonQuandoBodyFaltando() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Required request body is missing");
        ResponseEntity<ErroResponseDTO> response = handler.handleInvalidParametersException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("O corpo da requisição (body) é obrigatório"));
    }

    @Test
    void deveRetornarErroDeLeituraJsonQuandoFormatoIncorreto() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Cannot deserialize value of type");
        ResponseEntity<ErroResponseDTO> response = handler.handleInvalidParametersException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("O formato do JSON enviado está incorreto"));
    }

    @Test
    void deveRetornarErroDeLeituraJsonGenerico() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro ao processar a requisição");
        ResponseEntity<ErroResponseDTO> response = handler.handleInvalidParametersException(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().mensagens().get(0).get("mensagem").contains("Erro ao processar a requisição"));
    }
}
