package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PixChaveCriacaoDTOTest {

    @Test
    void testPixChaveCriacaoDTO() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(
                TipoChave.CPF, "12345678901",
                TipoConta.CORRENTE, 1234, 56789012,
                "João Silva", "Oliveira", TipoTitular.PF
        );

        assertNotNull(dto);
        assertEquals(TipoChave.CPF, dto.tipoChave());
        assertEquals("12345678901", dto.valorChave());
        assertEquals(TipoConta.CORRENTE, dto.tipoConta());
        assertEquals(1234, dto.numeroAgencia());
        assertEquals(56789012, dto.numeroConta());
        assertEquals("João Silva", dto.nomeCorrentista());
        assertEquals("Oliveira", dto.sobrenomeCorrentista());
        assertEquals(TipoTitular.PF, dto.tipoTitular());

        assertNotNull(dto.toString());
    }
}
