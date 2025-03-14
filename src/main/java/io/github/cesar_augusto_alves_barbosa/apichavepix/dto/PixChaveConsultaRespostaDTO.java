package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@ToString
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
