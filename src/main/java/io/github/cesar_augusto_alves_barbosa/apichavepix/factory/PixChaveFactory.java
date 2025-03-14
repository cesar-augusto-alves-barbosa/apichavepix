package io.github.cesar_augusto_alves_barbosa.apichavepix.factory;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveConsultaRespostaDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveConsultaDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.utils.DateUtils;

import java.time.LocalDateTime;

public class PixChaveFactory {


    public static PixChave consultaToEntity(PixChaveConsultaDTO dto) {
        return PixChave.builder()
                .tipoChave(dto.tipoChave() != null ? TipoChave.valueOf(dto.tipoChave()) : null)
                .valorChave(dto.valorChave() != null ? dto.valorChave() : null)
                .tipoConta(dto.tipoConta() != null ? TipoConta.valueOf(dto.tipoConta()) : null)
                .numeroAgencia(dto.numeroAgencia() != null ? dto.numeroAgencia() : null)
                .numeroConta(dto.numeroConta() != null ? dto.numeroConta() : null)
                .nomeCorrentista(dto.nomeCorrentista() != null ? dto.nomeCorrentista() : null)
                .sobrenomeCorrentista(dto.sobrenomeCorrentista() != null ? dto.sobrenomeCorrentista() : null)
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
                .tipoTitular(dto.tipoTitular())
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
        return new PixChaveConsultaRespostaDTO(
                pixChave.getId(),
                pixChave.getTipoChave() != null ? pixChave.getTipoChave().name() : "",
                pixChave.getValorChave() != null ? pixChave.getValorChave() : "",
                pixChave.getTipoConta() != null ? pixChave.getTipoConta().name() : "",
                pixChave.getNumeroAgencia(),
                pixChave.getNumeroConta(),
                pixChave.getNomeCorrentista() != null ? pixChave.getNomeCorrentista() : "",
                pixChave.getSobrenomeCorrentista() != null ? pixChave.getSobrenomeCorrentista() : "",
                pixChave.getStatus() != null ? pixChave.getStatus().name() : "",
                DateUtils.formatDate(pixChave.getDataCriacao()),
                DateUtils.formatDate(pixChave.getDataInativacao())
        );
    }
}
