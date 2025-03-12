package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PixChaveConsultaRespostaDTO {

    private UUID id;
    private String tipoChave;
    private String valorChave;
    private String tipoConta;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private String status;
    private String dataCriacao;
    private String dataInativacao;
}
