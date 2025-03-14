package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PixChaveCriacaoRespostaDTO {
    private UUID id;
}