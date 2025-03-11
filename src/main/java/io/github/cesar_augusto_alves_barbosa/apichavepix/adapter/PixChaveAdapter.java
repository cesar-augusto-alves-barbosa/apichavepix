package io.github.cesar_augusto_alves_barbosa.apichavepix.adapter;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;

import java.time.LocalDateTime;

public class PixChaveAdapter {

    public static PixChave criarChavePixToEntity(PixChaveCriacaoDTO dto) {
        return PixChave.builder()
                .tipoChave(dto.tipoChave())
                .valorChave(dto.valorChave())
                .tipoConta(dto.tipoConta())
                .numeroAgencia(dto.numeroAgencia())
                .numeroConta(dto.numeroConta())
                .nomeCorrentista(dto.nomeCorrentista())
                .sobrenomeCorrentista(dto.sobrenomeCorrentista())
                .dataCriacao(LocalDateTime.now())
                .status(StatusChave.ATIVA)
                .build();
    }

    public static PixChaveDTO toDTO(PixChave pixChave) {
        return new PixChaveDTO(
                pixChave.getId(),
                pixChave.getTipoChave(),
                pixChave.getValorChave(),
                pixChave.getTipoConta(),
                pixChave.getNumeroAgencia(),
                pixChave.getNumeroConta(),
                pixChave.getNomeCorrentista(),
                pixChave.getSobrenomeCorrentista(),
                pixChave.getStatus(),
                pixChave.getDataCriacao(),
                pixChave.getDataInativacao()
        );
    }
}
