package io.github.cesar_augusto_alves_barbosa.apichavepix.entity;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "pix_chave")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PixChave {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "O tipo da chave é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoChave tipoChave;

    @NotBlank(message = "O valor da chave não pode estar em branco")
    @Size(max = 77, message = "O valor da chave deve ter no máximo 77 caracteres")
    @Column(nullable = false)
    private String valorChave;

    @NotNull(message = "O tipo da conta é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipoConta;

    @NotNull(message = "O número da agência é obrigatório")
    @Digits(integer = 4, fraction = 0, message = "A agência deve ter até 4 dígitos")
    @Column(nullable = false)
    private Integer numeroAgencia;

    @NotNull(message = "O número da conta é obrigatório")
    @Digits(integer = 8, fraction = 0, message = "A conta deve ter até 8 dígitos")
    private Integer numeroConta;

    @NotBlank(message = "O nome do correntista é obrigatório")
    @Column(nullable = false)
    @Size(max = 30, message = "O nome do correntista deve ter no máximo 30 caracteres")
    private String nomeCorrentista;

    @Size(max = 45, message = "O sobrenome do correntista deve ter no máximo 45 caracteres")
    @Column
    private String sobrenomeCorrentista;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = true)
    private LocalDateTime dataInativacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusChave status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoTitular tipoTitular;
}
