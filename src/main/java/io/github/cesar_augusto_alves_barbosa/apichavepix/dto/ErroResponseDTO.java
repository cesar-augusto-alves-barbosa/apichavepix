package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ErroResponseDTO {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String erro;
    private List<Map<String, String>> mensagens;
}
