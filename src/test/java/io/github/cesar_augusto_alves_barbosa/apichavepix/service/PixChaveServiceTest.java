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
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.List;
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
    void deveLancarErroAoInativarChaveNaoExistente() {
        UUID idNaoExistente = UUID.randomUUID();
        when(pixChaveRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.inativarChave(idNaoExistente));
    }

    @Test
    void deveValidarEmailComSucesso() {
        pixChaveService.validarEmail("teste@email.com");
    }

    @Test
    void deveLancarErroParaEmailInvalido() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarEmail("email_invalido.com"));
    }

    @Test
    void deveValidarCpfComSucesso() {
        pixChaveService.validarCpf("12345678909");
    }

    @Test
    void deveLancarErroParaCpfInvalido() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarCpf("12345678A90"));
    }

    @Test
    void deveValidarCnpjComSucesso() {
        pixChaveService.validarCnpj("12345678000195");
    }

    @Test
    void deveLancarErroParaCnpjInvalido() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarCnpj("1234567800019A"));
    }

    @Test
    void deveValidarChaveAleatoriaComSucesso() {
        pixChaveService.validarChaveAleatoria(UUID.randomUUID().toString());
    }

    @Test
    void deveLancarErroParaChaveAleatoriaInvalida() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarChaveAleatoria("A".repeat(37)));
    }

    @Test
    void deveValidarCelularComSucesso() {
        pixChaveService.validarCelular("+5511998765432");
    }

    @Test
    void deveLancarErroParaCelularInvalido() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarCelular("1198765432"));
    }

    @Test
    void deveLancarErroParaEmailSemArroba() {
        String emailInvalido = "emailinvalido.com";
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarEmail(emailInvalido));
    }

    @Test
    void deveLancarErroAoValidarTipoChaveInvalido() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(
                null,
                "12345678909",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João",
                "Silva",
                TipoTitular.PF
        );

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
    }


    @Test
    void deveLancarErroAoValidarEmailSemArroba() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.EMAIL, "emailinvalido.com", TipoConta.CORRENTE, 1234, 56789012, "João", "Silva", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
    }

    @Test
    void deveLancarErroAoValidarEmailMuitoLongo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(
                TipoChave.EMAIL,
                "emailmuitolongotestemuitolongoparaserinvalidotestetestetesteteste@emaildemonstracao.com",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João",
                "Silva",
                TipoTitular.PF
        );

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
    }



    @Test
    void deveLancarErroAoConsultarIdNaoExistente() {
        UUID idInexistente = UUID.randomUUID();

        when(pixChaveRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.consultarPorId(idInexistente));
    }


    @Test
    void deveLancarErroAoInativarChaveNaoEncontrada() {
        UUID idInexistente = UUID.randomUUID();

        when(pixChaveRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.inativarChave(idInexistente));
    }

    @Test
    void deveLancarErroAoAlterarChaveNaoEncontrada() {
        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                UUID.randomUUID(),
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João",
                "Silva");

        when(pixChaveRepository.findById(dto.id())).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.alterarChave(dto));
    }

    @Test
    void deveLancarErroAoAlterarChaveNaoExistente() {
        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                UUID.randomUUID(),
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João",
                "Silva"
        );

        when(pixChaveRepository.findById(dto.id())).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.alterarChave(dto));
    }

    @Test
    void deveRetornarListaVaziaAoConsultarPorFiltrosSemCorrespondencias() {
        PixChaveFiltroDTO filtroDTO = PixChaveFiltroDTO.builder().build();

        when(pixChaveRepository.findAll(any(Example.class))).thenReturn(java.util.Collections.emptyList());

        assertTrue(pixChaveService.consultarPorFiltros(filtroDTO).isEmpty());
    }

    @Test
    void deveLancarErroAoConsultarPorFiltrosComCorrespondencias() {
        PixChaveFiltroDTO filtroDTO = PixChaveFiltroDTO.builder().build();
        when(pixChaveRepository.findAll(any(Example.class))).thenReturn(List.of(pixChave));

        assertFalse(pixChaveService.consultarPorFiltros(filtroDTO).isEmpty());
    }

    @Test
    void deveAlterarChaveComSucesso() {
        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                pixChave.getId(),
                TipoConta.POUPANCA,
                4321,
                87654321,
                "Carlos Silva",
                "Santos"
        );

        when(pixChaveRepository.findById(any())).thenReturn(Optional.of(pixChave));
        when(pixChaveRepository.save(any())).thenReturn(pixChave);

        PixChaveDTO chaveAlterada = pixChaveService.alterarChave(dto);

        assertNotNull(chaveAlterada);
        assertEquals(TipoConta.POUPANCA, chaveAlterada.tipoConta());
        assertEquals(4321, chaveAlterada.numeroAgencia());
        assertEquals(87654321, chaveAlterada.numeroConta());
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
    void deveLancarErroAoValidarRegrasComTipoChaveNulo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(null, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
    }

    @Test
    void deveLancarErroAoValidarRegrasComValorChaveNulo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, null, TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
    }

    @Test
    void deveLancarErroAoValidarRegrasComNumeroAgenciaInvalido() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, "12345678909", TipoConta.CORRENTE, 12345, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
        verify(pixChaveRepository, never()).save(any());
    }

    @Test
    void deveLancarErroAoValidarRegrasComTipoContaNulo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, "12345678909", null, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));
        verify(pixChaveRepository, never()).save(any());
    }

    @Test
    void deveLancarErroAoValidarRegrasComNumeroContaInvalido() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(
                TipoChave.CPF,
                "12345678909",
                TipoConta.CORRENTE,
                1234,
                123456789,
                "Carlos",
                "Oliveira",
                TipoTitular.PF
        );

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        verify(pixChaveRepository, never()).save(any());
    }

    @Test
    void deveLancarErroAoValidarRegrasComTipoChaveInvalido() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(
                null,
                "12345678909",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "Carlos",
                "Oliveira",
                TipoTitular.PF
        );

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        verify(pixChaveRepository, never()).save(any());
    }


    @Test
    void deveLancarErroAoValidarEmailSemDominio() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarEmail("email@com"));
    }

    @Test
    void deveLancarErroAoValidarEmailMuitoCurto() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarEmail("e@.c"));
    }

    @Test
    void deveLancarErroAoConsultarPorIdNaoEncontrado() {
        UUID idNaoExistente = UUID.randomUUID();
        when(pixChaveRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.consultarPorId(idNaoExistente));
    }

    @Test
    void deveLancarErroAoInativarChaveJaInativa() {
        pixChave.setStatus(StatusChave.INATIVA);
        when(pixChaveRepository.findById(pixChave.getId())).thenReturn(Optional.of(pixChave));

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.inativarChave(pixChave.getId()));
    }

}

