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
        filtro1 = new PixChaveFiltroDTO(
                "CPF", "12345678901", "CORRENTE",
                1234, 56789012, "João Silva", "Oliveira"
        );

        filtro2 = new PixChaveFiltroDTO(
                "CPF", "12345678901", "CORRENTE",
                1234, 56789012, "João Silva", "Oliveira"
        );

        filtroDiferente = new PixChaveFiltroDTO(
                "EMAIL", "email@email.com", "POUPANCA",
                4321, 87654321, "Carlos Silva", "Santos"
        );
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
    void deveRetornarValoresCorretosDosGetters() {
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
        assertEquals(filtro1, filtro2); // Mesmos valores devem ser iguais
        assertEquals(filtro1.hashCode(), filtro2.hashCode()); // hashCode deve ser igual para objetos equivalentes
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(filtro1, filtroDiferente); // Objetos diferentes não devem ser iguais
        assertNotEquals(filtro1.hashCode(), filtroDiferente.hashCode()); // hashCode deve ser diferente
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
}
