package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PixChaveAlteracaoDTOTest {

    @Test
    void testPixChaveAlteracaoDTO() {
        UUID id = UUID.randomUUID();
        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(id, TipoConta.POUPANCA, 4321, 87654321, "Carlos Silva", "Santos");

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(TipoConta.POUPANCA, dto.tipoConta());
        assertEquals(4321, dto.numeroAgencia());
        assertEquals(87654321, dto.numeroConta());
        assertEquals("Carlos Silva", dto.nomeCorrentista());
        assertEquals("Santos", dto.sobrenomeCorrentista());

        assertNotNull(dto.toString());
    }
}
