package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record  PixChaveConsultaDTO (
     String tipoChave,
     String valorChave,
     String tipoConta,
     Integer numeroAgencia,
     Integer numeroConta,
     String nomeCorrentista,
     String sobrenomeCorrentista
) {}