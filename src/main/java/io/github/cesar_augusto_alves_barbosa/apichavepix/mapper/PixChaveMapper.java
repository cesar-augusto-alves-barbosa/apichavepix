package io.github.cesar_augusto_alves_barbosa.apichavepix.mapper;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;

public class PixChaveMapper {

    public static PixChaveDTO toDTO(PixChave entity) {
        return new PixChaveDTO(
                entity.getId(),
                entity.getTipoChave(),
                entity.getValorChave(),
                entity.getTipoConta(),
                entity.getNumeroAgencia(),
                entity.getNumeroConta(),
                entity.getNomeCorrentista(),
                entity.getSobrenomeCorrentista(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataInativacao()
        );
    }

    public static PixChave toEntity(PixChaveDTO dto) {
        return new PixChave(
                dto.id(),
                dto.tipoChave(),
                dto.valorChave(),
                dto.tipoConta(),
                dto.numeroAgencia(),
                dto.numeroConta(),
                dto.nomeCorrentista(),
                dto.sobrenomeCorrentista(),
                dto.dataCriacao(),
                dto.dataInativacao(),
                dto.status()
        );
    }
}
