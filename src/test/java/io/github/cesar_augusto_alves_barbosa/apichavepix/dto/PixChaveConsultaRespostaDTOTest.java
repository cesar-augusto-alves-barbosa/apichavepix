package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveConsultaRespostaDTOTest {

    private PixChaveConsultaRespostaDTO dto1;
    private PixChaveConsultaRespostaDTO dto2;
    private PixChaveConsultaRespostaDTO dtoDiferente;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        dto1 = new PixChaveConsultaRespostaDTO(
                id, "CPF", "12345678901", "CORRENTE",
                1234, 56789012, "João Silva",
                "Oliveira", StatusChave.ATIVA.name(), "10/03/2025", null
        );

        dto2 = new PixChaveConsultaRespostaDTO(
                id, "CPF", "12345678901", "CORRENTE",
                1234, 56789012, "João Silva",
                "Oliveira", StatusChave.ATIVA.name(), "10/03/2025", null
        );

        dtoDiferente = new PixChaveConsultaRespostaDTO(
                UUID.randomUUID(), "EMAIL", "email@email.com", "POUPANCA",
                4321, 87654321, "Carlos Silva",
                "Santos", StatusChave.INATIVA.name(), "11/03/2025", "12/03/2025"
        );
    }

    @Test
    void deveCriarPixChaveConsultaRespostaDTOComSucesso() {
        assertNotNull(dto1);
        assertEquals(id, dto1.getId());
        assertEquals("CPF", dto1.getTipoChave());
        assertEquals("12345678901", dto1.getValorChave());
        assertEquals("CORRENTE", dto1.getTipoConta());
        assertEquals(1234, dto1.getNumeroAgencia());
        assertEquals(56789012, dto1.getNumeroConta());
        assertEquals("João Silva", dto1.getNomeCorrentista());
        assertEquals("Oliveira", dto1.getSobrenomeCorrentista());
        assertEquals(StatusChave.ATIVA.name(), dto1.getStatus());
        assertEquals("10/03/2025", dto1.getDataCriacao());
        assertNull(dto1.getDataInativacao());
    }

    @Test
    void deveRetornarValoresCorretosDosGetters() {
        assertEquals(id, dto1.getId());
        assertEquals("CPF", dto1.getTipoChave());
        assertEquals("12345678901", dto1.getValorChave());
        assertEquals("CORRENTE", dto1.getTipoConta());
        assertEquals(1234, dto1.getNumeroAgencia());
        assertEquals(56789012, dto1.getNumeroConta());
        assertEquals("João Silva", dto1.getNomeCorrentista());
        assertEquals("Oliveira", dto1.getSobrenomeCorrentista());
        assertEquals(StatusChave.ATIVA.name(), dto1.getStatus());
        assertEquals("10/03/2025", dto1.getDataCriacao());
        assertNull(dto1.getDataInativacao());
    }

    @Test
    void deveTestarEqualsComObjetosIguais() {
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(dto1, dtoDiferente);
        assertNotEquals(dto1.hashCode(), dtoDiferente.hashCode());
    }

    @Test
    void deveTestarToStringNaoRetornaNulo() {
        assertNotNull(dto1.toString());
    }

    @Test
    void deveAlterarValoresComSetters() {
        UUID novoId = UUID.randomUUID();
        dto1.setId(novoId);
        dto1.setTipoChave("CNPJ");
        dto1.setValorChave("12345678000199");
        dto1.setTipoConta("POUPANCA");
        dto1.setNumeroAgencia(4321);
        dto1.setNumeroConta(87654321);
        dto1.setNomeCorrentista("Carlos Silva");
        dto1.setSobrenomeCorrentista("Santos");
        dto1.setStatus(StatusChave.INATIVA.name());
        dto1.setDataCriacao("12/04/2025");
        dto1.setDataInativacao("15/04/2025");

        assertEquals(novoId, dto1.getId());
        assertEquals("CNPJ", dto1.getTipoChave());
        assertEquals("12345678000199", dto1.getValorChave());
        assertEquals("POUPANCA", dto1.getTipoConta());
        assertEquals(4321, dto1.getNumeroAgencia());
        assertEquals(87654321, dto1.getNumeroConta());
        assertEquals("Carlos Silva", dto1.getNomeCorrentista());
        assertEquals("Santos", dto1.getSobrenomeCorrentista());
        assertEquals(StatusChave.INATIVA.name(), dto1.getStatus());
        assertEquals("12/04/2025", dto1.getDataCriacao());
        assertEquals("15/04/2025", dto1.getDataInativacao());
    }
}
