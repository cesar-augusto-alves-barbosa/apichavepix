package io.github.cesar_augusto_alves_barbosa.apichavepix.factory;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PixChaveFactoryTest {

    @Test
    void deveConverterFiltroDTOParaEntity() {
        PixChaveConsultaDTO filtroDTO = new PixChaveConsultaDTO("EMAIL", "teste@email.com", "CORRENTE", 1234, 56789012, "Carlos", "Oliveira");
        PixChave entidade = PixChaveFactory.consultaToEntity(filtroDTO);

        assertEquals(TipoChave.EMAIL, entidade.getTipoChave());
        assertEquals("teste@email.com", entidade.getValorChave());
        assertEquals(TipoConta.CORRENTE, entidade.getTipoConta());
        assertEquals(1234, entidade.getNumeroAgencia());
        assertEquals(56789012, entidade.getNumeroConta());
        assertEquals("Carlos", entidade.getNomeCorrentista());
        assertEquals("Oliveira", entidade.getSobrenomeCorrentista());
    }

    @Test
    void deveConverterCriacaoDTOParaEntity() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "João", "Silva", TipoTitular.PF);
        PixChave entidade = PixChaveFactory.criarChavePixToEntity(dto);

        assertNotNull(entidade.getDataCriacao());
        assertEquals(StatusChave.ATIVA, entidade.getStatus());
        assertEquals(TipoTitular.PF, entidade.getTipoTitular());
    }

    @Test
    void deveConverterEntityParaDTO() {
        PixChave entidade = new PixChave(UUID.randomUUID(), TipoChave.CPF, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "João", "Silva", LocalDateTime.now(), null, StatusChave.ATIVA, TipoTitular.PF);
        PixChaveDTO dto = PixChaveFactory.toDTO(entidade);

        assertEquals(entidade.getId(), dto.id());
        assertEquals(entidade.getTipoChave(), dto.tipoChave());
        assertEquals(entidade.getValorChave(), dto.valorChave());
    }

    @Test
    void deveConverterEntityParaConsultaDTO() {
        PixChave entidade = new PixChave(UUID.randomUUID(), TipoChave.CPF, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "João", "Silva", LocalDateTime.now(), null, StatusChave.ATIVA, TipoTitular.PF);
        PixChaveConsultaRespostaDTO dto = PixChaveFactory.toConsultaDTO(entidade);

        assertEquals(entidade.getId(), dto.id());
        assertEquals(entidade.getTipoChave().name(), dto.tipoChave());
        assertEquals(entidade.getValorChave(), dto.valorChave());
    }
}
