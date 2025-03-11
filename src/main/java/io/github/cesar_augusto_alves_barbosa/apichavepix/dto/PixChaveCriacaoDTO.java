package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import jakarta.validation.constraints.*;

public record PixChaveCriacaoDTO(
        @NotNull(message = "O tipo da chave é obrigatório")
        TipoChave tipoChave,

        @NotBlank(message = "O valor da chave não pode estar em branco")
        @Size(max = 77, message = "O valor da chave deve ter no máximo 77 caracteres")
        String valorChave,

        @NotNull(message = "O tipo da conta é obrigatório")
        TipoConta tipoConta,

        @NotNull(message = "O número da agência é obrigatório")
        @Digits(integer = 4, fraction = 0, message = "A agência deve ter até 4 dígitos")
        Integer numeroAgencia,

        @NotNull(message = "O número da conta é obrigatório")
        @Digits(integer = 8, fraction = 0, message = "A conta deve ter até 8 dígitos")
        Integer numeroConta,

        @NotBlank(message = "O nome do correntista é obrigatório")
        @Size(max = 30, message = "O nome do correntista deve ter no máximo 30 caracteres")
        String nomeCorrentista,

        @Size(max = 45, message = "O sobrenome deve ter no máximo 45 caracteres")
        String sobrenomeCorrentista
) { }
