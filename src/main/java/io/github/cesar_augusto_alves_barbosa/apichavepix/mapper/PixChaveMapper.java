package io.github.cesar_augusto_alves_barbosa.apichavepix.mapper;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveConsultaRespostaDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveFiltroDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.utils.DateUtils;

import java.time.LocalDateTime;

public class PixChaveMapper {


    public static PixChave toEntity(PixChaveFiltroDTO dto) {
        return PixChave.builder()
                .tipoChave(dto.getTipoChave() != null ? TipoChave.valueOf(dto.getTipoChave()) : null)
                .valorChave(dto.getValorChave() != null ? dto.getValorChave() : null)
                .tipoConta(dto.getTipoConta() != null ? TipoConta.valueOf(dto.getTipoConta()) : null)
                .numeroAgencia(dto.getNumeroAgencia() != null ? dto.getNumeroAgencia() : null)
                .numeroConta(dto.getNumeroConta() != null ? dto.getNumeroConta() : null)
                .nomeCorrentista(dto.getNomeCorrentista() != null ? dto.getNomeCorrentista() : null)
                .sobrenomeCorrentista(dto.getSobrenomeCorrentista() != null ? dto.getSobrenomeCorrentista() : null)
                .build();
    }

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

    public static PixChaveConsultaRespostaDTO toConsultaDTO(PixChave pixChave) {
        return PixChaveConsultaRespostaDTO.builder()
                .id(pixChave.getId())
                .tipoChave(pixChave.getTipoChave() != null ? pixChave.getTipoChave().name() : "")
                .valorChave(pixChave.getValorChave() != null ? pixChave.getValorChave() : "")
                .tipoConta(pixChave.getTipoConta() != null ? pixChave.getTipoConta().name() : "")
                .numeroAgencia(pixChave.getNumeroAgencia())
                .numeroConta(pixChave.getNumeroConta())
                .nomeCorrentista(pixChave.getNomeCorrentista() != null ? pixChave.getNomeCorrentista() : "")
                .sobrenomeCorrentista(pixChave.getSobrenomeCorrentista() != null ? pixChave.getSobrenomeCorrentista() : "")
                .status(pixChave.getStatus() != null ? pixChave.getStatus().name() : "")
                .dataCriacao(DateUtils.formatDate(pixChave.getDataCriacao()))
                .dataInativacao(DateUtils.formatDate(pixChave.getDataInativacao()))
                .build();
    }
}
