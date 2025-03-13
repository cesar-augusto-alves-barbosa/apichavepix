package io.github.cesar_augusto_alves_barbosa.apichavepix.service;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.repository.PixChaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PixChaveServiceTest {

    @Mock
    private PixChaveRepository pixChaveRepository;

    @InjectMocks
    private PixChaveService pixChaveService;

    private PixChave pixChave;
    private PixChaveCriacaoDTO pixChaveCriacaoDTO;

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
                .status(StatusChave.ATIVA)
                .dataCriacao(LocalDateTime.now())
                .tipoTitular(TipoTitular.PF)
                .build();

        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(
                TipoChave.EMAIL,
                "teste@email.com",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "Carlos Oliveira",
                "Silva",
                TipoTitular.PF
        );
    }

    @Test
    void deveCadastrarChaveComSucesso() {
        when(pixChaveRepository.findByValorChave(any())).thenReturn(Optional.empty());
        when(pixChaveRepository.countByNumeroConta(any())).thenReturn(0L);
        when(pixChaveRepository.save(any())).thenReturn(pixChave);

        UUID chaveId = pixChaveService.cadastrarChave(pixChaveCriacaoDTO);

        assertNotNull(chaveId);
        verify(pixChaveRepository, times(1)).save(any());
    }

    @Test
    void deveLancarErroAoCadastrarChaveDuplicada() {
        when(pixChaveRepository.findByValorChave(any())).thenReturn(Optional.of(pixChave));

        assertThrows(ChavePixJaCadastradaException.class, () -> pixChaveService.cadastrarChave(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroAoExcederLimiteDeChaves() {
        when(pixChaveRepository.countByNumeroConta(any())).thenReturn(5L);

        assertThrows(LimiteChavesPixAtingidoException.class, () -> pixChaveService.cadastrarChave(pixChaveCriacaoDTO));
    }

    @Test
    void deveAlterarChaveComSucesso() {
        PixChaveAlteracaoDTO alteracaoDTO = new PixChaveAlteracaoDTO(
                pixChave.getId(),
                TipoConta.POUPANCA,
                4321,
                87654321,
                "Carlos Silva",
                "Santos"
        );

        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));
        when(pixChaveRepository.save(any())).thenReturn(pixChave);

        PixChaveDTO chaveAlterada = pixChaveService.alterarChave(alteracaoDTO);

        assertNotNull(chaveAlterada);
        assertEquals(TipoConta.POUPANCA, chaveAlterada.tipoConta());
        assertEquals(4321, chaveAlterada.numeroAgencia());
        assertEquals(87654321, chaveAlterada.numeroConta());
    }

    @Test
    void deveLancarErroAoAlterarChaveInativa() {
        pixChave.setStatus(StatusChave.INATIVA);

        PixChaveAlteracaoDTO alteracaoDTO = new PixChaveAlteracaoDTO(
                pixChave.getId(),
                TipoConta.POUPANCA,
                4321,
                87654321,
                "Carlos Silva",
                "Santos"
        );

        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));

        assertThrows(RuntimeException.class, () -> pixChaveService.alterarChave(alteracaoDTO));
    }

    @Test
    void deveInativarChaveComSucesso() {
        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));
        when(pixChaveRepository.save(any())).thenReturn(pixChave);

        PixChaveDTO chaveInativada = pixChaveService.inativarChave(pixChave.getId());

        assertNotNull(chaveInativada);
        assertEquals(StatusChave.INATIVA, chaveInativada.status());
    }

    @Test
    void deveLancarErroAoInativarChaveJaInativa() {
        pixChave.setStatus(StatusChave.INATIVA);

        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.inativarChave(pixChave.getId()));
    }

    @Test
    void deveConsultarChavePorIdComSucesso() {
        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));

        PixChaveConsultaRespostaDTO consulta = pixChaveService.consultarPorId(pixChave.getId());

        assertNotNull(consulta);
        assertEquals(pixChave.getId(), consulta.getId());
    }

    @Test
    void deveLancarErroAoConsultarChaveNaoEncontrada() {
        when(pixChaveRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pixChaveService.consultarPorId(UUID.randomUUID()));
    }
}
