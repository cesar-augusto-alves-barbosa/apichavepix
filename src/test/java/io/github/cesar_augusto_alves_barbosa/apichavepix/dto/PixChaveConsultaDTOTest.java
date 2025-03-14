package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;

class PixChaveConsultaDTOTest {

    private PixChaveConsultaDTO filtro1;
    private PixChaveConsultaDTO filtro2;
    private PixChaveConsultaDTO filtroDiferente;

    @BeforeEach
    void setUp() {
        filtro1 = new PixChaveConsultaDTO("CPF", "12345678901", "CORRENTE", 1234, 56789012, "João Silva", "Oliveira");
        filtro2 = new PixChaveConsultaDTO("CPF", "12345678901", "CORRENTE", 1234, 56789012, "João Silva", "Oliveira");
        filtroDiferente = new PixChaveConsultaDTO("EMAIL", "email@email.com", "POUPANCA", 4321, 87654321, "Carlos Silva", "Santos");
    }

    @Test
    void deveCriarPixChaveFiltroDTOComSucesso() {
        assertNotNull(filtro1);
        assertEquals("CPF", filtro1.tipoChave());
        assertEquals("12345678901", filtro1.valorChave());
        assertEquals("CORRENTE", filtro1.tipoConta());
        assertEquals(1234, filtro1.numeroAgencia());
        assertEquals(56789012, filtro1.numeroConta());
        assertEquals("João Silva", filtro1.nomeCorrentista());
        assertEquals("Oliveira", filtro1.sobrenomeCorrentista());
    }

    @Test
    void deveTestarEqualsComObjetosIguais() {
        assertEquals(filtro1, filtro2);
        assertEquals(filtro1.hashCode(), filtro2.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(filtro1, filtroDiferente);
        assertNotEquals(filtro1.hashCode(), filtroDiferente.hashCode());
    }

    @Test
    void deveTestarToStringNaoRetornaNulo() {
        assertNotNull(filtro1.toString());
    }

    @Test
    void deveTestarToStringComSucesso() {
        String resultadoToString = filtro1.toString();

        assertNotNull(resultadoToString);
        assertTrue(resultadoToString.contains("tipoChave=CPF"));
        assertTrue(resultadoToString.contains("valorChave=12345678901"));
        assertTrue(resultadoToString.contains("tipoConta=CORRENTE"));
        assertTrue(resultadoToString.contains("numeroAgencia=1234"));
        assertTrue(resultadoToString.contains("numeroConta=56789012"));
        assertTrue(resultadoToString.contains("nomeCorrentista=João Silva"));
        assertTrue(resultadoToString.contains("sobrenomeCorrentista=Oliveira"));
    }

    @Test
    void deveTestarEqualsComObjetosNulos() {
        assertNotEquals(filtro1, null);
    }

    @Test
    void deveTestarEqualsComTipoChaveNulo() {
        PixChaveConsultaDTO filtroNulo = new PixChaveConsultaDTO(null, "12345678901", "CORRENTE", 1234, 56789012, "Carlos", "Oliveira");
        assertNotEquals(filtro1, filtroNulo);
        assertNotEquals(filtro1.hashCode(), filtroNulo.hashCode());
    }
}
