package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import java.util.UUID;

public record PixChaveConsultaRespostaDTO(
        UUID id,
        String tipoChave,
        String valorChave,
        String tipoConta,
        Integer numeroAgencia,
        Integer numeroConta,
        String nomeCorrentista,
        String sobrenomeCorrentista,
        String status,
        String dataCriacao,
        String dataInativacao
) {}
