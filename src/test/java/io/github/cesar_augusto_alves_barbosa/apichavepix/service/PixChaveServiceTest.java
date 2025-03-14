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
    void deveConverterEntityParaConsultaDTO() {
        PixChaveConsultaRespostaDTO dto = new PixChaveConsultaRespostaDTO(
                pixChave.getId(),
                pixChave.getTipoChave().name(),
                pixChave.getValorChave(),
                pixChave.getTipoConta().name(),
                pixChave.getNumeroAgencia(),
                pixChave.getNumeroConta(),
                pixChave.getNomeCorrentista(),
                pixChave.getSobrenomeCorrentista(),
                pixChave.getStatus().name(),
                pixChave.getDataCriacao().toString(),
                pixChave.getDataInativacao() != null ? pixChave.getDataInativacao().toString() : null
        );

        assertEquals(pixChave.getId(), dto.id());
        assertEquals(pixChave.getTipoChave().name(), dto.tipoChave());
        assertEquals(pixChave.getValorChave(), dto.valorChave());
    }

    @Test
    void deveLancarErroAoAlterarChaveNaoEncontrada() {
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
    void deveLancarErroAoConsultarIdNaoExistente() {
        UUID idInexistente = UUID.randomUUID();

        when(pixChaveRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.consultarPorId(idInexistente));
    }

    @Test
    void deveLancarErroAoInativarChaveJaInativa() {
        pixChave.setStatus(StatusChave.INATIVA);
        when(pixChaveRepository.findById(pixChave.getId())).thenReturn(Optional.of(pixChave));

        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.inativarChave(pixChave.getId()));
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
    void deveValidarChaveAleatoriaComSucesso() {
        pixChaveService.validarChaveAleatoria(UUID.randomUUID().toString());
    }

    @Test
    void deveLancarErroParaChaveAleatoriaInvalida() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarChaveAleatoria("A".repeat(37)));
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
    void deveValidarCpfComSucesso() {
        pixChaveService.validarCpf("12345678909");
    }

    @Test
    void deveLancarErroParaCpfInvalido() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarCpf("12345678A90"));
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
    void deveValidarRegrasDeCadastroComSucesso() {
        pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO);
    }

    @Test
    void deveLancarErroAoValidarRegrasComTipoChaveNulo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(null, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(dto));
    }

    @Test
    void deveLancarErroAoValidarRegrasComValorChaveNulo() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, null, TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(dto));
    }

    @Test
    void deveConsultarPorFiltrosComSucesso() {
        PixChaveConsultaDTO filtroDTO = new PixChaveConsultaDTO("EMAIL", "teste@email.com", "CORRENTE", 1234, 56789012, "Carlos", "Oliveira");
        when(pixChaveRepository.findAll(any(Example.class))).thenReturn(List.of(pixChave));

        List<PixChaveConsultaRespostaDTO> resultado = pixChaveService.consultarPorFiltros(filtroDTO);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarListaVaziaAoConsultarPorFiltrosSemCorrespondencias() {
        PixChaveConsultaDTO filtroDTO = new PixChaveConsultaDTO("EMAIL", "teste@email.com", "CORRENTE", 1234, 56789012, "Carlos", "Oliveira");
        when(pixChaveRepository.findAll(any(Example.class))).thenReturn(List.of());

        List<PixChaveConsultaRespostaDTO> resultado = pixChaveService.consultarPorFiltros(filtroDTO);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveValidarChavePixComSucesso() {
        assertDoesNotThrow(() -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoTipoChaveForNulo() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(null, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoTipoContaForNulo() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(TipoChave.CPF, "12345678909", null, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoValorChaveForNuloOuVazio() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(TipoChave.EMAIL, "", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoNumeroContaForInvalido() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(TipoChave.EMAIL, "teste@email.com", TipoConta.CORRENTE, 1234, 123456789, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoNumeroAgenciaForInvalido() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(TipoChave.EMAIL, "teste@email.com", TipoConta.CORRENTE, 12345, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveLancarErroQuandoTipoChaveForInvalido() {
        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(null, "12345678909", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(pixChaveCriacaoDTO));
    }

    @Test
    void deveChamarValidacaoCorrespondenteAoTipoDeChave() {
        PixChaveService serviceSpy = spy(pixChaveService);

        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(TipoChave.CELULAR, "+5511998765432", TipoConta.CORRENTE, 1234, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        doNothing().when(serviceSpy).validarCelular(any());

        serviceSpy.validarRegrasDeCadastro(pixChaveCriacaoDTO);

        verify(serviceSpy, times(1)).validarCelular(any());
    }

    @Test
    void deveLancarErroAoInativarChaveNaoEncontrada() {
        UUID idNaoExistente = UUID.randomUUID();
        when(pixChaveRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(ChavePixNaoEncontradaException.class, () -> pixChaveService.inativarChave(idNaoExistente));
    }

    @Test
    void deveLancarErroParaChaveAleatoriaMaiorQue36Caracteres() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarChaveAleatoria("A".repeat(37)));
    }

    @Test
    void deveLancarErroParaChaveAleatoriaComCaracteresInvalidos() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarChaveAleatoria("chave@errada!"));
    }

    @Test
    void deveLancarErroParaEmailMaiorQue77Caracteres() {
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarEmail("muito_longo".repeat(10) + "@email.com"));
    }

    @Test
    void deveLancarErroAoValidarRegrasComNumeroAgenciaInvalido() {
        PixChaveCriacaoDTO dto = new PixChaveCriacaoDTO(TipoChave.CPF, "12345678909", TipoConta.CORRENTE, 12345, 56789012, "Carlos", "Oliveira", TipoTitular.PF);
        assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.validarRegrasDeCadastro(dto));
        verify(pixChaveRepository, never()).save(any());
    }
}

