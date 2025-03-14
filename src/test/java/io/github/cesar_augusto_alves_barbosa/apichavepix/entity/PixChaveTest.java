package io.github.cesar_augusto_alves_barbosa.apichavepix.entity;

import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveTest {

    private PixChave pixChave;

    @BeforeEach
    void setUp() {
        pixChave = PixChave.builder()
                .id(UUID.randomUUID())
                .tipoChave(TipoChave.EMAIL)
                .valorChave("teste@email.com")
                .tipoConta(TipoConta.CORRENTE)
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Carlos Oliveira")
                .sobrenomeCorrentista("Silva")
                .dataCriacao(LocalDateTime.now())
                .status(StatusChave.ATIVA)
                .tipoTitular(TipoTitular.PF)
                .build();
    }

    @Test
    void deveCriarPixChaveComSucesso() {
        assertNotNull(pixChave.getId());
        assertEquals(TipoChave.EMAIL, pixChave.getTipoChave());
        assertEquals("teste@email.com", pixChave.getValorChave());
        assertEquals(TipoConta.CORRENTE, pixChave.getTipoConta());
        assertEquals(1234, pixChave.getNumeroAgencia());
        assertEquals(56789012, pixChave.getNumeroConta());
        assertEquals("Carlos Oliveira", pixChave.getNomeCorrentista());
        assertEquals("Silva", pixChave.getSobrenomeCorrentista());
        assertNotNull(pixChave.getDataCriacao());
        assertEquals(StatusChave.ATIVA, pixChave.getStatus());
        assertEquals(TipoTitular.PF, pixChave.getTipoTitular());
    }

    @Test
    void devePermitirAlteracaoDeDados() {
        pixChave.setNumeroConta(12345678);
        pixChave.setNumeroAgencia(4321);
        pixChave.setStatus(StatusChave.INATIVA);
        pixChave.setDataInativacao(LocalDateTime.now());

        assertEquals(12345678, pixChave.getNumeroConta());
        assertEquals(4321, pixChave.getNumeroAgencia());
        assertEquals(StatusChave.INATIVA, pixChave.getStatus());
        assertNotNull(pixChave.getDataInativacao());
    }

    @Test
    void deveAceitarSobrenomeNulo() {
        pixChave.setSobrenomeCorrentista(null);
        assertNull(pixChave.getSobrenomeCorrentista());
    }

    @Test
    void deveAlterarIdComSucesso() {
        UUID novoId = UUID.randomUUID();
        pixChave.setId(novoId);
        assertEquals(novoId, pixChave.getId());
    }

    @Test
    void deveAlterarTipoChaveComSucesso() {
        pixChave.setTipoChave(TipoChave.CNPJ);
        assertEquals(TipoChave.CNPJ, pixChave.getTipoChave());
    }
    @Test
    void deveAlterarValorChaveComSucesso() {
        String novoValorChave = "novoemail@dominio.com";
        pixChave.setValorChave(novoValorChave);
        assertEquals(novoValorChave, pixChave.getValorChave());
    }

    @Test
    void deveAlterarDataCriacaoComSucesso() {
        LocalDateTime novaDataCriacao = LocalDateTime.now().minusDays(1);
        pixChave.setDataCriacao(novaDataCriacao);
        assertEquals(novaDataCriacao, pixChave.getDataCriacao());
    }

    @Test
    void deveAlterarTipoTitularComSucesso() {
        pixChave.setTipoTitular(TipoTitular.PJ);
        assertEquals(TipoTitular.PJ, pixChave.getTipoTitular());
    }

}
