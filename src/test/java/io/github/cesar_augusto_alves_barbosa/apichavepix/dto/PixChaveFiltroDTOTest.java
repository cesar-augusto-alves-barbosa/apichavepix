package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveFiltroDTOTest {

    private PixChaveFiltroDTO filtro1;
    private PixChaveFiltroDTO filtro2;
    private PixChaveFiltroDTO filtroDiferente;

    @BeforeEach
    void setUp() {
        filtro1 = PixChaveFiltroDTO.builder()
                .tipoChave("CPF")
                .valorChave("12345678901")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("João Silva")
                .sobrenomeCorrentista("Oliveira")
                .build();

        filtro2 = PixChaveFiltroDTO.builder()
                .tipoChave("CPF")
                .valorChave("12345678901")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("João Silva")
                .sobrenomeCorrentista("Oliveira")
                .build();

        filtroDiferente = PixChaveFiltroDTO.builder()
                .tipoChave("EMAIL")
                .valorChave("email@email.com")
                .tipoConta("POUPANCA")
                .numeroAgencia(4321)
                .numeroConta(87654321)
                .nomeCorrentista("Carlos Silva")
                .sobrenomeCorrentista("Santos")
                .build();
    }

    @Test
    void deveCriarPixChaveFiltroDTOComSucesso() {
        assertNotNull(filtro1);
        assertEquals("CPF", filtro1.getTipoChave());
        assertEquals("12345678901", filtro1.getValorChave());
        assertEquals("CORRENTE", filtro1.getTipoConta());
        assertEquals(1234, filtro1.getNumeroAgencia());
        assertEquals(56789012, filtro1.getNumeroConta());
        assertEquals("João Silva", filtro1.getNomeCorrentista());
        assertEquals("Oliveira", filtro1.getSobrenomeCorrentista());
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
    void deveAlterarValoresComSetters() {
        filtro1.setTipoChave("CNPJ");
        filtro1.setValorChave("12345678000199");
        filtro1.setTipoConta("POUPANCA");
        filtro1.setNumeroAgencia(4321);
        filtro1.setNumeroConta(87654321);
        filtro1.setNomeCorrentista("Carlos Silva");
        filtro1.setSobrenomeCorrentista("Santos");

        assertEquals("CNPJ", filtro1.getTipoChave());
        assertEquals("12345678000199", filtro1.getValorChave());
        assertEquals("POUPANCA", filtro1.getTipoConta());
        assertEquals(4321, filtro1.getNumeroAgencia());
        assertEquals(87654321, filtro1.getNumeroConta());
        assertEquals("Carlos Silva", filtro1.getNomeCorrentista());
        assertEquals("Santos", filtro1.getSobrenomeCorrentista());
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
    void deveTestarBuilderComSucesso() {
        PixChaveFiltroDTO filtroBuilder = PixChaveFiltroDTO.builder()
                .tipoChave("CPF")
                .valorChave("12345678901")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("João Silva")
                .sobrenomeCorrentista("Oliveira")
                .build();

        assertNotNull(filtroBuilder);
        assertEquals("CPF", filtroBuilder.getTipoChave());
        assertEquals("12345678901", filtroBuilder.getValorChave());
        assertEquals("CORRENTE", filtroBuilder.getTipoConta());
        assertEquals(1234, filtroBuilder.getNumeroAgencia());
        assertEquals(56789012, filtroBuilder.getNumeroConta());
        assertEquals("João Silva", filtroBuilder.getNomeCorrentista());
        assertEquals("Oliveira", filtroBuilder.getSobrenomeCorrentista());
    }

    @Test
    void deveTestarEqualsComObjetosNulos() {
        PixChaveFiltroDTO filtroNull = null;
        assertNotEquals(filtro1, null);
    }

    @Test
    void deveTestarEqualsComTipoChaveNulo() {
        PixChaveFiltroDTO filtroNulo = new PixChaveFiltroDTO(null, "12345678901", "CORRENTE", 1234, 56789012, "Carlos", "Oliveira");
        assertNotEquals(filtro1, filtroNulo);
        assertNotEquals(filtro1.hashCode(), filtroNulo.hashCode());
    }
}
