package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErroResponseDTOTest {

    @Test
    void deveTestarToStringComSucesso() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErroResponseDTO erroResponseDTO = ErroResponseDTO.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .erro("Erro de requisição")
                .mensagens(List.of(Map.of("mensagem", "Ocorreu um erro")))
                .timestamp(timestamp)
                .build();

        String resultadoToString = erroResponseDTO.toString();

        assertNotNull(resultadoToString);
        assertTrue(resultadoToString.contains("status=" + HttpStatus.UNPROCESSABLE_ENTITY.value()));
        assertTrue(resultadoToString.contains("erro=Erro de requisição"));
        assertTrue(resultadoToString.contains("mensagens=[{mensagem=Ocorreu um erro}]"));

        assertTrue(resultadoToString.contains("timestamp="));
        assertTrue(resultadoToString.contains(timestamp.toLocalDate().toString()));
    }



    @Test
    void deveDefinirTimestampComSucesso() {
        LocalDateTime timestampEsperado = LocalDateTime.of(2025, 3, 10, 10, 0, 0, 0);

        ErroResponseDTO erroResponseDTO = ErroResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de requisição")
                .mensagens(List.of(Map.of("mensagem", "Ocorreu um erro")))
                .timestamp(timestampEsperado)
                .build();

        assertEquals(timestampEsperado, erroResponseDTO.getTimestamp());
    }

    @Test
    void deveCriarErroResponseDTOComSucesso() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErroResponseDTO erroResponseDTO = ErroResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro("Chave não encontrada")
                .mensagens(List.of(Map.of("mensagem", "Chave não encontrada")))
                .timestamp(timestamp)
                .build();

        assertNotNull(erroResponseDTO);
        assertEquals(HttpStatus.NOT_FOUND.value(), erroResponseDTO.getStatus());
        assertEquals("Chave não encontrada", erroResponseDTO.getErro());
        assertEquals(List.of(Map.of("mensagem", "Chave não encontrada")), erroResponseDTO.getMensagens());
        assertEquals(timestamp, erroResponseDTO.getTimestamp());
    }
}
