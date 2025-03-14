package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErroResponseDTOTest {

    @Test
    void deveCriarErroResponseDTOComSucesso() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErroResponseDTO erroResponseDTO = new ErroResponseDTO(timestamp, HttpStatus.NOT_FOUND.value(),
                "Chave não encontrada", List.of(Map.of("mensagem", "Chave não encontrada")));

        assertNotNull(erroResponseDTO);
        assertEquals(HttpStatus.NOT_FOUND.value(), erroResponseDTO.status());
        assertEquals("Chave não encontrada", erroResponseDTO.erro());
        assertEquals(List.of(Map.of("mensagem", "Chave não encontrada")), erroResponseDTO.mensagens());
        assertEquals(timestamp, erroResponseDTO.timestamp());
    }

    @Test
    void deveCriarErroResponseDTOComTimestampPadrao() {
        ErroResponseDTO erroResponseDTO = new ErroResponseDTO(HttpStatus.BAD_REQUEST.value(),
                "Erro de requisição", List.of(Map.of("mensagem", "Ocorreu um erro")));

        assertNotNull(erroResponseDTO);
        assertEquals(HttpStatus.BAD_REQUEST.value(), erroResponseDTO.status());
        assertEquals("Erro de requisição", erroResponseDTO.erro());
        assertEquals(List.of(Map.of("mensagem", "Ocorreu um erro")), erroResponseDTO.mensagens());
        assertNotNull(erroResponseDTO.timestamp());
    }

    @Test
    void deveTestarToStringComSucesso() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErroResponseDTO erroResponseDTO = new ErroResponseDTO(timestamp, HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de requisição", List.of(Map.of("mensagem", "Ocorreu um erro")));

        String resultadoToString = erroResponseDTO.toString();

        assertNotNull(resultadoToString);
        assertTrue(resultadoToString.contains("status=" + HttpStatus.UNPROCESSABLE_ENTITY.value()));
        assertTrue(resultadoToString.contains("erro=Erro de requisição"));
        assertTrue(resultadoToString.contains("mensagens=[{mensagem=Ocorreu um erro}]"));
        assertTrue(resultadoToString.contains("timestamp="));
        assertTrue(resultadoToString.contains(timestamp.toLocalDate().toString()));
    }
}
