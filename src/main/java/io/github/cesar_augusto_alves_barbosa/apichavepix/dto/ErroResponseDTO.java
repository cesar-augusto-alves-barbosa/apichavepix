package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        List<Map<String, String>> mensagens
) {
    public ErroResponseDTO(int status, String erro, List<Map<String, String>> mensagens) {
        this(LocalDateTime.now(), status, erro, mensagens);
    }
}
