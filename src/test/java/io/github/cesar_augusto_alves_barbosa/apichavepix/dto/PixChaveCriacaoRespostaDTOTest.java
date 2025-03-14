package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveCriacaoRespostaDTOTest {

    private UUID chaveId;
    private PixChaveCriacaoRespostaDTO resposta1;
    private PixChaveCriacaoRespostaDTO resposta2;
    private PixChaveCriacaoRespostaDTO respostaDiferente;

    @BeforeEach
    void setUp() {
        chaveId = UUID.randomUUID();

        resposta1 = new PixChaveCriacaoRespostaDTO(chaveId);
        resposta2 = new PixChaveCriacaoRespostaDTO(chaveId);
        respostaDiferente = new PixChaveCriacaoRespostaDTO(UUID.randomUUID());
    }

    @Test
    void deveCriarPixChaveCriacaoRespostaDTOComSucesso() {
        assertNotNull(resposta1);
        assertEquals(chaveId, resposta1.getId());
    }

    @Test
    void deveRetornarValorCorretoNoGetter() {
        assertEquals(chaveId, resposta1.getId());
    }

    @Test
    void deveAlterarValorComSetter() {
        UUID novoId = UUID.randomUUID();
        resposta1.setId(novoId);
        assertEquals(novoId, resposta1.getId());
    }

    @Test
    void deveTestarEqualsComObjetosIguais() {
        assertEquals(resposta1, resposta2);
        assertEquals(resposta1.hashCode(), resposta2.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(resposta1, respostaDiferente);
        assertNotEquals(resposta1.hashCode(), respostaDiferente.hashCode());
    }

    @Test
    void deveTestarToStringNaoRetornaNulo() {
        assertNotNull(resposta1.toString());
    }

    @Test
    void deveTestarCanEqualComObjetosIguais() {
        assertTrue(resposta1.canEqual(resposta2));
    }

    @Test
    void deveTestarCanEqualComObjetosDiferentes() {
        assertFalse(resposta1.canEqual("String qualquer"));
    }
}
