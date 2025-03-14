package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveConsultaRespostaDTOTest {

    private PixChaveConsultaRespostaDTO dtoCpf;
    private PixChaveConsultaRespostaDTO dtoEmail;
    private PixChaveConsultaRespostaDTO dtoCnpj;

    @BeforeEach
    void setUp() {
        dtoCpf = new PixChaveConsultaRespostaDTO(
                UUID.randomUUID(), "CPF", "12345678909", "CORRENTE",
                1234, 56789012, "Carlos Silva", "Oliveira", "ATIVA",
                "2021-01-01T10:00:00", null);
        

        dtoEmail = new PixChaveConsultaRespostaDTO(
                UUID.randomUUID(), "EMAIL", "teste@email.com", "CORRENTE",
                1234, 56789012, "João Silva", "Oliveira", "ATIVA",
                "2025-03-10", null);

        dtoCnpj = new PixChaveConsultaRespostaDTO(
                UUID.randomUUID(), "CNPJ", "98765432100", "POUPANCA",
                4321, 98765432, "Carlos Souza", "Santos", "INATIVA",
                "2021-02-01T10:00:00", "2021-02-01T12:00:00");
    }

    @Test
    void deveTestarEqualsComObjetosIguais() {
        assertEquals(dtoCpf, dtoCpf);
        assertEquals(dtoCpf.hashCode(), dtoCpf.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(dtoCpf, dtoCnpj);
        assertNotEquals(dtoCpf.hashCode(), dtoCnpj.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosNulos() {
        assertNotEquals(dtoCpf, null);
    }

    @Test
    void deveTestarToStringNaoRetornaNulo() {
        assertNotNull(dtoCpf.toString());
    }

    @Test
    void deveCriarPixChaveConsultaRespostaDTOComSucesso() {
        assertNotNull(dtoEmail);
        assertEquals("EMAIL", dtoEmail.tipoChave());
        assertEquals("teste@email.com", dtoEmail.valorChave());
        assertEquals("CORRENTE", dtoEmail.tipoConta());
        assertEquals(1234, dtoEmail.numeroAgencia());
        assertEquals(56789012, dtoEmail.numeroConta());
        assertEquals("João Silva", dtoEmail.nomeCorrentista());
        assertEquals("Oliveira", dtoEmail.sobrenomeCorrentista());
        assertEquals("ATIVA", dtoEmail.status());
        assertEquals("2025-03-10", dtoEmail.dataCriacao());
    }

    @Test
    void deveTestarToStringComSucesso() {
        String resultadoToString = dtoCpf.toString();

        assertNotNull(resultadoToString);

        assertTrue(resultadoToString.contains("id=" + dtoCpf.id()));
        assertTrue(resultadoToString.contains("tipoChave=" + dtoCpf.tipoChave()));
        assertTrue(resultadoToString.contains("valorChave=" + dtoCpf.valorChave()));
        assertTrue(resultadoToString.contains("tipoConta=" + dtoCpf.tipoConta()));
        assertTrue(resultadoToString.contains("numeroAgencia=" + dtoCpf.numeroAgencia()));
        assertTrue(resultadoToString.contains("numeroConta=" + dtoCpf.numeroConta()));
        assertTrue(resultadoToString.contains("nomeCorrentista=" + dtoCpf.nomeCorrentista()));
        assertTrue(resultadoToString.contains("sobrenomeCorrentista=" + dtoCpf.sobrenomeCorrentista()));
        assertTrue(resultadoToString.contains("status=" + dtoCpf.status()));
        assertTrue(resultadoToString.contains("dataCriacao=" + dtoCpf.dataCriacao()));
    }
}
