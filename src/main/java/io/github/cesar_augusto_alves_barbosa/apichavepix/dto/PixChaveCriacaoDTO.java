package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import jakarta.validation.constraints.*;

public record PixChaveCriacaoDTO(
        @NotNull(message = "O tipo da chave é obrigatório")
        TipoChave tipoChave,

        @NotBlank(message = "O valor da chave não pode estar em branco")
        @Size(max = 77)
        String valorChave,

        @NotNull(message = "O tipo da conta é obrigatório")
        TipoConta tipoConta,

        @NotNull(message = "O número da agência é obrigatório")
        @Digits(integer = 4, fraction = 0)
        Integer numeroAgencia,

        @NotNull(message = "O número da conta é obrigatório")
        @Digits(integer = 8, fraction = 0) Integer numeroConta,

        @NotBlank(message = "O nome do correntista é obrigatório")
        @Size(max = 30)
        String nomeCorrentista,

        @Size(max = 45)
        String sobrenomeCorrentista,

        @NotNull(message = "O tipo do titular é obrigatório")
        TipoTitular tipoTitular
) {}
